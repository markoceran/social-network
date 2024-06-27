package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.PdfUtils;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.repository.GroupDocumentRepository;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.*;
import rs.ac.uns.ftn.service.implementation.MinioService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupRequestService groupRequestService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private GroupAdminService groupAdminService;

    @Autowired
    private BannedService bannedService;

    @Autowired
    private GroupDocumentRepository groupDocumentRepository;

    @Autowired
    private MinioService minioService;


    @GetMapping
    public ResponseEntity<List<Groupp>> getAllGroups() {
        List<Groupp> allGroups = groupService.getAll();
        List<Groupp> groups = new ArrayList<>();

        for (Groupp g : allGroups) {

            groups.add(g);

        }

        return ResponseEntity.ok(groups);
    }

    @GetMapping("/groupUsers/{groupId}")
    public ResponseEntity<List<User>> getGroupUsers(@PathVariable Long groupId) {

        List<GroupRequest> groupRequests = groupRequestService.getAll();
        List<User> groupUsers = new ArrayList<>();

        for(GroupRequest r:groupRequests){
            if(r.getApproved() == true && r.getForr().getIsSuspended() == false && r.getForr().getId() == groupId){
                groupUsers.add(r.getFrom());
            }
        }

        return ResponseEntity.ok(groupUsers);
    }

    @GetMapping("/groupAdmins/{groupId}")
    public ResponseEntity<List<GroupAdmin>> getGroupAdmins(@PathVariable Long groupId) {

        List<GroupAdmin> admins = groupAdminService.getAll();
        List<GroupAdmin> groupAdmins = new ArrayList<>();

        for(GroupAdmin a:admins){
            if(a.getGroup().getId() == groupId){
                groupAdmins.add(a);
            }
        }

        return ResponseEntity.ok(groupAdmins);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Groupp>> getMyGroups(@RequestHeader("Authorization") String token) {

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Groupp> groupps = new ArrayList<>();
        List<Banned> allBanned = bannedService.getAll();
        List<Banned> allBannedUsersInGroup = new ArrayList<>();

        if(!allBanned.isEmpty()){
            for(Banned b:allBanned){
                if(b.getBy1() != null){
                    allBannedUsersInGroup.add(b);
                }
            }
        }


        for (Groupp g : groupService.getAll()) {
            List<User> groupUsers = this.getGroupUsers(g.getId()).getBody();
            List<GroupAdmin> groupAdmins= this.getGroupAdmins(g.getId()).getBody();
            if (!g.getIsSuspended() && (groupUsers.stream().filter(u -> u.getUsername().equals(user.getUsername())).findAny().orElse(null) != null || groupAdmins.stream().filter(u -> u.getUser().getUsername().equals(user.getUsername())).findAny().orElse(null) != null)) {

                if(allBannedUsersInGroup.isEmpty() || (!allBannedUsersInGroup.isEmpty() && allBannedUsersInGroup.stream().filter(b -> !(b.getGroup().getId().equals(g.getId()) && b.getUser().getUsername().equals(user.getUsername()))).findAny().orElse(null) != null)){
                    groupps.add(g);
                }

            }
        }
        return ResponseEntity.ok(groupps);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Groupp> getGroupById(@PathVariable Long id) {
        Optional<Groupp> group = groupService.getById(id);
        if (!group.get().getIsSuspended() && group.isPresent()) {
            return ResponseEntity.ok(group.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Groupp> createGroup(@RequestBody Groupp group, @RequestHeader("Authorization") String token) {

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        if (user.getRole().equals(Roles.USER)) {

            userService.setRoleAsGroupAdmin(user);
            user = userService.findByUsername(username);

        }

        group.setCreationDate(LocalDateTime.now());
        group.setIsSuspended(false);
        groupService.save(group);

        GroupAdmin groupAdmin = new GroupAdmin();
        groupAdmin.setUser(user);
        groupAdmin.setGroup(group);
        groupAdminService.save(groupAdmin);

        return ResponseEntity.ok(group);
    }

    @PostMapping("/saveWithPdf")
    public ResponseEntity<GroupDocument> saveGroupWithPdf(@RequestPart("group") Groupp group,
                                                          @RequestPart("pdfFile") MultipartFile pdfFile,
                                                          @RequestHeader("Authorization") String token) {
        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        if (user.getRole().equals(Roles.USER)) {

            userService.setRoleAsGroupAdmin(user);
            user = userService.findByUsername(username);

        }

        group.setCreationDate(LocalDateTime.now());
        group.setIsSuspended(false);
        Groupp savedGroup = groupService.save(group);

        GroupAdmin groupAdmin = new GroupAdmin();
        groupAdmin.setUser(user);
        groupAdmin.setGroup(group);
        groupAdminService.save(groupAdmin);

        try {
            // Upload PDF to MinIO
            String pdfFileName = "group-" + savedGroup.getId() + ".pdf";
            try (InputStream pdfInputStream = pdfFile.getInputStream()) {
                minioService.uploadFile("group-bucket", pdfFileName, pdfInputStream, pdfFile.getSize(), "application/pdf");
            }

            // Extract text from PDF
            String pdfDescription;
            try (InputStream pdfInputStream = pdfFile.getInputStream()) {
                pdfDescription = PdfUtils.extractTextFromPdf(pdfInputStream);
            }

            // Create Elasticsearch document
            GroupDocument groupDocument = new GroupDocument();
            groupDocument.setId(savedGroup.getId());
            groupDocument.setName(savedGroup.getName());
            groupDocument.setDescription(savedGroup.getDescription());
            groupDocument.setPdfDescription(pdfDescription);

            groupDocumentRepository.save(groupDocument);

            return ResponseEntity.ok(groupDocument);
        } catch (Exception e) {
            // Handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Groupp> updateGroup(@PathVariable Long id, @RequestBody Groupp group) {

        Groupp updatedGroup = groupService.update(id, group);
        if (updatedGroup != null) {
            return ResponseEntity.ok(updatedGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Groupp> deleteGroup(@PathVariable Long id) {
        Groupp deleted = groupService.delete(id);
        if (deleted != null) {
            return ResponseEntity.ok(deleted);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/find/{unos}")
    //@PreAuthorize("isAuthenticated()")
    public List<Groupp> findGroup(@PathVariable String unos, @RequestHeader("Authorization") String token) {

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);


        List<Groupp> sve = groupService.getAll();
        List<Groupp> rezultat = new ArrayList<>();

        boolean valid = true;

        if (!sve.isEmpty()) {
            for (Groupp g : sve) {
                if (!g.getIsSuspended() && g.getName().toLowerCase().contains(unos.toLowerCase())) {

                    List<User> groupUsers = this.getGroupUsers(g.getId()).getBody();
                    List<GroupAdmin> groupAdmins = this.getGroupAdmins(g.getId()).getBody();

                    for (GroupAdmin admin : groupAdmins) {
                        for (User user : groupUsers) {
                            if (admin.getUser().getUsername().equals(username) || user.getUsername().equals(username)) {
                                valid = false;
                            }
                        }

                    }

                    if (valid) {
                        rezultat.add(g);
                    }
                }

            }
        }


        return rezultat;
    }

    @PutMapping("/suspend/{id}")
    public ResponseEntity<Groupp> suspendGroup(@PathVariable Long id, @RequestParam String suspendedReason) {

        Optional<Groupp> group = groupService.getById(id);

        if (group.isPresent()) {

            group.get().setIsSuspended(true);
            group.get().setSuspendedReason(suspendedReason);
            groupAdminService.deleteAllAdmins(id);

            Groupp updated = groupService.update(group.get().getId(), group.get());

            return ResponseEntity.ok(updated);

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/deleteGroupAdmin")
    public ResponseEntity<GroupAdmin> deleteGroupAdmin(@RequestParam Long groupId, @RequestParam Long groupAdminId) throws Exception {


        Optional<Groupp> group = groupService.getById(groupId);
        Optional<GroupAdmin> groupAdmin = groupAdminService.getById(groupAdminId);

        GroupAdmin deleted = null;
        boolean valid = true;

        if(!group.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<GroupAdmin> groupAdmins = this.getGroupAdmins(groupId).getBody();

        for(GroupAdmin a : groupAdmins){
            if(a.getId().equals(groupAdminId)){
                //groupAdminService.delete(a.getUser().getId(), groupId);

                deleted = a;

                groupAdminService.delete(a.getUser().getId(), groupId);

                break;
            }
        }
        //groupService.update(groupId, group.get());

        List<Groupp> allGroup = groupService.getAll();

        for(Groupp g:allGroup){
            List<GroupAdmin> groupAdminsThis = this.getGroupAdmins(g.getId()).getBody();
            if(!groupAdminsThis.isEmpty()){
                for(GroupAdmin admin : groupAdminsThis) {

                    if (admin.getUser().getId().equals(groupAdmin.get().getUser().getId())) {
                        valid = false;
                    }
                }
            }
        }

        if (valid) {
            userService.deleteRoleGroupAdmin(deleted);
        }

        return ResponseEntity.ok(deleted);

    }
}

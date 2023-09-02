package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.GroupAdminService;
import rs.ac.uns.ftn.service.GroupService;
import rs.ac.uns.ftn.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

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
    private TokenUtils tokenUtils;

    @Autowired
    private GroupAdminService groupAdminService;


    @GetMapping
    public ResponseEntity<List<Groupp>> getAllGroups() {
        List<Groupp> allGroups = groupService.getAll();
        List<Groupp> groups = new ArrayList<>();

        for (Groupp g : allGroups) {
            if (!g.getIsSuspended()) {
                groups.add(g);
            }
        }

        return ResponseEntity.ok(groups);
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

        for (Groupp g : groupService.getAll()) {
            if (!g.getIsSuspended() && g.getGroupAdmins().stream().filter(u -> u.getUsername().equals(user.getUsername())).findAny().orElse(null) != null) {
                groupps.add(g);
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

        }


        GroupAdmin groupAdminFind = groupAdminService.findByUsername(user.getUsername());
        group.getGroupAdmins().add(groupAdminFind);


        group.setCreationDate(LocalDateTime.now());
        group.setIsSuspended(false);

        Groupp createdGroup = groupService.save(group);
        return ResponseEntity.ok(createdGroup);
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

                if (g.getName().toLowerCase().contains(unos.toLowerCase())) {

                    for(GroupAdmin admin : g.getGroupAdmins()){
                        if (admin.getUsername().equals(username)) {
                            valid = false;
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

}

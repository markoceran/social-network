package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.service.GroupAdminService;
import rs.ac.uns.ftn.service.GroupRequestService;
import rs.ac.uns.ftn.service.GroupService;
import rs.ac.uns.ftn.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groupRequest")
public class GroupRequestController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRequestService groupRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupAdminService groupAdminService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GroupRequest>> getGroupRequest(@PathVariable Long id) {
        Optional<Groupp> groupp = groupService.getById(id);
        List<GroupRequest> allGroupRequest = groupRequestService.getAll();
        List<GroupRequest> requests = new ArrayList<>();

        if (groupp.isPresent()) {

            for(GroupRequest g:allGroupRequest){
                if(!g.getApproved() && g.getForr().equals(groupp) && g.getAt().equals(null)){
                    requests.add(g);
                }
            }
            return ResponseEntity.ok(requests);

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<GroupRequest> createRequest(@RequestParam Long id) {

        Optional<Groupp> groupp = groupService.getById(id);

        if (groupp.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String username = authentication.getName();
            User user = userService.findByUsername(username);

            GroupRequest groupRequest = new GroupRequest();
            groupRequest.setApproved(false);
            groupRequest.setCreatedAt(LocalDateTime.now());
            groupRequest.setForr(groupp.get());
            groupRequest.setFrom(user);


            GroupRequest created = groupRequestService.save(groupRequest);
            return ResponseEntity.ok(created);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<GroupRequest> acceptRequest(@RequestParam Long id) {

        Optional<GroupRequest> request = groupRequestService.getById(id);

        if (request.isPresent()) {
            User user = request.get().getFrom();
            Groupp groupp = request.get().getForr();

            if (user.getRole().equals(Roles.USER)) {

                user.setRole(Roles.GROUP_ADMIN);
                groupAdminService.update(user.getId(), (GroupAdmin) user);
            }

            groupp.getGroupAdmins().add((GroupAdmin) user);
            request.get().setAt(LocalDateTime.now());
            request.get().setApproved(true);
            groupRequestService.update(id, request.get());

            return ResponseEntity.ok(request.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deny")
    public ResponseEntity<GroupRequest> denyRequest(@RequestParam Long id) {

        Optional<GroupRequest> request = groupRequestService.getById(id);

        if (request.isPresent()) {

            request.get().setAt(LocalDateTime.now());
            request.get().setApproved(false);
            groupRequestService.update(id, request.get());

            return ResponseEntity.ok(request.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}

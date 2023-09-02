package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.security.TokenUtils;
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
    private TokenUtils tokenUtils;


    @Autowired
    private GroupAdminService groupAdminService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GroupRequest>> getGroupRequest(@PathVariable Long id) {
        Optional<Groupp> groupp = groupService.getById(id);
        List<GroupRequest> allGroupRequest = groupRequestService.getAll();
        List<GroupRequest> requests = new ArrayList<>();

        if (groupp.isPresent()) {

            for(GroupRequest g:allGroupRequest){
                if(!g.getApproved() && g.getForr().getId().equals(groupp.get().getId()) && g.getAt() == null){
                    requests.add(g);
                }
            }
            return ResponseEntity.ok(requests);

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<GroupRequest> createRequest(@RequestParam Long id, @RequestHeader("Authorization") String token) {

        Optional<Groupp> groupp = groupService.getById(id);

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (groupp.isPresent()) {

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

                userService.setRoleAsGroupAdmin(user);

            }

            GroupAdmin groupAdminFind = groupAdminService.findByUsername(user.getUsername());
            groupp.getGroupAdmins().add(groupAdminFind);

            request.get().setAt(LocalDateTime.now());
            request.get().setApproved(true);
            groupRequestService.update(id, request.get());
            groupService.update(groupp.getId(), groupp);

            return ResponseEntity.ok(request.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deny")
    public ResponseEntity<GroupRequest> denyRequest(@RequestParam Long id) {

        Optional<GroupRequest> request = groupRequestService.getById(id);

        if (request.isPresent()) {

            groupRequestService.delete(id);

            return ResponseEntity.ok(request.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}

package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.service.GroupAdminService;
import rs.ac.uns.ftn.service.GroupService;
import rs.ac.uns.ftn.service.UserService;

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
    private GroupAdminService groupAdminService;

    /*@GetMapping
    public ResponseEntity<List<Groupp>> getAllGroups() {
        List<Groupp> groups = groupService.getAll();
        return ResponseEntity.ok(groups);
    }*/

    @GetMapping("/my")
    public ResponseEntity<List<Groupp>> getMyGroups(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        List<Groupp> groupps = new ArrayList<>();

        for(Groupp g: groupService.getAll()){
            if(g.getGroupAdmins().contains((GroupAdmin) user)){
                groupps.add(g);
            }
        }
        return ResponseEntity.ok(groupps);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Groupp> getGroupById(@PathVariable Long id) {
        Optional<Groupp> group = groupService.getById(id);
        if (group.isPresent()) {
            return ResponseEntity.ok(group.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Groupp> createGroup(@RequestBody Groupp group) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if(user.getRole().equals(Roles.USER)){

            user.setRole(Roles.GROUP_ADMIN);
            groupAdminService.update(user.getId(), (GroupAdmin) user);
        }

        group.getGroupAdmins().add((GroupAdmin) user);
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
}

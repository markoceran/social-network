package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.Groupp;
import rs.ac.uns.ftn.service.GroupService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ResponseEntity<List<Groupp>> getAllGroups() {
        List<Groupp> groups = groupService.getAll();
        return ResponseEntity.ok(groups);
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

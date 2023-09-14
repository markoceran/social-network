package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.LogManager;

@RestController
@RequestMapping("/banneds")
public class BannedController {

    @Autowired
    private UserService userService;

    @Autowired
    private BannedService bannedService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupAdminService groupAdminService;



    @GetMapping("/all")
    public ResponseEntity<List<Banned>> all() {

        List<Banned> all = bannedService.getAll();

        return ResponseEntity.ok(all);

    }

    @GetMapping("/allBannedUser")
    public ResponseEntity<List<User>> allUser() {

        List<User> users = new ArrayList<>();
        List<Banned> all = bannedService.getAll();

        if(!all.isEmpty()){
            for(Banned b:all){
                if(b.getBy2() != null){
                    users.add(b.getUser());
                }

            }
        }

        return ResponseEntity.ok(users);

    }

    @PostMapping("/delete")
    public ResponseEntity<Banned> delete(@RequestParam Long id) {

        Optional<User> user = userService.getById(id);
        List<Banned> all = bannedService.getAll();
        Banned deleted = null;

        if (user.isPresent()) {

            if(!all.isEmpty()){
                for(Banned b:all){

                    if(b.getUser().getUsername().equals(user.get().getUsername()) && b.getBy2() != null){
                        deleted = bannedService.delete(b.getId());
                    }

                }
            }


            return ResponseEntity.ok(deleted);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createForGroupUser")
    public ResponseEntity<Banned> create(@RequestParam Long groupId, @RequestParam Long userId, @RequestParam Long groupAdminId) {

        Optional<User> user = userService.getById(userId);
        Optional<Groupp> group = groupService.getById(groupId);
        Optional<GroupAdmin> groupAdmin = groupAdminService.getById(groupAdminId);

        if (user.isPresent() && group.isPresent()) {

            Banned banned = new Banned();
            banned.setTimestamp(LocalDate.now());
            banned.setGroup(group.get());
            banned.setUser(user.get());
            banned.setBy1(groupAdmin.get());

            bannedService.save(banned);

            return ResponseEntity.ok(banned);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deleteForUserInGroup")
    public ResponseEntity<Banned> deleteForUserInGroup(@RequestParam Long id, @RequestParam Long groupId) {

        Optional<User> user = userService.getById(id);
        List<Banned> all = bannedService.getAll();

        Banned deleted = null;

        if (user.isPresent()) {

            if(!all.isEmpty()){
                for(Banned b:all){

                    if(b.getUser().getUsername().equals(user.get().getUsername()) && b.getBy1() != null && b.getGroup().getId().equals(groupId)){
                        deleted = bannedService.delete(b.getId());
                    }

                }
            }


            return ResponseEntity.ok(deleted);

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

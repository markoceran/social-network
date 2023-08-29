package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.service.AdministratorService;
import rs.ac.uns.ftn.service.BannedService;
import rs.ac.uns.ftn.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/banneds")
public class BannedController {

    @Autowired
    private UserService userService;

    @Autowired
    private BannedService bannedService;


    @GetMapping("/all")
    public ResponseEntity<List<Banned>> all() {

        List<Banned> all = bannedService.getAll();

        return ResponseEntity.ok(all);

    }

    @GetMapping("/allBannedUser")
    public ResponseEntity<List<User>> allUser() {

        List<User> users = new ArrayList<>();
        List<Banned> all = bannedService.getAll();

        for(Banned b:all){
            users.add(b.getUser());
        }

        return ResponseEntity.ok(users);

    }

    @PostMapping("/delete")
    public ResponseEntity<Banned> delete(@RequestParam Long id) {

        Optional<User> user = userService.getById(id);
        List<Banned> all = bannedService.getAll();
        Banned deleted = null;

        if (user.isPresent()) {

            for(Banned b:all){

                if(b.getUser().getUsername().equals(user.get().getUsername())){
                   deleted = bannedService.delete(b.getId());
                }

            }

            return ResponseEntity.ok(deleted);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

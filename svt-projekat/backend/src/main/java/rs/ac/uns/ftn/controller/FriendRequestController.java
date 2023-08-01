package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.service.FriendRequestService;
import rs.ac.uns.ftn.service.PostService;
import rs.ac.uns.ftn.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friendRequest")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<List<FriendRequest>> getFriendRequest(@PathVariable Long id) {
        Optional<User> user = userService.getById(id);

        List<FriendRequest> allFriendRequest = friendRequestService.getAll();
        List<FriendRequest> requests = new ArrayList<>();

        if (user.isPresent()) {

            for(FriendRequest f:allFriendRequest){
                if(!f.getApproved() && f.getForr().equals(user) && f.getAt().equals(null)){
                    requests.add(f);
                }
            }
            return ResponseEntity.ok(requests);

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<FriendRequest> createRequest(@RequestParam Long id) {

        Optional<User> forUser = userService.getById(id);

        if (forUser.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String username = authentication.getName();
            User user = userService.findByUsername(username);

            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setApproved(false);
            friendRequest.setCreatedAt(LocalDateTime.now());
            friendRequest.setForr(forUser.get());
            friendRequest.setFrom(user);


            FriendRequest created = friendRequestService.save(friendRequest);
            return ResponseEntity.ok(created);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<FriendRequest> acceptRequest(@RequestParam Long id) {

        Optional<FriendRequest> request = friendRequestService.getById(id);

        if (request.isPresent()) {

            User fromUser = request.get().getFrom();
            User forUser = request.get().getForr();


            forUser.getFriendsWith().add(fromUser);
            fromUser.getFriendsWith().add(forUser);

            request.get().setAt(LocalDateTime.now());
            request.get().setApproved(true);
            friendRequestService.update(id, request.get());

            return ResponseEntity.ok(request.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deny")
    public ResponseEntity<FriendRequest> denyRequest(@RequestParam Long id) {

        Optional<FriendRequest> request = friendRequestService.getById(id);

        if (request.isPresent()) {

            request.get().setAt(LocalDateTime.now());
            request.get().setApproved(false);
            friendRequestService.update(id, request.get());

            return ResponseEntity.ok(request.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}

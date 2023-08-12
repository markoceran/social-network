package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.security.TokenUtils;
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

    @Autowired
    private TokenUtils tokenUtils;


    @GetMapping("/all")
    public ResponseEntity<List<FriendRequest>> all() {

        List<FriendRequest> allFriendRequest = friendRequestService.getAll();


        return ResponseEntity.ok(allFriendRequest);


    }

    @GetMapping
    public ResponseEntity<List<FriendRequest>> getFriendRequest(@RequestHeader("Authorization") String token) {

        List<FriendRequest> allFriendRequest = friendRequestService.getAll();
        List<FriendRequest> requests = new ArrayList<>();

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!allFriendRequest.isEmpty()){

            for (FriendRequest f : allFriendRequest) {
                if (!f.getApproved() && f.getForr().equals(user) && f.getAt().equals(LocalDateTime.of(1, 1, 1, 1, 1))) {
                    requests.add(f);
                }
            }
        }

        return ResponseEntity.ok(requests);


    }

    @PostMapping
    public ResponseEntity<FriendRequest> createRequest(@RequestParam Long id, @RequestHeader("Authorization") String token) {

        Optional<User> forUser = userService.getById(id);

        if (forUser.isPresent()) {
            String tokenValue = token.replace("Bearer ", "");

            String username = tokenUtils.getUsernameFromToken(tokenValue);
            User user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setApproved(false);
            friendRequest.setCreatedAt(LocalDateTime.now());
            friendRequest.setForr(forUser.get());
            friendRequest.setFrom(user);
            friendRequest.setAt(LocalDateTime.of(1, 1, 1, 1, 1));


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
        } else {
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
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

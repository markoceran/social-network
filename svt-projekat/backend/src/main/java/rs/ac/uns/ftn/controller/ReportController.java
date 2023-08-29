package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdministratorService administratorService;


    @Autowired
    private BannedService bannedService;
    @Autowired
    private ReportService reportService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/allPost")
    public ResponseEntity<List<Report>> allPost() {

        List<Report> list = new ArrayList<>();
        List<Report> all = reportService.getAll();
        for(Report r:all){
            if(r.getPost()!=null && r.getAccepted() == false){
                list.add(r);
            }
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/allComment")
    public ResponseEntity<List<Report>> allComment() {

        List<Report> list = new ArrayList<>();
        List<Report> all = reportService.getAll();
        for(Report r:all){
            if(r.getComment()!=null && r.getAccepted() == false){
                list.add(r);
            }
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/allUser")
    public ResponseEntity<List<Report>> allUser() {

        List<Report> list = new ArrayList<>();
        List<Report> all = reportService.getAll();
        for(Report r:all){
            if(r.getUser()!=null && r.getAccepted() == false){
                list.add(r);
            }
        }

        return ResponseEntity.ok(list);
    }


    /*@GetMapping
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


    }*/

    @PostMapping("/createForPost")
    public ResponseEntity<Report> createForPost(@RequestParam Long id, @RequestParam String reason, @RequestHeader("Authorization") String token) {

        Optional<Post> post = postService.getById(id);

        if (post.isPresent()) {
            String tokenValue = token.replace("Bearer ", "");

            String username = tokenUtils.getUsernameFromToken(tokenValue);
            User user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Report report = new Report();
            report.setReason(ReportReason.valueOf(reason));
            report.setTimestamp(LocalDate.now());
            report.setPost(post.get());
            report.setByUser(user);

            Report created = reportService.save(report);
            return ResponseEntity.ok(created);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createForComment")
    public ResponseEntity<Report> createForComment(@RequestParam Long id, @RequestParam String reason, @RequestHeader("Authorization") String token) {

        Optional<Comment> comment = commentService.getById(id);

        if (comment.isPresent()) {
            String tokenValue = token.replace("Bearer ", "");

            String username = tokenUtils.getUsernameFromToken(tokenValue);
            User user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Report report = new Report();
            report.setReason(ReportReason.valueOf(reason));
            report.setTimestamp(LocalDate.now());
            report.setComment(comment.get());
            report.setByUser(user);

            Report created = reportService.save(report);
            return ResponseEntity.ok(created);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createForUser")
    public ResponseEntity<Report> createForUser(@RequestParam Long id, @RequestParam String reason, @RequestHeader("Authorization") String token) {

        Optional<User> userForReport = userService.getById(id);

        if (userForReport.isPresent()) {
            String tokenValue = token.replace("Bearer ", "");

            String username = tokenUtils.getUsernameFromToken(tokenValue);
            User user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Report report = new Report();
            report.setReason(ReportReason.valueOf(reason));
            report.setTimestamp(LocalDate.now());
            report.setUser(userForReport.get());
            report.setByUser(user);

            Report created = reportService.save(report);
            return ResponseEntity.ok(created);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/acceptReportForPost")
    public ResponseEntity<Report> acceptReportForPost(@RequestParam Long id) {

        Optional<Report> report = reportService.getById(id);

        if (report.isPresent()) {

            Post post = report.get().getPost();
            post.setIsDeleted(true);

            postService.update(post.getId(), post);

            report.get().setAccepted(true);

            reportService.update(report.get().getId(), report.get());


            return ResponseEntity.ok(report.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/acceptReportForComment")
    public ResponseEntity<Report> acceptReportForComment(@RequestParam Long id) {

        Optional<Report> report = reportService.getById(id);

        if (report.isPresent()) {

            Comment comment = report.get().getComment();
            comment.setIsDeleted(true);

            commentService.update(comment.getId(), comment);

            report.get().setAccepted(true);

            reportService.update(report.get().getId(), report.get());


            return ResponseEntity.ok(report.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/acceptReportForUser")
    public ResponseEntity<Banned> acceptReportForUser(@RequestParam Long id, @RequestHeader("Authorization") String token) {

        Optional<Report> report = reportService.getById(id);

        if (report.isPresent()) {
            String tokenValue = token.replace("Bearer ", "");

            String username = tokenUtils.getUsernameFromToken(tokenValue);
            Administrator admin = administratorService.findByUsername(username);

            if (admin == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User forBanned = report.get().getUser();
            Banned banned = new Banned();
            banned.setTimestamp(LocalDate.now());
            banned.setBy2(admin);
            banned.setUser(forBanned);

            bannedService.save(banned);

            report.get().setAccepted(true);

            reportService.update(report.get().getId(), report.get());

            return ResponseEntity.ok(banned);

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/denyReport")
    public ResponseEntity<Report> denyReport(@RequestParam Long id) {

        Optional<Report> report = reportService.getById(id);

        if (report.isPresent()) {

            reportService.delete(report.get().getId());

            return ResponseEntity.ok(report.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package rs.ac.uns.ftn.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reactions")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/byPost/{id}")
    public ResponseEntity<List<Reaction>> getPostReactions(@PathVariable Long id){

        Optional<Post> post = postService.getById(id);

        List<Reaction> reactions = new ArrayList<>();

        for(Reaction r: reactionService.getAll()){
            if(r.getPost().equals(post.get())){
                reactions.add(r);
            }
        }
        return ResponseEntity.ok(reactions);
    }

    @GetMapping("/byComment/{id}")
    public ResponseEntity<List<Reaction>> getCommentReactions(@PathVariable Long id){

        Optional<Comment> comment = commentService.getById(id);

        List<Reaction> reactions = new ArrayList<>();

        for(Reaction r: reactionService.getAll()){
            if(r.getComment().equals(comment.get())){
                reactions.add(r);
            }
        }
        return ResponseEntity.ok(reactions);
    }


    @PostMapping("/likePost")
    public ResponseEntity<Reaction> likePost(@RequestParam Long id, @RequestHeader("Authorization") String token) {

        Optional<Post> post = postService.getById(id);

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Reaction reaction = new Reaction();
        reaction.setPost(post.get());
        reaction.setType(ReactionType.LIKE);
        reaction.setTimestamp(LocalDate.now());
        reaction.setMadeBy(user);

        Reaction createdReaction = reactionService.save(reaction);
        return ResponseEntity.ok(createdReaction);
    }

    @PostMapping("/dislikePost")
    public ResponseEntity<Reaction> dislikePost(@RequestParam Long id, @RequestHeader("Authorization") String token) {

        Optional<Post> post = postService.getById(id);

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Reaction reaction = new Reaction();
        reaction.setPost(post.get());
        reaction.setType(ReactionType.DISLIKE);
        reaction.setTimestamp(LocalDate.now());
        reaction.setMadeBy(user);

        Reaction createdReaction = reactionService.save(reaction);
        return ResponseEntity.ok(createdReaction);
    }

    @PostMapping("/heartPost")
    public ResponseEntity<Reaction> heartPost(@RequestParam Long id, @RequestHeader("Authorization") String token) {

        Optional<Post> post = postService.getById(id);

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Reaction reaction = new Reaction();
        reaction.setPost(post.get());
        reaction.setType(ReactionType.HEART);
        reaction.setTimestamp(LocalDate.now());
        reaction.setMadeBy(user);

        Reaction createdReaction = reactionService.save(reaction);
        return ResponseEntity.ok(createdReaction);
    }

    @PostMapping("/likeComment")
    public ResponseEntity<Reaction> likeComment(@RequestParam Long id, @RequestHeader("Authorization") String token) {

        Optional<Comment> comment = commentService.getById(id);

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Reaction reaction = new Reaction();
        reaction.setComment(comment.get());
        reaction.setType(ReactionType.LIKE);
        reaction.setTimestamp(LocalDate.now());
        reaction.setMadeBy(user);

        Reaction createdReaction = reactionService.save(reaction);
        return ResponseEntity.ok(createdReaction);
    }

    @PostMapping("/dislikeComment")
    public ResponseEntity<Reaction> dislikeComment(@RequestParam Long id, @RequestHeader("Authorization") String token) {

        Optional<Comment> comment = commentService.getById(id);

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Reaction reaction = new Reaction();
        reaction.setComment(comment.get());
        reaction.setType(ReactionType.DISLIKE);
        reaction.setTimestamp(LocalDate.now());
        reaction.setMadeBy(user);

        Reaction createdReaction = reactionService.save(reaction);
        return ResponseEntity.ok(createdReaction);
    }

    @PostMapping("/heartComment")
    public ResponseEntity<Reaction> heartComment(@RequestParam Long id, @RequestHeader("Authorization") String token) {

        Optional<Comment> comment = commentService.getById(id);

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Reaction reaction = new Reaction();
        reaction.setComment(comment.get());
        reaction.setType(ReactionType.HEART);
        reaction.setTimestamp(LocalDate.now());
        reaction.setMadeBy(user);

        Reaction createdReaction = reactionService.save(reaction);
        return ResponseEntity.ok(createdReaction);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Reaction> deleteReaction(@PathVariable Long id) {
        Reaction deleted = reactionService.delete(id);
        if (deleted != null) {
            return ResponseEntity.ok(deleted);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

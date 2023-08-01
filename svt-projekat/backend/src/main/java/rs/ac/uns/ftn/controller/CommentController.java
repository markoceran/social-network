package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Post;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.service.CommentService;
import rs.ac.uns.ftn.service.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    /*@GetMapping
    public ResponseEntity<List<Comment>> getAll(){
        return ResponseEntity.ok(commentService.getAll());  //logicko brisanje provera
    }*/

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.save(comment);
        return ResponseEntity.ok(createdComment);
    }

    @GetMapping("/postComments/{id}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id){

        Optional<Post> post = postService.getById(id);


        List<Comment> comments = new ArrayList<>();

        for(Comment c: commentService.getAll()){
            if(!c.getIsDeleted() && c.getPost().equals(post.get())){
                comments.add(c);
            }
        }
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getById(id);
        if (!comment.get().getIsDeleted() && comment.get() != null) {
            return ResponseEntity.ok(comment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) throws Exception {
        Comment updatedComment = commentService.update(id, comment);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Long id) {
        Comment deleted = commentService.delete(id);
        if (deleted != null) {
            return ResponseEntity.ok(deleted);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/reply")
    public ResponseEntity<Comment> reply(@RequestParam Long commendId, @RequestBody Comment comment) {
        Optional<Comment> replyOn = commentService.getById(commendId);
        Comment createdComment = commentService.save(comment);

        replyOn.get().getRepliesTo().add(createdComment);
        return ResponseEntity.ok(createdComment);
    }
}

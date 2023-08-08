package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.Post;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.PostService;
import rs.ac.uns.ftn.service.UserService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    TokenUtils tokenUtils;

    /*@GetMapping("/lastId")
    public ResponseEntity<Long> getLastId(){
        List<Post> svi = postService.getAll();
        int size = svi.size();

        return ResponseEntity.ok(svi.get(size-1).getId());
    }*/

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestHeader("Authorization") String token) {

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        post.setPostedBy(user);
        post.setCreationDate(LocalDateTime.now());

        Post createdPost = postService.save(post);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/my/{username}")
    public ResponseEntity<List<Post>> getMyPosts(@PathVariable String username){

        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Post> posts = new ArrayList<>();

        for(Post p: postService.getAll()){
            if(p.getPostedBy().equals(user)){
                posts.add(p);
            }
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postService.getById(id);
        if (post.get() != null) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) throws Exception {
        Post updatedPost = postService.update(id, post);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id) {
        Post deleted = postService.delete(id);
        if (deleted != null) {
            return ResponseEntity.ok(deleted);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

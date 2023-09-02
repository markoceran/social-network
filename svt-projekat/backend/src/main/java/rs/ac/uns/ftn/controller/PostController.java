package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.Groupp;
import rs.ac.uns.ftn.model.Post;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.GroupService;
import rs.ac.uns.ftn.service.PostService;
import rs.ac.uns.ftn.service.UserService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

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
        post.setIsDeleted(false);
        post.setCreationDate(LocalDateTime.now());

        Post createdPost = postService.save(post);
        return ResponseEntity.ok(createdPost);
    }

    @PostMapping("/createPostInGroup")
    public ResponseEntity<Post> createPostInGroup(@RequestParam Long groupId, @RequestBody Post post, @RequestHeader("Authorization") String token) {

        Optional<Groupp> group = groupService.getById(groupId);

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        post.setPostedBy(user);
        post.setIsDeleted(false);
        post.setCreationDate(LocalDateTime.now());

        Post createdPost = postService.save(post);

        group.get().getContains().add(post);

        groupService.update(groupId, group.get());

        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/my/{username}")
    public ResponseEntity<List<Post>> getMyPosts(@PathVariable String username){

        User user = userService.findByUsername(username);
        List<Post> svi = postService.getAll();
        List<Groupp> sveGrupe = groupService.getAll();
        List<Post> sviPostoviGrupa = new ArrayList<>();

        for(Groupp g: sveGrupe){
            for(Post p: g.getContains()){
                sviPostoviGrupa.add(p);
            }
        }


        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Post> posts = new ArrayList<>();

        if(!svi.isEmpty()){
            for(Post p: svi){
                if(p.getPostedBy().getUsername().equals(user.getUsername()) && !p.getIsDeleted() && !sviPostoviGrupa.contains(p)){
                    posts.add(p);
                }
            }
        }

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/myFriends/{username}")
    public ResponseEntity<List<Post>> getFriendsPosts(@PathVariable String username){

        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Set<User> myFriendsSet = user.getFriendsWith();
        List<User> myFriends = new ArrayList<>(myFriendsSet);

        List<Groupp> sveGrupe = groupService.getAll();
        List<Post> sviPostoviGrupa = new ArrayList<>();

        for(Groupp g: sveGrupe){
            for(Post p: g.getContains()){
                sviPostoviGrupa.add(p);
            }
        }

        List<Post> allPosts = postService.getAll();
        List<Post> friendsPosts = new ArrayList<>();

        if(!myFriends.isEmpty() && !allPosts.isEmpty()){

            for(Post p:allPosts){
                for(User u:myFriends){
                    if(p.getPostedBy().getUsername().equals(u.getUsername()) && !p.getIsDeleted() && !sviPostoviGrupa.contains(p)){
                        friendsPosts.add(p);
                    }
                }
            }
        }


        return ResponseEntity.ok(friendsPosts);
    }

    @GetMapping("/myGroupsPosts/{username}")
    public ResponseEntity<List<Post>> getPostsFromMyGroups(@PathVariable String username){

        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Groupp> myGroups = new ArrayList<>();

        for (Groupp g : groupService.getAll()) {
            if (!g.getIsSuspended() && g.getGroupAdmins().stream().filter(u -> u.getUsername().equals(user.getUsername())).findAny().orElse(null) != null) {
                myGroups.add(g);
            }
        }

        List<Post> groupPosts = new ArrayList<>();

        if(!myGroups.isEmpty()){
            for(Groupp g: myGroups){
                for(Post p: g.getContains()) {
                    if (!p.getIsDeleted()) {
                        groupPosts.add(p);
                    }
                }
            }
        }

        return ResponseEntity.ok(groupPosts);
    }

    @GetMapping("/groupPost/{id}")
    public ResponseEntity<List<Post>> getGroupPosts(@PathVariable Long id){

        Optional<Groupp> group = groupService.getById(id);

        Set<Post> groupPostsSet = group.get().getContains();
        List<Post> groupPosts = new ArrayList<>(groupPostsSet);

        List<Post> posts = new ArrayList<>();

        if(!groupPosts.isEmpty()){
            for(Post p: groupPosts){
                if(!p.getIsDeleted()){
                    posts.add(p);
                }
            }
        }

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postService.getById(id);
        if (post.get() != null && !post.get().getIsDeleted()) {
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


    @PutMapping("/orderAsc")
    public ResponseEntity<List<Post>> orderAsc(@RequestBody List<Post> posts) {

        List<Post> list = postService.orderAsc(posts);

        return ResponseEntity.ok(list);
    }

    @PutMapping("/orderDesc")
    public ResponseEntity<List<Post>> orderDesc(@RequestBody List<Post> posts) {

        List<Post> list = postService.orderDesc(posts);

        return ResponseEntity.ok(list);
    }
}

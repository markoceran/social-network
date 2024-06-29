package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.PdfUtils;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.repository.PostDocumentRepository;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.*;
import rs.ac.uns.ftn.service.implementation.MinioService;

import java.io.InputStream;
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
    private GroupAdminService groupAdminService;

    @Autowired
    private GroupRequestService groupRequestService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private PostDocumentRepository postDocumentRepository;

    @Autowired
    private MinioService minioService;

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

    @PostMapping("/saveWithPdf")
    public ResponseEntity<PostDocument> savePostWithPdf(@RequestPart("post") PostDocument postDocumentFromRequest,
                                                          @RequestPart("pdfFile") MultipartFile pdfFile,
                                                          @RequestHeader("Authorization") String token) {
        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Post post = new Post();
        post.setContent(postDocumentFromRequest.getContent());
        post.setPostedBy(user);
        post.setIsDeleted(false);
        post.setCreationDate(LocalDateTime.now());

        Post createdPost = postService.save(post);

        try {
            // Upload PDF to MinIO
            String pdfFileName = "post-" + createdPost.getId() + ".pdf";
            try (InputStream pdfInputStream = pdfFile.getInputStream()) {
                minioService.uploadFile("post-bucket", pdfFileName, pdfInputStream, pdfFile.getSize(), "application/pdf");
            }

            // Extract text from PDF
            String pdfContent;
            try (InputStream pdfInputStream = pdfFile.getInputStream()) {
                pdfContent = PdfUtils.extractTextFromPdf(pdfInputStream);
            }

            // Create Elasticsearch document
            postDocumentFromRequest.setId(createdPost.getId());
            postDocumentFromRequest.setPdfContent(pdfContent);

            postDocumentRepository.save(postDocumentFromRequest);

            return ResponseEntity.ok(postDocumentFromRequest);
        } catch (Exception e) {
            // Handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
            List<User> groupUsers = getGroupUsers(g.getId());
            List<GroupAdmin> groupAdmins = getGroupAdmins(g.getId());
            if (!g.getIsSuspended() && (groupAdmins.stream().filter(u -> u.getUser().getUsername().equals(user.getUsername())).findAny().orElse(null) != null || groupUsers.stream().filter(u -> u.getUsername().equals(user.getUsername())).findAny().orElse(null) != null))  {
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


    public List<User> getGroupUsers(Long groupId) {

        List<GroupRequest> groupRequests = groupRequestService.getAll();
        List<User> groupUsers = new ArrayList<>();

        for(GroupRequest r:groupRequests){
            if(r.getApproved() == true && r.getForr().getIsSuspended() == false && r.getForr().getId() == groupId){
                groupUsers.add(r.getFrom());
            }
        }

        return groupUsers;
    }


    public List<GroupAdmin> getGroupAdmins(Long groupId) {

        List<GroupAdmin> admins = groupAdminService.getAll();
        List<GroupAdmin> groupAdmins = new ArrayList<>();

        for(GroupAdmin a:admins){
            if(a.getGroup().getId() == groupId){
                groupAdmins.add(a);
            }
        }

        return groupAdmins;
    }
}

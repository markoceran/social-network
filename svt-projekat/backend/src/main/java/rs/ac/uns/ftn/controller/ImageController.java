package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.model.Image;
import rs.ac.uns.ftn.model.Post;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.service.ImageService;
import rs.ac.uns.ftn.service.PostService;
import rs.ac.uns.ftn.service.UserService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.util.ArrayList;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<Resource>> getImagesByPost(@PathVariable Long postId) {

        Optional<Post> post = postService.getById(postId);
        List<Image> images = new ArrayList<>();

        if (post.isPresent()) {

            for(Image i: imageService.getAll()){
                if(i.getPost().equals(post)){
                    images.add(i);
                }
            }

            List<Resource> resources = new ArrayList<>();

            for (Image image : images) {
                try {
                    // Load the image file from the server based on the file path
                    Path imagePath = Paths.get("uploads", image.getPath());
                    Resource resource = new UrlResource(imagePath.toUri());

                    if (resource.exists()) {
                        resources.add(resource);
                    }
                } catch (MalformedURLException e) {
                    // Handle the exception, if necessary
                }
            }

            if (!resources.isEmpty()) {
                // Return the list of images as ResponseEntity with appropriate headers
                return ResponseEntity.ok()
                        .body(resources);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/profileImage")
    public ResponseEntity<Resource> getMyProfileImage() {

        //Optional<Post> post = postService.getById(postId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user != null) {

            Image image = new Image();

            for(Image i: imageService.getAll()){
                if(i.getBelongsTo().equals(user)){
                    image = i;
                }
            }

            if (image != null) {
                try {
                    // Load the image file from the server based on the file path
                    Path imagePath = Paths.get("uploads", image.getPath());
                    Resource resource = new UrlResource(imagePath.toUri());

                    if (resource.exists()) {
                        // Return the image as ResponseEntity with appropriate headers
                        return ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG) // Or the appropriate image type
                                .body(resource);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                } catch (MalformedURLException e) {
                    return ResponseEntity.badRequest().build();
                }
            }
        }

        return ResponseEntity.notFound().build();
    }


    @PostMapping("/setPostImage")
    public ResponseEntity<String> setPostImage(@RequestParam("image") MultipartFile imageFile) {

        List<Post> all = postService.getAll();
        Post post = all.get(all.size()-1);

        try {
            // Check if the uploaded file is not empty and is an image
            if (!imageFile.isEmpty() && imageFile.getContentType().startsWith("image/")) {
                // Save the image file to the server (you can choose your desired directory)
                String uploadDir = "uploads";
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(imageFile.getInputStream(), filePath);

                // Create an Image entity and set the path to the saved image file
                Image image = new Image();
                image.setPath(fileName);
                image.setPost(post);

                // Save the Image entity to the database
                imageService.save(image);

                // Return a success response
                return ResponseEntity.ok("Image uploaded successfully!");
            } else {
                return ResponseEntity.badRequest().body("Invalid file format. Please upload an image file.");
            }
        } catch (IOException e) {
            // Handle the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image.");
        }
    }

    @PostMapping("/setProfileImage")
    public ResponseEntity<String> setProfileImage(@RequestParam("imageFile") MultipartFile imageFile, @RequestParam Long id) {

        Optional<User> user = userService.getById(id);

        try {
            // Check if the uploaded file is not empty and is an image
            if (!imageFile.isEmpty() && imageFile.getContentType().startsWith("image/")) {
                // Save the image file to the server (you can choose your desired directory)
                String uploadDir = "uploads";
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(imageFile.getInputStream(), filePath);

                // Create an Image entity and set the path to the saved image file
                Image image = new Image();
                image.setPath(fileName);
                image.setBelongsTo(user.get());

                // Save the Image entity to the database
                imageService.save(image);

                // Return a success response
                return ResponseEntity.ok("Image uploaded successfully!");
            } else {
                return ResponseEntity.badRequest().body("Invalid file format. Please upload an image file.");
            }
        } catch (IOException e) {
            // Handle the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image.");
        }
    }




}

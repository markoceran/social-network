package rs.ac.uns.ftn.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.ImageService;
import rs.ac.uns.ftn.service.PostService;
import rs.ac.uns.ftn.service.UserService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private TokenUtils tokenUtils;

    private String uploadDir = "C:\\Users\\marko\\Desktop\\serverske-veb-tehnologije-projekat\\svt-projekat\\backend\\uploads";

    /*@GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws MalformedURLException {
        Path imagePath = Paths.get(imageUploadDir, imageName);
        Resource imageResource = new UrlResource(imagePath.toUri());

        if (imageResource.exists() && imageResource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Adjust content type as needed
                    .body(imageResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @GetMapping("/{postId}")
    public ResponseEntity<List<String>> getImagesByPost(@PathVariable Long postId) {
        Optional<Post> post = postService.getById(postId);
        List<Image> images = new ArrayList<>();

        if (post.isPresent()) {
            for (Image i : imageService.getAll()) {
                if (i.getPost().getId().equals(post.get().getId())) {
                    images.add(i);
                }
            }

            List<String> imageUrls = new ArrayList<>();

            for (Image image : images) {
                try {
                    Path imagePath = Paths.get(uploadDir, image.getPath());
                    Resource imageResource = new UrlResource(imagePath.toUri());

                    if (imageResource.exists() && imageResource.isReadable()) {
                        imageUrls.add(image.getPath());
                    }
                } catch (IOException e) {
                    // Handle the exception, if necessary
                }
            }

            if (!imageUrls.isEmpty()) {
                return ResponseEntity.ok().body(imageUrls);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getImage/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(uploadDir, imageName);
            Resource imageResource = new UrlResource(imagePath.toUri());

            if (imageResource.exists() && imageResource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Adjust content type as needed
                        .body(imageResource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            // Handle the exception, if necessary
            return ResponseEntity.notFound().build();
        }
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
                    Path imagePath = Paths.get(uploadDir, image.getPath());
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
    public ResponseEntity<String> setPostImage(@RequestParam("imageFile") MultipartFile imageFile, @RequestHeader("Authorization") String token) throws IOException {

        List<Post> all = postService.getAll();

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!all.isEmpty()) {
            Post post = all.get(all.size() - 1);

            if (!imageFile.isEmpty() && imageFile.getContentType().startsWith("image/")) {
                try {
                    // Generate a unique file name
                    String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();


                    File tempFile = File.createTempFile(fileName, null, new File(uploadDir));
                    Files.copy(imageFile.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    Path tempFilePath = tempFile.toPath();
                    Path destinationPath = Paths.get(uploadDir, fileName);

                    Files.move(tempFilePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);


                    Image image = new Image();
                    image.setPath(fileName);
                    image.setPost(post);
                    image.setBelongsTo(user);

                    // Save the image entity to the database using imageService
                    imageService.save(image);


                    return ResponseEntity.ok("Image uploaded successfully!");
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image.");
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid file format. Please upload an image file.");
            }
        }

        return ResponseEntity.badRequest().body("No posts available.");
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

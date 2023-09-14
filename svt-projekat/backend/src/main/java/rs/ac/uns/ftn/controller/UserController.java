package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.model.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.model.dto.UserTokenState;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private GroupService groupService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    GroupAdminService groupAdminService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private BannedService bannedService;

    @Autowired
    UserDetailsService userDetailsService;

    private final Logger logger;

    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.logger = Logger.getLogger(String.valueOf(UserController.class));
    }


    @GetMapping("/all")
    public List<User> loadAll() {
        return this.userService.getAll();
    }

    @GetMapping("/profile/{username}")
    public User user(@PathVariable String username) {
        //response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        logger.info("Find user with username");
        return this.userService.findByUsername(username);
    }

    @GetMapping("/{unos}")
    public List<User> findUsers(@PathVariable String unos, @RequestHeader("Authorization") String token) {

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
            logger.info("User is not present");
            return (List<User>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<User> svi = userService.getAll();
        List<User> rezultat = new ArrayList<>();

        if (!svi.isEmpty()) {
            for (User u : svi) {
                if (!u.getUsername().equals(user.getUsername()) && (u.getFirstName().toLowerCase().contains(unos.toLowerCase()) || u.getLastName().toLowerCase().contains(unos.toLowerCase()) || u.getUsername().toLowerCase().contains(unos.toLowerCase()))) {
                    rezultat.add(u);
                }
            }
        }

        logger.info("Users found successfully");
        return rezultat;
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser) {

        boolean isValid = true;
        List<User> sviKorisnici = userService.getAll();
        User createdUser;

        for (User u : sviKorisnici) {
            if (u.getUsername().equals(newUser.getUsername()) || !newUser.getEmail().contains("@")) {
                isValid = false;
                logger.info("Email is not valid");
            }
        }

        if (isValid) {
            createdUser = userService.createUser(newUser);
            logger.info("User is created");
        } else {
            createdUser = null;
            logger.info("User is not created");

        }

        if (createdUser == null) {

            logger.info("User is null");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);

        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {

        boolean isValid = true;

        List<Banned> banneds = bannedService.getAll();

        if(!banneds.isEmpty()){
            for (Banned b : banneds) {
                if (b.getUser() != null && b.getUser().getUsername().equals(authenticationRequest.getUsername()) && b.getBy2() != null) {
                    isValid = false;
                    logger.info("User is bannned");
                }
            }
        }


        if (isValid) {
            // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
            // AuthenticationException
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));

            // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
            // kontekst
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Kreiraj token za tog korisnika
            UserDetails user = (UserDetails) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user);
            int expiresIn = tokenUtils.getExpiredIn();

            User loggedUser = userService.findByUsername(authenticationRequest.getUsername());
            loggedUser.setLastLogin(LocalDateTime.now());

            userService.update(loggedUser.getId(), loggedUser);

            logger.info("Token is created");

            // Vrati token kao odgovor na uspesnu autentifikaciju
            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
        }

        logger.info("User is unauthorized");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {

        SecurityContextHolder.getContext().setAuthentication(null);

        TokenUtils.clearAuthenticationCookie(request, response);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logged out successfully");

        logger.info("Logged out successfully");

        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/editProfile")
    public ResponseEntity<User> updateUserData(@RequestHeader("Authorization") String token, @RequestBody User user) throws Exception {

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User loggedUser = userService.findByUsername(username);

        if (loggedUser == null) {
            logger.info("Logged user is not present");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<User> allUser = userService.getAll();
        for (User u : allUser) {
            if (!loggedUser.getUsername().equals(user.getUsername()) && u.getUsername().equals(user.getUsername())) {
                logger.info("FORBIDDEN");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        user.setPassword(loggedUser.getPassword());


        User updated = userService.update(loggedUser.getId(), user);
        if (updated != null) {
            logger.info("Success");
            return ResponseEntity.ok(updated);
        } else {
            logger.info("Not found");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/editPassword")
    public ResponseEntity<User> editPassword(@RequestHeader("Authorization") String token, @RequestParam String oldPassword, @RequestParam String newPassword) throws Exception {

        User updated = null;

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User loggedUser = userService.findByUsername(username);

        if (loggedUser == null) {
            logger.info("User is not present");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        if (passwordEncoder.matches(oldPassword, loggedUser.getPassword())) {

            loggedUser.setPassword(passwordEncoder.encode(newPassword));
            updated = userService.update(loggedUser.getId(), loggedUser);

        }

        if (updated != null) {
            logger.info("Success");
            return ResponseEntity.ok(updated);
        } else {
            logger.info("Bad request");
            return ResponseEntity.badRequest().build();
        }
    }


}






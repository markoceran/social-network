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
import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.Banned;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.model.dto.UserTokenState;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.AdministratorService;
import rs.ac.uns.ftn.service.BannedService;
import rs.ac.uns.ftn.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdministratorService administratorService;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private BannedService bannedService;

    @Autowired
    UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.getAll();
    }

    @GetMapping("/profile/{username}")
    //@PreAuthorize("isAuthenticated()")
    public User user(@PathVariable String username, HttpServletResponse response) {
        //response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        return this.userService.findByUsername(username);
    }

    @GetMapping("/{unos}")
    //@PreAuthorize("isAuthenticated()")
    public List<User> findUsers(@PathVariable String unos, @RequestHeader("Authorization") String token) {

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User user = userService.findByUsername(username);

        if (user == null) {
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
            }
        }

        if (isValid) {
            createdUser = userService.createUser(newUser);
        } else {
            createdUser = null;
        }

        if (createdUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {

        boolean isValid = true;

        List<Banned> banneds = bannedService.getAll();
        for (Banned b : banneds) {
            if (b.getUser().getUsername().equals(authenticationRequest.getUsername())) {
                isValid = false;
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

            // Vrati token kao odgovor na uspesnu autentifikaciju
            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {

        SecurityContextHolder.getContext().setAuthentication(null);

        TokenUtils.clearAuthenticationCookie(request, response);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logged out successfully");

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<UserDTO> addAdmin(@RequestBody @Validated UserDTO newUser) {

        Administrator createdUser = administratorService.createAdmin(newUser);

        if (createdUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PutMapping("/editProfile")
    public ResponseEntity<User> updateUserData(@RequestHeader("Authorization") String token, @RequestBody User user) throws Exception {

        String tokenValue = token.replace("Bearer ", "");

        String username = tokenUtils.getUsernameFromToken(tokenValue);
        User loggedUser = userService.findByUsername(username);

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<User> allUser = userService.getAll();
        for (User u : allUser) {
            if (!loggedUser.getUsername().equals(user.getUsername()) && u.getUsername().equals(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        user.setPassword(loggedUser.getPassword());


        User updated = userService.update(loggedUser.getId(), user);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        if (passwordEncoder.matches(oldPassword, loggedUser.getPassword())) {

            loggedUser.setPassword(passwordEncoder.encode(newPassword));
            updated = userService.update(loggedUser.getId(), loggedUser);

        }

        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


}






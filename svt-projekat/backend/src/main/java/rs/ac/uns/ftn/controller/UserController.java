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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.model.dto.UserTokenState;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    UserDetailsService userDetailsService;




    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.getAll();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }



    @PostMapping("/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser){

        User createdUser = userService.createUser(newUser);

        if(createdUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

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

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        SecurityContextHolder.getContext().setAuthentication(null);

        TokenUtils.clearAuthenticationCookie(request, response);

        return ResponseEntity.ok("Logged out successfully");
    }


}






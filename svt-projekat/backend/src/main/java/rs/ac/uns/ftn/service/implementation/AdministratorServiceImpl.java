package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.Roles;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.repository.AdministratorRepository;
import rs.ac.uns.ftn.service.AdministratorService;
import rs.ac.uns.ftn.service.UserService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class AdministratorServiceImpl implements AdministratorService {

    @Autowired
    AdministratorRepository administratorRepository;

    @Autowired
    UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdministratorServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Administrator> getAll() {
        return administratorRepository.findAll();
    }

    @Override
    public Optional<Administrator> getById(Long id) {
        return administratorRepository.findById(id);
    }

    @Override
    public Administrator createAdmin(UserDTO userDTO) {

        User user = this.userService.findByUsername(userDTO.getUsername());

        if(user != null){
            return null;
        }

        Administrator newUser = new Administrator();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRole(Roles.ADMIN);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setLastLogin(LocalDateTime.now());
        newUser.setEmail(userDTO.getEmail());
        newUser.setFriendsWith(new HashSet<>());
        newUser = administratorRepository.save(newUser);

        return newUser;
    }

    @Override
    public Administrator findByUsername(String username) {

        List<Administrator> svi = this.getAll();
        for(Administrator a : svi){
            if(a.getUsername().equals(username)){
                return a;
            }
        }

        return null;
    }

}

package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.repository.UserRepository;
import rs.ac.uns.ftn.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findByUsername(String username) {
        return getAll().stream().filter(s->s.getUsername().equals(username)).findAny().orElse(null);
    }

    @Override
    public User createUser(UserDTO userDTO) {

        User user = findByUsername(userDTO.getUsername());

        if(user != null){
            return null;
        }

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        //newUser.setRole(Roles.USER);
        newUser = userRepository.save(newUser);

        return newUser;
    }

    @Override
    public User findByEmail(String email) {
        return getAll().stream().filter(s->s.getEmail().equals(email)).findAny().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}

package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.UserDTO;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User createUser(UserDTO userDTO);

    User findByEmail(String email);

    List<User> getAll();
}

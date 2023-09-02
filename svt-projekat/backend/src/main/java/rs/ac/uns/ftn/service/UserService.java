package rs.ac.uns.ftn.service;


import rs.ac.uns.ftn.model.GroupAdmin;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    User createUser(UserDTO userDTO);

    Optional<User> getById(Long id);

    User findByEmail(String email);

    List<User> getAll();

    User update(Long id, User user);

    User delete(Long id);

    void setRoleAsGroupAdmin(User user);

    User deleteRoleAsGroupAdmin(Long id);

}

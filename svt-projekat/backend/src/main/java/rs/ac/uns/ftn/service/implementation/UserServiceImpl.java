package rs.ac.uns.ftn.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.GroupAdmin;
import rs.ac.uns.ftn.model.Post;
import rs.ac.uns.ftn.model.Roles;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.repository.GroupAdminRepository;
import rs.ac.uns.ftn.repository.UserRepository;
import rs.ac.uns.ftn.service.GroupAdminService;
import rs.ac.uns.ftn.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupAdminRepository groupAdminRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GroupAdminService groupAdminService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User findByUsername(String username) {

        List<User> svi = this.getAll();
        for(User u : svi){
            if(u.getUsername().equals(username)){
                return u;
            }
        }

        return null;
    }

    @Override
    public User createUser(UserDTO userDTO) {

        User user = this.findByUsername(userDTO.getUsername());

        if(user != null){
            return null;
        }

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRole(Roles.USER);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setLastLogin(LocalDateTime.now());
        newUser.setEmail(userDTO.getEmail());
        newUser.setFriendsWith(new HashSet<>());
        newUser = userRepository.save(newUser);

        return newUser;
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return getAll().stream().filter(s->s.getEmail().equals(email)).findAny().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(Long id, User user) {

        Optional<User> toUpdate = this.getById(id);

        if (toUpdate.isPresent()) {

            toUpdate.get().setFirstName(user.getFirstName());
            toUpdate.get().setLastName(user.getLastName());
            toUpdate.get().setPassword(user.getPassword());
            toUpdate.get().setEmail(user.getEmail());
            toUpdate.get().setUsername(user.getUsername());
            toUpdate.get().setDisplayName(user.getDisplayName());
            toUpdate.get().setDescription(user.getDescription());
            userRepository.save(toUpdate.get());

            return toUpdate.get();

        } else {
            return null;
        }

    }

    @Override
    public User delete(Long id) {
        Optional<User> user = this.getById(id);
        if(user.isPresent()){
            userRepository.deleteById(id);
            return user.get();
        }else {return null;}
    }

    @Override
    @Transactional
    public void setRoleAsGroupAdmin(User user) {

        String jpql = "UPDATE User SET type = :value1, role = :value2 WHERE username = :value3";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("value1", "GA");
        query.setParameter("value2", Roles.GROUP_ADMIN);
        query.setParameter("value3", user.getUsername());

        int updatedCount = query.executeUpdate();

    }

    @Override
    @Transactional
    public void deleteRoleGroupAdmin(GroupAdmin user) {

        String jpql = "UPDATE User SET type = :value1, role = :value2 WHERE username = :value3";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("value1", "U");
        query.setParameter("value2", Roles.USER);
        query.setParameter("value3", user.getUsername());

        int updatedCount = query.executeUpdate();

    }
}

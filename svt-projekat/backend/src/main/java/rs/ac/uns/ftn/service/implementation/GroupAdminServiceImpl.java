package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.GroupAdmin;
import rs.ac.uns.ftn.model.Roles;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.repository.CommentRepository;
import rs.ac.uns.ftn.repository.GroupAdminRepository;
import rs.ac.uns.ftn.repository.UserRepository;
import rs.ac.uns.ftn.service.GroupAdminService;
import rs.ac.uns.ftn.service.UserService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class GroupAdminServiceImpl implements GroupAdminService {

    @Autowired
    GroupAdminRepository groupAdminRepository;

    @Override
    public List<GroupAdmin> getAll() {
        return groupAdminRepository.findAll();
    }

    @Override
    public Optional<GroupAdmin> getById(Long id) {
        return groupAdminRepository.findById(id);
    }

    @Override
    public GroupAdmin save(GroupAdmin groupAdmin) {

        GroupAdmin newUser = groupAdminRepository.save(groupAdmin);

        return newUser;
    }


    @Override
    public GroupAdmin update(Long id, GroupAdmin groupAdmin) {

        Optional<GroupAdmin> toUpdate = this.getById(id);

        if (toUpdate.isPresent()) {

            toUpdate.get().setFirstName(groupAdmin.getFirstName());
            toUpdate.get().setLastName(groupAdmin.getLastName());
            toUpdate.get().setPassword(groupAdmin.getPassword());
            toUpdate.get().setEmail(groupAdmin.getEmail());
            toUpdate.get().setUsername(groupAdmin.getUsername());
            groupAdminRepository.save(toUpdate.get());

            return toUpdate.get();

        } else {
            return null;
        }

    }

    @Override
    public GroupAdmin findByUsername(String username) {

        List<GroupAdmin> svi = this.getAll();
        for(GroupAdmin u : svi){
            if(u.getUsername().equals(username)){
                return u;
            }
        }

        return null;
    }

    @Override
    public GroupAdmin delete(Long id) {
        Optional<GroupAdmin> groupAdmin = this.getById(id);
        if(groupAdmin.isPresent()){
            groupAdminRepository.deleteById(id);
            return groupAdmin.get();
        }else {return null;}
    }
}

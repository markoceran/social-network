package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.GroupAdmin;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface GroupAdminService {

    GroupAdmin findByUsername(String username);
    List<GroupAdmin> getAll();
    Optional<GroupAdmin> getById(Long id);
    GroupAdmin save(GroupAdmin groupAdmin);
    GroupAdmin update(Long id, GroupAdmin groupAdmin);

    GroupAdmin delete(Long id);
}

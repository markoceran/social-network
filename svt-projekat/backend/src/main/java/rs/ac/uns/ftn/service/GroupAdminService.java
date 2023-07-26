package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.GroupAdmin;

import java.util.List;
import java.util.Optional;

public interface GroupAdminService {
    List<GroupAdmin> getAll();
    Optional<GroupAdmin> getById(Long id);

    GroupAdmin update(Long id, GroupAdmin groupAdmin);

    GroupAdmin delete(Long id);
}

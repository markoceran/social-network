package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Group> getAll();
    Optional<Group> getById(Long id);
    Group save(Group group);
}

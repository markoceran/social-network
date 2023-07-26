package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.GroupRequest;

import java.util.List;
import java.util.Optional;

public interface GroupRequestService {
    List<GroupRequest> getAll();
    Optional<GroupRequest> getById(Long id);
    GroupRequest save(GroupRequest groupRequest);

    GroupRequest update(Long id, GroupRequest groupRequest);
    GroupRequest delete(Long id);
}

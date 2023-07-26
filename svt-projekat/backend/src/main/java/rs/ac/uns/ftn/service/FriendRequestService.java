package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.FriendRequest;

import java.util.List;
import java.util.Optional;

public interface FriendRequestService {
    List<FriendRequest> getAll();
    Optional<FriendRequest> getById(Long id);
    FriendRequest save(FriendRequest friendRequest);

    FriendRequest update(Long id, FriendRequest friendRequest);
    FriendRequest delete(Long id);
}

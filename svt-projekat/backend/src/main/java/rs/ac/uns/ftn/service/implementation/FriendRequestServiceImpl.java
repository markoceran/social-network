package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.FriendRequest;
import rs.ac.uns.ftn.repository.CommentRepository;
import rs.ac.uns.ftn.repository.FriendRequestRepository;
import rs.ac.uns.ftn.service.FriendRequestService;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    FriendRequestRepository friendRequestRepository;


    @Override
    public List<FriendRequest> getAll() {
        return friendRequestRepository.findAll();
    }

    @Override
    public Optional<FriendRequest> getById(Long id) {
        return friendRequestRepository.findById(id);
    }

    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        try{
            return friendRequestRepository.save(friendRequest);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public FriendRequest update(Long id, FriendRequest friendRequest) {

        Optional<FriendRequest> toUpdate = this.getById(id);

        if (toUpdate.isPresent()) {

            toUpdate.get().setApproved(friendRequest.getApproved());
            toUpdate.get().setAt(friendRequest.getAt());
            friendRequestRepository.save(toUpdate.get());

            return toUpdate.get();

        } else {
            return null;
        }

    }

    @Override
    public FriendRequest delete(Long id) {

        Optional<FriendRequest> friendRequest = this.getById(id);
        if(friendRequest.isPresent()){
            friendRequestRepository.deleteById(id);
            return friendRequest.get();
        }else {return null;}

    }
}

package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.FriendRequest;
import rs.ac.uns.ftn.model.GroupRequest;
import rs.ac.uns.ftn.repository.GroupRequestRepository;
import rs.ac.uns.ftn.service.GroupRequestService;
import rs.ac.uns.ftn.service.GroupService;

import java.util.List;
import java.util.Optional;

@Service
public class GroupRequestServiceImpl implements GroupRequestService {

    @Autowired
    GroupRequestRepository groupRequestRepository;

    @Override
    public List<GroupRequest> getAll() {
        return groupRequestRepository.findAll();
    }

    @Override
    public Optional<GroupRequest> getById(Long id) {
        return groupRequestRepository.findById(id);
    }

    @Override
    public GroupRequest save(GroupRequest groupRequest) {
        try{
            return groupRequestRepository.save(groupRequest);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public GroupRequest update(Long id, GroupRequest groupRequest) {

        Optional<GroupRequest> toUpdate = this.getById(id);

        if (toUpdate.isPresent()) {

            toUpdate.get().setApproved(groupRequest.getApproved());
            toUpdate.get().setAt(groupRequest.getAt());
            groupRequestRepository.save(toUpdate.get());

            return toUpdate.get();

        } else {
            return null;
        }

    }


    @Override
    public GroupRequest delete(Long id) {

        Optional<GroupRequest> groupRequest = this.getById(id);
        if(groupRequest.isPresent()){
            groupRequestRepository.deleteById(id);
            return groupRequest.get();
        }else {return null;}

    }
}

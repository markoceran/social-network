package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Groupp;
import rs.ac.uns.ftn.repository.GroupRepository;
import rs.ac.uns.ftn.service.GroupService;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Override
    public List<Groupp> getAll() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<Groupp> getById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public Groupp save(Groupp group) {

        try{
            return groupRepository.save(group);
        }catch (IllegalArgumentException e){
            return null;
        }

    }

    @Override
    public Groupp update(Long id, Groupp group) {

        Optional<Groupp> toUpdate = this.getById(id);

        if (toUpdate.isPresent()) {

            toUpdate.get().setName(group.getName());
            toUpdate.get().setDescription(group.getDescription());
            toUpdate.get().setCreationDate(group.getCreationDate());
            toUpdate.get().setSuspendedReason(group.getSuspendedReason());
            toUpdate.get().setIsSuspended(group.getIsSuspended());
            toUpdate.get().setContains(group.getContains());
            toUpdate.get().setGroupAdmins(group.getGroupAdmins());

            groupRepository.save(toUpdate.get());

            return toUpdate.get();

        } else {
            return null;
        }


    }

    @Override
    public Groupp delete(Long id) {

        Optional<Groupp> groupp = this.getById(id);
        if(groupp.isPresent()){
            groupp.get().setIsSuspended(true);
            groupRepository.save(groupp.get());
            return groupp.get();
        }else {return null;}

    }

}

package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}

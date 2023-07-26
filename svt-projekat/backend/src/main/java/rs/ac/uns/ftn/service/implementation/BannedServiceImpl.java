package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Banned;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.repository.AdministratorRepository;
import rs.ac.uns.ftn.repository.BannedRepository;
import rs.ac.uns.ftn.service.BannedService;

import java.util.List;
import java.util.Optional;

@Service
public class BannedServiceImpl implements BannedService {

    @Autowired
    BannedRepository bannedRepository;

    @Override
    public List<Banned> getAll() {
        return bannedRepository.findAll();
    }

    @Override
    public Optional<Banned> getById(Long id) {
        return bannedRepository.findById(id);
    }

    @Override
    public Banned save(Banned banned) {
        try{
            return bannedRepository.save(banned);
        }catch (IllegalArgumentException e){
            return null;
        }
    }


    @Override
    public Banned delete(Long id) {

        Optional<Banned> banned = this.getById(id);
        if(banned.isPresent()){
            bannedRepository.deleteById(id);
            return banned.get();
        }else {return null;}

    }
}

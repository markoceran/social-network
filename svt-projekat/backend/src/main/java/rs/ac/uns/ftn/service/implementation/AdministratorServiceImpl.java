package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.repository.AdministratorRepository;
import rs.ac.uns.ftn.repository.CommentRepository;
import rs.ac.uns.ftn.service.AdministratorService;

import java.util.List;
import java.util.Optional;

@Service
public class AdministratorServiceImpl implements AdministratorService {

    @Autowired
    AdministratorRepository administratorRepository;

    @Override
    public List<Administrator> getAll() {
        return administratorRepository.findAll();
    }

    @Override
    public Optional<Administrator> getById(Long id) {
        return administratorRepository.findById(id);
    }

}

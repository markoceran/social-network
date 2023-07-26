package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Post;

import java.util.List;
import java.util.Optional;

public interface AdministratorService {

    List<Administrator> getAll();
    Optional<Administrator> getById(Long id);

}

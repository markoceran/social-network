package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.Reaction;

import java.util.List;
import java.util.Optional;

public interface ReactionService {
    List<Reaction> getAll();
    Optional<Reaction> getById(Long id);
    Reaction save(Reaction reaction);

    Reaction update(Long id, Reaction reaction);

    Reaction delete(Long id);
}

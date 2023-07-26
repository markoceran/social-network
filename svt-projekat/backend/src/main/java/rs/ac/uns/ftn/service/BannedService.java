package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.Banned;

import java.util.List;
import java.util.Optional;

public interface BannedService {

    List<Banned> getAll();
    Optional<Banned> getById(Long id);
    Banned save(Banned banned);

    Banned delete(Long id);
}

package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Groupp;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Groupp> getAll();
    Optional<Groupp> getById(Long id);
    Groupp save(Groupp group);
}

package rs.ac.uns.ftn.service;

import org.springframework.web.bind.annotation.ExceptionHandler;
import rs.ac.uns.ftn.model.Groupp;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Groupp> getAll();
    Optional<Groupp> getById(Long id);

    Groupp save(Groupp group);

    Groupp update(Long id, Groupp group);

    Groupp delete(Long id);

}

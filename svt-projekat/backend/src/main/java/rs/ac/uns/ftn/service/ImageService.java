package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.Image;

import java.util.List;
import java.util.Optional;

public interface ImageService {
    List<Image> getAll();
    Optional<Image> getById(Long id);
    Image save(Image image);

    Image delete(Long id);
}

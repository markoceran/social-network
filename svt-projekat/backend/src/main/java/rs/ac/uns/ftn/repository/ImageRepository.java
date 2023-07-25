package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}

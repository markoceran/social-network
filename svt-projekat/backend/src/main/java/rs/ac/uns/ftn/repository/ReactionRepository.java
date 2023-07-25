package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Reaction;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}

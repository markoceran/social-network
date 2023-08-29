package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Post;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}

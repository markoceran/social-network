package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}

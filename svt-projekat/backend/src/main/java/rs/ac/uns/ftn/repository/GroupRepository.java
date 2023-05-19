package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Groupp;

@Repository
public interface GroupRepository extends JpaRepository<Groupp, Long> {
}

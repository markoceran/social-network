package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequest extends JpaRepository<rs.ac.uns.ftn.model.FriendRequest, Long> {
}

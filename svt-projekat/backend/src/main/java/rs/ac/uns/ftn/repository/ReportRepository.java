package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}

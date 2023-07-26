package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Administrator;
import rs.ac.uns.ftn.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportService {
    List<Report> getAll();
    Optional<Report> getById(Long id);
    Report save(Report report);

    Report update(Long id, Report report);

    Report delete(Long id);
}

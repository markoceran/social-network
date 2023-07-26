package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Reaction;
import rs.ac.uns.ftn.model.Report;
import rs.ac.uns.ftn.repository.CommentRepository;
import rs.ac.uns.ftn.repository.ReportRepository;
import rs.ac.uns.ftn.service.ReportService;

import java.util.List;
import java.util.Optional;

@Repository
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepository reportRepository;


    @Override
    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    @Override
    public Optional<Report> getById(Long id) {
        return reportRepository.findById(id);
    }

    @Override
    public Report save(Report report) {
        try{
            return reportRepository.save(report);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public Report update(Long id, Report report) {

        Optional<Report> toUpdate = this.getById(id);

        if (toUpdate.isPresent()) {

            toUpdate.get().setReason(report.getReason());
            toUpdate.get().setTimestamp(report.getTimestamp());
            toUpdate.get().setAccepted(report.getAccepted());
            reportRepository.save(toUpdate.get());

            return toUpdate.get();

        } else {
            return null;
        }

    }

    @Override
    public Report delete(Long id) {

        Optional<Report> report = this.getById(id);
        if(report.isPresent()){
            reportRepository.deleteById(id);
            return report.get();
        }else {return null;}

    }
}

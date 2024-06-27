package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.QueryPreprocessor;
import rs.ac.uns.ftn.model.GroupDocument;
import rs.ac.uns.ftn.repository.GroupDocumentRepository;

import java.util.List;

@Service
public class GroupSearchService {

    @Autowired
    private GroupDocumentRepository groupDocumentRepository;

    @Autowired
    private QueryPreprocessor queryPreprocessor;

    public List<GroupDocument> searchGroupsByName(String name) {
        String preprocessedName = queryPreprocessor.preprocessQuery(name);
        return groupDocumentRepository.findByNameContainingIgnoreCase(preprocessedName);
    }

    public List<GroupDocument> searchGroupsByDescription(String description) {
        String preprocessedDescription = queryPreprocessor.preprocessQuery(description);
        return groupDocumentRepository.findByDescriptionContainingIgnoreCase(preprocessedDescription);
    }

    public List<GroupDocument> searchGroupsByPdfDescription(String pdfDescription) {
        String preprocessedPdfDescription = queryPreprocessor.preprocessQuery(pdfDescription);
        return groupDocumentRepository.findByPdfDescriptionContainingIgnoreCase(preprocessedPdfDescription);
    }
}

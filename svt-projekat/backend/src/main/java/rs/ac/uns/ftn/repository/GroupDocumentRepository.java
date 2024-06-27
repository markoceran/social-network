package rs.ac.uns.ftn.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import rs.ac.uns.ftn.model.GroupDocument;

import java.util.List;

public interface GroupDocumentRepository extends ElasticsearchRepository<GroupDocument, Long> {
    List<GroupDocument> findByNameContainingIgnoreCase(String name);
    List<GroupDocument> findByDescriptionContainingIgnoreCase(String description);
    List<GroupDocument> findByPdfDescriptionContainingIgnoreCase(String pdfDescription);
}

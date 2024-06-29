package rs.ac.uns.ftn.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import rs.ac.uns.ftn.model.PostDocument;

import java.util.List;

public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, Long> {
    List<PostDocument> findByTitleContainingIgnoreCase(String title);
    List<PostDocument> findByContentContainingIgnoreCase(String content);
    List<PostDocument> findByPdfContentContainingIgnoreCase(String pdfContent);
}

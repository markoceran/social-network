package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.QueryPreprocessor;
import rs.ac.uns.ftn.model.PostDocument;
import rs.ac.uns.ftn.repository.PostDocumentRepository;

import java.util.List;

@Service
public class PostSearchService {

    @Autowired
    private PostDocumentRepository postDocumentRepository;

    @Autowired
    private QueryPreprocessor queryPreprocessor;

    public List<PostDocument> searchPostsByTitle(String title) {
        String preprocessedTitle = queryPreprocessor.preprocessQuery(title);
        return postDocumentRepository.findByTitleContainingIgnoreCase(preprocessedTitle);
    }

    public List<PostDocument> searchPostsByContent(String content) {
        String preprocessedContent = queryPreprocessor.preprocessQuery(content);
        return postDocumentRepository.findByContentContainingIgnoreCase(preprocessedContent);
    }

    public List<PostDocument> searchPostsByPdfContent(String pdfContent) {
        String preprocessedPdfContent = queryPreprocessor.preprocessQuery(pdfContent);
        return postDocumentRepository.findByPdfContentContainingIgnoreCase(preprocessedPdfContent);
    }
}

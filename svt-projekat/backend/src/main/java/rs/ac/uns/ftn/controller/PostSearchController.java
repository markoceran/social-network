package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.model.PostDocument;
import rs.ac.uns.ftn.service.implementation.PostSearchService;

import java.util.List;

@RestController
public class PostSearchController {

    @Autowired
    private PostSearchService postSearchService;

    @GetMapping("/search/posts/title")
    public List<PostDocument> searchPostsByTitle(@RequestParam String title) {
        return postSearchService.searchPostsByTitle(title);
    }

    @GetMapping("/search/posts/content")
    public List<PostDocument> searchPostsByContent(@RequestParam String content) {
        return postSearchService.searchPostsByContent(content);
    }

    @GetMapping("/search/posts/pdfContent")
    public List<PostDocument> searchPostsByPdfContent(@RequestParam String pdfContent) {
        return postSearchService.searchPostsByPdfContent(pdfContent);
    }
}

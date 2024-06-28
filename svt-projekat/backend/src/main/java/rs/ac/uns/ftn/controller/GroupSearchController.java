package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.model.GroupDocument;
import rs.ac.uns.ftn.service.implementation.GroupSearchService;

import java.util.List;

@RestController
public class GroupSearchController {

    @Autowired
    private GroupSearchService groupSearchService;

    @GetMapping("/search/groups/name")
    public List<GroupDocument> searchGroupsByName(@RequestParam String name) {
        return groupSearchService.searchGroupsByName(name);
    }

    @GetMapping("/search/groups/description")
    public List<GroupDocument> searchGroupsByDescription(@RequestParam String description) {
        return groupSearchService.searchGroupsByDescription(description);
    }

    @GetMapping("/search/groups/pdfDescription")
    public List<GroupDocument> searchGroupsByPdfDescription(@RequestParam String pdfDescription) {
        return groupSearchService.searchGroupsByPdfDescription(pdfDescription);
    }
}

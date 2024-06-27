package rs.ac.uns.ftn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "groups")
public class GroupDocument {

    @Id
    private Long id;
    private String name;
    private String description;
    private String pdfDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPdfDescription() {
        return pdfDescription;
    }

    public void setPdfDescription(String pdfDescription) {
        this.pdfDescription = pdfDescription;
    }
}

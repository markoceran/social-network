package rs.ac.uns.ftn.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Setter
@Getter
@Document(indexName = "groups")
public class GroupDocument {

    @Id
    private Long id;
    private String name;
    private String description;
    private String pdfDescription;

}

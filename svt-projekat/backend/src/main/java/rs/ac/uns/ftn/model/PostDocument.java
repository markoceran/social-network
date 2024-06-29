package rs.ac.uns.ftn.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Setter
@Getter
@Document(indexName = "posts")
public class PostDocument {

    @Id
    private Long id;
    private String title;
    private String content;
    private String pdfContent;
}

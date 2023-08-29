package rs.ac.uns.ftn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "creationDate",nullable = false)
    private LocalDateTime creationDate;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private User postedBy;

    @Column(name = "isDeleted",nullable = false)
    private Boolean isDeleted;

}

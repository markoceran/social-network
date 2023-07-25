package rs.ac.uns.ftn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path",nullable = false)
    private String path;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Post post;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User belongsTo;
}

package rs.ac.uns.ftn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text",nullable = false)
    private String text;

    @Column(name = "timestamp",nullable = false)
    private LocalDate timestamp;

    @Column(name = "isDeleted",nullable = false)
    private Boolean isDeleted;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User belongsTo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name = "RepliesOfComment",
            joinColumns = @JoinColumn(name = "comment_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "reply_id",referencedColumnName = "id"))

    private Set<Comment> repliesTo = new HashSet<Comment>();

}
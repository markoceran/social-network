package rs.ac.uns.ftn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type",nullable = false)
    @Enumerated(EnumType.STRING)
    private ReactionType type;

    @Column(name = "timestamp",nullable = false)
    private LocalDate timestamp;
    @OneToOne(fetch = FetchType.EAGER)
    @Nullable
    private Post post;

    @OneToOne(fetch = FetchType.EAGER)
    private User madeBy;

    @OneToOne(fetch = FetchType.EAGER)
    @Nullable
    private Comment comment;
}

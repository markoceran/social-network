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
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason",nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @Column(name = "timestamp",nullable = false)
    private LocalDate timestamp;

    @OneToOne(fetch = FetchType.EAGER)
    private User byUser;

    @Column(name = "accepted",nullable = false)
    private Boolean accepted = false;

    @OneToOne(fetch = FetchType.EAGER)
    @Nullable
    private Post post;

    @OneToOne(fetch = FetchType.EAGER)
    @Nullable
    private Comment comment;

    @OneToOne(fetch = FetchType.EAGER)
    @Nullable
    private User user;
}

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
public class Banned {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp",nullable = false)
    private LocalDate timestamp;

    @OneToOne(fetch = FetchType.EAGER)
    @Nullable
    private GroupAdmin by1;

    @OneToOne(fetch = FetchType.EAGER)
    @Nullable
    private Administrator by2;

    @OneToOne(fetch = FetchType.EAGER)
    @Nullable
    private Groupp group;

    @OneToOne(fetch = FetchType.EAGER)
    @Nullable
    private User user;
}

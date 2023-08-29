package rs.ac.uns.ftn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "approved",nullable = false)
    private Boolean approved;
    @Column(name = "createdAt",nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "at")
    @Nullable
    private LocalDateTime at;

    @OneToOne(fetch = FetchType.EAGER)
    private User from;

    @OneToOne(fetch = FetchType.EAGER)
    private User forr;
}

package rs.ac.uns.ftn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "creationDate",nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "isSuspended",nullable = false)
    private Boolean isSuspended;

    @Column(name = "suspendedReason",nullable = false)
    private String suspendedReason;

    @ManyToMany
    @JoinTable(name = "group_post",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"))
    private Set<Post> contains = new HashSet<Post>();

    @ManyToMany
    @JoinTable(name = "group_admin",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "id"))
    private Set<GroupAdmin> groupAdmins = new HashSet<GroupAdmin>();



}

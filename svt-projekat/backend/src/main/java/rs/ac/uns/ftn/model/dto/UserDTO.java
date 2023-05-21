package rs.ac.uns.ftn.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email(message="Mora biti email")
    private String email;

    @NotBlank
    private LocalDateTime lastLogin;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private Set<User> friendsWith = new HashSet<User>();

    public UserDTO(User createdUser) {
        this.id = createdUser.getId();
        this.username = createdUser.getUsername();
        this.email = createdUser.getEmail();
        this.lastLogin = createdUser.getLastLogin();
        this.firstName = createdUser.getFirstName();
        this.lastName = createdUser.getLastName();
        this.friendsWith = createdUser.getFriendsWith();
    }
}

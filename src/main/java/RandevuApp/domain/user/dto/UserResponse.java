package RandevuApp.domain.user.dto;

import RandevuApp.domain.user.model.Gender;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String address;
    private Set<Role> roles;
    private UserStatus status;
}

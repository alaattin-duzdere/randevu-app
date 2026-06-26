package RandevuApp.test;

import RandevuApp.domain.user.model.Gender;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestUserService {

    private final PasswordEncoder passwordEncoder;

    public User createMockUser(String pass,String email){
        return User.builder()
                .password(passwordEncoder.encode(pass))
                .email(email)
                .gender(Gender.MALE)
                .roles(Set.of(Role.USER))
                .firstName("Alaattin")
                .lastName("Düzdere")
                .status(UserStatus.ACTIVE)
                .phoneNumber("5464934661")
                .build();
    }

}

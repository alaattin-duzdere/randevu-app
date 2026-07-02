package RandevuApp.domain.user.mapper;

import RandevuApp.domain.user.dto.UserResponse;
import RandevuApp.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse userToUserResponse(User user){
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getRoles(),
                user.getStatus()
        );
    }
}

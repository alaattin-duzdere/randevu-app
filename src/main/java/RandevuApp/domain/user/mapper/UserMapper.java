package RandevuApp.domain.user.mapper;

import RandevuApp.domain.user.dto.UserResponse;
import RandevuApp.domain.user.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse userToUserResponse(User user){
        UserResponse userResponse = new UserResponse();

        BeanUtils.copyProperties(user,userResponse);

        return userResponse;
    }
}

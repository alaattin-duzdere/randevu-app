package RandevuApp.domain.user.dto;

import RandevuApp.domain.user.model.Gender;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.UserStatus;

import java.util.Set;

public record UserResponse(
        Long id,
        String email,
        String phoneNumber,
        String firstName,
        String lastName,
        Gender gender,
        String address,
        Set<Role> roles,
        UserStatus status
) {}

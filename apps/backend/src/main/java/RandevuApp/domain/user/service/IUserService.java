package RandevuApp.domain.user.service;

import RandevuApp.domain.user.dto.*;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface IUserService {

    UserResponse updateCurrentUserProfile(Long userId,UpdateUserRequest request);

    void changePassword(Long userId, ChangePasswordRequest request);

    void initiatePhoneChange(Long userId, PhoneChangeInitiateRequest request);

    void changePhone(Long userId);

    void initiateEmailChange(Long userId, EmailChangeInitiateRequest request);

    void changeEmail(Long userId);

    void initiateEmailVerification(Long userId);

    void deleteUser(Long userId);

    // Admin
    Page<UserResponse> getAllUsers(Pageable pageable, String search);

    UserResponse getUserById(Long id);

    void changeUserStatus(Long id, UserStatus status);

    UserResponse updateUserRoles(Long id, Set<Role> roles);
}

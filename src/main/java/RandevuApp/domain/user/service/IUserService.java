package RandevuApp.domain.user.service;

import RandevuApp.domain.user.dto.ChangePasswordRequest;
import RandevuApp.domain.user.dto.ContactChangeInitiateRequest;
import RandevuApp.domain.user.dto.UpdateUserRequest;
import RandevuApp.domain.user.dto.UserResponse;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface IUserService {

    UserResponse updateCurrentUserProfile(Long userId,UpdateUserRequest request);

    void changePassword(Long userId, ChangePasswordRequest request);

    void initiatePhoneChange(Long userId, ContactChangeInitiateRequest request);

    void changePhone(Long userId);

    void initiateEmailChange(Long userId, ContactChangeInitiateRequest request);

    void changeEmail(Long userId);

    void initiateEmailVerification(Long userId);

    void deleteUser(Long userId);

    // Admin
    Page<UserResponse> getAllUsers(Pageable pageable, String search);

    UserResponse getUserById(Long id);

    void changeUserStatus(Long id, UserStatus status);

    UserResponse updateUserRoles(Long id, Set<Role> roles);
}

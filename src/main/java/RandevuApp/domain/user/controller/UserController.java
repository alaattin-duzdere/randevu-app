package RandevuApp.domain.user.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.user.dto.*;
import RandevuApp.domain.user.service.IUserService;
import RandevuApp.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/me")
    public ResponseEntity<CustomResponseBody<UserResponse>> getMyProfile() {
        UserResponse userResponse = userService .getUserById(SecurityUtils.getCurrentUserId());
        return ResponseEntity.ok(CustomResponseBody.ok(userResponse, "Profile retrieved successfully"));
    }

    @PutMapping("/me")
    public ResponseEntity<CustomResponseBody<UserResponse>> updateMyProfile(@Valid @RequestBody UpdateUserRequest request) {
        UserResponse updatedUser = userService.updateCurrentUserProfile(SecurityUtils.getCurrentUserId(),request);
        return ResponseEntity.ok(CustomResponseBody.ok(updatedUser, "Profile updated successfully"));
    }

    @PatchMapping("/me/change-password")
    public ResponseEntity<CustomResponseBody<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(SecurityUtils.getCurrentUserId(),request);
        return ResponseEntity.noContent().build();
        //return ResponseEntity.ok(CustomResponseBody.ok(null, "Password changed successfully"));
    }

    @PostMapping("/phone/change-request")
    public ResponseEntity<Void> initiatePhoneChange(@Valid @RequestBody PhoneChangeInitiateRequest request) {
        userService.initiatePhoneChange(SecurityUtils.getCurrentUserId(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email/change-request")
    public ResponseEntity<Void> initiateEmailChange(@Valid @RequestBody EmailChangeInitiateRequest request) {
        userService.initiateEmailChange(SecurityUtils.getCurrentUserId(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/verify-email")
    public ResponseEntity<CustomResponseBody<Void>> initiateEmailVerification() {
        userService.initiateEmailVerification(SecurityUtils.getCurrentUserId());
        return ResponseEntity.ok(CustomResponseBody.ok(null, "Email verification link sent successfully"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<CustomResponseBody<Void>> deleteMyAccount() {
        userService.deleteUser(SecurityUtils.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}

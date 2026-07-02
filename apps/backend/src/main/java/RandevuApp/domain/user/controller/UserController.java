package RandevuApp.domain.user.controller;

import RandevuApp.domain.user.dto.*;
import RandevuApp.domain.user.service.IUserService;
import RandevuApp.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/me")
    public UserResponse getMyProfile() {
        return userService .getUserById(SecurityUtils.getCurrentUserId());
    }

    @PutMapping("/me")
    public UserResponse updateMyProfile(@Valid @RequestBody UpdateUserRequest request) {
        return userService.updateCurrentUserProfile(SecurityUtils.getCurrentUserId(),request);
    }

    @PatchMapping("/me/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(SecurityUtils.getCurrentUserId(),request);
    }

    @PostMapping("/phone/change-request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void initiatePhoneChange(@Valid @RequestBody PhoneChangeInitiateRequest request) {
        userService.initiatePhoneChange(SecurityUtils.getCurrentUserId(), request);
    }

    @PostMapping("/email/change-request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void initiateEmailChange(@Valid @RequestBody EmailChangeInitiateRequest request) {
        userService.initiateEmailChange(SecurityUtils.getCurrentUserId(), request);
    }

    @PostMapping("/me/verify-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void initiateEmailVerification() {
        userService.initiateEmailVerification(SecurityUtils.getCurrentUserId());
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyAccount() {
        userService.deleteUser(SecurityUtils.getCurrentUserId());
    }
}

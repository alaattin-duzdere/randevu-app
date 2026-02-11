package RandevuApp.domain.user.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.user.dto.UserResponse;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final IUserService userService;

    @GetMapping
    public ResponseEntity<CustomResponseBody<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Page<UserResponse> users = userService.getAllUsers(PageRequest.of(page, size), search);
        return ResponseEntity.ok(CustomResponseBody.ok(users, "Users retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponseBody<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(CustomResponseBody.ok(user, "User details retrieved"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomResponseBody<Void>> changeUserStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status) {
        userService.changeUserStatus(id, status);
        return ResponseEntity.ok(CustomResponseBody.ok(null, "User status updated to " + status));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<CustomResponseBody<UserResponse>> updateUserRoles(
            @PathVariable Long id,
            @RequestBody Set<Role> roles) {
        UserResponse user = userService.updateUserRoles(id, roles);
        return ResponseEntity.ok(CustomResponseBody.ok(user, "User roles updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponseBody<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(CustomResponseBody.ok(null, "User deleted successfully"));
    }
}

package RandevuApp.domain.auth.controller;

import RandevuApp.api.ApiStatus;
import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.auth.dto.*;
import RandevuApp.domain.auth.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponseBody<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        CustomResponseBody<LoginResponse> body = CustomResponseBody.ok(authService.login(request), "Login successful");
        return new ResponseEntity<>(body, HttpStatus.valueOf(body.getHttpStatus()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<CustomResponseBody<RefreshResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        CustomResponseBody<RefreshResponse> body = CustomResponseBody.ok(authService.refresh(request), "Token refreshed successfully");
        return new ResponseEntity<>(body, HttpStatus.valueOf(body.getHttpStatus()));
    }

    @PostMapping("/logout")
    public ResponseEntity<CustomResponseBody<?>> logout(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            String token = headerAuth.substring(7);

            CustomResponseBody<String> body = CustomResponseBody.ok(authService.logout(token), "Successfully completed");
            return new ResponseEntity<>(body, HttpStatusCode.valueOf(body.getHttpStatus()));
        }
        CustomResponseBody<Object> body = CustomResponseBody.failure(ApiStatus.ERROR_INVALID_INPUT, "No token found to invalidate");
        return new ResponseEntity<>(body,HttpStatusCode.valueOf(body.getHttpStatus()));
    }
}

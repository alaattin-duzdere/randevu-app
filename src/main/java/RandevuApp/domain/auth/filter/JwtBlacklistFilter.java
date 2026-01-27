package RandevuApp.domain.auth.filter;

import RandevuApp.domain.auth.service.TokenBlacklistService;
import RandevuApp.exceptions.auth.InvalidTokenException;
import com.authcore.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final TokenBlacklistService tokenBlacklistService;
    private final HandlerExceptionResolver exceptionResolver;
    private final JwtService jwtService;

    public JwtBlacklistFilter(TokenBlacklistService tokenBlacklistService,
                              @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver, JwtService jwtService) {
        this.tokenBlacklistService = tokenBlacklistService;
        this.exceptionResolver = exceptionResolver;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");
            final String token;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            token = authHeader.substring(7);

            String jti = jwtService.extractClaim(token, Claims::getId);

            if (jti != null && tokenBlacklistService.isTokenBlacklisted(jti)) {
                throw new InvalidTokenException("This token has been blacklisted (logged out).");
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            this.exceptionResolver.resolveException(request, response, null, e);
        }
    }
}

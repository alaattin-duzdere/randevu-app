package RandevuApp.security.filter;

import RandevuApp.domain.auth.service.TokenBlacklistService;
import RandevuApp.exceptions.auth.InvalidTokenException;
import com.authcore.property.AuthProperties;
import com.authcore.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final TokenBlacklistService tokenBlacklistService;
    private final HandlerExceptionResolver exceptionResolver;
    private final JwtService jwtService;
    private final AuthProperties authProperties;

    public JwtBlacklistFilter(TokenBlacklistService tokenBlacklistService,
                              @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver, JwtService jwtService, AuthProperties authProperties) {
        this.tokenBlacklistService = tokenBlacklistService;
        this.exceptionResolver = exceptionResolver;
        this.jwtService = jwtService;
        this.authProperties = authProperties;
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

    @Override
    protected boolean shouldNotFilter(@org.jspecify.annotations.NonNull HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String path = request.getServletPath();

        String[] patterns = new String[0];
        if (authProperties != null && authProperties.getWhitelist() != null) {
            patterns = authProperties.getWhitelist().toArray(new String[0]);
        }

        boolean shouldNotFilter = Arrays.stream(patterns)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));

        if (shouldNotFilter) {
            log.trace("Skipping JWT filter for public path: {}", path);
        }

        return shouldNotFilter;
    }
}

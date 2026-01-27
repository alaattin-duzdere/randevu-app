package RandevuApp.config;

import RandevuApp.domain.auth.filter.JwtBlacklistFilter;
import com.authcore.filter.JwtAuthenticationFilter;
import com.authcore.handler.OAuth2SuccessHandler;
import com.authcore.property.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final AuthProperties authProperties;

    public SecurityConfig(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtBlacklistFilter jwtBlacklistFilter,
            @Autowired(required = false) OAuth2SuccessHandler successHandler
    ) throws Exception
    {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(authProperties.getWhitelist().toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((sess) -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtBlacklistFilter, UsernamePasswordAuthenticationFilter.class) // first blacklist filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // second authentication filter
        if (successHandler != null && this.authProperties.isEnableOauth()) {
            http.oauth2Login((oauth2) -> oauth2.successHandler(successHandler));
        }

        return http.build();
    }
}

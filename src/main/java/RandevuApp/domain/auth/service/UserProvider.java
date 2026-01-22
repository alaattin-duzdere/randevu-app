package RandevuApp.domain.auth.service;

import RandevuApp.domain.auth.model.SecurityUser;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.repository.UserRepository;
import com.authcore.context.AuthUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProvider implements AuthUserProvider {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByIdentifier(String identifier) {
        long id = Long.parseLong(identifier);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı ID ile bulunamadı: " + identifier));
        return new SecurityUser(user);
    }
}

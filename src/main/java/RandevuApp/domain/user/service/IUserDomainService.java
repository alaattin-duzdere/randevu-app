package RandevuApp.domain.user.service;

import RandevuApp.domain.auth.dto.RegisterRequest;
import RandevuApp.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserDomainService {
    User findUserByIdentifier(String identifier);

    User findUserById(Long id);

    User createNewUser(RegisterRequest request, String encodedPassword);

    User saveUser(User user);

    Page<User> getAllUsers(Pageable pageable, String search);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}

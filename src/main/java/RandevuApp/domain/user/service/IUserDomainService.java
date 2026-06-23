package RandevuApp.domain.user.service;

import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.service.param.CreateUserParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserDomainService {
    User findUserByIdentifier(String identifier);

    User findUserById(Long id);

    User createNewUser(CreateUserParams params, String encodedPassword);

    User saveUser(User user);

    Page<User> getAllUsers(Pageable pageable, String search);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByVerifiedEmail(String email);

    void clearUnverifiedEmailExcluding(String email, Long excludedUserId);

    void deleteUser(User user);
}

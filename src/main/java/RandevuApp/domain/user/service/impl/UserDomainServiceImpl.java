package RandevuApp.domain.user.service.impl;

import RandevuApp.commons.util.ContactFormatUtil;
import RandevuApp.domain.auth.repository.RefreshTokenRepository;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.service.param.CreateUserParams;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.domain.user.repository.UserSpecifications;
import RandevuApp.domain.user.service.IUserDomainService;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDomainServiceImpl implements IUserDomainService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public User findUserByIdentifier(String identifier) {
        if (ContactFormatUtil.isEmail(identifier)) {
            return userRepository.findByEmailAndEmailVerifiedAtIsNotNull(identifier)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", identifier));

        } else if (ContactFormatUtil.isPhone(identifier)) {
            return userRepository.findByPhoneNumber(identifier)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "phone", identifier));

        } else {
            throw new InvalidInputException("Invalid email/phone format");
        }
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    @Transactional
    public User createNewUser(CreateUserParams params, String encodedPassword) {

        return User.builder()
                .email(params.email())
                .phoneNumber(params.phoneNumber())
                .password(encodedPassword)
                .firstName(params.firstName())
                .lastName(params.lastName())
                .address(params.address())
                .gender(params.gender())
                .roles(Set.of(Role.USER))
                .status(UserStatus.PENDING)
                .build();
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable, String search) {
        Specification<User> spec = UserSpecifications.withSearch(search);

        return userRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsByVerifiedEmail(String email) {
        return userRepository.existsByEmailAndEmailVerifiedAtIsNotNull(email);
    }

    @Override
    @Transactional
    public void clearUnverifiedEmailExcluding(String email, Long excludedUserId) {
        List<User> usersToUpdate = userRepository.findAllByEmailAndEmailVerifiedAtIsNullAndIdNot(email, excludedUserId);

        if (usersToUpdate.isEmpty()) {
            return;
        }

        usersToUpdate.forEach(u -> u.setEmail(null));
        userRepository.saveAll(usersToUpdate);

        log.info("{} unverified email addresses were cleared (email: {}, triggered by userId: {}).",
                usersToUpdate.size(), email, excludedUserId);
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        refreshTokenRepository.deleteByUserId(user.getId());

        String timestamp = String.valueOf(System.currentTimeMillis());

        if (user.getEmail() != null) {
            user.setEmail("deleted_" + timestamp + "_" + user.getEmail());
        }
        user.setPhoneNumber("deleted_" + timestamp + "_" + user.getPhoneNumber());

        user.setFirstName("Deleted User");
        user.setLastName(timestamp);
        user.setPassword("password_for_deleted_user");
        user.setAddress(null);
        user.setDeleted(true);

        userRepository.save(user);

        log.info("User (ID: {}) was anonymized and soft-deleted.", user.getId());
    }
}
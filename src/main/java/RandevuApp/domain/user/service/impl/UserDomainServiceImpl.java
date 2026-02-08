package RandevuApp.domain.user.service.impl;

import RandevuApp.commons.validator.ContactValidator;
import RandevuApp.domain.auth.dto.RegisterRequest;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.domain.user.repository.UserSpecifications;
import RandevuApp.domain.user.service.IUserDomainService;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDomainServiceImpl implements IUserDomainService {

    private final UserRepository userRepository;
    private final ContactValidator contactValidator;

    @Override
    public User findUserByIdentifier(String identifier) {
        if (contactValidator.isValidEmail(identifier)) {
            return userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", identifier));
        } else if (contactValidator.isValidPhoneNumber(identifier)) {
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
    public User createNewUser(RegisterRequest request, String encodedPassword) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already in use");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ConflictException("Phone number already in use");
        }

        return User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(encodedPassword)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .gender(request.getGender())
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
}

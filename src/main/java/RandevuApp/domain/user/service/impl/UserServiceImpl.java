package RandevuApp.domain.user.service.impl;

import RandevuApp.commons.validator.ContactValidator;
import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.repository.AppointmentRepository;
import RandevuApp.domain.business.repository.BusinessRepository;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.user.dto.ChangePasswordRequest;
import RandevuApp.domain.user.dto.ContactChangeInitiateRequest;
import RandevuApp.domain.user.dto.UpdateUserRequest;
import RandevuApp.domain.user.dto.UserResponse;
import RandevuApp.domain.user.mapper.UserMapper;
import RandevuApp.domain.user.model.Role;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserChangeRequest;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.repository.UserChangeRequestRepository;
import RandevuApp.domain.user.service.IUserDomainService;
import RandevuApp.domain.user.service.IUserService;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationRequest;
import RandevuApp.domain.verification.model.VerificationType;
import RandevuApp.domain.verification.service.VerificationService;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.PasswordMismatchException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import RandevuApp.exceptions.client.ObjectDeletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserDomainService userDomainService;
    private final ContactValidator contactValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    private final UserChangeRequestRepository userChangeRequestRepository ;
    private final BusinessRepository businessRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userDomainService.findUserById(userId);
        return userMapper.userToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateCurrentUserProfile(Long userId,UpdateUserRequest request) {
        User user = userDomainService.findUserById(userId);

        user.setAddress(request.getAddress());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());

        userDomainService.saveUser(user);
        return userMapper.userToUserResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userDomainService.findUserById(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new PasswordMismatchException("Current password is incorrect.");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())){
            throw new PasswordMismatchException("New passwords do not match.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userDomainService.saveUser(user);
    }

    @Override
    @Transactional
    public void initiatePhoneChange(Long userId, ContactChangeInitiateRequest request) {
        String newPhoneNumber = request.getNewValue();
        User user = userDomainService.findUserById(userId);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new PasswordMismatchException("Incorrect password");
        }

        if (!contactValidator.isValidPhoneNumber(newPhoneNumber)){
            throw new InvalidInputException("Invalid phone number format");
        }

        if (userDomainService.existsByPhoneNumber(newPhoneNumber)){
            throw new ConflictException("Phone number already in use");
        }

        // Clean up old requests
        userChangeRequestRepository.deleteByUserIdAndType(userId, UserChangeRequest.RequestType.PHONE_UPDATE);

        // Create change request
        UserChangeRequest changeRequest = UserChangeRequest.builder()
                .userId(user.getId())
                .type(UserChangeRequest.RequestType.PHONE_UPDATE)
                .newValue(newPhoneNumber)
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .build();
        userChangeRequestRepository.save(changeRequest);

        // Send verification code
        VerificationRequest verificationRequest = new VerificationRequest(
                user.getId(),
                VerificationType.CODE,
                NotificationChannel.SMS,
                VerificationPurpose.PHONE_CHANGE,
                null
        );

        verificationService.startVerification(verificationRequest, newPhoneNumber);
        log.info("Phone change verification initiated for user: {}", userId);
    }

    @Override
    @Transactional
    public void changePhone(Long userId){
        UserChangeRequest userChangeRequest = userChangeRequestRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("UserChangeRequest", "userId", userId));

        if (userChangeRequest.isExpired()) {
            userChangeRequestRepository.delete(userChangeRequest);
            throw new InvalidInputException("Change request expired");
        }

        User user = userDomainService.findUserById(userId);
        user.setPhoneNumber(userChangeRequest.getNewValue());
        user.setPhoneVerifiedAt(Instant.now());

        userDomainService.saveUser(user);
        userChangeRequestRepository.delete(userChangeRequest);
        log.info("Phone number updated successfully for user: {}", userId);
    }

    @Override
    @Transactional
    public void initiateEmailChange(Long userId, ContactChangeInitiateRequest request) {
        String newEmail = request.getNewValue();
        User user = userDomainService.findUserById(userId);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new PasswordMismatchException("Incorrect password");
        }

        if (!contactValidator.isValidEmail(newEmail)){
            throw new InvalidInputException("Invalid email format");
        }

        if (userDomainService.existsByEmail(newEmail)){
            throw new ConflictException("Email already in use");
        }

        // Clean up old requests
        userChangeRequestRepository.deleteByUserIdAndType(userId, UserChangeRequest.RequestType.EMAIL_UPDATE);

        // Create change request
        UserChangeRequest changeRequest = UserChangeRequest.builder()
                .userId(user.getId())
                .type(UserChangeRequest.RequestType.EMAIL_UPDATE)
                .newValue(newEmail)
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .build();
        userChangeRequestRepository.save(changeRequest);

        // Send verification link
        VerificationRequest verificationRequest = new VerificationRequest(
                user.getId(),
                VerificationType.LINK,
                NotificationChannel.EMAIL,
                VerificationPurpose.EMAIL_CHANGE,
                null
        );

        verificationService.startVerification(verificationRequest, newEmail);
        log.info("Email change verification initiated for user: {}", userId);
    }

    @Override
    @Transactional
    public void changeEmail(Long userId){
        UserChangeRequest userChangeRequest = userChangeRequestRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("UserChangeRequest", "userId", userId));

        if (userChangeRequest.isExpired()) {
            userChangeRequestRepository.delete(userChangeRequest);
            throw new InvalidInputException("Change request expired");
        }

        User user = userDomainService.findUserById(userId);
        user.setEmail(userChangeRequest.getNewValue());
        user.setEmailVerifiedAt(Instant.now());

        userDomainService.saveUser(user);
        userChangeRequestRepository.delete(userChangeRequest);
        log.info("Email updated successfully for user: {}", userId);
    }

    @Override
    @Transactional
    public void initiateEmailVerification(Long userId) {
        User user = userDomainService.findUserById(userId);

        if (user.getEmailVerifiedAt() != null) {
            throw new ConflictException("Email is already verified.");
        }

        VerificationRequest verificationRequest = new VerificationRequest(
                user.getId(),
                VerificationType.LINK,
                NotificationChannel.EMAIL,
                VerificationPurpose.EMAIL_VERIFICATION,
                null
        );

        verificationService.startVerification(verificationRequest);
        log.info("Email verification initiated for user: {}", userId);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userDomainService.findUserById(userId);

        // Business check
        if (businessRepository.existsByOwner(user)){
            throw new ObjectDeletionException("Cannot delete user with associated business.");
        }

        // Appointment check
        List<Appointment> appointments = appointmentRepository.findAllByUserId(userId);
        appointments.forEach(appointment -> {
            if (appointment.getAppointmentStatus()== AppointmentStatus.CREATED || appointment.getAppointmentStatus()== AppointmentStatus.CONFIRMED){
                throw new ObjectDeletionException("Cannot delete user with associated business.");
            }
        });

        userDomainService.deleteUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable, String search) {
        Page<User> users = userDomainService.getAllUsers(pageable, search);
        return users.map(userMapper::userToUserResponse);
    }

    @Override
    @Transactional
    public void changeUserStatus(Long id, UserStatus status) {
        User user = userDomainService.findUserById(id);
        user.setStatus(status);
        userDomainService.saveUser(user);
    }

    @Override
    @Transactional
    public UserResponse updateUserRoles(Long id, Set<Role> roles) {
        User user = userDomainService.findUserById(id);
        user.setRoles(roles);
        userDomainService.saveUser(user);
        return userMapper.userToUserResponse(user);
    }
}

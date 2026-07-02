package RandevuApp.domain.user.repository;

import RandevuApp.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> , JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailAndEmailVerifiedAtIsNotNull(String email);
    boolean existsByEmailAndEmailVerifiedAtIsNotNull(String email);
    List<User> findAllByEmailAndEmailVerifiedAtIsNullAndIdNot(String email, Long excludedUserId);
}

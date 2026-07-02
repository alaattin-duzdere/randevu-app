package RandevuApp.domain.user.repository;

import RandevuApp.domain.user.model.UserChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChangeRequestRepository extends JpaRepository<UserChangeRequest, Long> {

    Optional<UserChangeRequest> findByUserId(Long userId);

    void deleteByUserIdAndType(Long userId, UserChangeRequest.RequestType type);
}

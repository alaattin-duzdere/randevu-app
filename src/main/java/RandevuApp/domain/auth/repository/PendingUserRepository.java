package RandevuApp.domain.auth.repository;

import RandevuApp.domain.auth.model.PendingUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingUserRepository extends CrudRepository<PendingUser, String> {
}

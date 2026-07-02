package RandevuApp.domain.staff.repository;

import RandevuApp.domain.staff.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByIdAndBusinessId(Long id, Long businessId);

    List<Staff> findAllByBusinessId(Long businessId);

    boolean existsByBusinessIdAndEmail(Long businessId, String email);
    boolean existsByBusinessIdAndPhone(Long businessId, String phone);

    void deleteAllByBusinessId(Long businessId);
}
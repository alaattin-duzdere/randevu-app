package RandevuApp.domain.business.repository;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long>, JpaSpecificationExecutor<Business> {

    boolean existsBySlug(String slug);

    boolean existsByOwnerId(Long ownerId);

    Optional<Business> findBySlug(String slug);

    List<Business> findAllByOwner(User owner);

    // Admin için;
    @Query(value = "SELECT * FROM business b WHERE b.is_deleted = true " +
            "AND (:query IS NULL OR (" +
            "LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.slug) LIKE LOWER(CONCAT('%', :query, '%'))" +
            "))",
            countQuery = "SELECT count(*) FROM business b WHERE b.is_deleted = true " +
                    "AND (:query IS NULL OR (" +
                    "LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    "LOWER(b.slug) LIKE LOWER(CONCAT('%', :query, '%'))" +
                    "))",
            nativeQuery = true)
    Page<Business> findAllDeletedBusinesses(@Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM business WHERE id = :id", nativeQuery = true)
    Optional<Business> findByIdIncludingDeleted(@Param("id") Long id);
}

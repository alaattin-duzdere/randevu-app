package RandevuApp.domain.user.repository;

import RandevuApp.domain.user.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    private UserSpecifications() {
    }

    public static Specification<User> buildSpecification(String search) {
        Specification<User> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (!StringUtils.hasText(search)) {
            return spec;
        }

        return spec.and(hasSearch(search));
    }

    public static Specification<User> hasSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            String searchPattern = "%" + search.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), searchPattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}

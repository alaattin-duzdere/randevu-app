package RandevuApp.domain.user.repository;

import RandevuApp.domain.user.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    public static Specification<User> withSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            // Eğer arama metni boşsa, hiçbir filtre uygulama (tüm kayıtları getir)
            if (!StringUtils.hasText(search)) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + search.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            // firstName içinde ara
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), searchPattern));

            // lastName içinde ara
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchPattern));

            // email içinde ara
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern));

            // phoneNumber içinde ara (Genelde numerik olsa da String tutuyorsun, like çalışır)
            predicates.add(criteriaBuilder.like(root.get("phoneNumber"), searchPattern));

            // Predicate'leri 'OR' ile birleştir (Herhangi biri eşleşirse getir)
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}

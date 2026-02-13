package RandevuApp.domain.business.repository;

import RandevuApp.domain.business.dto.BusinessSearchRequest;
import RandevuApp.domain.business.model.Business;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BusinessSpecification {

    public static Specification<Business> init(BusinessSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Genel Metin Araması (Name, Description veya Slug içinde)
            if (StringUtils.hasText(request.getQuery())) {
                String searchKey = "%" + request.getQuery().toLowerCase() + "%";

                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchKey);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchKey);
                Predicate slugPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), searchKey);

                // (name LIKE %x% OR description LIKE %x% OR slug LIKE %x%)
                predicates.add(criteriaBuilder.or(namePredicate, descPredicate, slugPredicate));
            }

            // 2. Adres Filtresi
            if (StringUtils.hasText(request.getAddress())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("address")),
                        "%" + request.getAddress().toLowerCase() + "%"
                ));
            }

            // 3. Aktiflik Durumu
            if (request.getActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), request.getActive()));
            } else {
                // Varsayılan olarak sadece aktif işletmeleri göstermek istersen burayı açabilirsin:
                // predicates.add(criteriaBuilder.isTrue(root.get("active")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

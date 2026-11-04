package RandevuApp.domain.business.repository;

import RandevuApp.domain.business.dto.BusinessSearchRequest;
import RandevuApp.domain.business.model.Business;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BusinessSpecs {

    private BusinessSpecs() {
    }

    public static Specification<Business> buildSpecification(BusinessSearchRequest request) {
        Specification<Business> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (request == null) {
            return spec;
        }

        if (StringUtils.hasText(request.query())) {
            spec = spec.and(hasQuery(request.query()));
        }

        if (StringUtils.hasText(request.address())) {
            spec = spec.and(hasAddress(request.address()));
        }

        if (request.active() != null) {
            spec = spec.and(isActive(request.active()));
        }

        return spec;
    }

    public static Specification<Business> hasQuery(String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            String searchKey = "%" + query.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchKey));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchKey));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), searchKey));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Business> hasAddress(String address) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("address")),
                "%" + address.toLowerCase() + "%"
        );
    }

    public static Specification<Business> isActive(Boolean active) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), active);
    }
}


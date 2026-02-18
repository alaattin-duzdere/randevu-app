package RandevuApp.domain.business.service;

import RandevuApp.domain.business.dto.CreateBusinessRequest;
import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessSettings;
import RandevuApp.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IBusinessDomainService {

    Business createBusinessEntity(CreateBusinessRequest request, User owner, BusinessSettings defaultSettings, String defaultTimeZone);

    void validateBusinessOwner(Business business, Long ownerId);

    Business getById(Long id);

    Business getBySlug(String slug);

    List<Business> getAllByOwner(User owner);

    Page<Business> findAll(Specification<Business> spec, Pageable pageable);

    boolean existsBySlug(String slug);

    Business save(Business business);

    Business performUpdateBusiness(Business business, UpdateBusinessRequest request);

    void performDeleteBusiness(Business business);
}

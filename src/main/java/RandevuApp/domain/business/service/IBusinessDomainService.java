package RandevuApp.domain.business.service;

import RandevuApp.domain.business.model.Address;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessSettings;
import RandevuApp.domain.business.service.params.CreateBusinessParams;
import RandevuApp.domain.business.service.params.UpdateBusinessParams;
import RandevuApp.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IBusinessDomainService {

    Business createBusinessEntity(CreateBusinessParams param, User owner, BusinessSettings defaultSettings, String defaultTimeZone, Address address);

    void validateBusinessOwner(Business business, Long ownerId);

    Business getById(Long id);

    Business getBySlug(String slug);

    List<Business> getAllByOwner(User owner);

    Page<Business> findAll(Specification<Business> spec, Pageable pageable);

    boolean existsBySlug(String slug);

    Business save(Business business);

    Business performUpdateBusiness(Business business, UpdateBusinessParams params, Address newAddress);

    void performDeleteBusiness(Business business);
}

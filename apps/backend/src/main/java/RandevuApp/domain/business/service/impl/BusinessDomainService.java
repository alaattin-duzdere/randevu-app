package RandevuApp.domain.business.service.impl;

import RandevuApp.domain.business.model.Address;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessSettings;
import RandevuApp.domain.business.repository.BusinessRepository;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.business.service.params.CreateBusinessParams;
import RandevuApp.domain.business.service.params.UpdateBusinessParams;
import RandevuApp.domain.service_offering.service.IServiceOfferingDomainService;
import RandevuApp.domain.staff.service.IStaffDomainService;
import RandevuApp.domain.user.model.User;
import RandevuApp.exceptions.client.OwnerMismatchException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessDomainService implements IBusinessDomainService {

    private final BusinessRepository businessRepository;
    private final IStaffDomainService staffDomainService;
    private final IServiceOfferingDomainService serviceOfferingDomainService;

    @Override
    public Business createBusinessEntity(CreateBusinessParams param, User owner, BusinessSettings defaultSettings, String defaultTimeZone, Address address) {
        Business business = new Business();
        business.setName(param.name());
        business.setAddress(address);
        business.setDescription(param.description());
        business.setActive(true);
        business.setOwner(owner);

        String timeZone = StringUtils.hasText(param.timeZone())
                ? param.timeZone()
                : defaultTimeZone;
        business.setTimeZone(timeZone);

        business.setSlug(generateSlug(param.name(), param.slug()));

        business.setBusinessSettings(defaultSettings);
        defaultSettings.setBusiness(business);

        // Operating hours creation moved to BusinessServiceImp after saving business

        return business;
    }

    private String generateSlug(String name, String requestedSlug) {
        if (StringUtils.hasText(requestedSlug)) {
            return requestedSlug;
        }
        return name.toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");
    }

    @Override
    public void validateBusinessOwner(Business business, Long ownerId) {
        if (!business.getOwner().getId().equals(ownerId)) {
            throw new OwnerMismatchException("You are not the owner of this business.");
        }
    }

    @Override
    public Business getById(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "id", id));
    }

    @Override
    public Business getBySlug(String slug) {
        return businessRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "slug", slug));
    }

    @Override
    public List<Business> getAllByOwner(User owner) {
        return businessRepository.findAllByOwner(owner);
    }

    @Override
    public Page<Business> findAll(Specification<Business> spec, Pageable pageable) {
        return businessRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return businessRepository.existsBySlug(slug);
    }

    @Override
    public Business save(Business business) {
        return businessRepository.save(business);
    }

    @Override
    public Business performUpdateBusiness(Business business, UpdateBusinessParams params, Address newAddress) {
        business.setName(params.name());

        if (newAddress != null) {
            business.setAddress(newAddress);
        }

        business.setDescription(params.description());
        business.setTimeZone(params.timeZone());
        business.setActive(params.active());

        return businessRepository.save(business);
    }


    @Override
    @Transactional
    public void performDeleteBusiness(Business business) {

        // delete associated offerings and staffs
        serviceOfferingDomainService.deleteAllByBusinessId(business.getId());
        staffDomainService.deleteAllByBusinessId(business.getId());

        String timestamp = String.valueOf(System.currentTimeMillis());
        business.setSlug(business.getSlug() + "-deleted-" + timestamp);

        business.setActive(false);
        business.setDeleted(true);

        businessRepository.save(business);

        log.info("Business (ID: {}) ve bağlı tüm alt varlıklar soft-delete yapıldı.", business.getId());
    }
}

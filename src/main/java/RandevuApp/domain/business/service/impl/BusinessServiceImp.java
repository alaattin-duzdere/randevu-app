package RandevuApp.domain.business.service.impl;

import RandevuApp.config.BusinessProperties;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.repository.AppointmentRepository;
import RandevuApp.domain.business.dto.BusinessSearchRequest;
import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import RandevuApp.domain.business.mapper.BusinessMapper;
import RandevuApp.domain.business.dto.BusinessResponse;
import RandevuApp.domain.business.dto.CreateBusinessRequest;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessSettings;
import RandevuApp.domain.business.repository.BusinessSpecification;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.business.service.IBusinessService;
import RandevuApp.domain.business.service.IBusinessSettingsService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.service.IUserDomainService;
import RandevuApp.exceptions.auth.UserBannedException;
import RandevuApp.exceptions.auth.UserNotActiveException;
import RandevuApp.exceptions.client.ObjectDeletionException;
import RandevuApp.exceptions.client.ConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessServiceImp implements IBusinessService {
    private final IUserDomainService userDomainService;
    private final BusinessProperties businessProperties;
    private final BusinessMapper businessMapper;
    private final AppointmentRepository appointmentRepository;
    private final IBusinessDomainService businessDomainService;
    private final IBusinessSettingsService businessSettingsService;

    @Transactional
    public BusinessResponse createBusiness(CreateBusinessRequest request, Long ownerId) {
        User owner = userDomainService.findUserById(ownerId);

        // User stat check
        if (owner.getStatus() == UserStatus.BANNED) {
            throw new UserBannedException("Your account has been banned. You cannot create a business.");
        }
        if (owner.getStatus() != UserStatus.ACTIVE) {
            throw new UserNotActiveException("Your account is not active. Please verify your account to create a business.");
        }

        // Slug check
        if (businessDomainService.existsBySlug(request.getSlug())) {
            throw new ConflictException("Business with slug '" + request.getSlug() + "' already exists.");
        }

        // Get default settings
        BusinessSettings defaultSettings = businessSettingsService.createDefaultSettings();

        // Create business entity
        Business business = businessDomainService.createBusinessEntity(
                request,
                owner,
                defaultSettings,
                businessProperties.getDefaultTimezone()
        );

        Business savedBusiness = businessDomainService.save(business);
        return businessMapper.businessToBusinessResponse(savedBusiness);
    }

    @Override
    public BusinessResponse getBusinessById(Long id) {
        Business business = businessDomainService.getById(id);
        return businessMapper.businessToBusinessResponse(business);
    }

    @Override
    public BusinessResponse getBusinessBySlug(String slug) {
        Business business = businessDomainService.getBySlug(slug);
        return businessMapper.businessToBusinessResponse(business);
    }

    @Override
    public List<BusinessResponse> getBusinessesByOwner(Long ownerId) {
        User owner = userDomainService.findUserById(ownerId);
        List<Business> businesses = businessDomainService.getAllByOwner(owner);
        return businesses.stream().map(businessMapper::businessToBusinessResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessResponse> searchBusinesses(BusinessSearchRequest searchRequest, Pageable pageable) {
        Specification<Business> spec = BusinessSpecification.init(searchRequest);
        Page<Business> businessPage = businessDomainService.findAll(spec, pageable);
        return businessPage.map(businessMapper::businessToBusinessResponse);
    }

    @Override
    public BusinessResponse updateBusiness(Long businessId, UpdateBusinessRequest request, Long ownerId) {
        Business business = businessDomainService.getById(businessId);

        businessDomainService.validateBusinessOwner(business, ownerId);

        Business updatedBusiness = businessDomainService.performUpdateBusiness(business, request);
        return businessMapper.businessToBusinessResponse(updatedBusiness);
    }

    @Override
    public void deleteBusiness(Long businessId, Long ownerId) {
        Business business = businessDomainService.getById(businessId);

        businessDomainService.validateBusinessOwner(business, ownerId);

        // check for active appointments
        boolean hasActiveAppointments = appointmentRepository.existsByBusinessIdAndAppointmentStatusIn(
                business.getId(),
                List.of(AppointmentStatus.CREATED, AppointmentStatus.CONFIRMED)
        );

        if (hasActiveAppointments) {
            // appointments might be canceled.
            throw new ObjectDeletionException("Cannot delete business with associated active appointments.");
        }

        businessDomainService.performDeleteBusiness(business);
    }
}

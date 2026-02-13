package RandevuApp.domain.business.service.impl;

import RandevuApp.config.BusinessProperties;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.repository.AppointmentRepository;
import RandevuApp.domain.business.dto.BusinessSearchRequest;
import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import RandevuApp.domain.business.mapper.BusinessMapper;
import RandevuApp.domain.business.repository.BusinessRepository;
import RandevuApp.domain.business.dto.BusinessResponse;
import RandevuApp.domain.business.dto.CreateBusinessRequest;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessSettings;
import RandevuApp.domain.business.repository.BusinessSpecification;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.business.service.IBusinessService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.service.IUserDomainService;
import RandevuApp.exceptions.client.ObjectDeletionException;
import RandevuApp.exceptions.client.OwnerMismatchException;
import RandevuApp.exceptions.client.ConflictException;
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
public class BusinessServiceImp implements IBusinessService {
    private final BusinessRepository businessRepository;
    private final IUserDomainService userDomainService;
    private final BusinessProperties businessProperties;
    private final BusinessMapper businessMapper;
    private final AppointmentRepository appointmentRepository;
    private final IBusinessDomainService businessDomainService;

    @Transactional
    public BusinessResponse createBusiness(CreateBusinessRequest request, Long ownerId) {

        User owner = userDomainService.findUserById(ownerId);

        Business business = new Business();
        business.setName(request.getName());
        business.setAddress(request.getAddress());
        business.setDescription(request.getDescription());

        // Timezone check
        String timeZone = StringUtils.hasText(request.getTimeZone())
                ? request.getTimeZone()
                : businessProperties.getDefaultTimezone();
        business.setTimeZone(timeZone);

        // Slug check
        String slug;
        if (StringUtils.hasText(request.getSlug())) {
            slug = request.getSlug();
        } else {
            slug = request.getName().toLowerCase()
                    .trim()
                    .replaceAll("\\s+", "-")
                    .replaceAll("[^a-z0-9-]", "");
        }
        if (businessRepository.existsBySlug(slug)) {
            throw new ConflictException("Business with slug '" + slug + "' already exists.");
        }
        business.setSlug(slug);

        business.setActive(true);
        business.setOwner(owner);

        // Default settings for business
        BusinessProperties.Defaults defaults = businessProperties.getDefaults();

        BusinessSettings defaultSettings = new BusinessSettings();
        defaultSettings.setSlotDurationTime(defaults.getSlotDurationTime());
        defaultSettings.setOpeningTime(defaults.getOpeningTime());
        defaultSettings.setClosingTime(defaults.getClosingTime());
        defaultSettings.setTimeZone(timeZone);
        business.setBusinessSettings(defaultSettings);
        defaultSettings.setBusiness(business);

        Business savedBusiness = businessRepository.save(business);

        return businessMapper.businessToBusinessResponse(savedBusiness);
    }

    @Override
    public BusinessResponse getBusinessById(Long id) {
        Business business = businessRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Business", "id",id));
        return businessMapper.businessToBusinessResponse(business);
    }

    @Override
    public BusinessResponse getBusinessBySlug(String slug) {
        Business business = businessRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("Business", "slug", slug));
        return businessMapper.businessToBusinessResponse(business);
    }

    @Override
    public List<BusinessResponse> getBusinessesByOwner(Long ownerId) {
        User owner = userDomainService.findUserById(ownerId);
        List<Business> businesses = businessRepository.findAllByOwner(owner);
        return businesses.stream().map(businessMapper::businessToBusinessResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessResponse> searchBusinesses(BusinessSearchRequest searchRequest, Pageable pageable) {
        Specification<Business> spec = BusinessSpecification.init(searchRequest);
        Page<Business> businessPage = businessRepository.findAll(spec, pageable);
        return businessPage.map(businessMapper::businessToBusinessResponse);
    }

    @Override
    public BusinessResponse updateBusiness(Long businessId, UpdateBusinessRequest request, Long ownerId) {
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new ResourceNotFoundException("Business", "id", businessId));

        businessOwnerCheck(business,ownerId);

        Business updatedBusiness = businessDomainService.performUpdateBusiness(business, request);
        return businessMapper.businessToBusinessResponse(updatedBusiness);
    }

    @Override
    public void deleteBusiness(Long businessId, Long ownerId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "id", businessId));

        businessOwnerCheck(business, ownerId);

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

    private void businessOwnerCheck(Business business, Long ownerId){
        if (!business.getOwner().getId().equals(ownerId)) {
            throw new OwnerMismatchException("You are not the owner of this business.");
        }
    }
}

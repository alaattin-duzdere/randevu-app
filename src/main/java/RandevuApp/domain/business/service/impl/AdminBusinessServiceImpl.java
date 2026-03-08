package RandevuApp.domain.business.service.impl;

import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.repository.AppointmentRepository;
import RandevuApp.domain.business.dto.BusinessResponse;
import RandevuApp.domain.business.dto.BusinessSearchRequest;
import RandevuApp.domain.business.dto.GeoLocationResult;
import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import RandevuApp.domain.business.mapper.BusinessMapper;
import RandevuApp.domain.business.model.Address;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.service.params.UpdateBusinessParams;
import RandevuApp.integration.location.port.IGeocodingPort;
import RandevuApp.domain.business.repository.BusinessRepository;
import RandevuApp.domain.business.repository.BusinessSpecification;
import RandevuApp.domain.business.service.IAdminBusinessService;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.service.IUserDomainService;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.ObjectDeletionException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
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
public class AdminBusinessServiceImpl implements IAdminBusinessService {

    private final BusinessRepository businessRepository;
    private final AppointmentRepository appointmentRepository;
    private final BusinessMapper businessMapper;
    private final IUserDomainService userDomainService;
    private final IBusinessDomainService businessDomainService;
    private final IGeocodingPort geocodingPort;

    @Override
    public BusinessResponse getBusinessByIdForAdmin(Long id) {
        Business business = businessRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "id", id));

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
    @Transactional(readOnly = true)
    public Page<BusinessResponse> getDeletedBusinesses(String query, Pageable pageable){
        Page<Business> deletedPage = businessRepository.findAllDeletedBusinesses(query, pageable);

        return deletedPage.map(businessMapper::businessToBusinessResponse);
    }

    @Override
    public BusinessResponse updateBusinessByAdmin(Long businessId, UpdateBusinessRequest request) {
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new ResourceNotFoundException("Business", "id", businessId));

        Address address = business.getAddress();
        // Address Update Check
        if (request.getAddress() != null && !request.getAddress().getExternalLocationId().equals(business.getAddress().getExternalLocationId())) {
            GeoLocationResult geoLocationResult = geocodingPort.getPlaceDetailsById(request.getAddress().getExternalLocationId())
                    .orElseThrow(() -> new InvalidInputException("Invalid address location ID."));

            address = businessMapper.geoLocationResultToAddress(geoLocationResult);
        }

        // Update Business
        UpdateBusinessParams params = new UpdateBusinessParams(
                request.getName(),
                request.getDescription(),
                request.getTimeZone(),
                request.getActive()
        );
        Business updatedBusiness = businessDomainService.performUpdateBusiness(business, params, address);

        return businessMapper.businessToBusinessResponse(updatedBusiness);
    }

    @Override
    public void deleteBusinessByAdmin(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "id", businessId));

        // check for active appointments
        boolean hasActiveAppointments = appointmentRepository.existsByBusinessIdAndAppointmentStatusIn(
                business.getId(),
                List.of(AppointmentStatus.CREATED, AppointmentStatus.CONFIRMED)
        );

        if (hasActiveAppointments) {
            throw new ObjectDeletionException("Cannot delete business with associated active appointments.");
        }

        businessDomainService.performDeleteBusiness(business);
    }


    //    @Override
//    @Transactional
//    public void restoreBusiness(Long businessId) {
//        Business business = businessRepository.findByIdIncludingDeleted(businessId)
//                .orElseThrow(() -> new ResourceNotFoundException("Business", "id", businessId));
//
//        if(business.isDeleted()) {
//            business.setDeleted(false);
//            business.setActive(true);
//            // Slug'daki timestamp'i temizleme mantığı buraya eklenebilir
//            // business.setSlug(cleanSlug(business.getSlug()));
//            businessRepository.save(business);
//        }
//    }
}

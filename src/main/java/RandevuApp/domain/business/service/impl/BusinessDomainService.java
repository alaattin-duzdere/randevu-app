package RandevuApp.domain.business.service.impl;

import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.repository.AppointmentRepository;
import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.repository.BusinessRepository;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.service_offering.service.IServiceOfferingService;
import RandevuApp.domain.staff.service.IStaffService;
import RandevuApp.exceptions.client.ObjectDeletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessDomainService implements IBusinessDomainService {

    private final BusinessRepository businessRepository;
    private final AppointmentRepository appointmentRepository;
    private final IStaffService staffService;
    private final IServiceOfferingService serviceOfferingService;

    @Override
    public Business performUpdateBusiness(Business business, UpdateBusinessRequest request) {
        business.setName(request.getName());
        business.setAddress(request.getAddress());
        business.setDescription(request.getDescription());
        business.setTimeZone(request.getTimeZone());
        business.setActive(request.getActive());

        return businessRepository.save(business);
    }


    @Override
    @Transactional
    public void performDeleteBusiness(Business business) {

        // delete associated offerings and staffs
        serviceOfferingService.deleteAllByBusinessId(business.getId());
        staffService.deleteAllByBusinessId(business.getId());

        String timestamp = String.valueOf(System.currentTimeMillis());
        business.setSlug(business.getSlug() + "-deleted-" + timestamp);

        business.setActive(false);
        business.setDeleted(true);

        businessRepository.save(business);

        log.info("Business (ID: {}) ve bağlı tüm alt varlıklar soft-delete yapıldı.", business.getId());
    }
}

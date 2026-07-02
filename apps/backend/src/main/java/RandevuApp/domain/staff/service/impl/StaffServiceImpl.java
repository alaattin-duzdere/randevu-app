package RandevuApp.domain.staff.service.impl;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.service_offering.service.IServiceOfferingDomainService;
import RandevuApp.domain.staff.dto.CreateStaffRequest;
import RandevuApp.domain.staff.dto.StaffResponse;
import RandevuApp.domain.staff.dto.UpdateStaffRequest;
import RandevuApp.domain.staff.mapper.StaffMapper;
import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.staff.model.StaffService;
import RandevuApp.domain.staff.service.IStaffDomainService;
import RandevuApp.domain.staff.service.IStaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements IStaffService {

    private final IStaffDomainService staffDomainService;
    private final IBusinessDomainService businessDomainService;
    private final IServiceOfferingDomainService serviceOfferingDomainService;
    private final StaffMapper mapper;

    private static final String DEFAULT_COLOR_CODE = "#3B82F6";

    @Override
    @Transactional
    public StaffResponse createStaff(Long businessId, CreateStaffRequest request, Long ownerId) {
        Business business = getBusinessAndValidateOwner(businessId, ownerId);

        staffDomainService.validateStaffContactUniqueForBusiness(businessId, request.email(), request.phone());

        String colorCode = StringUtils.hasText(request.colorCode()) ? request.colorCode() : DEFAULT_COLOR_CODE;

        Staff staff = staffDomainService.createEntity(
                request.name(),
                request.title(),
                request.email(),
                request.phone(),
                colorCode,
                request.photo(),
                business
        );

        Staff savedStaff = staffDomainService.save(staff);

        if (request.serviceIds() != null && !request.serviceIds().isEmpty()) {
            assignServicesInternal(businessId, savedStaff, request.serviceIds());
            savedStaff = staffDomainService.save(savedStaff);
        }

        return mapper.entityToResponse(savedStaff);
    }

    @Override
    @Transactional
    public StaffResponse updateStaff(Long businessId, Long staffId, UpdateStaffRequest request, Long ownerId) {
        getBusinessAndValidateOwner(businessId, ownerId);

        Staff staff = staffDomainService.getByIdAndBusinessId(staffId, businessId);

        boolean emailChanged = request.email() != null && !request.email().equals(staff.getEmail());
        boolean phoneChanged = request.phone() != null && !request.phone().equals(staff.getPhone());

        // email / phone change
        if (emailChanged || phoneChanged) {
            String emailToCheck = emailChanged ? request.email() : null;
            String phoneToCheck = phoneChanged ? request.phone() : null;
            staffDomainService.validateStaffContactUniqueForBusiness(businessId, emailToCheck, phoneToCheck);
        }

        // service offerings change
        if (request.serviceIds() != null) {
            assignServicesInternal(businessId, staff, request.serviceIds());
        }

        mapper.updateStaffFromDto(request, staff);

        Staff updatedStaff = staffDomainService.save(staff);
        return mapper.entityToResponse(updatedStaff);
    }

    @Override
    @Transactional
    public void deleteStaff(Long businessId, Long staffId, Long ownerId) {
        getBusinessAndValidateOwner(businessId, ownerId);
        Staff staff = staffDomainService.getByIdAndBusinessId(staffId, businessId);

        // active appointment check

        staffDomainService.delete(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponse> getAllStaffOfBusiness(Long businessId) {
        businessDomainService.getById(businessId);
        List<Staff> staffs = staffDomainService.getAllByBusinessId(businessId);
        return staffs.stream().map(mapper::entityToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StaffResponse getStaffById(Long businessId, Long staffId) {
        businessDomainService.getById(businessId);
        Staff staff = staffDomainService.getByIdAndBusinessId(staffId, businessId);
        return mapper.entityToResponse(staff);
    }

    @Override
    @Transactional
    public void assignServicesToStaff(Long businessId, Long staffId, List<Long> serviceIds, Long ownerId) {
        getBusinessAndValidateOwner(businessId, ownerId);
        Staff staff = staffDomainService.getByIdAndBusinessId(staffId, businessId);

        assignServicesInternal(businessId, staff, serviceIds);
        staffDomainService.save(staff);
    }

    // --- HELPER METHODS---

    private Business getBusinessAndValidateOwner(Long businessId, Long ownerId) {
        Business business = businessDomainService.getById(businessId);
        businessDomainService.validateBusinessOwner(business, ownerId);
        return business;
    }

    private void assignServicesInternal(Long businessId, Staff staff, List<Long> serviceIds) {
        if (serviceIds == null) return;

        Set<Long> requestedIds = new HashSet<>(serviceIds);

        if (staff.getStaffServices() == null) {
            staff.setStaffServices(new ArrayList<>());
        }

        staff.getStaffServices().removeIf(ss -> !requestedIds.contains(ss.getServiceOffering().getId()));

        Set<Long> existingIds = staff.getStaffServices().stream()
                .map(ss -> ss.getServiceOffering().getId())
                .collect(Collectors.toSet());

        for (Long serviceId : requestedIds) {
            if (!existingIds.contains(serviceId)) {

                ServiceOffering serviceOffering = serviceOfferingDomainService.getByIdAndBusinessId(serviceId, businessId);

                StaffService newStaffService = StaffService.builder()
                        .staff(staff)
                        .serviceOffering(serviceOffering)
                        .build();

                staff.getStaffServices().add(newStaffService);
            }
        }
    }
}

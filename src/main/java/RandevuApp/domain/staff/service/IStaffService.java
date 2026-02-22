package RandevuApp.domain.staff.service;

import RandevuApp.domain.staff.dto.CreateStaffRequest;
import RandevuApp.domain.staff.dto.StaffResponse;
import RandevuApp.domain.staff.dto.UpdateStaffRequest;

import java.util.List;

public interface IStaffService {

    StaffResponse createStaff(Long businessId, CreateStaffRequest request, Long ownerId);

    StaffResponse updateStaff(Long businessId, Long staffId, UpdateStaffRequest request, Long ownerId);

    void deleteStaff(Long businessId, Long staffId, Long ownerId);

    List<StaffResponse> getAllStaffOfBusiness(Long businessId);

    StaffResponse getStaffById(Long businessId, Long staffId);

    /**
     * Bir personelin verebileceği hizmetleri belirler (Örn: Sadece saç kesimi ve sakal tıraşı yapabilir).
     * Bu işlem StaffService ara tablosunu günceller.
     * * @param serviceIds personelin yapabileceği ServiceOffering ID'lerinin listesi
     */
    void assignServicesToStaff(Long businessId, Long staffId, List<Long> serviceIds, Long ownerId);
}

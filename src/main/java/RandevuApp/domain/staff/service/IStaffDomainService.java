package RandevuApp.domain.staff.service;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.staff.model.Staff;

import java.util.List;

public interface IStaffDomainService {

    Staff createEntity(String name, String title, String email, String phone, String colorCode, String photo, Business business);

    void validateStaffContactUniqueForBusiness(Long businessId, String email, String phone);

    // Db wrappers
    Staff save(Staff staff);

    Staff getByIdAndBusinessId(Long staffId, Long businessId);

    List<Staff> getAllByBusinessId(Long businessId);

    void delete(Staff staff);

    void deleteAllByBusinessId(Long businessId);
}

package RandevuApp.domain.staff.service.impl;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.staff.repository.StaffRepository;
import RandevuApp.domain.staff.service.IStaffDomainService;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffDomainServiceImpl implements IStaffDomainService {

    private final StaffRepository staffRepository;

    // --- FACTORY ---

    @Override
    public Staff createEntity(String name, String title, String email, String phone, String colorCode, String photo, Business business) {
        return Staff.builder()
                .name(name)
                .title(title)
                .email(email)
                .phone(phone)
                .colorCode(colorCode)
                .photo(photo)
                .active(true)
                .business(business)
                .build();
    }


    // --- business rules and valid ---

    @Override
    public void validateStaffContactUniqueForBusiness(Long businessId, String email, String phone) {
        if (StringUtils.hasText(email) && staffRepository.existsByBusinessIdAndEmail(businessId, email)) {
            throw new ConflictException("Bu e-posta adresi (" + email + ") işletmedeki başka bir personel tarafından kullanılıyor.");
        }

        if (StringUtils.hasText(phone) && staffRepository.existsByBusinessIdAndPhone(businessId, phone)) {
            throw new ConflictException("Bu telefon numarası (" + phone + ") işletmedeki başka bir personel tarafından kullanılıyor.");
        }
    }

    // --- (REPOSITORY WRAPPERS) ---

    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public Staff getByIdAndBusinessId(Long staffId, Long businessId) {
        return staffRepository.findByIdAndBusinessId(staffId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Personel bulunamadı veya bu işletmeye ait değil", "id", staffId));
    }

    @Override
    public List<Staff> getAllByBusinessId(Long businessId) {
        return staffRepository.findAllByBusinessId(businessId);
    }

    @Override
    public void delete(Staff staff) {
        staffRepository.delete(staff);
    }

    @Override
    public void deleteAllByBusinessId(Long businessId) {
        staffRepository.deleteAllByBusinessId(businessId);
    }
}
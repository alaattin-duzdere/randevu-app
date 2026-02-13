package RandevuApp.domain.business.service;

import RandevuApp.domain.business.dto.BusinessResponse;
import RandevuApp.domain.business.dto.BusinessSearchRequest;
import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAdminBusinessService {

    void deleteBusinessByAdmin(Long businessId);

    BusinessResponse getBusinessByIdForAdmin(Long id);

    List<BusinessResponse> getBusinessesByOwner(Long ownerId);

    Page<BusinessResponse> searchBusinesses(BusinessSearchRequest searchRequest, Pageable pageable);

    Page<BusinessResponse> getDeletedBusinesses(String query, Pageable pageable);

    BusinessResponse updateBusinessByAdmin(Long businessId, UpdateBusinessRequest request);


}

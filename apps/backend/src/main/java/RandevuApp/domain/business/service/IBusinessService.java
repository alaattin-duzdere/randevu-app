package RandevuApp.domain.business.service;

import RandevuApp.domain.business.dto.BusinessResponse;
import RandevuApp.domain.business.dto.BusinessSearchRequest;
import RandevuApp.domain.business.dto.CreateBusinessRequest;
import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBusinessService {

    BusinessResponse createBusiness(CreateBusinessRequest request, Long ownerId);

    BusinessResponse getBusinessById(Long id);

    BusinessResponse getBusinessBySlug(String slug);

    List<BusinessResponse> getBusinessesByOwner(Long ownerId);

    Page<BusinessResponse> searchBusinesses(BusinessSearchRequest searchRequest, Pageable pageable);

//    void restoreBusiness(Long businessId);

    BusinessResponse updateBusiness(Long businessId, UpdateBusinessRequest request, Long ownerId);

    void deleteBusiness(Long businessId, Long ownerId);
}

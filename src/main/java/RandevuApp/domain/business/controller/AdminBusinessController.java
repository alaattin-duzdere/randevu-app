package RandevuApp.domain.business.controller;

import RandevuApp.domain.business.dto.BusinessResponse;
import RandevuApp.domain.business.dto.BusinessSearchRequest;
import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import RandevuApp.domain.business.service.IAdminBusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/business")
@RequiredArgsConstructor
public class AdminBusinessController {

    private final IAdminBusinessService businessService;

    @GetMapping("/search")
    public Page<BusinessResponse> searchAllBusinesses(
            @ModelAttribute BusinessSearchRequest searchRequest,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return businessService.searchBusinesses(searchRequest, pageable);
    }

    @GetMapping("/{id}")
    public BusinessResponse getBusinessById(@PathVariable Long id) {
        return businessService.getBusinessByIdForAdmin(id);
    }

    @GetMapping("/by-owner/{ownerId}")
    public List<BusinessResponse> getBusinessesByOwner(@PathVariable Long ownerId) {
        return businessService.getBusinessesByOwner(ownerId);
    }

    @GetMapping("/deleted")
    public Page<BusinessResponse> getDeletedBusinesses(
            @RequestParam(required = false) String query,
            @PageableDefault(sort = "updated_at", direction = Sort.Direction.DESC) Pageable pageable) {

        return businessService.getDeletedBusinesses(query, pageable);
    }

    @PutMapping("/{id}")
    public BusinessResponse updateBusinessByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBusinessRequest request
    ) {
        return businessService.updateBusinessByAdmin(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBusinessByAdmin(@PathVariable Long id) {
        businessService.deleteBusinessByAdmin(id);
    }

//    @PutMapping("/{id}/restore")
//    public void restoreBusiness(@PathVariable Long id) {
//        businessService.restoreBusiness(id);
//    }
}

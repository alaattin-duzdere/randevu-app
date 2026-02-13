package RandevuApp.domain.business.controller;

import RandevuApp.api.CustomResponseBody;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/business")
@RequiredArgsConstructor
public class AdminBusinessController {

    private final IAdminBusinessService businessService;

    // --- READ
    @GetMapping("/search")
    public ResponseEntity<CustomResponseBody<Page<BusinessResponse>>> searchAllBusinesses(
            @ModelAttribute BusinessSearchRequest searchRequest,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<BusinessResponse> data = businessService.searchBusinesses(searchRequest, pageable);
        return ResponseEntity.ok(CustomResponseBody.ok(data, "All businesses retrieved successfully (Admin view)"));
    }

    // --- READ (Single by ID - Admin Check) ---
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponseBody<BusinessResponse>> getBusinessById(@PathVariable Long id) {
        BusinessResponse data = businessService.getBusinessByIdForAdmin(id);
        return ResponseEntity.ok(CustomResponseBody.ok(data, "Business details retrieved successfully"));
    }

    // --- READ (By Owner ID - Admin'in belirli bir kişinin işletmelerini görmesi) ---
    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<CustomResponseBody<List<BusinessResponse>>> getBusinessesByOwner(@PathVariable Long ownerId) {
        List<BusinessResponse> data = businessService.getBusinessesByOwner(ownerId);

        return ResponseEntity.ok(CustomResponseBody.ok(data, "Businesses retrieved successfully for owner: " + ownerId));
    }

    // 2. Silinenleri Getir (Geri Dönüşüm Kutusu)
    @GetMapping("/deleted")
    public ResponseEntity<Page<BusinessResponse>> getDeletedBusinesses(
            @RequestParam(required = false) String query,
            @PageableDefault(sort = "updated_at", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(businessService.getDeletedBusinesses(query, pageable));
    }

    // --- UPDATE (Admin Override) ---
    @PutMapping("/{id}")
    public ResponseEntity<CustomResponseBody<BusinessResponse>> updateBusinessByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBusinessRequest request
    ) {
        BusinessResponse data = businessService.updateBusinessByAdmin(id, request);

        return ResponseEntity.ok(CustomResponseBody.ok(data, "Business updated by admin successfully"));
    }

    // --- DELETE (Admin Force Delete) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponseBody<Void>> deleteBusinessByAdmin(@PathVariable Long id) {
        businessService.deleteBusinessByAdmin(id);

        return ResponseEntity.ok(CustomResponseBody.ok(null, "Business deleted by admin successfully"));
    }

//    // 3. Opsiyonel: Geri Yükle
//    @PutMapping("/{id}/restore")
//    public ResponseEntity<Void> restoreBusiness(@PathVariable Long id) {
//        businessService.restoreBusiness(id);
//        return ResponseEntity.ok().build();
//    }
}

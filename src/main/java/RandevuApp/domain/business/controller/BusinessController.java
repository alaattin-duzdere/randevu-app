package RandevuApp.domain.business.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.business.dto.BusinessResponse;
import RandevuApp.domain.business.dto.BusinessSearchRequest;
import RandevuApp.domain.business.dto.CreateBusinessRequest;
import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import RandevuApp.domain.business.service.IBusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final IBusinessService businessService;

    @PostMapping
    // TODO:Yetki kontrolü yapılmalı
    public ResponseEntity<CustomResponseBody<BusinessResponse>> createBusiness(
            @Valid @RequestBody CreateBusinessRequest request,
            Authentication authentication
    ) {
        Long ownerId = Long.parseLong(authentication.getName());

        BusinessResponse data = businessService.createBusiness(request, ownerId);

        CustomResponseBody<BusinessResponse> response = CustomResponseBody.ok(data, "Business created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // --- READ (Single by ID) ---
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponseBody<BusinessResponse>> getBusinessById(@PathVariable Long id) {
        BusinessResponse data = businessService.getBusinessById(id);
        return ResponseEntity.ok(CustomResponseBody.ok(data,"Business retrieved successfully"));
    }

    // --- READ (Single by Slug - Public Profile URL'leri için) ---
    @GetMapping("/slug/{slug}")
    public ResponseEntity<CustomResponseBody<BusinessResponse>> getBusinessBySlug(@PathVariable String slug) {
        BusinessResponse data = businessService.getBusinessBySlug(slug);
        return ResponseEntity.ok(CustomResponseBody.ok(data,"Business retrieved successfully"));
    }

    // --- READ (My Businesses - İşletme sahibinin kendi listesi) ---
    @GetMapping("/my-businesses")
    public ResponseEntity<CustomResponseBody<List<BusinessResponse>>> getMyBusinesses(Authentication authentication) {
        Long ownerId = Long.parseLong(authentication.getName());
        List<BusinessResponse> data = businessService.getBusinessesByOwner(ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(data,"Businesses retrieved successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<CustomResponseBody<Page<BusinessResponse>>> searchBusinesses(
            @ModelAttribute BusinessSearchRequest searchRequest,
            @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        Page<BusinessResponse> data = businessService.searchBusinesses(searchRequest, pageable);

        return ResponseEntity.ok(CustomResponseBody.ok(data, "Businesses retrieved successfully"));
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<CustomResponseBody<BusinessResponse>> updateBusiness(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBusinessRequest request,
            Authentication authentication
    ) {
        Long ownerId = Long.parseLong(authentication.getName());
        BusinessResponse data = businessService.updateBusiness(id, request, ownerId);

        return ResponseEntity.ok(CustomResponseBody.ok(data, "Business updated successfully"));
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponseBody<Void>> deleteBusiness(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long ownerId = Long.parseLong(authentication.getName());
        businessService.deleteBusiness(id, ownerId);

        return ResponseEntity.ok(CustomResponseBody.ok(null, "Business deleted successfully"));
    }

}

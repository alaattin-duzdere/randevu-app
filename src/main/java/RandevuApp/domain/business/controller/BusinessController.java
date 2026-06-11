package RandevuApp.domain.business.controller;

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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final IBusinessService businessService;

    // TODO:Yetki kontrolü yapılmalı
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BusinessResponse createBusiness(
            @Valid @RequestBody CreateBusinessRequest request,
            Authentication authentication
    ) {
        Long ownerId = Long.parseLong(authentication.getName());

        return businessService.createBusiness(request, ownerId);
    }

    @GetMapping("/{id}")
    public BusinessResponse getBusinessById(@PathVariable Long id) {
        return businessService.getBusinessById(id);
    }

    @GetMapping("/slug/{slug}")
    public BusinessResponse getBusinessBySlug(@PathVariable String slug) {
        return businessService.getBusinessBySlug(slug);
    }

    @GetMapping("/my-businesses")
    public List<BusinessResponse> getMyBusinesses(Authentication authentication) {
        Long ownerId = Long.parseLong(authentication.getName());
        return businessService.getBusinessesByOwner(ownerId);
    }

    @GetMapping("/search")
    public Page<BusinessResponse> searchBusinesses(
            @ModelAttribute BusinessSearchRequest searchRequest,
            @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        return businessService.searchBusinesses(searchRequest, pageable);
    }

    @PutMapping("/{id}")
    public BusinessResponse updateBusiness(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBusinessRequest request,
            Authentication authentication
    ) {
        Long ownerId = Long.parseLong(authentication.getName());
        return businessService.updateBusiness(id, request, ownerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBusiness(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long ownerId = Long.parseLong(authentication.getName());
        businessService.deleteBusiness(id, ownerId);
    }

}

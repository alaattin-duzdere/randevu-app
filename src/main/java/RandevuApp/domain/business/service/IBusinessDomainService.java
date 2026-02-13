package RandevuApp.domain.business.service;

import RandevuApp.domain.business.dto.UpdateBusinessRequest;
import RandevuApp.domain.business.model.Business;

public interface IBusinessDomainService {

    Business performUpdateBusiness(Business business, UpdateBusinessRequest request);

    void performDeleteBusiness(Business business);
}

package RandevuApp.exceptions.client;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class ResourceNotFoundException extends BaseApiException {
    public ResourceNotFoundException(String resourceName, String field, Object value) {
        super(
                ErrorCode.ERROR_RESOURCE_NOT_FOUND,
                String.format("%s not found with %s : '%s'", resourceName, field, value)
        );
    }
}

package RandevuApp.integration.location.port;

import RandevuApp.domain.business.dto.GeoLocationResult;

import java.util.Optional;

public interface IGeocodingPort {

    /**
     * DETAY VE DOĞRULAMA (Place Details) -
     * Kullanıcı bir adresi seçip kaydetmek istediğinde, frontend bize sadece bir ID
     * (externalLocationId) gönderir. Biz bu ID'yi alıp harita sağlayıcısına (Mapbox) sorarız
     * ve "Bu ID'nin gerçek enlem/boylamı, şehri nedir?" diye kesinleştiririz.
     * Sahte veri girişini (Postman hilelerini) engellemenin tek yoludur.
     */
    Optional<GeoLocationResult> getPlaceDetailsById(String externalLocationId);
}

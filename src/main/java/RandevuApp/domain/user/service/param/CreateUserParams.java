package RandevuApp.domain.user.service.param;

import RandevuApp.commons.util.ContactFormatUtil;
import RandevuApp.domain.user.model.Gender;
import RandevuApp.exceptions.client.InvalidInputException;

public record CreateUserParams(
        String email,
        String phoneNumber,
        String firstName,
        String lastName,
        Gender gender,
        String address
) {

    public CreateUserParams {

        if (email == null || email.isBlank()) {
            throw new InvalidInputException("Email adresi boş olamaz.");
        }
        if (firstName == null || firstName.trim().length() < 2) {
            throw new InvalidInputException("İsim en az 2 karakter olmalıdır.");
        }

        if(!ContactFormatUtil.isEmail(email)){
            throw new InvalidInputException("Geçersiz email adresi.");
        }
        if (!ContactFormatUtil.isPhone(phoneNumber)) {
            throw new InvalidInputException("Geçersiz telefon numarası.");
        }
    }
}

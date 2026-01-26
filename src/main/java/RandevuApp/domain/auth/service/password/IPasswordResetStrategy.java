package RandevuApp.domain.auth.service.password;

public interface IPasswordResetStrategy {
    String sendResetToken(String email);
}
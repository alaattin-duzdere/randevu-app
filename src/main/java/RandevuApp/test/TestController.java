package RandevuApp.test;

import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.domain.verification.service.VerificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class TestController {
    private final TestUserService testUserService;
    private final UserRepository userRepository;
    private final VerificationService verificationService;
    private final PasswordEncoder passwordEncoder;

    public TestController(TestUserService testUserService, UserRepository userRepository, VerificationService verificationService, PasswordEncoder passwordEncoder) {
        this.testUserService = testUserService;
        this.userRepository = userRepository;
        this.verificationService = verificationService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/test/saveuser")
    public String testSaveUser(@RequestBody TestUserDto testUserDto) {
        User mockUser = testUserService.createMockUser(testUserDto.getPassword(), testUserDto.getEmail());

        userRepository.save(mockUser);
        return "User saved successfully";
    }

    @PostMapping("/test/verify")
    public String testIsPassTrue(@RequestParam String pass, @RequestParam String email){

        User user = userRepository.findByEmail(email).get();
        if (user==null){
            return "kullanıcı bulunamadı";
        }

        if (passwordEncoder.matches(pass,user.getPassword())){
            return "şifre doğru";
        }

        return "şifre yanlış";
    }

    @GetMapping("/private")
    public String testPrivateController(){
        return "Hello from private controller";
    }

}
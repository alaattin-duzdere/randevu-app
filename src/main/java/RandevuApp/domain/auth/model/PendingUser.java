package RandevuApp.domain.auth.model;

import RandevuApp.domain.user.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("pending_user")
public class PendingUser implements Serializable {

    @Id
    private String phoneNumber;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Gender gender;

    @TimeToLive(unit = TimeUnit.MINUTES)
    @Builder.Default
    private Long ttl = 15L;
}

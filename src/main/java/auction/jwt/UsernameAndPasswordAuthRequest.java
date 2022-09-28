package auction.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UsernameAndPasswordAuthRequest {
    private String username;
    private String password;
}

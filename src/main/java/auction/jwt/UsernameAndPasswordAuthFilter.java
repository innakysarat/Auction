package auction.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
// this class validates credentials
public class UsernameAndPasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    // validates credentials
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {

            // get request.inputstream() and put it to the class UsernameAndPasswordAuthRequest
            UsernameAndPasswordAuthRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    // principal
                    authenticationRequest.getUsername(),
                    // credentials
                    authenticationRequest.getPassword()
            );
            // .authenticate() check whether a username exists
            // if yes, then whether a password is correct or not
            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException e) {
            //AuthenticationException <- RuntimeException
            throw new RuntimeException(e);
        }

    }

    // creates and sends a token
    // this method is executed after the attemptAuthentication() if it was successful
    // (credentials were validated)
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = Jwts.builder()
                .setSubject(authResult.getName()) // inna
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
               // .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey) // sign a token - 3 часть токена
                .compact(); // compacting into its final String form
        // a signed JWT is called 'JWS'

        //response.setHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
        // add a token to a response header
        //response.addHeader("Authorization", "Bearer " + token);
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
    }
}


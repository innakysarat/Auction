package auction.jwt;

import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import com.google.common.base.Strings;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// some filters are invoked more than once
// this one will be invoked only once per request
@AllArgsConstructor
public class TokenVerifier extends OncePerRequestFilter {
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        // get token from header
        String authorizationHeader = request.getHeader("Authorization");
        // the request is rejected
        // token prefix = "Bearer "
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        // убрать "Bearer " у токена
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        try {
            // тело токена берём
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            // можем взять что угодно с тела(аттрибуты юзера), берём имя
            String username = body.getSubject();

            // authority - st:write ...
            // authority - ROLE_
            var authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }
        // позволяем дальше продолжать цепочку фильтров
        filterChain.doFilter(request, response);
    }
}

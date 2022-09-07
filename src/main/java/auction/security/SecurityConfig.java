package auction.security;

import auction.jwt.JwtConfig;
import auction.jwt.TokenVerifier;
import auction.jwt.UsernameAndPasswordAuthFilter;
import auction.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({JwtConfig.class})

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // sessions are stored in a database now
                .and()
                .addFilterBefore(new CorsFilter(), UsernameAndPasswordAuthFilter.class)
                .addFilter(new UsernameAndPasswordAuthFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new TokenVerifier(secretKey, jwtConfig), UsernameAndPasswordAuthFilter.class)
                .authorizeRequests()
                .antMatchers("/**", "/api/users", "index", "/css/*", "/js/*").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}

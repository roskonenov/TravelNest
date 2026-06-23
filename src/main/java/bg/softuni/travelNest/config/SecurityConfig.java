package bg.softuni.travelNest.config;

import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.service.impl.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/",
            "/about",
            "/services",
            "/contact",
            "/users/**",
            "/*/rental",
            "/*/details/*",
            "/*/list"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, CustomAuthSuccessHandler customAuthSuccessHandler) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(HttpMethod.GET, AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").not().authenticated()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/users/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(customAuthSuccessHandler)
                        .failureUrl("/users/login-error"))
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeParameter("rememberMe")
                        .key("remember Me Encryption Key")
                        .rememberMeCookieName("rememberMeCookie")
                        .tokenValiditySeconds(604800))
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("rememberMeCookie")
                        .invalidateHttpSession(true))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}

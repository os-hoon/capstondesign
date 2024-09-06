package cherrysumer.cherrysumer.config;

import cherrysumer.cherrysumer.util.jwt.*;
import cherrysumer.cherrysumer.service.CustomUserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailService userDetailService;
    private final JwtExceptionFilter jwtExceptionFilter;

    private static final String[] permitRequest = {
            "/user/join", "/user/id-exists", "/user/nickname-exists",
            "/user/email-verification", "/user/login",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .sessionManagement((session) -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .formLogin((form) -> form.disable())
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(permitRequest).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthFilter(userDetailService, tokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // 인증 예외
                                .accessDeniedHandler(new JwtAccessDenyHandler())) // 인가 예외
        ;

        return http.build();
    }
}

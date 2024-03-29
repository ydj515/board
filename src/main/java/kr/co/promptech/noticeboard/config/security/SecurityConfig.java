package kr.co.promptech.noticeboard.config.security;

import kr.co.promptech.noticeboard.config.security.handler.CustomAccessDeniedHandler;
import kr.co.promptech.noticeboard.config.security.handler.CustomAuthenticationEntryPoint;
import kr.co.promptech.noticeboard.config.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Resource 대상
    public static final String[] RESOURCE_LIST = {
            "/css/**", "/fonts/**", "/images/**", "/js/**", "/modules/**", "/h2-console/**", "/swagger-ui/**"
    };

    // 권한 제외 대상
    public static final String[] PERMITTED_LIST = {
            "/", "/login", "/logout", "/swagger-ui/**", "/swagger-ui", "/swagger/**", "/v3/api-docs/**"
    };

    // 인터셉터 제외 대상
    public static final String[] INTERCEPTOR_LIST = {
            "/css/**", "/fonts/**", "/images/**", "/js/**", "/modules/**", "/login", "/logout", "/upload"
    };

    // api white list
    private static final String[] API_WHITE_LIST = {
            "/api/auth/join",
            "/api/auth/login",
            "/api/auth/refresh",
    };

    private final TokenProvider tokenProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(config -> config.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .authorizeHttpRequests(auth -> {
                    try {
                        auth
                                .requestMatchers(RESOURCE_LIST).permitAll()
                                .requestMatchers(PERMITTED_LIST).permitAll()
                                .requestMatchers(INTERCEPTOR_LIST).permitAll()
                                .requestMatchers(API_WHITE_LIST).permitAll()
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll() // 로그인 하지 않고 모두 권한을 가짐
                        ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionHandling(config -> config.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
                ).sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .apply(new JwtSecurityConfig(tokenProvider))
        ;
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
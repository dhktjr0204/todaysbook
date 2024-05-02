package com.example.todaysbook.config;

import com.example.todaysbook.domain.entity.Role;
import com.example.todaysbook.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailService;

    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations()
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(auth ->              // 인증, 인가 설정
                    auth.requestMatchers(
                                    "/index",
                                    "/search",
                                    "/detail"
                                    ).permitAll()
                            .requestMatchers("/list",
                                    "/registration",
                                    "/mypage/**",
                                    "/cart/list",
                                    "/payment/**").hasAnyRole(Role.ROLE_COMMON_BRONZE.value(), Role.ROLE_COMMON_SILVER.value(), Role.ROLE_COMMON_GOLD.value(), Role.ROLE_COMMON_DIAMOND.value())
                            .requestMatchers("/admin/**").hasRole(Role.ROLE_ADMIN.value())
                            .anyRequest().permitAll()
            );

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);

        httpSecurity
                .formLogin((auth) -> auth.loginPage("/signup")
                        .loginProcessingUrl("user/registration")
                        .permitAll()
                );

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }
}

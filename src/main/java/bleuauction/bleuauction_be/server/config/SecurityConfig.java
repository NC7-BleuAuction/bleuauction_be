package bleuauction.bleuauction_be.server.config;

import bleuauction.bleuauction_be.server.common.jwt.APILoginFilter;
import bleuauction.bleuauction_be.server.common.jwt.APILoginSuccessHandler;
import bleuauction.bleuauction_be.server.common.jwt.APIUserDetailsService;
import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import bleuauction.bleuauction_be.server.common.jwt.RefreshTokenFilter;
import bleuauction.bleuauction_be.server.common.jwt.TokenCheckFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
  private final JwtUtils jwtUtils;
  private final APIUserDetailsService apiUserDetailsService;
  private final CorsConfigurationSource corsConfigurationSource;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
                    authorizationManagerRequestMatcherRegistry ->
                            authorizationManagerRequestMatcherRegistry
                                    .anyRequest()
                                    .permitAll()
            ).logout(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .userDetailsService(apiUserDetailsService)
            .addFilterBefore(new APILoginFilter("/generateToken",  new APILoginSuccessHandler(jwtUtils)), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new TokenCheckFilter(apiUserDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new RefreshTokenFilter("refreshToken", jwtUtils), TokenCheckFilter.class)
            //H2 사용을 하기 위한 옵션
            //.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource)); //CORS Spring Boot 설정
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories
            .createDelegatingPasswordEncoder();
  }

}

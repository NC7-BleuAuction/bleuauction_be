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

  // TODO : 추후 WhiteList 항목 코드 변경 필요
  private static String[] tempWhiteListArray = {"/hello", "/health"};

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder
            .userDetailsService(apiUserDetailsService)
            .passwordEncoder(passwordEncoder());

    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
    http.authenticationManager(authenticationManager);

    // 로그인 필터
    APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
    apiLoginFilter.setAuthenticationManager(authenticationManager);

    // 로그인 성공시 호출 핸들러 설정
    APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtils);
    apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

    // UsernamePasswordAuthenticationFilter 앞쪽으로 APILoginFilter지정
    http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

    // Token검증 필터 추가
    http.addFilterBefore(
            new TokenCheckFilter(apiUserDetailsService, jwtUtils),
            UsernamePasswordAuthenticationFilter.class
    );

    // refreshToken 필터 등록 - JWT관련 다른 필터들 이전에 동작하도록 TokenCheckFilter 앞에 배치
    http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtils), TokenCheckFilter.class);

    http.authorizeHttpRequests(
                    authorizationManagerRequestMatcherRegistry ->
                            authorizationManagerRequestMatcherRegistry
                                    .anyRequest()
                                    .permitAll()
            ).logout(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource)); //CORS Spring Boot 설정

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories
            .createDelegatingPasswordEncoder();
  }

}

package bleuauction.bleuauction_be.server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
  private static String[] tempWhiteListArray = {"/hello", "/health"};

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
                                         HandlerMappingIntrospector introspector) throws Exception {
    http.authorizeHttpRequests(
                    authorizationManagerRequestMatcherRegistry ->
                            authorizationManagerRequestMatcherRegistry
                                    .anyRequest()
                                    .permitAll()
            )
            .logout(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(httpSecurityCorsConfigurer -> {
              CorsConfiguration configuration = new CorsConfiguration();

              configuration.setAllowedOriginPatterns(Arrays.asList("*"));
              configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
              configuration.setAllowedHeaders(Arrays.asList("*"));
//              configuration.setAllowedOrigins(List.of("http://localhost:3000"));
              configuration.setAllowedOrigins(List.of("http://bleuauction.co.kr:80"));
              configuration.setAllowCredentials(true);

              UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
              source.registerCorsConfiguration("/**", configuration);
              httpSecurityCorsConfigurer.configurationSource(source);
            })
            .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                    FrameOptionsConfig::sameOrigin)
            );
    return http.build();
  }
  @Bean
  public PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories
            .createDelegatingPasswordEncoder();
  }
 }

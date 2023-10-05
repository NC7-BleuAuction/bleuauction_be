package bleuauction.bleuauction_be;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private static String[] tempWhiteListArray = {"/hello", "/health"};


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
//        http.authorizeHttpRequests(
//                authorizationManagerRequestMatcherRegistry ->
//                        authorizationManagerRequestMatcherRegistry.requestMatchers(
//                                        new MvcRequestMatcher(introspector, "/hello"),
//                                        new MvcRequestMatcher(introspector, "/health")
//
//                                )
//                                .permitAll()
//        );
//        return http.build();
//    }
//}

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .anyRequest().permitAll() // 모든 URL에 대한 접근을 허용
        );
        return http.build();
    }
}

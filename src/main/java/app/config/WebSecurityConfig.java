package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/login", "/css/**", "/js/**", "/error", "/").permitAll()
                        .requestMatchers("/api/rest/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")              // The URL to trigger logout
                        .logoutSuccessUrl("/login?logout") // Where to go after
                        .permitAll()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // CRITICAL FIX: Add /logout to the ignoring list
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/rest/**", "/logout"));

        return http.build();
    }

}
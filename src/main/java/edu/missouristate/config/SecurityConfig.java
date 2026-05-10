package edu.missouristate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Password encoder bean for hashing passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure the security filter chain for the application.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Static resources allowed for everyone
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        // Home and login pages accessible to everyone
                        .requestMatchers("/", "/login").permitAll()
                        // Admin pages: only users with role ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Student pages: only users with role STUDENT
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                // Form login configuration
                .formLogin(form -> form
                        .loginPage("/login")                  // custom login page
                        .loginProcessingUrl("/perform_login") // form submit URL
                        .defaultSuccessUrl("/dashboard", true) // redirect after successful login
                        .permitAll()
                )
                // Logout configuration
                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        // Optional: disable CSRF temporarily if testing
        // http.csrf().disable();

        return http.build();
    }
}

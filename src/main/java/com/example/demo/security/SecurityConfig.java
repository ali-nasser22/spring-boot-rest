package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    /*
    * in-memory auth system
    * for jdbc auth see "jdbc-auth.md"
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails ali = User.builder()
                .username("ali")
                .password("{noop}12345")
                .roles("manager")
                .build();

        UserDetails ahmad = User.builder()
                .username("ahmad")
                .password("{noop}12345")
                .roles("admin")
                .build();

        UserDetails james = User.builder()
                .username("james")
                .password("{noop}12345")
                .roles("employee")
                .build();

        return new InMemoryUserDetailsManager(ali, ahmad, james);
    }
*/
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer->
                configurer
                        .requestMatchers(HttpMethod.GET,"/").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/employees").hasAnyRole("ADMIN","MANAGER","EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/employees/**").hasAnyRole("ADMIN","MANAGER")
                        .requestMatchers(HttpMethod.POST,"/employees").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/employees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/employees/**").hasRole("ADMIN")

        );

        http.httpBasic(Customizer.withDefaults());


        // use csrf with web-forms but in rest api it should be disabled
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

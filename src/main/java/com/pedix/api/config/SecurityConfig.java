package com.pedix.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // Público
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Páginas base
                        .requestMatchers("/", "/home", "/403").authenticated()

                        // =========================
                        // FRONT WEB
                        // =========================

                        // Home
                        .requestMatchers(HttpMethod.GET, "/api", "/api/home").hasRole("ADMIN")

                        // Cardápio - visualização para ADMIN e GARCOM
                        .requestMatchers(HttpMethod.GET, "/cardapio", "/cardapio/**")
                        .hasAnyRole("ADMIN", "GARCOM")

                        // Cardápio - manutenção só ADMIN
                        .requestMatchers(
                                "/cardapio/novo",
                                "/cardapio/salvar",
                                "/cardapio/editar/**",
                                "/cardapio/atualizar/**",
                                "/cardapio/excluir/**"
                        ).hasRole("ADMIN")

                        // Pedidos - operação para ADMIN e GARCOM
                        .requestMatchers(HttpMethod.GET, "/pedidos", "/pedidos/novo", "/pedidos/**")
                        .hasAnyRole("ADMIN", "GARCOM")

                        .requestMatchers(HttpMethod.POST, "/pedidos/salvar")
                        .hasAnyRole("ADMIN", "GARCOM")

                        .requestMatchers(HttpMethod.POST, "/pedidos/atualizar/**")
                        .hasAnyRole("ADMIN", "GARCOM")

                        // Cancelar / excluir pedido só ADMIN
                        .requestMatchers(HttpMethod.POST, "/pedidos/cancelar/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/pedidos/excluir/**")
                        .hasRole("ADMIN")

                        // =========================
                        // API - SOMENTE ADMIN
                        // =========================
                        .requestMatchers("/api/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403")
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails garcom = User.builder()
                .username("garcom")
                .password(passwordEncoder.encode("garcom123"))
                .roles("GARCOM")
                .build();

        return new InMemoryUserDetailsManager(admin, garcom);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
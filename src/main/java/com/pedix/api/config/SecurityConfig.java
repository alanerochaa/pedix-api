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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/", "/home", "/403").permitAll()
                        .requestMatchers("/api/health").permitAll()

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api",
                                "/api/home",
                                "/api/item-cardapio/**",
                                "/api/categorias-cardapio/**",
                                "/api/avaliacoes/**",
                                "/api/historicos-pedidos/**",
                                "/api/relatorios/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/avaliacoes").permitAll()

                        // LIBERADO PARA O MOBILE - CRUD CARDÁPIO
                        .requestMatchers("/api/item-cardapio/**").permitAll()
                        .requestMatchers("/api/categorias-cardapio/**").permitAll()

                        // RESTANTE DA API CONTINUA PROTEGIDA
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/cardapio", "/cardapio/**")
                        .hasAnyRole("ADMIN", "GARCOM")

                        .requestMatchers(
                                "/cardapio/novo",
                                "/cardapio/salvar",
                                "/cardapio/editar/**",
                                "/cardapio/atualizar/**",
                                "/cardapio/excluir/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/pedidos", "/pedidos/**")
                        .hasAnyRole("ADMIN", "GARCOM")

                        .requestMatchers(HttpMethod.POST, "/pedidos/**")
                        .hasAnyRole("ADMIN", "GARCOM")

                        .requestMatchers("/pedidos/cancelar/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .permitAll()
                )

                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403")
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
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
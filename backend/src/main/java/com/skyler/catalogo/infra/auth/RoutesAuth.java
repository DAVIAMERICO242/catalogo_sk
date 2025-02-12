package com.skyler.catalogo.infra.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class RoutesAuth {

    private final JwtInterceptor jwtInterceptor;

    public RoutesAuth(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults()) // Add this line to enable CORS
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/produtos/**", HttpMethod.GET.toString())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/cep/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/carrinho/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/login-by-loja")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/shipping-calculator/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/lojas/**",HttpMethod.GET.toString())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/descontos/**",HttpMethod.GET.toString())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/catalogo/**",HttpMethod.GET.toString())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/media/banner",HttpMethod.GET.toString())).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/pedidos",HttpMethod.POST.toString())).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtInterceptor, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

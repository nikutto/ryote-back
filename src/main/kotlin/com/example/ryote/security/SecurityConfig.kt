package com.example.ryote.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.csrf()
            .disable()

        // http.authorizeExchange()
        //     .pathMatchers("/login").permitAll()
        //     .anyExchange().permitAll()

        // http.formLogin()
        //     .loginPage("/login")

        // http.cors(Customizer.withDefaults())
        http.authorizeExchange()
            .pathMatchers("/login").permitAll()
            .pathMatchers("/admin").hasRole("ADMIN")
            .anyExchange().authenticated()
        http.formLogin()
            .loginPage("/login")
        return http.build()
    }

    @Bean
    fun userDetailsService(): ReactiveUserDetailsService {
        val user =
            User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build()

        return MapReactiveUserDetailsService(user)
    }

    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val configuration = CorsConfiguration()
        configuration.setAllowedOrigins(listOf("http://localhost:3000"))
        configuration.setAllowedMethods(listOf("GET", "POST"))
        configuration.setAllowCredentials(true)
        configuration.setAllowedHeaders(listOf("*"))

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return CorsWebFilter(source)
    }
}

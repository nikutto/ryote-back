package com.example.ryote.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    class AuthenticationSuccessHandler : ServerAuthenticationSuccessHandler {
        override fun onAuthenticationSuccess(
            webFilterExchange: WebFilterExchange,
            authentication: Authentication
        ): Mono<Void> {
            webFilterExchange
                .getExchange()
                .getResponse()
                .setStatusCode(HttpStatus.OK)
            return Mono.empty<Void>().then()
        }
    }

    class AuthenticationFailureHandler : ServerAuthenticationFailureHandler {
        override fun onAuthenticationFailure(
            webFilterExchange: WebFilterExchange,
            exception: AuthenticationException
        ): Mono<Void> {
            webFilterExchange
                .getExchange()
                .getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED)
            return Mono.empty<Void>().then()
        }
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.csrf()
            .disable()

        http.authorizeExchange()
            .pathMatchers("/login", "/health").permitAll()
            .anyExchange().authenticated()

        http.formLogin()
            .loginPage("/login")
            .authenticationSuccessHandler(AuthenticationSuccessHandler())
            .authenticationFailureHandler(AuthenticationFailureHandler())
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

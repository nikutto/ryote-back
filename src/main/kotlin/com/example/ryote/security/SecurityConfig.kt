package com.example.ryote.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Autowired val loginSuccessHandler: LoginSuccessHandler,
    @Autowired val loginFailureHandler: LoginFailureHandler
) : WebSecurityConfigurerAdapter() {

    protected override fun configure(http: HttpSecurity) {
        http
            .csrf()
            .disable()
        http
            .authorizeRequests()
            .antMatchers(
                HttpMethod.GET,
            ).permitAll()
            .anyRequest().authenticated()
        http
            .formLogin()
            .loginProcessingUrl("/login")
            .loginPage("/login_hello")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .permitAll()
        http
            .logout().permitAll()
        http
            .cors(Customizer.withDefaults())
    }

    @Bean
    override fun userDetailsService(): UserDetailsService {
        val user =
            User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build()

        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.setAllowedOrigins(listOf("http://localhost:3000"))
        configuration.setAllowedMethods(listOf("GET", "POST"))
        configuration.setAllowCredentials(true)
        configuration.setAllowedHeaders(listOf("*"))
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}

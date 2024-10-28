package com.login.demo.configurations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Suppress("DEPRECATION")
@Configuration
@EnableWebSecurity
class SecurityConfig(private val authenticationConfiguration: AuthenticationConfiguration) {

@Autowired
lateinit var  securityFilter: SecurityFilter;

 @Bean
 fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
  http{
   csrf {
    disable()
   }
    authorizeRequests {
     authorize("/user/**", permitAll);
    }
     sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS}
     addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

  return http.build()
 }

@Bean
  fun authenticationManager(): AuthenticationManager {
   return authenticationConfiguration.getAuthenticationManager();
  }

 @Bean
 fun passwordEncoder(): PasswordEncoder {
  return BCryptPasswordEncoder()
 }
}


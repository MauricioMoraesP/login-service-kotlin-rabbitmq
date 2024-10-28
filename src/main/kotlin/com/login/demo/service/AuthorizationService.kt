package com.login.demo.service

import com.login.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthorizationService : UserDetailsService{
    @Autowired
    lateinit var repository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
         if (username == null) {
            throw UsernameNotFoundException("Username cannot be null")
        }
        return repository.findByLogin(username) ?: throw UsernameNotFoundException("User not found with username: $username")
    }
}
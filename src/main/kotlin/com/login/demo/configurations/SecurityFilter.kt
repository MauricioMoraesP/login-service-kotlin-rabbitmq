package com.login.demo.configurations

import com.login.demo.model.User
import com.login.demo.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter : OncePerRequestFilter() {

    @Autowired
    var tokenService: TokenService? = null

    @Autowired
    var userRepository: UserRepository? = null

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = this.recoverToken(request)
        if (token != null) {
            val login = tokenService?.validateToken(token)

             if (login != null) {
                val user: User? = userRepository?.findByLogin(login.toString())

                if (user != null) {
                    val authentication = UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
                     SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun recoverToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")
        return if (authHeader != null) {
            authHeader.replace("Bearer ", "")
        } else {
            null
        }
    }
}

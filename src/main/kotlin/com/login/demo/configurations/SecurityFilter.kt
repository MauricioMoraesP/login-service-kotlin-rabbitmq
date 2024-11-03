package com.login.demo.configurations

import com.login.demo.model.User
import com.login.demo.repository.UserRepository
import com.login.demo.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var tokenService: TokenService


    @Autowired
    lateinit var userRepository: UserRepository;

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
                    if(user.getChangesAccount().toString()!=tokenService.verifyTokenChangesAccount(token)){
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token de sessão inválido. Por favor, faça login novamente.")
                    }
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

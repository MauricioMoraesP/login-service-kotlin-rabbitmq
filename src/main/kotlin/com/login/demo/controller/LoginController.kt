package com.login.demo.controller

import com.login.demo.configurations.TokenService
import com.login.demo.dto.CreateUserRequest
import com.login.demo.dto.LoginDto
import com.login.demo.model.User
import com.login.demo.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/user")
 class LoginController (private val userService: UserService) {

     @Autowired
     private lateinit var authenticationManager: AuthenticationManager;

    @Autowired
    private lateinit var token:TokenService;

    //logar
    @PostMapping("/login")
    fun login(@Valid @RequestBody loginDto: LoginDto): ResponseEntity<Map<String, String>> {
        val loginAndPassword = UsernamePasswordAuthenticationToken(loginDto.login, loginDto.password)
        val auth = this.authenticationManager.authenticate(loginAndPassword)
        val token = token.generateToken(auth.principal as User)
        return ResponseEntity.ok(mapOf("token" to token))
    }


    //Criar a conta
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(@Valid @RequestBody request: CreateUserRequest): Any {
        println(request);
         return try {
            val newUser = User(request.login, request.password, request.email)
            userService.create(newUser)
            "Criação realizada com sucesso!"
        } catch (e: Exception) {
            return e.message.toString()
        }
    }


    //Editar informações de usuário
    @PutMapping("/edit-user")
    fun changeUser(@RequestBody user: User): String {

        return "";
    }
    //Validar conta do usuário
    @PutMapping("/validate-user")
    fun validar(@RequestBody user: User): String {
        return "";
    }
}
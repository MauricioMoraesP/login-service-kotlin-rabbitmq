package com.login.demo.controller
import com.login.demo.service.TokenService
import com.login.demo.dto.CreateUserRequest
import com.login.demo.dto.LoginDto
import com.login.demo.dto.UpdateUserDTO
import com.login.demo.model.User
import com.login.demo.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.naming.AuthenticationException


@RestController
@RequestMapping("/user")
@Validated
 class LoginController(private val userService: UserService) {


    @Autowired
     private lateinit var authenticationManager: AuthenticationManager;

    @Autowired
    private lateinit var token: TokenService;


    @PostMapping("/login")
    fun login(@Valid @RequestBody loginDto: LoginDto): ResponseEntity<Map<String, String>> {
         try {
             val loginAndPassword = UsernamePasswordAuthenticationToken(loginDto.login, loginDto.password)
             val auth = this.authenticationManager.authenticate(loginAndPassword)
             val tokenInit= token.generateToken(auth.principal as User)
             val idUser=  token.verifyToken(tokenInit)?.replace("\"", "")?.trim();
             if(idUser != null){
                 if(token.verifyTokenChangesAccount(tokenInit)=="true"){
                     userService.resetChanges(idUser.toLong());
                     val tokenReplace= token.generateToken(auth.principal as User)
                     return ResponseEntity.ok(mapOf("token" to tokenReplace))
                 }else{
                     return ResponseEntity.ok(mapOf("token" to tokenInit))
                 }
             }
             throw UsernameNotFoundException("Account not found");
          }
         catch (ex: AuthenticationException) {
              val errorMessage = ex.message ?: "Unauthorized"
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to errorMessage))
         }
    }



    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(@RequestBody @Valid request: CreateUserRequest): Map<String,String> {
    println(request);
         return try {
             val newUser = User(request.login, request.password, request.email, request.userName,)
            userService.create(newUser)
           mapOf("message" to "Sucesso! Sua conta foi criada com sucesso!")
        } catch (e: Exception) {
            return mapOf("error" to "${e.message}")
        }
    }



    @PutMapping("/edit-user")
    @ResponseStatus(HttpStatus.OK)
    fun changeUser(@RequestHeader("Authorization") authHeader: String,
                   @RequestBody updateUser: UpdateUserDTO): String {
         val tokenBearer = authHeader.replace("Bearer ", "").trim()
           try {
             val idUser=  token.verifyToken(tokenBearer)?.replace("\"", "")?.trim();
               println(token.verifyTokenChangesAccount(tokenBearer));
                if(!(idUser.isNullOrEmpty())){
                      userService.updateUser(idUser.toLong(),updateUser.userName, updateUser.password, updateUser.email,updateUser.login)
             }

         }
         catch (e: Exception) {
    println(e.message)
         }

        return ""
    }

    //Validar conta do usuário conectar com microsserviço
    @PutMapping("/validate-user")
    fun validar(@RequestBody user: User): String {
        return "";
    }
}
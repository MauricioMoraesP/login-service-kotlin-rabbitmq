package com.login.demo.controller
import com.login.demo.service.TokenService
import com.login.demo.dto.CreateUserRequest
import com.login.demo.dto.EmailFormatDTO
import com.login.demo.dto.LoginDto
import com.login.demo.dto.UpdateUserDTO
import com.login.demo.model.User
import com.login.demo.service.UserService
import jakarta.validation.Valid
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
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
 class LoginController(private val userService: UserService,  @Autowired
private  var rabbitTemplate : RabbitTemplate,@Autowired
private  var authenticationManager: AuthenticationManager,  @Autowired
private  var token: TokenService) {


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
    fun createAccount(@RequestBody @Valid request: CreateUserRequest): Map<String, String> {
        println(request)
        return try {
            val newUser = User(request.login, request.password, request.email, request.userName)
            userService.create(newUser)
             val routingKey = "users.v1.user-validate"
            val objectSendEmail=EmailFormatDTO(newUser.getEmail(), "Sua conta foi criada com sucesso ${newUser.username}!!", "Criação de conta com sucesso!")
             rabbitTemplate.convertAndSend("my.direct",routingKey, objectSendEmail)
            mapOf("message" to "Sucesso! Sua conta foi criada com sucesso!")
        } catch (e: Exception) {
            mapOf("error" to "${e.message}")
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
    @GetMapping("/validate-user")
    fun validar(@RequestParam hashCode:String): String {
    try {
        println(hashCode);
        val idValidation=token.verifyToken(hashCode);
        val idUser = idValidation.replace("\"", "")?.trim()?.toLongOrNull()
        if (idUser != null) {
            val user = userService.getUserId(idUser).orElse(null)
            println(user.getId().toString().toLong())
            userService.verifyAccount(idUser);
        }
     }catch (e:Exception){
        println(e.message);
    }
        return "";
    }

    @GetMapping("/link-verify")
    fun sendEmailVerify(@RequestHeader("Authorization") authHeader: String): String {
        val tokenBearer = authHeader.replace("Bearer ", "").trim()
        try {
            val idUser = token.verifyToken(tokenBearer)?.replace("\"", "")?.trim()?.toLongOrNull()
            if (idUser != null) {
                val user = userService.getUserId(idUser).orElse(null)
                if (user != null) {
                    val routingKey = "users.v1.user-validate"
                    val verificationToken = token.generateTokenVerify(user)
                    val verificationUrl = "http://localhost:8090/user/validate-user?hashCode=$verificationToken"

                    val objectSendEmail = EmailFormatDTO(
                        user.getEmail(),
                        """
                    <html>
                    <body>
                      <p>Olá! Acesse esse link para validação da sua conta:</p>
                      <p><a href="$verificationUrl">Clique aqui!</a></p>
                    </body>
                    </html>
                    """.trimIndent(),
                        "Criação de conta com sucesso!"
                    )

                    rabbitTemplate.convertAndSend("my.direct", routingKey, objectSendEmail)
                    return verificationUrl
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return ""
    }


}
package com.login.demo.controller
import com.login.demo.dto.*
import com.login.demo.service.TokenService
import com.login.demo.model.User
import com.login.demo.service.UserService
import jakarta.validation.Valid
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/user")
@Validated
 class LoginController(private val userService: UserService,  @Autowired
private  var rabbitTemplate : RabbitTemplate,@Autowired
private  var authenticationManager: AuthenticationManager,  @Autowired
private  var token: TokenService) {


    @PostMapping("/login")
    fun login(@Valid @RequestBody loginDto: LoginDTO): ResponseEntity<Map<String, String>> {
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
             throw BadCredentialsException("Account not found!");
          }
         catch (ex:BadCredentialsException){
             val errorMessage = ex.message ?: "Unauthorized"
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to errorMessage))
         }

    }



    @PostMapping("/create")
     fun createAccount(@RequestBody @Valid request: CreateUserRequestDTO): ResponseEntity<Map<String, String>> {
        println(request)
        return try {
            val newUser = User(request.login, request.password, request.email, request.userName)
            userService.create(newUser)
             val routingKey = "users.v1.user-validate"
             val objectSendEmail=EmailFormatDTO(newUser.getEmail(), "Sua conta foi criada com sucesso ${newUser.username}!!", "Criação de conta com sucesso!")
             rabbitTemplate.convertAndSend("my.direct",routingKey, objectSendEmail)
             return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("message" to "Sucesso! Sua conta foi criada com sucesso!"))
        }

        catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "${e.message}"))        }
    }


    @PutMapping("/edit-user")
    @ResponseStatus(HttpStatus.OK)
    fun changeUser(@RequestHeader("Authorization") authHeader: String,
                   @RequestBody updateUser: UpdateUserDTO): Any {
         val tokenBearer = authHeader.replace("Bearer ", "").trim()
           try {
             val idUser=  token.verifyToken(tokenBearer)?.replace("\"", "")?.trim();

                 if(!(idUser.isNullOrEmpty())){
                     val user = userService.getUserId(idUser.toLong()).orElse(null)
                     if(user.getCodeVerification()==false){

                      }else{
                         userService.updateUser(idUser.toLong(),updateUser.userName, updateUser.password, updateUser.email,updateUser.login);
                         return ResponseEntity.status(HttpStatus.OK).body( "atualização feita com sucesso!");
                     }

              }

         }
         catch (e: Exception) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "Não foi possível realizar a atualização, tente novamente!");
          }

        return ""
    }

     @GetMapping("/validate-user")
     @ResponseStatus(HttpStatus.OK)
    fun validar(@RequestParam hashCode:String): String {
      try {
        val idValidation=token.verifyToken(hashCode);
        val idUser = idValidation.replace("\"", "")?.trim()?.toLongOrNull()
        if (idUser != null) {
            val user = userService.getUserId(idUser).orElse(null)
            println(user.getId().toString().toLong())
            userService.verifyAccount(idUser);
            return "Validação feita com sucesso!"
        }
          throw Exception("Usuário não encontrado ou token inválido")
     }catch (e:Exception){
        return e.message.toString()
    }
    }

    @GetMapping("/link-verify")
    fun sendEmailVerify(@RequestHeader("Authorization") authHeader: String) {
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

                }
            }
        } catch (e: Exception) {

        }

    }


}
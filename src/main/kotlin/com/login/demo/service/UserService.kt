package com.login.demo.service

 import com.login.demo.model.User
import com.login.demo.repository.UserRepository
 import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userRepository: UserRepository

    fun create(user: User) {
        val existingUserByLogin = userRepository.findByLogin(user.getLogin())
        if (existingUserByLogin != null) {
            throw Exception("Esse login já existe, por favor, faça outra tentativa!")
        }

        val existingUserByEmail = userRepository.findByEmail(user.getEmail())
        if (existingUserByEmail != null) {
            throw Exception("Esse e-mail já existe, por favor, faça outra tentativa!")
        }

        val password = user.getPassword()
        user.setPassword(passwordEncoder.encode(password))
        userRepository.save(user)
    }

    fun updateUser(userId: Long, name: String? = null, password: String? = null, email: String? = null, login: String? = null) {
        val userOptional = userRepository.findById(userId)
        if (userOptional.isPresent) {
            val user = userOptional.get()
            println(user);
            if (!name.isNullOrBlank()) {
                userRepository.updateUsernameById(userId, name)
                println("Nome atualizado para: $name")
            }
            if (!login.isNullOrBlank()) {
                userRepository.updateLoginById(userId, login)
                println("Nome atualizado para: $name")
            }

            if (!password.isNullOrBlank()) {
                userRepository.updatePasswordById(userId, passwordEncoder.encode(password))
                println("Senha atualizada.")
            }

            if (!email.isNullOrBlank()) {
                userRepository.updateEmailById(userId, email)
                println("Email atualizado para: $email")
            }
            if(!name.isNullOrBlank() || !login.isNullOrBlank() || !password.isNullOrBlank() || !email.isNullOrBlank()){
                userRepository.updateChangesUpdate(userId, true);
            }

        } else {
            println("Usuário não encontrado com o ID: $userId")
        }
    }

    fun resetChanges(userId: Long) {
        val userOptional = userRepository.updateChangesUpdate(userId, false);

    }

    fun getUserId(userId:Long): Optional<User> {
        return userRepository.findById(userId)
    }

    fun verifyAccount(userId:Long) {
         userRepository.updateCodeVerifcation(userId,true);
    }




}

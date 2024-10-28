package com.login.demo.service

 import com.login.demo.dto.ErrorDto
 import com.login.demo.model.User
import com.login.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
 import org.springframework.security.crypto.password.PasswordEncoder
 import org.springframework.stereotype.Service
@Service
class UserService {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userRepository: UserRepository


    fun create(user: User) {
        var login= userRepository.findByLogin(user.getLogin());
        var auxLogin= userRepository.findByEmail(user.getEmail());
        if(auxLogin==null){
            val password = user.getPassword()
            user.setPassword(passwordEncoder.encode(password))
            userRepository.save(user)
        }else{
            ErrorDto(201,"Esse login já existe, por favor, faça outra tentantiva!" )
            throw  Exception("Esse login já existe, por favor, faça outra tentantiva!" );
        }

        var auxEmail= userRepository.findByEmail(user.getEmail());
            if(auxEmail==null){
                val password = user.getPassword()
                user.setPassword(passwordEncoder.encode(password))
                userRepository.save(user)
            }else{
                ErrorDto(201,"Esse e-mail já existe, por favor, faça outra tentantiva!" )
                throw  Exception("Esse e-mail já existe, por favor, faça outra tentantiva!" );
            }

    }
}

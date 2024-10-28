import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
 import org.springframework.stereotype.Service

@Service
class CustomDelegatingPasswordEncoder {

    val idForEncode = "2313254efdAWER894EWRAWEuesdfkoaweirjh1878@@%$@SD234";
    val encoders: MutableMap<String, PasswordEncoder> = mutableMapOf()

    init {
        encoders[idForEncode] = BCryptPasswordEncoder()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return DelegatingPasswordEncoder(idForEncode, encoders)
    }
}

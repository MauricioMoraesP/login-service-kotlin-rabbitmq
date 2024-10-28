package com.login.demo

 import com.login.demo.model.User
 import com.login.demo.service.UserService
import org.junit.jupiter.api.Test
 import org.springframework.beans.factory.annotation.Autowired
 import org.springframework.boot.test.context.SpringBootTest
 import java.util.*

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private lateinit var db: UserService

	@Test
	fun contextLoads() {
		// Verifica se o contexto carrega sem problemas
	}

	/*@Test
	fun testCreateEntity() {
		val newUser = User("mmpc", "mmpc", "mmpc@mail.com", Date())
	println(newUser.toString());
	}*/
}


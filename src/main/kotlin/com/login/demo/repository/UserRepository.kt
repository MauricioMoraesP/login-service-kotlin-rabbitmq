package com.login.demo.repository

import com.login.demo.model.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByLogin(login: String): User?
    override fun findById(id: Long): Optional<User>

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    fun updateUsernameById(@Param("id") id: Long, @Param("username") username: String)

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    fun updateEmailById(@Param("id") id: Long, @Param("email") email: String)

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    fun updatePasswordById(@Param("id") id: Long, @Param("password") password: String)

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.login = :login WHERE u.id = :id")
    fun updateLoginById(@Param("id") id: Long, @Param("login") login: String)

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.changesAccount = :changesAccount WHERE u.id = :id")
    fun updateChangesUpdate(@Param("id") id: Long, @Param("changesAccount") changesAccount: Boolean)

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.codeVerification = :codeVerification WHERE u.id = :id")
    fun updateCodeVerifcation(@Param("id") id: Long, @Param("codeVerification") codeVerification: Boolean)



}

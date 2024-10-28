package com.login.demo.configurations

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.login.demo.model.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TokenService{

    @Value("\${api.security.token}")
    private lateinit var secret: String

    private fun  genExpirationDate(): Instant{
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"))
    }

    public fun validateToken( token:String):String{
        try {
            val algorithm = Algorithm.HMAC256(secret);
            var token:String= JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getSubject();
            return token;

        }
        catch (exception: JWTVerificationException){
           return "";
        }
    }

    fun   generateToken(user: User):String{
    try {
        val algorithm = Algorithm.HMAC256(secret);
        var token:String= JWT.create().withIssuer("auth-api").withSubject(user.getLogin()).withExpiresAt(genExpirationDate()).sign(algorithm);
        return token;

    }
    catch (exception: JWTVerificationException){
    throw RuntimeException("Error while generating token", exception);
    }
    }





}
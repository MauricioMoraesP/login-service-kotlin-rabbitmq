package com.login.demo.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
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

    fun generateToken(user: User): String {
        try {
            val algorithm = Algorithm.HMAC256(secret)
            return JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.getLogin())
                .withClaim("userId", user.getId().toString())
                .withClaim("changesAccount", user.getChangesAccount())
                .withExpiresAt(genExpirationDate())
                .sign(algorithm)
        } catch (exception: JWTVerificationException) {
            throw RuntimeException("Error while generating token", exception)
        }
    }

    fun verifyToken(token: String): String {
        try {
            val decodedJWT =validatorToken(token);
             val claim = decodedJWT.getClaims();
            val userId = claim.get("userId").toString();
             return userId;
        }catch (exception: JWTVerificationException){
            throw RuntimeException("Error while verifying token", exception)
        }

    }

    fun validatorToken(token:String): DecodedJWT {
        try {
            val algorithm = Algorithm.HMAC256(secret)
            val verifier = JWT.require(algorithm).withIssuer("auth-api").build()
            val decodedJWT = verifier.verify(token);
            return decodedJWT;
        }
        catch (exception: JWTVerificationException){
            throw RuntimeException("Error while validating token", exception)
        }
    }

     fun verifyTokenChangesAccount(token: String): String {
        try {
            val decodedJWT =validatorToken(token);
            val claim = decodedJWT.getClaims();
            val status = claim.get("changesAccount").toString();
            return status;
        }catch (exception: JWTVerificationException){
            throw RuntimeException("Error while verifying token", exception)
        }

    }


    fun generateTokenVerify(user: User): String {
        try {
            val algorithm = Algorithm.HMAC256(secret)
            return JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.getLogin())
                .withClaim("userId", user.getId().toString())
                .withClaim("code", user.getCodeNumber())
                .withExpiresAt(genExpirationDate())
                .sign(algorithm)
        } catch (exception: JWTVerificationException) {
            throw RuntimeException("Error while generating token", exception)
        }
    }







}
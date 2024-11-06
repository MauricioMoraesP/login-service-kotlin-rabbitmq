package com.login.demo.dto

import jakarta.validation.constraints.*



class CreateUserRequestDTO(
    @field:NotNull(message = "Login cannot be null!")
    @field:NotBlank(message = "Login cannot be blank!")
    @field:Size(min = 6, max = 25, message = "Your login must be between 6 and 25 characters!")
    val login: String,

    @field:NotNull(message = "Password cannot be null!")
    @field:NotBlank(message = "Password cannot be blank!")
    @field:Size(min = 6, max = 50, message = "Your password must be between 6 and 50 characters!")
    val password: String,

    @field:NotNull(message = "Email cannot be null!")
    @field:NotBlank(message = "Email must be provided!")
    @field:Email(message = "The email format is not valid!")
    val email: String,

    @field:NotNull(message = "Email cannot be null!")
    @field:NotBlank(message = "Email must be provided!")
    @field:Size(min = 3, max = 50, message = "Your userName must be between 6 and 50 characters!")
    val userName: String
){
    override fun toString(): String {
        return "CreateUserRequest(login='$login', password='$password', email='$email', name='$userName')"
    }

}

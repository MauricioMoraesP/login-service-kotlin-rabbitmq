package com.login.demo.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


data class CreateUserRequest(
    @NotNull(message = "Name cannot be null!")
    @Min(message = "Your login must be longer than 6 characters!", value = 6)
    @Max(message = "Your login must be less than 25 characters!", value = 25)
    @NotBlank(message = "Name cannot be blank!")
    val login: String,

    @NotNull(message = "Name cannot be null!")
    @NotBlank(message = "Name cannot be blank!")
    @Min(message = "Your password must be longer than 6 characters!", value = 6)
    @Max(message = "Your password must be less than 25 characters!", value = 50)
    val password: String,

    @NotNull(message = "O e-mail não pode ser nulo!")
    @NotBlank(message = "O e-mail deve ser preenchido!")
    @Email(message = "Your email cannot be valid!")
    val email: String
)

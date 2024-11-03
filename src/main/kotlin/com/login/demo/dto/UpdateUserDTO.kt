package com.login.demo.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UpdateUserDTO(
    @field:Size(min = 6, max = 25, message = "Your username must be between 6 and 25 characters!")
    val userName: String? = null,

    @field:Size(min = 6, max = 25, message = "Your password must be between 6 and 25 characters!")
    val password: String? = null,

    @field:Size(min = 6, max = 25, message = "Your login must be between 6 and 25 characters!")
    val login: String? = null,

    @field:Email(message = "The email format is not valid!")
    val email: String? = null
)

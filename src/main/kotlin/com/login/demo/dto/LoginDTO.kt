package com.login.demo.dto

import jakarta.validation.constraints.NotNull

data class LoginDTO(
    @NotNull
    val login:String,
    @NotNull
    val password:String){

}

package com.login.demo.configurations

import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun methodArgumentNotValidException(ex: MethodArgumentNotValidException): Map<String, List<String>> {
        println(ex);
         val errors = ex.bindingResult.allErrors.mapNotNull { error ->
            when (error) {
                is FieldError -> error.defaultMessage
                else -> null
            }
        }

         return mapOf("errors" to errors)
    }

}



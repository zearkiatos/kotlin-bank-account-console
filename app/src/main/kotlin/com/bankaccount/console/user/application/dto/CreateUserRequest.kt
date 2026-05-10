package com.bankaccount.console.user.application.dto

data class CreateUserRequest(
    val id: String? = "",
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
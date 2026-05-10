package com.bankaccount.console.user.application.dto

data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val passwordHash: String
)
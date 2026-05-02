package com.bankaccount.console.user.domain.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val passwordHash: String
)
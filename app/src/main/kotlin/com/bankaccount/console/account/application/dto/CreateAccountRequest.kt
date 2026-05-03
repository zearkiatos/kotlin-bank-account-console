package com.bankaccount.console.account.application.dto

data class CreateAccountRequest(
    val id: String? = "",
    val userId: String,
    val accountNumber: String? = "",
    val accountType: String,
    val balance: Double? = 0.0,
    val transactions: List<String> = listOf()
)
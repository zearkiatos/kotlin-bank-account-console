package com.bankaccount.console.account.application.dto

data class AccountResponse(
    val id: String,
    val userId: String,
    val accountNumber: String,
    val accountType: String
)
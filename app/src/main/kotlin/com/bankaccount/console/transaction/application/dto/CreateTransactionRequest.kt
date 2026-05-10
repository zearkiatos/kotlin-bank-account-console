package com.bankaccount.console.transaction.application.dto

data class CreateTransactionRequest(
    val id: String? = "",
    val accountId: String,
    val balanceBefore: Double,
    val amount: Double,
    val type: String,
    val timestamp: Long
)
package com.bankaccount.console.transaction.domain.model

import com.bankaccount.console.shared.transaction.domain.model.TransactionType

data class Transaction(
    val id: String,
    val accountId: String,
    val amount: Double,
    val type: TransactionType,
    val timestamp: Long
)
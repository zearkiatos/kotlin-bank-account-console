package com.bankaccount.console.transaction.application.mapper

import com.bankaccount.console.shared.transaction.domain.model.TransactionType
import com.bankaccount.console.transaction.domain.model.Transaction
import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.transaction.application.dto.TransactionResponse

fun CreateTransactionRequest.toDomain(): Transaction {
    return Transaction(
        id = this.id ?: "",
        accountId = this.accountId,
        balanceBefore = this.balanceBefore,
        amount = this.amount,
        type = TransactionType.valueOf(this.type.uppercase()),
        timestamp = this.timestamp
    )
}

fun Transaction.toResponse(): TransactionResponse {
    return TransactionResponse(
        id = this.id,
        accountId = this.accountId,
        balanceBefore = this.balanceBefore,
        amount = this.amount,
        type = this.type.name,
        timestamp = this.timestamp.toString()
    )
}
package com.bankaccount.console.transaction.application

import com.bankaccount.console.transaction.application.ports.input.CreateTransactionInputPort
import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.transaction.application.dto.TransactionResponse
import com.bankaccount.console.transaction.domain.repository.TransactionRepository

class CreateTransactionUseCases(
    val transactionRepository: TransactionRepository
) : CreateTransactionInputPort {
    override fun create(request: CreateTransactionRequest): TransactionResponse {
        
        return TransactionResponse(
            id = "",
            accountId = request.accountId,
            balanceBefore = request.balanceBefore,
            amount = request.amount,
            type = request.type,
            timestamp = ""
        )
    }
}
package com.bankaccount.console.transaction.application

import java.util.UUID
import java.sql.Date
import java.time.Instant
import com.bankaccount.console.transaction.application.ports.input.CreateTransactionInputPort
import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.transaction.application.dto.TransactionResponse
import com.bankaccount.console.transaction.domain.repository.TransactionRepository
import com.bankaccount.console.transaction.application.mapper.toDomain
import com.bankaccount.console.transaction.application.mapper.toResponse

class CreateTransactionUseCases(
    val transactionRepository: TransactionRepository
) : CreateTransactionInputPort {
    override fun create(request: CreateTransactionRequest): TransactionResponse {
        require(request.amount < 0) { "Amount must be less than zero" }
        require(request.accountId.isNotBlank()) { "Account ID must not be blank" }
        require(request.type.isNotBlank()) { "Type must not be blank" }

         val transactionId = UUID.randomUUID().toString()

         val transaction = request.copy(
            id = transactionId,
            amount = request.amount,
            balanceBefore = request.balanceBefore,
            type = request.type.trim().uppercase(),
            timestamp = Instant.now().toEpochMilli()
         ).toDomain()

        transactionRepository.create(transaction)
        return transaction.toResponse()
    }
}
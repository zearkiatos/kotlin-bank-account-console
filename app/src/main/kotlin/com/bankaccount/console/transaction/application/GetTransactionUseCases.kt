package com.bankaccount.console.transaction.application

import com.bankaccount.console.transaction.application.ports.input.TransactionInputPort
import com.bankaccount.console.transaction.application.dto.TransactionResponse
import com.bankaccount.console.transaction.domain.repository.TransactionRepository
import com.bankaccount.console.transaction.application.mapper.toResponse

class GetTransactionUseCases(
    private val transactionRepository: TransactionRepository
) : TransactionInputPort {

     override fun getByAccountId(accountId: String): List<TransactionResponse>? {
        require(accountId.isNotBlank()) { "Account ID must not be blank" }

        val transactions = transactionRepository.getByAccountId(accountId) ?: return null

        return transactions.map { it.toResponse() }
    }
}
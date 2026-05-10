package com.bankaccount.console.transaction.application

import com.bankaccount.console.account.domain.repository.AccountRepository
import com.bankaccount.console.account.domain.model.CheckingAccount
import com.bankaccount.console.account.domain.model.DebitAccount
import com.bankaccount.console.account.domain.model.CreditAccount
import com.bankaccount.console.shared.transaction.domain.model.TransactionType
import com.bankaccount.console.transaction.application.dto.CreateTransactionRequest
import com.bankaccount.console.transaction.application.dto.TransactionResponse
import com.bankaccount.console.transaction.application.mapper.toDomain
import com.bankaccount.console.transaction.application.mapper.toResponse
import com.bankaccount.console.transaction.application.ports.input.CreateTransactionInputPort
import com.bankaccount.console.transaction.domain.repository.TransactionRepository
import java.time.Instant
import java.util.UUID

class CreateTransactionUseCases(
        val transactionRepository: TransactionRepository,
        val accountRepository: AccountRepository
) : CreateTransactionInputPort {
    override fun create(request: CreateTransactionRequest): TransactionResponse {
        require(request.amount > 0) { "Amount must be greater than zero" }
        require(request.accountId.isNotBlank()) { "Account ID must not be blank" }
        require(request.type.isNotBlank()) { "Type must not be blank" }

        val transactionId = UUID.randomUUID().toString()

        val transaction =
                request.copy(
                                id = transactionId,
                                amount = request.amount,
                                balanceBefore = request.balanceBefore,
                                type = request.type.trim().uppercase(),
                                timestamp = Instant.now().toEpochMilli()
                        )
                        .toDomain()

        transactionRepository.create(transaction)
        val account = accountRepository.get(request.accountId)
        var newBalance = 0.0
        if (account != null) {
            if (TransactionType.DEPOSIT == transaction.type) {
                newBalance = account.balance + request.amount
            } else if (TransactionType.WITHDRAWAL == transaction.type && account.balance >= request.amount
            ) {
                newBalance = account.balance - request.amount
            }
            val transactions = account.transactions
            transactions.add(transaction.id)
            when (account) {
                is CheckingAccount -> {
                    val updated = account.copy(balance = newBalance, transactions = transactions)
                    accountRepository.update(updated)
                }
                is DebitAccount -> {
                    val updated = account.copy(balance = newBalance, transactions = transactions)
                    accountRepository.update(updated)
                }
                is CreditAccount -> {
                    val updated = account.copy(balance = newBalance, transactions = transactions)
                    accountRepository.update(updated)
                }
            }
        }
        return transaction.toResponse()
    }
}

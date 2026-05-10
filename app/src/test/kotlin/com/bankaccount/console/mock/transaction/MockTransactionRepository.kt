package com.bankaccount.console.mock.transaction

import com.bankaccount.console.transaction.domain.repository.TransactionRepository
import com.bankaccount.console.transaction.domain.model.Transaction

class MockTransactionRepository : TransactionRepository {
    private val transactions = mutableListOf<Transaction>()

    override fun create(transaction: Transaction) {
        transactions.add(transaction)
    }

    override fun get(): List<Transaction> {
        return transactions.toList()
    }

    override fun get(id: String): Transaction? {
        return transactions.find { it.id == id }
    }

    override fun getByAccountId(accountId: String): List<Transaction> {
        return transactions.filter { it.accountId == accountId }
    }
}
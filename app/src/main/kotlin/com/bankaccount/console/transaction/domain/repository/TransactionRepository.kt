package com.bankaccount.console.transaction.domain.repository

import com.bankaccount.console.transaction.domain.model.Transaction

interface TransactionRepository {
    fun create(transaction: Transaction)
    fun get(): List<Transaction>
    fun get(id: String): Transaction?
    fun getByAccountId(accountId: String): List<Transaction>
}
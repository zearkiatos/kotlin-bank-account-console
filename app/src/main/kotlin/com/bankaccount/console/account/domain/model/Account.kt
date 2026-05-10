package com.bankaccount.console.account.domain.model

import com.bankaccount.console.shared.account.domain.model.AccountType

sealed class Account {
    abstract val id: String
    abstract val userId: String
    abstract val accountNumber: String
    abstract val balance: Double
    abstract val transactions: MutableList<String>
    abstract fun withdraw(amount: Int): Int
    abstract fun deposit(amount:Int): Int
}
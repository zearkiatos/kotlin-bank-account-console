package com.bankaccount.console.account.domain.model

import com.bankaccount.console.account.domain.model.Account
import com.bankaccount.console.account.domain.handleError.BalanceEqualZeroException
import com.bankaccount.console.account.domain.handleError.NotEnoughBalanceException

data class DebitAccount(
    override val id: String,
    override val userId: String,
    override val accountNumber: String,
    override val balance: Double,
    override val transactions: MutableList<String>
): Account() {
    override fun withdraw(amount: Int): Int {
        if (balance == 0.0) {
            throw BalanceEqualZeroException(balance)
        }
        else if (amount > balance) {
            throw NotEnoughBalanceException(balance)
        }
        return (balance - amount).toInt()
    }

    override fun deposit(amount: Int): Int {
        return (balance + amount).toInt()
    }
}
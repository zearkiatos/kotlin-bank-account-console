package com.bankaccount.console.account.domain.model

import com.bankaccount.console.account.domain.model.Account
import com.bankaccount.console.account.domain.handleError.UnecessaryPayOffException
import com.bankaccount.console.account.domain.handleError.PayOffAmountGraterThanCreditBalanceException
import com.bankaccount.console.account.domain.handleError.PendingPayOffException

data class CreditAccount(
    override val id: String,
    override val userId: String,
    override val accountNumber: String,
    override val balance: Double,
    override val transactions: MutableList<String>
): Account() {
    override fun withdraw(amount: Int): Int {
        return if (amount > balance) {
            0
        } else {
            (balance - amount).toInt()
        }
    }

    override fun deposit(amount: Int): Int {
        if (balance == 0.0) {
            throw UnecessaryPayOffException(balance)
        }
        else if (balance + amount > 0.0) {
            throw PayOffAmountGraterThanCreditBalanceException(balance)
        }
        else if (amount.toDouble() == -balance) {
            throw PendingPayOffException(amount)
        }
        return (balance + amount).toInt()
    }
}
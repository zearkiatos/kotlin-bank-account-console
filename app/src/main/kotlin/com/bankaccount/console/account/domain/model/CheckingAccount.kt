package com.bankaccount.console.account.domain.model

import com.bankaccount.console.account.domain.model.Account

data class CheckingAccount(
    override val id: String,
    override val userId: String,
    override val accountNumber: String,
    override val balance: Double,
    override val transactions: MutableList<String>
): Account() 
{
    override fun withdraw(amount: Int): Int {
        return if (amount > balance) {
            0
        } else {
            (balance - amount).toInt()
        }
    }

    override fun deposit(amount: Int): Int {
        return (balance + amount).toInt()
    }
}
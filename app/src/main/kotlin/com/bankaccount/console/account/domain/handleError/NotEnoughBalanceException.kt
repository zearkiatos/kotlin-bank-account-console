package com.bankaccount.console.account.domain.handleError

class NotEnoughBalanceException(val balance: Double): Exception() {
    override val message: String
        get() = "There is not enough money in this account! The account balance is $balance"
}
package com.bankaccount.console.account.domain.handleError

class BalanceEqualZeroException(val balance: Double): Exception() {
    override val message: String
        get() = "You cannot withdraw, there is no money in this account! Balance = $balance"
}
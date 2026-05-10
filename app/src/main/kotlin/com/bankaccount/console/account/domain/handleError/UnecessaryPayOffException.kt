package com.bankaccount.console.account.domain.handleError

class UnecessaryPayOffException(val balance: Double): Exception() {
    override val message: String
        get() = "You don't need to deposit anything in order to pay off the account since it has already been paid off. Balance = $balance"
}
package com.bankaccount.console.account.domain.handleError

class PendingPayOffException(val amount: Int): Exception(){
    override val message: String
        get() = "You have a paid off this account! $amount"
}
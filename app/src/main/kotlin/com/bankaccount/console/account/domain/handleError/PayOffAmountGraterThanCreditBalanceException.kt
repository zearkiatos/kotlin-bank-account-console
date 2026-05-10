package com.bankaccount.console.account.domain.handleError

class PayOffAmountGraterThanCreditBalanceException(val balance: Double): Exception() {
    override val message: String
        get() = "Deposit failed, you tried to pay off an amount greater than the credit balance. The checking balance is $balance."
}
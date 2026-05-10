package com.bankaccount.console.shared.account.domain.model

enum class AccountType(val code: String) {
    DEBIT("01"),
    CREDIT("02"),
    CHECKING("03")
}
package com.bankaccount.console.shared.account.domain.model

enum class AccountType(val code: String) {
    CHECKING("01"),
    DEBIT("02"),
    CREDIT("03")
}
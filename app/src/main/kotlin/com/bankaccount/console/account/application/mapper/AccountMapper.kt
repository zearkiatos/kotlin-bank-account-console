package com.bankaccount.console.account.application.mapper

import com.bankaccount.console.account.application.dto.AccountResponse
import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.domain.model.Account
import com.bankaccount.console.shared.account.domain.model.AccountType
import com.bankaccount.console.account.domain.model.CreditAccount
import com.bankaccount.console.account.domain.model.CheckingAccount
import com.bankaccount.console.account.domain.model.DebitAccount

fun CreateAccountRequest.toDomain(): Account {

    return when(accountType.trim().uppercase()) {
        AccountType.CHECKING.name -> CheckingAccount(
            id = id?.trim() ?: "",
            userId = userId.trim(),
            accountNumber = accountNumber?.trim() ?: "",
            balance = balance ?: 0.0,
            transactions = transactions
        )
        AccountType.DEBIT.name -> DebitAccount(
            id = id?.trim() ?: "",
            userId = userId.trim(),
            accountNumber = accountNumber?.trim() ?: "",
            balance = balance ?: 0.0,
            transactions = transactions
        )
        AccountType.CREDIT.name -> CreditAccount(
            id = id?.trim() ?: "",
            userId = userId.trim(),
            accountNumber = accountNumber?.trim() ?: "",
            balance = balance ?: 0.0,
            transactions = transactions
        )
        else -> throw IllegalArgumentException("Invalid account type: ${accountType.trim()}")
    }
}

fun Account.toResponse(): AccountResponse {
    val accountType = when(this) {
        is CheckingAccount -> AccountType.CHECKING.name
        is DebitAccount -> AccountType.DEBIT.name
        is CreditAccount -> AccountType.CREDIT.name
    }
    return AccountResponse(
        id = id.trim(),
        userId = userId.trim(),
        accountNumber = accountNumber.trim(),
        accountType = accountType.trim(),
        balance = balance,
        transactions = transactions
    )
}

fun AccountResponse.toDomain(): Account {
    return when(accountType.trim().uppercase()) {
        AccountType.CHECKING.name -> CheckingAccount(
            id = id.trim(),
            userId = userId.trim(),
            accountNumber = accountNumber.trim(),
            balance = balance,
            transactions = transactions
        )
        AccountType.DEBIT.name -> DebitAccount(
            id = id.trim(),
            userId = userId.trim(),
            accountNumber = accountNumber.trim(),
            balance = balance,
            transactions = transactions
        )
        AccountType.CREDIT.name -> CreditAccount(
            id = id.trim(),
            userId = userId.trim(),
            accountNumber = accountNumber.trim(),
            balance = balance,
            transactions = transactions
        )
        else -> throw IllegalArgumentException("Invalid account type: ${accountType.trim()}")
    }
}
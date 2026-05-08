package com.bankaccount.console.account.application.ports.input

import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.application.dto.AccountResponse

interface AccountInputPort {
    fun getByUserId(userId: String): AccountResponse
    fun updateBalance(accountId: String, newBalance: Double)
}
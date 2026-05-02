package com.bankaccount.console.account.application.ports.input

import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.application.dto.AccountResponse

interface CreateAccountInputPort {
    fun create(request: CreateAccountRequest): AccountResponse
}
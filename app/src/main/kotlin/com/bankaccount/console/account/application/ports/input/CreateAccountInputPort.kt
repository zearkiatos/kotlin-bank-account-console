package com.bankaccount.console.account.application.ports.input

import com.bankaccount.console.account.application.dto.CreateAccountRequest

interface CreateAccountInputPort {
    fun create(request: CreateAccountRequest):String
}
package com.bankaccount.console.user.application.ports.input

import com.bankaccount.console.user.application.dto.CreateUserRequest

interface CreateUserInputPort {
    fun create(request: CreateUserRequest):String
}
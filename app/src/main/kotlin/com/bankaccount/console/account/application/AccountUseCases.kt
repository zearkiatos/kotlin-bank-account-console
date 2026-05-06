package com.bankaccount.console.account.application

import com.bankaccount.console.account.application.ports.input.AccountInputPort
import com.bankaccount.console.account.domain.repository.AccountRepository
import com.bankaccount.console.account.application.dto.AccountResponse
import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.application.mapper.toResponse
import com.bankaccount.console.account.application.mapper.toDomain

class AccountUseCases(
    private val accountRepository: AccountRepository,
) : AccountInputPort {

    override fun getByUserId(userId: String): AccountResponse {
        require (userId.isNotBlank()) { "User ID must not be blank" }
        
        val account = accountRepository.getByUserId(userId)
        
        return account!!.toResponse()
    }
}
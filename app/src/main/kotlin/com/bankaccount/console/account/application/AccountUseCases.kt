package com.bankaccount.console.account.application

import com.bankaccount.console.account.application.ports.input.AccountInputPort
import com.bankaccount.console.account.domain.repository.AccountRepository
import com.bankaccount.console.account.application.dto.AccountResponse
import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.application.mapper.toResponse
import com.bankaccount.console.account.application.mapper.toDomain
import com.bankaccount.console.account.domain.model.CheckingAccount
import com.bankaccount.console.account.domain.model.DebitAccount
import com.bankaccount.console.account.domain.model.CreditAccount
import com.bankaccount.console.account.domain.model.Account

class AccountUseCases(
    private val accountRepository: AccountRepository,
) : AccountInputPort {

    override fun getByUserId(userId: String): AccountResponse {
        require (userId.isNotBlank()) { "User ID must not be blank" }
        
        val account = accountRepository.getByUserId(userId)
        
        return account!!.toResponse()
    }

    override fun updateBalance(accountId: String, newBalance: Double) {
        require (accountId.isNotBlank()) { "Account ID must not be blank" }
        require (newBalance >= 0) { "New balance must be greater than or equal to zero" }

        val account = accountRepository.get(accountId) ?: throw IllegalArgumentException("Account not found with ID: $accountId")
        var accountUpdated: Account
        if (account is CheckingAccount) {
            accountUpdated = account.copy(balance = newBalance)
            accountRepository.update(accountUpdated)
        } else if (account is DebitAccount) {
            accountUpdated = account.copy(balance = newBalance)
            accountRepository.update(accountUpdated)
        } else if (account is CreditAccount) {
            accountUpdated = account.copy(balance = newBalance)
            accountRepository.update(accountUpdated)
        }
    

    }
}
package com.bankaccount.console.account.application

import java.util.UUID
import com.bankaccount.console.account.application.dto.CreateAccountRequest
import com.bankaccount.console.account.application.dto.AccountResponse
import com.bankaccount.console.account.application.ports.input.CreateAccountInputPort
import com.bankaccount.console.account.domain.repository.AccountRepository
import com.bankaccount.console.account.domain.utils.BankAccountNumberGenerator
import com.bankaccount.console.account.application.mapper.toDomain
import com.bankaccount.console.account.application.mapper.toResponse
import com.bankaccount.console.shared.account.domain.model.AccountType

class CreateAccountUseCases(
    private val accountRepository: AccountRepository
) : CreateAccountInputPort {
    override fun create(request: CreateAccountRequest): AccountResponse {
        require(request.accountType.isNotBlank()) { "Account type must not be blank" }
        require(request.userId.isNotBlank()) { "User ID must not be blank" }

        val accountId = UUID.randomUUID().toString()
        val balance = request.balance
        val account = request.copy(
            id = accountId,
            accountNumber = BankAccountNumberGenerator.generate(
                when(request.accountType.trim().uppercase()) {
                    "CHECKING" -> AccountType.CHECKING
                    "DEBIT" -> AccountType.DEBIT
                    "CREDIT" -> AccountType.CREDIT
                    else -> throw IllegalArgumentException("Invalid account type: ${request.accountType.trim()}")
                }
            ),
            balance = balance
        ).toDomain()

        accountRepository.create(account)

        val accountCreated = accountRepository.get(accountId)
        
        return accountCreated!!.toResponse()
    }
}
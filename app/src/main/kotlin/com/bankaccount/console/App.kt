package com.bankaccount.console
import com.bankaccount.console.account.infrastructure.console.BankAccountConsoleAdapter
import com.bankaccount.console.account.infrastructure.repository.InMemoryAccountRepository
import com.bankaccount.console.user.infrastructure.repository.InMemoryUserRepository
import com.bankaccount.console.account.application.CreateAccountUseCases
import com.bankaccount.console.user.application.CreateUserUseCases

fun main() {
    val accountUseCases = CreateAccountUseCases(
        accountRepository = InMemoryAccountRepository()
    )
    val userUseCases = CreateUserUseCases(
        userRepository = InMemoryUserRepository()
    )
    val app = BankAccountConsoleAdapter(
        createAccountPort = accountUseCases,
        createUserPort = userUseCases
    )

    app.run()
}



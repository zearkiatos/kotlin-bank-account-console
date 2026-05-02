package com.bankaccount.console.account.infrastructure.repository

import com.bankaccount.console.account.domain.model.Account
import com.bankaccount.console.shared.account.domain.model.AccountType
import com.bankaccount.console.account.domain.repository.AccountRepository

class InMemoryAccountRepository : AccountRepository {
    private val accounts = mutableListOf<Account>()

    override fun create(account: Account) {
        accounts.add(account)
    }

    override fun get(): List<Account> {
        return accounts
    }

    override fun get(id: String): Account? {
        return accounts.find { it.id == id }
    }

    override fun update(account: Account) {
        val index = accounts.indexOfFirst { it.id == account.id }
        if (index != -1) {
            accounts[index] = account
        }
    }

    override fun delete(id: String) {
        accounts.removeIf { it.id == id }
    }
}
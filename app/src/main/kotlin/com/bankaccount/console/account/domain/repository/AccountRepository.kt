package com.bankaccount.console.account.domain.repository

import com.bankaccount.console.account.domain.model.Account

interface AccountRepository {
    fun create(account: Account)
    fun get(): List<Account>
    fun get(id: String): Account?
    fun update(account: Account)
    fun delete(id: String)
    fun getByUserId(userId: String): List<Account>
}
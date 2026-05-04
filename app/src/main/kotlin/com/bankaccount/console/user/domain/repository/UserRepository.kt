package com.bankaccount.console.user.domain.repository

import com.bankaccount.console.user.domain.model.User

interface UserRepository {
    fun create(user: User)
    fun get(): List<User>
    fun get(id: String): User?
    fun update(user: User)
    fun delete(id: String)
    fun getUserByEmail(email: String): User?
}
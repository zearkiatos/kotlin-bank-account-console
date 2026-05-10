package com.bankaccount.console.mock.user

import com.bankaccount.console.user.domain.model.User
import com.bankaccount.console.user.domain.repository.UserRepository

class MockUserRepository : UserRepository {
    private val users = mutableListOf<User>()

    override fun create(user: User) {
        users.add(user)
    }

    override fun get(): List<User> {
        return users
    }

    override fun get(id: String): User? {
        return users.find { it.id == id }
    }

    override fun update(user: User) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = user
        }
    }

    override fun delete(id: String) {
        users.removeIf { it.id == id }
    }

    override fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }
}
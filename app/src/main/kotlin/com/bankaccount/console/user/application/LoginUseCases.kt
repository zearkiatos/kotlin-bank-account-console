package com.bankaccount.console.user.application

import com.bankaccount.console.user.application.ports.input.LoginInputPort
import com.bankaccount.console.user.domain.repository.UserRepository
import com.bankaccount.console.user.domain.utils.PasswordHasher

class LoginUseCases(
    val userRepository: UserRepository
): LoginInputPort {
    override fun login(email: String, password: String): String {
        require(email.isNotBlank()) { "Email must not be blank" }
        require(password.isNotBlank()) { "Password must not be blank" }

        val user = userRepository.getUserByEmail(email)

        if (user != null && PasswordHasher.verify(password, user.passwordHash)) {
            return user.id
        } else {
            throw IllegalArgumentException("Invalid email or password")
        }
    }
}
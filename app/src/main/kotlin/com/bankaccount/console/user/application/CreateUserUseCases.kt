package com.bankaccount.console.user.application

import java.util.UUID
import com.bankaccount.console.user.application.ports.input.CreateUserInputPort
import com.bankaccount.console.user.domain.repository.UserRepository
import com.bankaccount.console.user.application.dto.CreateUserRequest
import com.bankaccount.console.user.application.mapper.toDomain
import com.bankaccount.console.user.domain.utils.PasswordHasher

class CreateUserUseCases(
    private val userRepository: UserRepository
) : CreateUserInputPort {
    override fun create(request: CreateUserRequest): String {
        require(request.firstName.isNotBlank()) { "First name must not be blank" }
        require(request.lastName.isNotBlank()) { "Last name must not be blank" }
        require(request.email.isNotBlank()) { "Email must not be blank" }
        require(request.password.isNotBlank()) { "Password must not be blank" }

        val userId = UUID.randomUUID().toString()
        val user = request.copy(id = userId, password = PasswordHasher.hash(request.password)).toDomain()

        userRepository.create(user)
        
        return userId
    }

}
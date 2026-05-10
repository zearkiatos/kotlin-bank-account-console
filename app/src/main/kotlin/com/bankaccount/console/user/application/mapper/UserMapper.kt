package com.bankaccount.console.user.application.mapper

import com.bankaccount.console.user.domain.model.User
import com.bankaccount.console.user.application.dto.CreateUserRequest
import com.bankaccount.console.user.application.dto.UserResponse

fun CreateUserRequest.toDomain(): User {
    return User(
        id = this.id ?: "",
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        passwordHash = this.password
    )
}

fun User.toResponse(): UserResponse {
    return UserResponse(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        passwordHash = this.passwordHash
    )
}
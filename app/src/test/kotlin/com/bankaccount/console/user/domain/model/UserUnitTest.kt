package com.bankaccount.console.user.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class UserUnitTest {
    @Test
    fun `Given a User, when created, then the properties are correctly assigned`() {
        val userId = UUID.randomUUID().toString()
        val firstName = "John"
        val lastName = "Doe"
        val email = "john.doe@example.com"
        val passwordHash = "hashed_password"

        val user = User(
            id = userId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            passwordHash = passwordHash
        )
        
        assertEquals(userId, user.id)
        assertEquals(firstName, user.firstName)
        assertEquals(lastName, user.lastName)
        assertEquals(email, user.email)
        assertEquals(passwordHash, user.passwordHash)
    }
}
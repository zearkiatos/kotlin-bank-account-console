package com.bankaccount.console.user.application.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.UUID

class UserResponseUnitTest {
    @Test
    fun `Given a UserResponse, when all fields are provided, then it should be created successfully`() {
        val id = UUID.randomUUID().toString()
        
        val userResponse = UserResponse(
            id = id,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            passwordHash = "hashedPassword"
        )
        
        assertEquals(id, userResponse.id)
        assertEquals("John", userResponse.firstName)
        assertEquals("Doe", userResponse.lastName)
        assertEquals("john.doe@example.com", userResponse.email)
        assertEquals("hashedPassword", userResponse.passwordHash)
    }
}

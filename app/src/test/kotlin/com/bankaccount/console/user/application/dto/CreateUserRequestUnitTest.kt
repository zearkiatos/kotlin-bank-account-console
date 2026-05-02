package com.bankaccount.console.user.application.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.user.application.dto.CreateUserRequest

class CreateUserRequestUnitTest {
    @Test
    fun `Given a CreateUserRequest, when all fields are provided, then it should be created successfully`() {
        val id = UUID.randomUUID().toString()
        val createUserRequest = CreateUserRequest(
            id = id,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "hashedPassword"
        )

        assertEquals(id, createUserRequest.id)
        assertEquals("John", createUserRequest.firstName)
        assertEquals("Doe", createUserRequest.lastName)
        assertEquals("john.doe@example.com", createUserRequest.email)
        assertEquals("hashedPassword", createUserRequest.password)
    }
}
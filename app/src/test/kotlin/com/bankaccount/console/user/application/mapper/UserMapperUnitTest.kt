package com.bankaccount.console.user.application.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.user.application.dto.CreateUserRequest
import com.bankaccount.console.user.application.dto.UserResponse
import com.bankaccount.console.user.domain.model.User
import com.bankaccount.console.user.application.mapper.toDomain
import com.bankaccount.console.user.application.mapper.toResponse

class UserMapperUnitTest {
    @Test
    fun `Given a CreateUserRequest, when toDomain is called, then it should be mapped to a User correctly`() {
        val id = UUID.randomUUID().toString()
        val createUserRequest = CreateUserRequest(
            id = id,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "hashedPassword"
        )

        val user = createUserRequest.toDomain()

        assertEquals(id, user.id)
        assertEquals("John", user.firstName)
        assertEquals("Doe", user.lastName)
        assertEquals("john.doe@example.com", user.email)
        assertEquals("hashedPassword", user.passwordHash)
    }

    @Test
    fun `Given a User, when toResponse is called, then it should be mapped to a UserResponse correctly`() {
        val id = UUID.randomUUID().toString()
        val user = User(
            id = id,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            passwordHash = "hashedPassword"
        )

        val userResponse = user.toResponse()

        assertEquals(id, userResponse.id)
        assertEquals("John", userResponse.firstName)
        assertEquals("Doe", userResponse.lastName)
        assertEquals("john.doe@example.com", userResponse.email)
        assertEquals("hashedPassword", userResponse.passwordHash)
    }

       @Test
    fun `Given a CreateUserRequest with null id, when toDomain is called, then id should be empty string`() {
        val createUserRequest = CreateUserRequest(
            id = null,
            firstName = "Jane",
            lastName = "Smith",
            email = "jane.smith@example.com",
            password = "hashedPassword123"
        )

        val user = createUserRequest.toDomain()

        assertEquals("", user.id)
        assertEquals("Jane", user.firstName)
        assertEquals("Smith", user.lastName)
        assertEquals("jane.smith@example.com", user.email)
        assertEquals("hashedPassword123", user.passwordHash)
    }
}
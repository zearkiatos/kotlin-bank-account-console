package com.bankaccount.console.user.application

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic 
import io.mockk.unmockkStatic
import org.junit.Assert.assertNotNull

import com.bankaccount.console.user.application.dto.CreateUserRequest
import com.bankaccount.console.user.application.ports.input.CreateUserInputPort
import com.bankaccount.console.user.application.CreateUserUseCases
import com.bankaccount.console.mock.user.MockUserRepository

class CreateUserUseCasesUnitTest {

    private lateinit var createUserUseCases: CreateUserUseCases

    @Before
    fun setUp() {
        val userRepository = MockUserRepository()
        createUserUseCases = CreateUserUseCases(userRepository)
    }

    @Test
    fun `Given a user When it is valid then create user successfully`() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID().toString() } returns "test-user-id"
        
        val createUserRequest = CreateUserRequest(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "hashedPassword"
        )
        val userId = createUserUseCases.create(createUserRequest)
        
        assertNotNull(userId)
        assertEquals("test-user-id", userId)
        unmockkStatic(UUID::class)
    }
}
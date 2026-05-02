package com.bankaccount.console.user.infrastructure.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.UUID
import com.bankaccount.console.user.domain.model.User

class InMemoryUserRepositoryUnitTest {
    private val repository = InMemoryUserRepository()

    @Test
    fun `Given a User, when create is called, then the user is stored in the repository`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            passwordHash = "hashed_password"
        )
        
        repository.create(user)
        val retrievedUser = repository.get(user.id)
        
        assertEquals(user, retrievedUser)
    }

    @Test
    fun `Given a User, when delete is called, then the user is removed from the repository`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "Jane",
            lastName = "Smith",
            email = "jane.smith@example.com",
            passwordHash = "hashed_password"
        )
        repository.create(user)
        
        repository.delete(user.id)
        val retrievedUser = repository.get(user.id)
        
        assertNull(retrievedUser)
    }

    @Test
    fun `Given a User, when authenticate is called with correct credentials, then it returns the User`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "Alice",
            lastName = "Johnson",
            email = "alice.johnson@example.com",
            passwordHash = "hashed_password"
        )
        repository.create(user)
        
        val authenticatedUser = repository.authenticate(user.email, user.passwordHash)
        
        assertEquals(user, authenticatedUser)
    }

    @Test
    fun `Given a User, when authenticate is called with incorrect credentials, then it returns null`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "Bob",
            lastName = "Brown",
            email = "bob.brown@example.com",
            passwordHash = "hashed_password"
        )
        repository.create(user)
        
        val authenticatedUser = repository.authenticate(user.email, "wrong_password")
        
        assertNull(authenticatedUser)
    }

    @Test
    fun `Given a User, when update is called, then the changes are reflected when retrieved`() {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = "Charlie",
            lastName = "Davis",
            email = "charlie.davis@example.com",
            passwordHash = "hashed_password"
        )
        repository.create(user)
        val updatedUser = user.copy(firstName = "Charles")
        
        repository.update(updatedUser)
        val retrievedUser = repository.get(user.id)
        
        assertEquals(updatedUser, retrievedUser)
    }
}

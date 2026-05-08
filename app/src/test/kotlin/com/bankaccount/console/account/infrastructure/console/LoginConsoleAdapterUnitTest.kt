package com.bankaccount.console.user.infrastructure.console

import com.bankaccount.console.user.application.ports.input.LoginInputPort
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.ByteArrayInputStream

class LoginConsoleAdapterUnitTest {
	@Test
	fun `Given valid input, when run is called, then userId is returned`() {
		val loginInputPort = mockk<LoginInputPort>()
		every { loginInputPort.login("user@example.com", "pass") } returns "user-1"
		val adapter = LoginConsoleAdapter(loginInputPort)

		withInput("user@example.com\npass\n") {
			val result = adapter.run()
			assertEquals("user-1", result)
		}
	}

	@Test
	fun `Given invalid email then valid input, when login is called, then it retries and returns userId`() {
		val loginInputPort = mockk<LoginInputPort>()
		every { loginInputPort.login("user@example.com", "pass") } returns "user-2"
		val adapter = LoginConsoleAdapter(loginInputPort)

		withInput("invalid\nuser@example.com\npass\n") {
			val result = adapter.login()
			assertEquals("user-2", result)
		}
	}

	@Test
	fun `Given blank input then valid input, when login is called, then it retries and returns userId`() {
		val loginInputPort = mockk<LoginInputPort>()
		every { loginInputPort.login("user@example.com", "pass") } returns "user-3"
		val adapter = LoginConsoleAdapter(loginInputPort)

		withInput("\nuser@example.com\npass\n") {
			val result = adapter.login()
			assertEquals("user-3", result)
		}
	}

	@Test
	fun `Given cancel input, when login is called, then empty string is returned`() {
		val loginInputPort = mockk<LoginInputPort>()
		val adapter = LoginConsoleAdapter(loginInputPort)

		withInput("cancelar\n") {
			val result = adapter.login()
			assertEquals("", result)
		}
	}

	@Test
	fun `Given loginInputPort returns null, when run is called, then null is returned`() {
		val loginInputPort = mockk<LoginInputPort>()
		every { loginInputPort.login("user@example.com", "pass") } returns null
		val adapter = LoginConsoleAdapter(loginInputPort)

		withInput("user@example.com\npass\n") {
			val result = adapter.run()
			assertNull(result)
		}
	}

	@Test
	fun `Given loginInputPort throws, when login is called, then null is returned`() {
		val loginInputPort = mockk<LoginInputPort>()
		every { loginInputPort.login("user@example.com", "pass") } throws IllegalArgumentException("bad login")
		val adapter = LoginConsoleAdapter(loginInputPort)

		withInput("user@example.com\npass\n") {
			val result = adapter.login()
			assertNull(result)
		}
	}

	private fun withInput(input: String, block: () -> Unit) {
		val originalIn = System.`in`
		System.setIn(ByteArrayInputStream(input.toByteArray()))
		try {
			block()
		} finally {
			System.setIn(originalIn)
		}
	}
}
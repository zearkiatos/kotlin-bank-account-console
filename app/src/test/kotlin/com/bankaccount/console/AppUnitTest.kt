package com.bankaccount.console

import com.github.ajalt.mordant.terminal.Terminal
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppUnitTest {
	private class ExitCalled(val code: Int) : RuntimeException()

	private fun exitWith(code: Int): Nothing {
		throw ExitCalled(code)
	}

	private fun menuProvider(vararg choices: String?): (Terminal, String?) -> String? {
		val items = choices.toList()
		var index = 0
		return { _, _ ->
			if (index >= items.size) null else items[index++]
		}
	}

	@Test
	fun resolvedMenuChoiceReturnsCreateAccount() {
		assertEquals("1", resolvedMenuChoice("1. Create Account"))
	}

	@Test
	fun resolvedMenuChoiceReturnsLogin() {
		assertEquals("2", resolvedMenuChoice("2. Login"))
	}

	@Test
	fun resolvedMenuChoiceReturnsExit() {
		assertEquals("3", resolvedMenuChoice("3. Exit"))
	}

	@Test
	fun resolvedMenuChoiceReturnsAccountBalance() {
		assertEquals("4", resolvedMenuChoice("1. Get Account Balance"))
	}

	@Test
	fun resolvedMenuChoiceReturnsTransactions() {
		assertEquals("5", resolvedMenuChoice("2. Transactions"))
	}

	@Test
	fun resolvedMenuChoiceReturnsLogout() {
		assertEquals("6", resolvedMenuChoice("3. Logout"))
	}

	@Test
	fun resolvedMenuChoiceReturnsExitForAlternateMenu() {
		assertEquals("3", resolvedMenuChoice("4. Exit"))
	}

	@Test
	fun resolvedMenuChoiceReturnsInvalidForUnknown() {
		assertEquals("invalid", resolvedMenuChoice("unknown"))
	}

	@Test
	fun headerPrintsBanner() {
		val terminal = mockk<Terminal>(relaxed = true)

		header(terminal)

		verify(exactly = 2) { terminal.println(any<Any>()) }
	}

	@Test
	fun mainMenuReturnsSelectionForLoggedOutUser() {
		val terminal = mockk<Terminal>(relaxed = true)
		val result = mainMenu(terminal, null) { _, _ -> "1. Create Account" }

		assertEquals("1", result)
	}

	@Test
	fun mainMenuReturnsSelectionForLoggedInUser() {
		val terminal = mockk<Terminal>(relaxed = true)
		val result = mainMenu(terminal, "user") { _, _ -> "2. Transactions" }

		assertEquals("5", result)
	}

	@Test
	fun mainMenuReturnsNullWhenSelectionIsNull() {
		val terminal = mockk<Terminal>(relaxed = true)
		val result = mainMenu(terminal, null) { _, _ -> null }

		assertNull(result)
	}

	@Test
	fun runAppExecutesCreateAccount() {
		val terminal = mockk<Terminal>(relaxed = true)
		var createAccountCalls = 0
		val dependencies = AppDependencies(
			createAccount = { createAccountCalls += 1 },
			login = { null },
			accountBalance = { },
			transactionHistory = { }
		)

		runApp(terminal, dependencies, menuProvider("1"), ::exitWith, maxIterations = 1)

		assertEquals(1, createAccountCalls)
	}

	@Test
	fun runAppExecutesLoginAndAccountBalance() {
		val terminal = mockk<Terminal>(relaxed = true)
		var loginCalls = 0
		var balanceUserId: String? = null
		val dependencies = AppDependencies(
			createAccount = { },
			login = {
				loginCalls += 1
				"user-1"
			},
			accountBalance = { id -> balanceUserId = id },
			transactionHistory = { }
		)

		runApp(terminal, dependencies, menuProvider("2", "4"), ::exitWith, maxIterations = 2)

		assertEquals(1, loginCalls)
		assertEquals("user-1", balanceUserId)
	}

	@Test
	fun runAppExecutesLoginAndTransactions() {
		val terminal = mockk<Terminal>(relaxed = true)
		var transactionUserId: String? = null
		val dependencies = AppDependencies(
			createAccount = { },
			login = { "user-2" },
			accountBalance = { },
			transactionHistory = { id -> transactionUserId = id }
		)

		runApp(terminal, dependencies, menuProvider("2", "5"), ::exitWith, maxIterations = 2)

		assertEquals("user-2", transactionUserId)
	}

	@Test
	fun runAppHandlesLogout() {
		val terminal = mockk<Terminal>(relaxed = true)
		var loginCalls = 0
		val dependencies = AppDependencies(
			createAccount = { },
			login = {
				loginCalls += 1
				"user-3"
			},
			accountBalance = { },
			transactionHistory = { }
		)

		runApp(terminal, dependencies, menuProvider("2", "6"), ::exitWith, maxIterations = 2)

		assertEquals(1, loginCalls)
	}

	@Test
	fun runAppHandlesInvalidOption() {
		val terminal = mockk<Terminal>(relaxed = true)
		val dependencies = AppDependencies(
			createAccount = { },
			login = { null },
			accountBalance = { },
			transactionHistory = { }
		)

		runApp(terminal, dependencies, menuProvider("invalid"), ::exitWith, maxIterations = 1)
	}

	@Test
	fun runAppExitsOnOption3() {
		val terminal = mockk<Terminal>(relaxed = true)
		val dependencies = AppDependencies(
			createAccount = { },
			login = { null },
			accountBalance = { },
			transactionHistory = { }
		)

		try {
			runApp(terminal, dependencies, menuProvider("3"), ::exitWith, maxIterations = 1)
			throw AssertionError("Expected exit")
		} catch (ex: ExitCalled) {
			assertEquals(0, ex.code)
		}
	}

	@Test
	fun mainStopsWhenMaxIterationsIsZero() {
		val previous = System.getProperty("app.maxIterations")
		try {
			System.setProperty("app.maxIterations", "0")
			main()
		} finally {
			if (previous == null) {
				System.clearProperty("app.maxIterations")
			} else {
				System.setProperty("app.maxIterations", previous)
			}
		}
	}
}
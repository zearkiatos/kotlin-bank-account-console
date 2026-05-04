package com.bankaccount.console.user.infrastructure.console

import com.bankaccount.console.user.application.ports.input.LoginInputPort
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.danger
import com.github.ajalt.mordant.terminal.prompt
import com.github.ajalt.mordant.terminal.success

class LoginConsoleAdapter(private val loginInputPort: LoginInputPort) {
    fun login(email: String, password: String): String? {
        return loginInputPort.login(email, password)
    }

    private val terminal = Terminal()

    fun run(): String? {
        val userId = login()
        if (userId != null) {
            terminal.success("Login successful! userId: $userId")
        } else {
            terminal.danger("Login failed. Please try again.")
        }

        return userId
    }

    fun login(): String? {
        val email = askRequiredText("Enter your email:") { it.contains("@") } ?: return ""
        val password = askRequiredText("Enter your password:") ?: return ""
        var userId: String? = null
        try {
            userId = loginInputPort.login(email, password)
        } catch (e: IllegalArgumentException) {
            terminal.danger(e.message ?: "An error occurred during login")
        }

        terminal.success("Login successful! userId: $userId")

        return userId
    }

    private fun askRequiredText(
            label: String,
            errorMessage: String = "Invalid input",
            validator: (String) -> Boolean = { true }
    ): String? {
        while (true) {
            val input = terminal.prompt(label)?.trim()

            if (input == null) {
                terminal.danger("Operation cancelled")
                return null
            }

            if (input.equals("cancelar", ignoreCase = true)) {
                terminal.danger("Operation cancelled")
                return null
            }

            if (input.isBlank()) {
                terminal.println(red("This field is required"))
                continue
            }

            if (!validator(input)) {
                terminal.println(red(errorMessage))
                continue
            }

            return input
        }
    }
}

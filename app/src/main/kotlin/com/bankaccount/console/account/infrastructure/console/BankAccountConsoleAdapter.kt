package com.bankaccount.console.account.infrastructure.console

import com.github.ajalt.mordant.input.interactiveSelectList
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.rendering.Whitespace
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.danger
import com.github.ajalt.mordant.terminal.prompt
import com.github.ajalt.mordant.terminal.success
import com.github.ajalt.mordant.widgets.Panel
import com.github.ajalt.mordant.widgets.Text
import com.bankaccount.console.account.application.ports.input.CreateAccountInputPort
import com.bankaccount.console.shared.account.domain.model.AccountType
import com.bankaccount.console.user.application.dto.CreateUserRequest
import com.bankaccount.console.user.application.ports.input.CreateUserInputPort
import com.bankaccount.console.account.application.dto.CreateAccountRequest

class BankAccountConsoleAdapter(
        private val createAccountPort: CreateAccountInputPort,
        private val createUserPort: CreateUserInputPort
) {
    private val terminal = Terminal()

    fun run() {
        var option: String? = null
        while (option != "4") {
            option = menu()
            if (option == "4") {
                terminal.danger("Returning to main menu...")
            }

            val accountType =
                    when (option) {
                        "1" -> AccountType.DEBIT
                        "2" -> AccountType.CREDIT
                        "3" -> AccountType.CHECKING
                        "4" -> {
                            terminal.danger("Redirect to the main menu..")
                            return
                        }
                        else -> {
                            terminal.danger("Invalid option, please try again.")
                            continue
                        }
                    }

            var balance: Double = 0.0
            if (option == "2") {
                balance = balanceForm()
            }
            

            val userId = this.createUser()

            this.createAccount(userId, accountType, balance)
        }
    }

    fun balanceForm(): Double {
        val balance = askRequiredText("How much credit would you like to add?") ?: return 0.0

        if (balance.toDoubleOrNull() == null) {
            terminal.danger("Invalid balance amount, please enter a valid number.")
            balanceForm()
        }
        return balance.toDouble()
    }

    private fun menu(): String? {
        val title =
                """
            What type of account would you like to create?:
        """.trimIndent()
        terminal.println((brightGreen + bold)(title))

        val userChoices = listOf("1. Debit Account", "2. Credit Account", "3. Checking Account", "Return to Main Menu")
        val selection =
                terminal.interactiveSelectList(
                        userChoices,
                        "Choose an option or exit (1, 2, 3 or 4)",
                )

        if (selection == null) {
            terminal.danger("Aborted account creation")
            return null
        }

        val index = userChoices.indexOf(selection)
        terminal.success("You chose option $index: $selection")
        return (index + 1).toString()
    }

    fun createUser(): String {
        val name = askRequiredText("Enter your name:") ?: return ""
        val lastName = askRequiredText("Enter your last name:") ?: return ""
        val email = askRequiredText("Enter your email:") { it.contains("@") } ?: return ""
        val password = askRequiredText("Enter your password:") ?: return ""

        val userRequest =
                CreateUserRequest(
                        firstName = name,
                        lastName = lastName,
                        email = email,
                        password = password
                )

        val userId = createUserPort.create(userRequest)

        terminal.success(
                "User created successfully! userId: $userId, Name: $name, Last Name: $lastName, Email: $email"
        )

        val text =
                """
        User: $userId
        Name: $name
        Last Name: $lastName
        Email: $email
            """.trimIndent()

        terminal.println(
                Panel(
                        content = Text(text, whitespace = Whitespace.PRE, width = 17),
                        title = Text("User Created"),
                )
        )

        return userId
    }

    private fun createAccount(userId: String, accountType: AccountType, balance: Double) {
        val request =
                CreateAccountRequest(
                        userId = userId,
                        accountType = accountType.name,
                        balance = balance,
                        transactions = mutableListOf()
                )
        val account = createAccountPort.create(request)
        terminal.success("Account created successfully! Account ID: ${account.id}")

        val text =
                """
        Account ID: ${account.id}
        User ID: ${account.userId}
        Account Number: ${account.accountNumber}
        Account Type: ${account.accountType}
        Balance: ${account.balance}
            """.trimIndent()

        terminal.println(
                Panel(
                        content = Text(text, whitespace = Whitespace.PRE, width = 17),
                        title = Text("Account Created"),
                )
        )
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

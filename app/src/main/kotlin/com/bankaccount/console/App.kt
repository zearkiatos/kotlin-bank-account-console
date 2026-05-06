package com.bankaccount.console

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
import com.bankaccount.console.account.infrastructure.console.BankAccountConsoleAdapter
import com.bankaccount.console.account.infrastructure.repository.InMemoryAccountRepository
import com.bankaccount.console.user.infrastructure.repository.InMemoryUserRepository
import com.bankaccount.console.user.infrastructure.console.LoginConsoleAdapter
import com.bankaccount.console.account.application.CreateAccountUseCases
import com.bankaccount.console.user.application.CreateUserUseCases
import com.bankaccount.console.user.application.LoginUseCases
import com.bankaccount.console.transaction.application.CreateTransactionUseCases
import com.bankaccount.console.transaction.infrastructure.repository.InMemoryTransactionRepository

fun main() {
    val terminal = Terminal()
    val userRepository = InMemoryUserRepository()
    val accountRepository = InMemoryAccountRepository()
    val transactionRepository = InMemoryTransactionRepository()
    val accountUseCases = CreateAccountUseCases(
        accountRepository = accountRepository
    )
    val userUseCases = CreateUserUseCases(
        userRepository = userRepository
    )
    val transactionUseCases = CreateTransactionUseCases(
        transactionRepository = transactionRepository,
        accountRepository = accountRepository
    )
    val app = BankAccountConsoleAdapter(
        createAccountPort = accountUseCases,
        createUserPort = userUseCases,
    )

    val authenticateApp = LoginConsoleAdapter(
        loginInputPort = LoginUseCases(
            userRepository = userRepository
        )
    )

    header(terminal)
    var userId: String? = null
    while(true) {
        val options = mainMenu(terminal, userId)

        if (options == "3") {
            terminal.danger("Exiting...")
            System.exit(0)
        }

        when (options) {
            "1" -> {
                app.run()
            }
            "2" -> {
                userId = authenticateApp.run()
            }
            "3" -> {
                terminal.danger("Exiting...")
                System.exit(0)
            }
            "4" -> {
                terminal.success("Fetching account balance...")
                terminal.danger("This feature is not implemented yet. Stay tuned for updates!")
            }
            "5" -> {
                userId = null
                terminal.danger("Logged out successfully...")
            }
            else -> terminal.danger("Invalid option, please try again.")
        }
    }
}

private fun header(terminal: Terminal) {
        val pixelTitle =
                """
                ██████╗  █████╗ ███╗   ██╗██╗  ██╗
                ██╔══██╗██╔══██╗████╗  ██║██║ ██╔╝
                ██████╔╝███████║██╔██╗ ██║█████╔╝ 
                ██╔══██╗██╔══██║██║╚██╗██║██╔═██╗ 
                ██████╔╝██║  ██║██║ ╚████║██║  ██╗
                ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝
                                                  
                 █████╗  ██████╗ ██████╗ ██████╗ ██╗   ██╗███╗   ██╗████████╗
                ██╔══██╗██╔════╝██╔════╝██╔═══██╗██║   ██║████╗  ██║╚══██╔══╝
                ███████║██║     ██║     ██║   ██║██║   ██║██╔██╗ ██║   ██║   
                ██╔══██║██║     ██║     ██║   ██║██║   ██║██║╚██╗██║   ██║   
                ██║  ██║╚██████╗╚██████╗╚██████╔╝╚██████╔╝██║ ╚████║   ██║   
                ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═══╝   ╚═╝                                                                                                 
    """.trimIndent()
        val subtitle =
                """
            Welcome to your banking system.                                                                                                
    """.trimIndent()
        terminal.println((brightBlue + bold)(pixelTitle))
        terminal.println((brightBlue + italic)(subtitle))
    }

    private fun mainMenu(terminal: Terminal, userId: String?): String? {
        val title =
                """
            What would you like to do?:
        """.trimIndent()
        terminal.println((brightGreen + bold)(title))
        var userChoices = listOf("1. Create Account", "2. Login", "3. Exit")
        var menuInfo = "Choose an option or exit (1, 2 or 3)"

        if (!userId.isNullOrBlank()) {
            userChoices = listOf("1. Get Account Balance", "2. Logout", "3. Exit")
            menuInfo = "Choose an option or exit (1, 2 or 3)"
        }

        val selection =
                terminal.interactiveSelectList(
                        userChoices,
                        menuInfo,
                )

        if (selection == null) {
            terminal.danger("Aborted account creation")
            return null
        }

        val index = resolvedMenuChoice(selection)
        terminal.success("You chose option $index: $selection")
        return index
    }

    fun resolvedMenuChoice(selection: String): String {
        return when (selection) {
            "1. Create Account" -> "1"
            "2. Login" -> "2"
            "3. Exit" -> "3"
            "1. Get Account Balance" -> "4"
            "2. Logout" -> "5"
            else -> "invalid"
        }
    }



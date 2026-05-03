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
import com.bankaccount.console.account.application.CreateAccountUseCases
import com.bankaccount.console.user.application.CreateUserUseCases

fun main() {
    val terminal = Terminal()
    val accountUseCases = CreateAccountUseCases(
        accountRepository = InMemoryAccountRepository()
    )
    val userUseCases = CreateUserUseCases(
        userRepository = InMemoryUserRepository()
    )
    val app = BankAccountConsoleAdapter(
        createAccountPort = accountUseCases,
        createUserPort = userUseCases
    )

    header(terminal)
    while(true) {
        val options = mainMenu(terminal)

        if (options == "3") {
            terminal.danger("Exiting...")
            System.exit(0)
        }

        when (options) {
            "1" -> app.run()
            "2" -> terminal.success("Login functionality not implemented yet.")
            else -> terminal.danger("Invalid option, please try again.")
        }
    }


    app.run()
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

    private fun mainMenu(terminal: Terminal): String? {
        val title =
                """
            What would you like to do?:
        """.trimIndent()
        terminal.println((brightGreen + bold)(title))

        val userChoices = listOf("1. Create Account", "2. Login", "Exit")
        val selection =
                terminal.interactiveSelectList(
                        userChoices,
                        "Choose an option or exit (1, 2 or 3)",
                )

        if (selection == null) {
            terminal.danger("Aborted account creation")
            return null
        }

        val index = userChoices.indexOf(selection)
        terminal.success("You chose option $index: $selection")
        return (index + 1).toString()
    }



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
import com.bankaccount.console.account.infrastructure.console.AccountBalanceConsoleAdapter
import com.bankaccount.console.account.infrastructure.repository.InMemoryAccountRepository
import com.bankaccount.console.user.infrastructure.repository.InMemoryUserRepository
import com.bankaccount.console.user.infrastructure.console.LoginConsoleAdapter
import com.bankaccount.console.account.application.CreateAccountUseCases
import com.bankaccount.console.user.application.CreateUserUseCases
import com.bankaccount.console.user.application.LoginUseCases
import com.bankaccount.console.account.application.AccountUseCases
import com.bankaccount.console.transaction.application.CreateTransactionUseCases
import com.bankaccount.console.transaction.application.GetTransactionUseCases
import com.bankaccount.console.transaction.infrastructure.repository.InMemoryTransactionRepository
import com.bankaccount.console.transaction.infrastructure.console.TransactionConsoleAdapter
import kotlin.system.exitProcess

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
    val createTransactionUseCases = CreateTransactionUseCases(
        transactionRepository = transactionRepository,
        accountRepository = accountRepository
    )
    val transactionUseCases = GetTransactionUseCases(
        transactionRepository = transactionRepository
    )
    val accountBalanceUseCases = AccountUseCases(
        accountRepository = accountRepository
    )
    val app = BankAccountConsoleAdapter(
        createAccountPort = accountUseCases,
        createUserPort = userUseCases,
    )

    val accountBalanceApp = AccountBalanceConsoleAdapter(
        accountPort = accountBalanceUseCases
    )

    val authenticateApp = LoginConsoleAdapter(
        loginInputPort = LoginUseCases(
            userRepository = userRepository
        )
    )

    val transactionApp = TransactionConsoleAdapter(
        createTransactionPort = createTransactionUseCases,
        transactionPort = transactionUseCases,
        accountPort = accountBalanceUseCases
    )

    val dependencies = AppDependencies(
        createAccount = { app.run() },
        login = { authenticateApp.run() },
        accountBalance = { id -> accountBalanceApp.run(id) },
        transactionHistory = { id -> transactionApp.run(id) }
    )
    val maxIterations = System.getProperty("app.maxIterations")?.toIntOrNull()
    runApp(
        terminal = terminal,
        dependencies = dependencies,
        menu = ::mainMenu,
        exit = ::exitProcess,
        maxIterations = maxIterations
    )
}

internal fun header(terminal: Terminal) {
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

    internal fun mainMenu(terminal: Terminal, userId: String?): String? {
        return mainMenu(terminal, userId) { choices, info ->
            terminal.interactiveSelectList(choices, info)
        }
    }

    internal fun mainMenu(
        terminal: Terminal,
        userId: String?,
        select: (List<String>, String) -> String?
    ): String? {
        val title =
                """
            What would you like to do?:
        """.trimIndent()
        terminal.println((brightGreen + bold)(title))
        var userChoices = listOf("1. Create Account", "2. Login", "3. Exit")
        var menuInfo = "Choose an option or exit (1, 2 or 3)"

        if (!userId.isNullOrBlank()) {
            userChoices = listOf("1. Get Account Balance", "2. Transactions", "3. Logout", "4. Exit")
            menuInfo = "Choose an option or exit (1, 2, 3 or 4)"
        }

        val selection = select(userChoices, menuInfo)

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
            "2. Transactions" -> "5"
            "3. Logout" -> "6"
            "4. Exit" -> "3"
            else -> "invalid"
        }
    }

internal data class AppDependencies(
    val createAccount: () -> Unit,
    val login: () -> String?,
    val accountBalance: (String) -> Unit,
    val transactionHistory: (String) -> Unit
)

internal fun runApp(
    terminal: Terminal,
    dependencies: AppDependencies,
    menu: (Terminal, String?) -> String?,
    exit: (Int) -> Nothing,
    maxIterations: Int? = null
) {
    header(terminal)
    var userId: String? = null
    var iterations = 0
    while (true) {
        if (maxIterations != null && iterations >= maxIterations) {
            return
        }
        iterations += 1
        val options = menu(terminal, userId)

        when (options) {
            "1" -> {
                dependencies.createAccount()
            }
            "2" -> {
                userId = dependencies.login()
            }
            "3" -> {
                terminal.danger("Exiting...")
                exit(0)
            }
            "4" -> {
                terminal.success("Fetching account balance...")
                dependencies.accountBalance(userId!!)
            }
            "5" -> {
                terminal.success("Fetching transaction history...")
                dependencies.transactionHistory(userId!!)
            }
            "6" -> {
                userId = null
                terminal.danger("Logged out successfully...")
            }
            else -> terminal.danger("Invalid option, please try again.")
        }
    }
}



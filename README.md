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
# Description
This is a Kotlin console application to management a bank account 🏦 and make banking operations 💸🏧

# Made with
[![Kotlin](https://img.shields.io/badge/kotlin-7f52ff?style=for-the-badge&logo=kotlin&logoColor=white&labelColor=000000)]()

# Project Flow

```mermaid
flowchart TD
    A["Welcome to your banking system<br/>What type of account would you like to create?"]
    B["1. Debit account"]
    C["2. Credit account"]
    D["3. Checking account"]
    E["accountType = debit"]
    F["accountType = credit"]
    G["accountType = checking"]
    
    A --> B --> E
    A --> C --> F
    A --> D --> G

    classDef entry fill:#1f6f8b,stroke:#0e2f3a,color:#ffffff;
    classDef option fill:#e9eef2,stroke:#546e7a,color:#1b1f23;
    classDef result fill:#2e7d32,stroke:#1b5e20,color:#ffffff;

    class A entry;
    class B,C,D option;
    class E,F,G result;
```

# Debit Withdraw Logic

```mermaid
flowchart TD
    A[debitWithdraw] --> B{accountBalance == 0}
    B -- true --> C[Print error message and return accountBalance]
    B -- false --> D{amount > accountBalance}
    D -- true --> E[Print error message and return 0]
    D -- false --> F["return withdraw(amount)"]

    classDef entry fill:#1f6f8b,stroke:#0e2f3a,color:#ffffff;
    classDef decision fill:#f9a825,stroke:#c17900,color:#1b1f23;
    classDef error fill:#c62828,stroke:#8e0000,color:#ffffff;
    classDef action fill:#2e7d32,stroke:#1b5e20,color:#ffffff;

    class A entry;
    class B,D decision;
    class C,E error;
    class F action;
```

# Credit Deposit Logic

```mermaid
flowchart TD
    A[creditDeposit] --> B{accountBalance == 0}
    B -- true --> C["Print message: No deposit needed to pay off the account. Return accountBalance"]
    B -- false --> D{"accountBalance + amount > 0"}
    D -- true --> E["Print message: Deposit failed. Amount exceeds credit balance. Return 0"]
    D -- false --> F{"amount == -accountBalance"}
    F -- true --> G["Set accountBalance = 0. Print message: Account paid off. Return amount"]
    F -- false --> H["Call deposit(amount) and return its result"]

    classDef entry fill:#1f6f8b,stroke:#0e2f3a,color:#ffffff;
    classDef decision fill:#f9a825,stroke:#c17900,color:#1b1f23;
    classDef error fill:#c62828,stroke:#8e0000,color:#ffffff;
    classDef action fill:#2e7d32,stroke:#1b5e20,color:#ffffff;

    class A entry;
    class B,D,F decision;
    class C,E error;
    class G,H action;
```

## 📊 Code Coverage

Execute **JaCoCo** to measure the code coverage.

### Instalación del Pre-commit Hook

Execute once to install the hook:

```bash
$ bash scripts/install-hooks.sh

```
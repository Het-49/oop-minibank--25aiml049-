# Practical 5 - Abstraction and polymorphism

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To make Account abstract and add SavingsAccount, CurrentAccount and FixedDepositAccount, each with its own interest and withdrawal rules.

Account declares abstract methods interestRate() and canWithdraw(). Each subclass supplies its own version and calls super(...) in its constructor. When the same method is called on an Account[] the correct version runs at run time (polymorphism).

## What this practical covers

- `abstract class Account` with `interestRate()` and `canWithdraw()`
- `SavingsAccount` (minBalance, 4.0%)
- `CurrentAccount` (overdraftLimit, 0%)
- `FixedDepositAccount` (7.0%, withdrawal locked)
- Pattern `instanceof` for one type

## Project structure

```text
practical-5/
├── .vscode/
│   └── settings.json
├── src/
│   ├── Account.java
│   ├── BankInfo.java
│   ├── Command.java
│   ├── CommandParser.java
│   ├── CurrentAccount.java
│   ├── Customer.java
│   ├── FixedDepositAccount.java
│   ├── MenuOption.java
│   ├── MiniBank.java
│   ├── SavingsAccount.java
│   ├── StatementFormatter.java
│   ├── TransactionType.java
│   └── Validator.java
├── .gitignore
└── README.md
```

## Getting started

### Prerequisites

- JDK 17 or newer (check with `java -version` and `javac -version`)
- Any editor. In VS Code open **this folder** (`practical-5`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-5
mkdir -p bin
javac -d bin src/*.java
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-5
mkdir bin
javac -d bin src\*.java
java -cp bin MiniBank
```

## Sample output

```text
SavingsAccount: interestRate = 4.0%, canWithdraw(9500) = false, canWithdraw(6000) = true
   special case: savings minimum balance = 1000
CurrentAccount: interestRate = 0.0%, canWithdraw(9500) = false, canWithdraw(6000) = true
FixedDepositAccount: interestRate = 7.0%, canWithdraw(9500) = false, canWithdraw(6000) = false
withdraw 6500 from the current account -> true, balance = -4500
withdraw 100 from the fixed deposit    -> false
==============================
      MINIBANK STATEMENT
==============================
Account No : AC0001
Owner      : Asha
Balance    : Rs.10000
Status     : ACTIVE
==============================
```

## Concepts used

Abstract classes, inheritance, super(...), run-time polymorphism, pattern matching for instanceof.

## Navigation

[← Practical 4](../practical-4) · [All practicals](..) · [Practical 6 →](../practical-6)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

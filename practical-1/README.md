# Practical 1 - Project set-up and menu shell

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To set up the MiniBank project and build its console menu shell using a record (BankInfo), an enum (MenuOption), a Scanner, a loop and a switch expression.

A record is a short read-only class: Java generates its constructor and accessors. An enum lists the fixed menu actions. The program prints a numbered menu, reads the choice with Scanner, and a switch expression picks the placeholder message. The loop repeats until EXIT is chosen.

## What this practical covers

- Public class `MiniBank` with the `main` method
- `BankInfo` record (name, branch) printed as the header
- `MenuOption` enum: OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
- Numbered menu read with `Scanner` in a loop until EXIT
- Switch expression that prints a placeholder for every option

## Project structure

```text
practical-1/
├── .vscode/
│   └── settings.json
├── src/
│   ├── BankInfo.java
│   ├── MenuOption.java
│   └── MiniBank.java
├── .gitignore
└── README.md
```

## Getting started

### Prerequisites

- JDK 17 or newer (check with `java -version` and `javac -version`)
- Any editor. In VS Code open **this folder** (`practical-1`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-1
mkdir -p bin
javac -d bin src/*.java
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-1
mkdir bin
javac -d bin src\*.java
java -cp bin MiniBank
```

This program reads from the keyboard: type a menu number and press Enter (`5` = EXIT).

## Sample output

```text
=======================================
  BankInfo[name=MiniBank, branch=Vadodara Main Branch]
=======================================

1. OPEN_ACCOUNT
2. DEPOSIT
3. WITHDRAW
4. TRANSFER
5. EXIT
Choose an option: 9
Please enter a number from 1 to 5.
Choose an option: abc
Please enter a number from 1 to 5.
Choose an option: 2
Deposit - to be implemented in a later lab
Choose an option: 3
Withdraw - to be implemented in a later lab
Choose an option: 1
Open Account - to be implemented in a later lab
Choose an option: 4
Transfer - to be implemented in a later lab
Choose an option: 5
Goodbye! Thank you for using MiniBank.
```

## Concepts used

Records, enums, switch expressions, loops, Scanner, input validation.

## Navigation

[All practicals](..) · [Practical 2 →](../practical-2)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

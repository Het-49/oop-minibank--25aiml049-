# Practical 2 - Customer and Account (encapsulation)

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To create the Customer and Account classes so that an account balance can change only through deposit() and withdraw().

Encapsulation keeps fields private and exposes behaviour through public methods. Private static counters generate the ids CUST101 and AC0001. The second Account constructor calls the first with this(ownerName, 0). withdraw() returns false and leaves the balance unchanged when the money is not enough.

## What this practical covers

- `Customer` with private fields, final `customerId`, static counter and `generateCustomerId()`
- `Account` with final `accountNumber` (AC0001 ...), balance in whole rupees and an active flag
- Two constructors, the second using `this(ownerName, 0)`
- `deposit()` and boolean `withdraw()`; getters only, no `setBalance()`
- `Account[]` with three accounts in `main`

## Project structure

```text
practical-2/
├── .vscode/
│   └── settings.json
├── src/
│   ├── Account.java
│   ├── BankInfo.java
│   ├── Customer.java
│   ├── MenuOption.java
│   └── MiniBank.java
├── .gitignore
└── README.md
```

## Getting started

### Prerequisites

- JDK 17 or newer (check with `java -version` and `javac -version`)
- Any editor. In VS Code open **this folder** (`practical-2`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-2
mkdir -p bin
javac -d bin src/*.java
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-2
mkdir bin
javac -d bin src\*.java
java -cp bin MiniBank
```

## Sample output

```text
BankInfo[name=MiniBank, branch=Vadodara Main Branch]
CUST101 Asha Patel | CUST102 Ravi Shah
withdraw 2500 from AC0001 -> true
withdraw 4000 from AC0002 -> false
withdraw 20000 from AC0003 -> true
AC0001 Asha Patel balance = 3500
AC0002 Ravi Shah balance = 500
AC0003 Meena Joshi balance = 0
```

## Concepts used

Encapsulation, static members, constructor overloading and chaining, arrays of objects.

## Navigation

[← Practical 1](../practical-1) · [All practicals](..) · [Practical 3 →](../practical-3)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

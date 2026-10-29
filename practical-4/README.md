# Practical 4 - Validation and command parsing

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To build MiniBank's validation layer: regex checks for mobile, email, PAN and IFSC, and a parser that turns a typed line into a Command object.

Patterns are compiled once with Pattern.compile() and reused. CommandParser splits "DEPOSIT AC0001 500" with split(), converts the parts and returns a Command record; bad lines throw a clear IllegalArgumentException. StatementFormatter builds a multi-line statement with a StringBuilder.

## What this practical covers

- `Validator`: isValidMobile, isValidEmail, isValidPan, isValidIfsc
- `TransactionType` enum and `Command` record
- `CommandParser.parse(String)` using `split()`
- `StatementFormatter.buildStatement(Account)` using `StringBuilder`

## Project structure

```text
practical-4/
├── .vscode/
│   └── settings.json
├── src/
│   ├── Account.java
│   ├── BankInfo.java
│   ├── Command.java
│   ├── CommandParser.java
│   ├── Customer.java
│   ├── MenuOption.java
│   ├── MiniBank.java
│   ├── StatementFormatter.java
│   ├── TransactionType.java
│   └── Validator.java
├── .gitignore
└── README.md
```

## Getting started

### Prerequisites

- JDK 17 or newer (check with `java -version` and `javac -version`)
- Any editor. In VS Code open **this folder** (`practical-4`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-4
mkdir -p bin
javac -d bin src/*.java
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-4
mkdir bin
javac -d bin src\*.java
java -cp bin MiniBank
```

## Sample output

```text
mobile 9876543210  -> true
mobile 12345       -> false
email  a@b.com     -> true
email  a@b         -> false
PAN    ABCDE1234F  -> true
PAN    abcde1234   -> false
IFSC   SBIN0001234 -> true
IFSC   SBIN1001234 -> false
type    = DEPOSIT
account = AC0001
amount  = 500
rejected: Expected: <TYPE> <ACCOUNT> <AMOUNT>, got: DEPOSIT AC0001
==============================
      MINIBANK STATEMENT
==============================
Account No : AC0001
Owner      : Asha Patel
Balance    : Rs.7500
Status     : ACTIVE
==============================
```

## Concepts used

Regular expressions, records, enum.valueOf, String.split, StringBuilder.

## Navigation

[← Practical 3](../practical-3) · [All practicals](..) · [Practical 5 →](../practical-5)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

# Practical 8 - Custom exceptions

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To replace the true/false results with a custom checked-exception hierarchy so every failure is explicit and carries information.

BankException extends Exception, and InsufficientFundsException (with a shortfall), AccountNotFoundException and InvalidAmountException extend it. withdraw() and deposit() throw them, transfer() uses try, catch, finally and re-throws, and a try-with-resources block closes an AutoCloseable AuditSession automatically.

## What this practical covers

- Package `exception` with four checked exceptions
- `deposit()` and `withdraw()` throw exceptions instead of returning false
- `transfer(Account, long)` with try / catch / finally and re-throw
- try-with-resources using `AuditSession`

## Project structure

```text
practical-8/
├── .vscode/
│   └── settings.json
├── src/
│   ├── exception/
│   │   ├── AccountNotFoundException.java
│   │   ├── BankException.java
│   │   ├── InsufficientFundsException.java
│   │   └── InvalidAmountException.java
│   ├── model/
│   │   ├── annotation/
│   │   │   ├── Id.java
│   │   │   ├── MaxLength.java
│   │   │   └── Positive.java
│   │   ├── Account.java
│   │   ├── BankInfo.java
│   │   ├── Command.java
│   │   ├── CurrentAccount.java
│   │   ├── Customer.java
│   │   ├── FixedDepositAccount.java
│   │   ├── InterestBearing.java
│   │   ├── MenuOption.java
│   │   ├── Premium.java
│   │   ├── PremiumSavingsAccount.java
│   │   ├── SavingsAccount.java
│   │   ├── Transactable.java
│   │   ├── TransactionType.java
│   │   └── WithdrawRule.java
│   ├── service/
│   │   └── BankService.java
│   ├── util/
│   │   ├── AnnotationValidator.java
│   │   ├── AuditSession.java
│   │   ├── CommandParser.java
│   │   ├── StatementFormatter.java
│   │   └── Validator.java
│   └── MiniBank.java
├── .gitignore
├── build.sh
├── manifest.txt
└── README.md
```

## Getting started

### Prerequisites

- JDK 17 or newer (check with `java -version` and `javac -version`)
- Any editor. In VS Code open **this folder** (`practical-8`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-8
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-8
mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
java -cp bin MiniBank
```

### Build the runnable JAR

```bash
jar --create --file MiniBank.jar --manifest manifest.txt -C bin .
java -jar MiniBank.jar
```

`manifest.txt` sets `Main-Class: MiniBank`. `./build.sh` does all of this in one step.

## Sample output

```text
Sorry: Insufficient funds: short by Rs.2500 (shortfall field = 2500)
finally: withdrawal attempt finished
Invalid amount: Deposit must be positive, got -10
  [audit] transfer AC0001 -> AC0002 of Rs.1000 attempted
transfer OK -> 2000 / 1000
  [audit] transfer AC0001 -> AC0002 of Rs.99999 attempted
Transfer failed: Insufficient funds: short by Rs.98499
Lookup failed: Account not found: AC9999
  [session] opened: nightly-audit
  [session] nightly-audit -> checking AC0001
  [session] closed: nightly-audit
```

## Concepts used

Checked exceptions, throw and throws, try-catch-finally, re-throwing, try-with-resources.

## Navigation

[← Practical 7](../practical-7) · [All practicals](..) · [Practical 9 →](../practical-9)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

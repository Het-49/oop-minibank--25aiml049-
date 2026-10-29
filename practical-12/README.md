# Practical 12 - Collections framework

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To upgrade MiniBank's storage to the Collections Framework: a HashMap store, sorting with a Comparator and Comparable, and a thread-safe ConcurrentHashMap.

A HashMap keyed by account number finds an account without searching an array. A Comparator sorts the same accounts by balance, while Comparable (compareTo) gives the natural order by account number. A ConcurrentHashMap lets several worker threads look up accounts safely.

## What this practical covers

- `AccountStore` with `HashMap&lt;String, Account&gt;`: `add`, `get`, `getAll`
- Transaction history in an `ArrayList&lt;String&gt;`
- `sortedByBalance()` using a `Comparator`
- `Account implements Comparable&lt;Account&gt;` (compare account numbers)
- `TransactionProcessor` keeps its accounts in a `ConcurrentHashMap`

## Project structure

```text
practical-12/
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
│   │   ├── AccountStore.java
│   │   ├── BankService.java
│   │   ├── DeadlockDemo.java
│   │   ├── ProducerConsumerDemo.java
│   │   └── TransactionProcessor.java
│   ├── util/
│   │   ├── AnnotationValidator.java
│   │   ├── AuditSession.java
│   │   ├── CommandParser.java
│   │   ├── ReportGenerator.java
│   │   ├── StatementFormatter.java
│   │   ├── StatePersister.java
│   │   ├── TransactionLog.java
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
- Any editor. In VS Code open **this folder** (`practical-12`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-12
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-12
mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
java -cp bin MiniBank
```

The program creates a `data/` folder in the folder you run it from (it is listed in `.gitignore`).

### Build the runnable JAR

```bash
jar --create --file MiniBank.jar --manifest manifest.txt -C bin .
java -jar MiniBank.jar
```

`manifest.txt` sets `Main-Class: MiniBank`. `./build.sh` does all of this in one step.

## Sample output

```text
get("AC0002") -> Account[accountNumber=AC0002, ownerName=Ravi Shah, balance=3500]
get("AC9999") -> Account not found: AC9999
---- sorted by balance (low to high) ----
  800  Account[accountNumber=AC0004, ownerName=Kiran Desai, balance=800]
  3500  Account[accountNumber=AC0002, ownerName=Ravi Shah, balance=3500]
  7600  Account[accountNumber=AC0005, ownerName=Neha Mehta, balance=7600]
  12000  Account[accountNumber=AC0001, ownerName=Asha Patel, balance=12000]
  50000  Account[accountNumber=AC0003, ownerName=Meena Joshi, balance=50000]
---- sorted by account number (compareTo) ----
  AC0001  Asha Patel
  AC0002  Ravi Shah
  AC0003  Meena Joshi
  AC0004  Kiran Desai
  AC0005  Neha Mehta
---- history ----
  ADDED AC0001 (Asha Patel)
  ADDED AC0002 (Ravi Shah)
  ADDED AC0003 (Meena Joshi)
  ADDED AC0004 (Kiran Desai)
  ADDED AC0005 (Neha Mehta)
  NOTE sample accounts created
---- thread-safe processing ----
AC0001 balance = 15000 (expected 15000)
processed = 50, failed = 1
```

## Concepts used

HashMap, ArrayList, Comparator, Comparable, ConcurrentHashMap, thread safety.

## Navigation

[← Practical 11](../practical-11) · [All practicals](..) · [Practical 13 →](../practical-13)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

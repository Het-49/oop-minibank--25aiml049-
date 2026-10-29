# Practical 11 - Persistence and reporting

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To make MiniBank remember its data: save and load all accounts with serialization, write an append-only transaction log and build an end-of-day report with NIO.

A class that implements Serializable can be written with ObjectOutputStream and read back with ObjectInputStream; serialVersionUID identifies the class version. Files.write with CREATE and APPEND only adds lines to a log. The report walks the logs folder with Files.newDirectoryStream, reads each file with a BufferedReader and reads size and last-modified time with Files.readAttributes.

## What this practical covers

- `Account`, its subclasses and `Customer` implement `Serializable` with `serialVersionUID`
- `StatePersister.save(Account[], Path)` and `load(Path)` with try-with-resources
- `TransactionLog.append(Path, String)` using `Files.write(...)` with CREATE and APPEND
- `ReportGenerator`: `newDirectoryStream`, `BufferedReader`, totals in `long` variables, `Files.readAttributes`, report file
- The saved account counter is restored on load so new accounts never repeat a number

## Project structure

```text
practical-11/
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
- Any editor. In VS Code open **this folder** (`practical-11`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-11
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-11
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
Saved 3 accounts to data/accounts.dat (405 bytes)
Reloaded 3 accounts:
  Account[accountNumber=AC0001, ownerName=Asha Patel, balance=6500]  equals original: true
  Account[accountNumber=AC0002, ownerName=Ravi Shah, balance=-1500]  equals original: true
  Account[accountNumber=AC0003, ownerName=Meena Joshi, balance=20000]  equals original: true
Logs written to data/logs
===== END-OF-DAY SUMMARY =====
File 2026-10-27.log  size=95 bytes  modified=2026-10-29T04:49:56Z
File 2026-10-28.log  size=107 bytes  modified=2026-10-29T04:49:56Z
Log files read      : 2
Lines read          : 7 (skipped 1)
Total deposits      : Rs.5750
Total withdrawals   : Rs.3100
Transfers           : 1
Net movement        : Rs.2650
Report saved to data/report.txt
```

## Concepts used

Serialization, ObjectOutputStream / ObjectInputStream, NIO.2 (Path, Files), DirectoryStream, BufferedReader, file attributes.

## Navigation

[← Practical 10](../practical-10) · [All practicals](..) · [Practical 12 →](../practical-12)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

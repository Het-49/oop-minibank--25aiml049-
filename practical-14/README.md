# MiniBank - Practical 14: Final Capstone

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

The finished console banking application. `BankService` joins the repository, the validation layer, the custom
exceptions, persistence, batch processing and the report; the menu from Practical 1 calls real operations.

## Features

- **Menu:** open account, deposit, withdraw, transfer, statement, list accounts, batch, report, exit
- **Accounts:** Savings, Current (overdraft) and Fixed Deposit, each with its own interest and withdrawal rules
- **Validation:** mobile, email and amount checks through `Validator`; every failure is a custom exception
- **Persistence:** accounts are loaded on start-up and saved on exit (`data/accounts.dat`); account numbers continue after a restart
- **Transaction log and report:** every operation is appended to `data/logs/<date>.log`; the REPORT option builds the end-of-day summary
- **Safe batch processing:** a batch of deposits and withdrawals runs on a 4-thread pool over a `ConcurrentHashMap`
- **Audit trail (optional):** methods marked `@Audit` are recorded by a reflective logger (dynamic proxy)
- **JUnit 5 tests** for the main rules, including a concurrency test

## How the parts fit together

```text
MiniBank (menu)
   |  calls through a dynamic proxy that records @Audit methods (AuditLogger)
   v
BankOperations  <---- implemented by ---- BankService
                                              |-- Repository<Account>, Repository<Customer>   (generic, key found by @Id)
                                              |-- Validator, exceptions                       (Practical 4, 8)
                                              |-- TransactionLog / ReportGenerator            (Practical 11)
                                              |-- StatePersister                              (Practical 11)
                                              '-- TransactionProcessor + ConcurrentHashMap    (Practical 10, 12)
```

## Project structure

```text
practical-14/
├── .vscode/
│   └── settings.json
├── src/
│   ├── exception/
│   │   ├── AccountNotFoundException.java
│   │   ├── BankException.java
│   │   ├── InsufficientFundsException.java
│   │   ├── InvalidAmountException.java
│   │   └── ValidationException.java
│   ├── model/
│   │   ├── annotation/
│   │   │   ├── Audit.java
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
│   │   ├── BankOperations.java
│   │   ├── BankService.java
│   │   ├── DeadlockDemo.java
│   │   ├── ProducerConsumerDemo.java
│   │   ├── Repository.java
│   │   └── TransactionProcessor.java
│   ├── util/
│   │   ├── AnnotationValidator.java
│   │   ├── AuditLogger.java
│   │   ├── AuditSession.java
│   │   ├── CommandParser.java
│   │   ├── ReportGenerator.java
│   │   ├── StatementFormatter.java
│   │   ├── StatePersister.java
│   │   ├── TransactionLog.java
│   │   └── Validator.java
│   └── MiniBank.java
├── test/
│   └── BankServiceTest.java
├── .gitignore
├── build.bat
├── build.sh
├── manifest.txt
├── MiniBank.jar
├── pom.xml
└── README.md
```

## Getting started

Requires JDK 17 or newer.

### Run the ready-made JAR

```bash
cd practical-14
java -jar MiniBank.jar
```

### Build from source

```bash
./build.sh            # macOS / Linux: compile, build MiniBank.jar and run it
build.bat             # Windows
```

or by hand:

```bash
mkdir -p bin
javac -d bin $(find src -name "*.java")
jar --create --file MiniBank.jar --manifest manifest.txt -C bin .
java -jar MiniBank.jar
```

### Run the tests

The tests are in `test/BankServiceTest.java` (JUnit 5).

- **Maven:** `mvn test` (uses `pom.xml`; JUnit 5.10.2 is downloaded automatically)
- **VS Code:** open this folder, open `BankServiceTest.java` and choose *Enable Java tests* in the Testing view; click the run icon beside a test

| Test | What it checks |
|------|----------------|
| `depositIncreasesTheBalance` | a deposit increases the balance |
| `withdrawalOverTheLimitThrowsAnException` | an over-limit withdrawal throws `InsufficientFundsException` and leaves the balance unchanged |
| `manyConcurrentDepositsKeepTheBalanceCorrect` | 1000 concurrent deposits give the exact total |
| `serviceOpensDepositsAndWithdraws` | `BankService` open / deposit / withdraw / statement |
| `serviceRejectsAnInvalidMobileNumber` | invalid input throws `ValidationException` |
| `repositoryFindsTheIdWithTheIdAnnotation` | the generic repository finds the key through `@Id` |
| `savedAccountsSurviveReloading` | save and load keep the accounts |

## Using the menu

| Option | What it does |
|--------|--------------|
| 1 OPEN_ACCOUNT | asks for name, mobile, email, type (1 Savings, 2 Current, 3 Fixed Deposit) and opening amount |
| 2 DEPOSIT / 3 WITHDRAW | asks for the account number and the amount |
| 4 TRANSFER | asks for the source account, target account and amount |
| 5 STATEMENT | prints a statement for one account |
| 6 LIST_ACCOUNTS | lists all accounts |
| 7 BATCH | runs 100 deposits and 40 withdrawals of Rs.10 on one account using the thread pool |
| 8 REPORT | prints the end-of-day summary and the audit trail |
| 9 EXIT | saves the accounts and quits |

Files created at run time (folder `data/`, ignored by Git): `accounts.dat`, `logs/<date>.log`, `report.txt`.

## Sample session

Run 1 (the menu listing that is printed before every prompt is left out here):

```text
=======================================
  BankInfo[name=MiniBank, branch=Vadodara Main Branch]
=======================================
Loaded 0 saved account(s).
Choose an option: 1
Customer name: Asha Patel
Mobile (10 digits): 9876543210
Email: asha@example.com
Account type (1=Savings, 2=Current, 3=Fixed Deposit): 1
Opening amount (Rs.): 5000
Opened SavingsAccount AC0001 for Asha Patel, balance Rs.5000
Choose an option: 1
Customer name: Ravi Shah
Mobile (10 digits): 9123456780
Email: ravi@example.com
Account type (1=Savings, 2=Current, 3=Fixed Deposit): 2
Opening amount (Rs.): 1000
Opened CurrentAccount AC0002 for Ravi Shah, balance Rs.1000
Choose an option: 1
Customer name: Bad User
Mobile (10 digits): 12345
Email: bad@example.com
Account type (1=Savings, 2=Current, 3=Fixed Deposit): 1
Opening amount (Rs.): 100
Failed: Invalid mobile number (10 digits starting with 6-9)
Choose an option: 2
Account number: AC0001
Amount (Rs.): 1500
New balance: Rs.6500
Choose an option: 3
Account number: AC0001
Amount (Rs.): 9000
Failed: Insufficient funds: short by Rs.3000
Choose an option: 4
From account: AC0001
To account: AC0002
Amount (Rs.): 2000
  [audit] transfer AC0001 -> AC0002 of Rs.2000 attempted
Transfer complete.
Choose an option: 7
Account number for the batch: AC0001
Batch finished: 140 processed, 0 failed
Expected change: +Rs.600 (100 x 10 - 40 x 10)
Choose an option: 5
Account number: AC0001
==============================
      MINIBANK STATEMENT
==============================
Account No : AC0001
Owner      : Asha Patel
Balance    : Rs.5100
Status     : ACTIVE
==============================
Choose an option: 6
AC0001  SavingsAccount       Asha Patel     Rs.5100
AC0002  CurrentAccount       Ravi Shah      Rs.3000
Choose an option: 8
===== END-OF-DAY SUMMARY =====
File 2026-10-28.log  size=3938 bytes  modified=2026-10-28T12:06:48Z
Log files read      : 1
...
```

Run 2, after restarting the program:

```text
=======================================
  BankInfo[name=MiniBank, branch=Vadodara Main Branch]
=======================================
Loaded 2 saved account(s).
Choose an option: 6
AC0001  SavingsAccount       Asha Patel     Rs.5100
AC0002  CurrentAccount       Ravi Shah      Rs.3000
Choose an option: 1
Customer name: Meena Joshi
Mobile (10 digits): 9988776655
Email: meena@example.com
Account type (1=Savings, 2=Current, 3=Fixed Deposit): 3
Opening amount (Rs.): 20000
Opened FixedDepositAccount AC0003 for Meena Joshi, balance Rs.20000
Choose an option: 6
AC0001  SavingsAccount       Asha Patel     Rs.5100
AC0002  CurrentAccount       Ravi Shah      Rs.3000
AC0003  FixedDepositAccount  Meena Joshi    Rs.20000
Choose an option: 9
Accounts saved. Goodbye! Thank you for using MiniBank.
```

## Concepts used

Service layer, generics and reflection (repository), custom annotations, dynamic proxy, serialization, NIO.2,
collections, thread pool and thread-safe collections, JUnit 5, Maven, runnable JAR.

## Navigation

[← Practical 13](../practical-13) · [All practicals](..)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

# Practical 10 - Transaction engine (concurrency)

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To build the transaction engine: a thread pool, a wait/notify producer-consumer and deadlock avoidance by lock ordering. The question of Practical 10 is the same as Practical 9, so the code is the same.

TransactionProcessor runs tasks on Executors.newFixedThreadPool(4). A producer and a consumer share an array buffer and use wait() and notify() inside synchronized code. Two transfers locking accounts in opposite order deadlock; locking the account with the smaller accountNumber first removes the circular wait.

## What this practical covers

- `TransactionProcessor` with `submit()` and `stop()`
- 150 Runnable tasks (deposits and withdrawals) on 4 threads
- Producer-consumer with `wait()` / `notify()`
- A real deadlock, detected, and the lock-ordering fix

## Project structure

```text
practical-10/
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
- Any editor. In VS Code open **this folder** (`practical-10`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-10
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-10
mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
java -cp bin MiniBank
```

> The question of Practical 10 is the same as Practical 9, so this folder contains the same code.

### Build the runnable JAR

```bash
jar --create --file MiniBank.jar --manifest manifest.txt -C bin .
java -jar MiniBank.jar
```

`manifest.txt` sets `Main-Class: MiniBank`. `./build.sh` does all of this in one step.

## Sample output

```text
thread pool: 100 deposits + 50 withdrawals of 100 -> balance 15000 (expected 15000)
---- producer / consumer ----
  produced: Command[type=DEPOSIT, accountNumber=AC0002, amount=100]
  produced: Command[type=WITHDRAW, accountNumber=AC0002, amount=200]
  produced: Command[type=DEPOSIT, accountNumber=AC0002, amount=300]
  produced: Command[type=WITHDRAW, accountNumber=AC0002, amount=400]
  consumed: Command[type=DEPOSIT, accountNumber=AC0002, amount=100]  -> balance 600
  produced: Command[type=DEPOSIT, accountNumber=AC0002, amount=500]
  consumed: Command[type=WITHDRAW, accountNumber=AC0002, amount=200]  -> balance 400
  produced: Command[type=WITHDRAW, accountNumber=AC0002, amount=600]
  consumed: Command[type=DEPOSIT, accountNumber=AC0002, amount=300]  -> balance 700
  consumed: Command[type=WITHDRAW, accountNumber=AC0002, amount=400]  -> balance 300
  consumed: Command[type=DEPOSIT, accountNumber=AC0002, amount=500]  -> balance 800
  consumed: Command[type=WITHDRAW, accountNumber=AC0002, amount=600]  -> balance 200
---- deadlock and fix ----
  DEADLOCK: both threads are stuck (JVM reports 2 deadlocked threads)
  FIXED: both threads finished. AC0005=950, AC0006=1050
```

## Concepts used

ExecutorService, synchronized, wait and notify, deadlock, lock ordering.

## Navigation

[← Practical 9](../practical-9) · [All practicals](..) · [Practical 11 →](../practical-11)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

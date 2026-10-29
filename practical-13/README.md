# Practical 13 - Generic repository

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To make storage reusable with one generic Repository<T> that finds each object's key by reflection through the @Id annotation.

Generics let one class store any type safely. save() uses reflection to find the field marked @Id (Practical 7) and uses its value as the map key, so the same repository works for Account and Customer. @SafeVarargs allows a safe generic varargs method, and a bounded type parameter (N extends Number) limits a generic method to numbers.

## What this practical covers

- `Repository&lt;T&gt;` with `save`, `findById`, `findAll`, `delete`
- Key found by reflection from the `@Id` field
- `@SafeVarargs saveAll(T...)`
- `static &lt;N extends Number&gt; double average(N[])`
- `@Id` also placed on `Customer.customerId`

## Project structure

```text
practical-13/
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
│   │   ├── Repository.java
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
- Any editor. In VS Code open **this folder** (`practical-13`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-13
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-13
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
saveAll saved 3 accounts
findById(AC0002) -> Account[accountNumber=AC0002, ownerName=Ravi Shah, balance=3500]
findById(AC9999) -> null
findAll:
  Account[accountNumber=AC0001, ownerName=Asha Patel, balance=12000]
  Account[accountNumber=AC0003, ownerName=Meena Joshi, balance=50000]
  Account[accountNumber=AC0002, ownerName=Ravi Shah, balance=3500]
  Account[accountNumber=AC0004, ownerName=Kiran Desai, balance=800]
delete(AC0004) -> true, delete again -> false
accounts left: 3
Repository<Customer>.findById(CUST102) -> Customer[CUST102, Ravi Shah, ravi@example.com, 9123456780]
average(Integer[]) = 80.0
average(Double[])  = 3.6666666666666665
average(Long[])    = 21833.333333333332
Repository<String>.save -> String has no field marked with @Id
```

## Concepts used

Generics, reflection, annotations, @SafeVarargs, bounded type parameters, repository pattern.

## Navigation

[← Practical 12](../practical-12) · [All practicals](..) · [Practical 14 →](../practical-14)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

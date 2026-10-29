# Practical 7 - Annotations and reflection

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To define custom annotations (@Id, @Positive, @MaxLength) and read them at run time with reflection to validate any object.

Annotations with RUNTIME retention can be read while the program runs. AnnotationValidator uses getDeclaredFields(), isAnnotationPresent(), setAccessible(true) and field.get() to check the rules and returns the error messages. It walks up the superclasses so the fields of Account are checked for every account type.

## What this practical covers

- `@Override` on every overridden method, `@FunctionalInterface` on `WithdrawRule`
- `@Id`, `@Positive`, `@MaxLength` in `model.annotation`
- Annotations placed on `Account` fields
- `AnnotationValidator.validate(Object)` returning `String[]`

## Project structure

```text
practical-7/
├── .vscode/
│   └── settings.json
├── src/
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
- Any editor. In VS Code open **this folder** (`practical-7`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-7
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-7
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
withdraw 1500 via Transactable -> true, balance = 19000
yearlyInterest = 1045.0 at 5.5%
anonymous class: allow(10000) = true
lambda         : allow(16000) = false
premium instanceof Premium = true
current instanceof Premium = false
total money in bank = Rs.26000
find AC0002 -> Ravi
isValidMobile(9000000001) = true
---- annotation validation ----
errors found: 2
  validation error: ownerName length must be <= 30 (was 47)
  validation error: balance must be > 0 (was -250)
errors for a valid account: 0
```

## Concepts used

Custom annotations, @Target and @Retention, reflection API, @Override.

## Navigation

[← Practical 6](../practical-6) · [All practicals](..) · [Practical 8 →](../practical-8)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

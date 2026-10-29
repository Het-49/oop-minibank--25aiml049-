# Practical 6 - Interfaces, packages and runnable JAR

> **MiniBank** · CEUE203 Object Oriented Programming · Het Patel (25AIML049)

## About

To give MiniBank a clean structure using interfaces, the packages model, util and service, and a runnable JAR.

Transactable and InterestBearing describe account abilities; InterestBearing has a default method yearlyInterest(). WithdrawRule is a functional interface used with an anonymous class and a lambda. Premium is a marker interface. The classes are placed in packages and the JAR manifest sets Main-Class to MiniBank.

## What this practical covers

- Interfaces `Transactable`, `InterestBearing` (default method), `WithdrawRule`, `Premium`
- Anonymous class and lambda for `WithdrawRule`
- Packages `model`, `util`, `service` and one static import
- Runnable JAR built from `manifest.txt` and started with `java -jar`

## Project structure

```text
practical-6/
├── .vscode/
│   └── settings.json
├── src/
│   ├── model/
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
- Any editor. In VS Code open **this folder** (`practical-6`) with *File → Open Folder*; the included
  `.vscode/settings.json` marks `src` as the source folder, so VS Code treats it as a normal Java project.

### Compile and run

macOS / Linux:

```bash
cd practical-6
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin MiniBank
```

Windows (Command Prompt):

```bat
cd practical-6
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
```

## Concepts used

Interfaces, default methods, functional interfaces, lambdas, marker interfaces, packages, JAR.

## Navigation

[← Practical 5](../practical-5) · [All practicals](..) · [Practical 7 →](../practical-7)

## Author

**Het Patel** - Enrollment No. 25AIML049 - CSPIT-AIML

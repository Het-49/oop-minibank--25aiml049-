# oop-minibank-25aiml049

**MiniBank** - a console banking application in Java, built practical by practical for
**CEUE203 - Object Oriented Programming** (CSPIT-AIML).

Student: **Het Patel** · Enrollment No. **25AIML049**

## Repository layout

Every practical is a separate, runnable project. The practicals build on each other, so each folder contains the
earlier work plus what that practical adds.

| Folder | Practical | Topic |
|--------|-----------|-------|
| [practical-1](practical-1) | 1 | Project set-up and menu shell |
| [practical-2](practical-2) | 2 | Customer and Account (encapsulation) |
| [practical-3](practical-3) | 3 | Object methods, nested class and clone |
| [practical-4](practical-4) | 4 | Validation and command parsing |
| [practical-5](practical-5) | 5 | Abstraction and polymorphism |
| [practical-6](practical-6) | 6 | Interfaces, packages and runnable JAR |
| [practical-7](practical-7) | 7 | Annotations and reflection |
| [practical-8](practical-8) | 8 | Custom exceptions |
| [practical-9](practical-9) | 9 | Transaction engine (concurrency) |
| [practical-10](practical-10) | 10 | Transaction engine (concurrency) |
| [practical-11](practical-11) | 11 | Persistence and reporting |
| [practical-12](practical-12) | 12 | Collections framework |
| [practical-13](practical-13) | 13 | Generic repository |
| [practical-14](practical-14) | 14 | Final capstone - BankService and packaging |

Each folder has the same shape:

```text
practical-N/
├── README.md        how to compile and run, sample output
├── src/             the Java source files
├── .vscode/         makes VS Code treat src/ as the source folder
└── .gitignore
```

## How to run a practical

1. Install JDK 17 or newer.
2. Open **one practical folder** in VS Code (*File → Open Folder → practical-N*). Do not open the whole repository at once:
   every practical has its own `MiniBank` class, so they would clash.
3. Follow the *Getting started* section in that folder's `README.md`, for example:

```bash
cd practical-3
mkdir -p bin
javac -d bin src/*.java
java -cp bin MiniBank
```

Practicals 6-14 use packages; their README shows the `find` command and the JAR steps. **Practical 14** is the finished application (`MiniBank.jar`, JUnit tests, `pom.xml`).

## Author

**Het Patel** - 25AIML049

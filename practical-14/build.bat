@echo off
rem Compile, build MiniBank.jar and run it (needs JDK 17+)
if exist bin rmdir /s /q bin
mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
jar --create --file MiniBank.jar --manifest manifest.txt -C bin .
java -jar MiniBank.jar

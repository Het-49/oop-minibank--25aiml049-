#!/bin/sh
# Compile, build MiniBank.jar and run it (needs JDK 17+)
rm -rf bin && mkdir bin
javac -d bin $(find src -name "*.java")
jar --create --file MiniBank.jar --manifest manifest.txt -C bin .
java -jar MiniBank.jar

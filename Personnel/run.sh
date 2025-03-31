#!/bin/bash
echo "Lancement de l'application Personnel..."
cd "$(dirname "$0")"
echo "Compilation du projet..."
mkdir -p bin
javac -d bin -cp "lib/*" src/personnel/*.java src/commandLine/*.java src/jdbc/*.java src/serialisation/*.java
echo "Exécution de l'application..."
java -cp "bin:lib/*" commandLine.PersonnelConsole 
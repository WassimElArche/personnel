#!/bin/bash
echo "Téléchargement des dépendances..."
cd "$(dirname "$0")"
mkdir -p lib

echo "Téléchargement de MySQL Connector/J..."
curl -L "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.16/mysql-connector-java-8.0.16.jar" -o "lib/mysql-connector-java-8.0.16.jar"

echo "Téléchargement de CommandLineMenus..."
curl -L "https://maven.alexandre-mesle.com/maven/CommandLineMenus/CommandLineMenus/2.0/CommandLineMenus-2.0.jar" -o "lib/CommandLineMenus-2.0.jar"

echo "Dépendances téléchargées avec succès!" 
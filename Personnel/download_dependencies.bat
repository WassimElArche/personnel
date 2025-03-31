@echo off
echo Téléchargement des dépendances...
cd %~dp0
mkdir -p lib

echo Téléchargement de MySQL Connector/J...
powershell -Command "(New-Object Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.16/mysql-connector-java-8.0.16.jar', 'lib\mysql-connector-java-8.0.16.jar')"

echo Téléchargement de CommandLineMenus...
powershell -Command "(New-Object Net.WebClient).DownloadFile('https://maven.alexandre-mesle.com/maven/CommandLineMenus/CommandLineMenus/2.0/CommandLineMenus-2.0.jar', 'lib\CommandLineMenus-2.0.jar')"

echo Dépendances téléchargées avec succès!
pause 
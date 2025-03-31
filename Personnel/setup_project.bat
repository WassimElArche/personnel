@echo off
echo Configuration et exécution du projet Personnel...
cd %~dp0

echo Création du dossier bin pour les fichiers compilés...
mkdir bin

echo Téléchargement des dépendances...
call download_dependencies.bat

echo Configuration de la base de données...
echo ATTENTION : Assurez-vous que MySQL est installé et en cours d'exécution.
echo Le script va créer une base de données 'personnel' et les tables nécessaires.
echo.
echo Appuyez sur une touche pour continuer ou CTRL+C pour annuler...
pause > nul

echo Exécution du script SQL...
echo Les commandes suivantes peuvent être exécutées manuellement dans MySQL si l'exécution automatique échoue:
echo 1. Créer une base de données 'personnel'
echo 2. Exécuter le script script_creation_bd.sql

echo.
echo Compilation du projet...
mkdir -p bin
javac -d bin -cp "lib/*;." src/personnel/*.java src/commandLine/*.java src/jdbc/*.java src/serialisation/*.java 2>compilation_errors.txt
type compilation_errors.txt

echo.
echo Exécution de l'application...
echo Utilisez "root" comme identifiant et "toor" comme mot de passe.
java -cp "bin;lib/*;." commandLine.PersonnelConsole
pause 
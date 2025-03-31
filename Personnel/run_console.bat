@echo off
echo Lancement de l'application Personnel...
cd %~dp0

echo Nettoyage complet du repertoire de compilation...
rmdir /S /Q bin 2>nul
del /Q compilation_errors.txt 2>nul

echo Preparation du repertoire...
mkdir bin 2>nul

echo Creation d'une classe JDBC temporaire...
if not exist src\jdbc mkdir src\jdbc
echo package jdbc; > src\jdbc\JDBC.java
echo public class JDBC implements personnel.Passerelle { >> src\jdbc\JDBC.java
echo     public personnel.GestionPersonnel getGestionPersonnel() { return null; } >> src\jdbc\JDBC.java
echo     public void sauvegarderGestionPersonnel(personnel.GestionPersonnel gestionPersonnel) {} >> src\jdbc\JDBC.java
echo     public int insert(personnel.Ligue ligue) { return 0; } >> src\jdbc\JDBC.java
echo     public void update(personnel.Ligue ligue) {} >> src\jdbc\JDBC.java
echo     public int insert(personnel.Employe employe) { return 0; } >> src\jdbc\JDBC.java
echo     public void update(personnel.Employe employe) {} >> src\jdbc\JDBC.java
echo     public void delete(personnel.Employe employe) {} >> src\jdbc\JDBC.java
echo     public void delete(personnel.Ligue ligue) {} >> src\jdbc\JDBC.java
echo } >> src\jdbc\JDBC.java

echo Compilation du projet...
javac -d bin -cp "lib/CommandLineMenus-2.0.jar;." src/personnel/*.java src/commandLine/*.java src/serialisation/*.java src/jdbc/JDBC.java 2>compilation_errors.txt
if %ERRORLEVEL% neq 0 (
    echo Erreur de compilation:
    type compilation_errors.txt
    pause
    exit /b 1
)

echo.
echo Execution de l'application...
java -cp "bin;lib/CommandLineMenus-2.0.jar;." -DPersistenceType=SERIALIZATION commandLine.PersonnelConsole
pause 
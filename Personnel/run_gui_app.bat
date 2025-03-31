@echo off
echo Lancement de l'application Personnel (interface multi-pages)...
cd %~dp0

echo Preparation du repertoire...
mkdir bin 2>nul

echo Compilation du projet...
javac -d bin -cp "lib/*;." src/personnel/*.java src/commandLine/*.java src/jdbc/*.java src/serialisation/*.java src/gui/*.java 2>compilation_errors.txt
type compilation_errors.txt

if %ERRORLEVEL% neq 0 (
    echo La compilation a echoue. Tentative avec des options de compatibilite...
    javac -source 1.8 -target 1.8 -d bin -cp "lib/*;." src/personnel/*.java src/commandLine/*.java src/jdbc/*.java src/serialisation/*.java src/gui/*.java 2>compilation_errors.txt
    type compilation_errors.txt
)

echo.
echo Execution de l'application en mode serialisation...
java -cp "bin;lib/*;." -DPersistenceType=SERIALIZATION gui.MainApplication
pause 
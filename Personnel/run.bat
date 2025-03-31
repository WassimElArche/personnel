@echo off
echo Lancement de l'application Personnel (interface multi-pages)...

REM Vérification de la présence de Java
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Java n'est pas trouve dans le PATH.
    echo Veuillez installer Java et reessayer.
    pause
    exit /b 1
)

REM Préparation du répertoire
echo Preparation du repertoire...
mkdir bin 2>nul

REM Compilation du projet
echo Compilation du projet...
javac -d bin -cp "lib/*;." src/commandLine/*.java src/gui/*.java src/personnel/*.java src/jdbc/*.java src/serialisation/*.java 2>compilation_errors.txt
if %ERRORLEVEL% neq 0 (
    echo La compilation a echoue.
    type compilation_errors.txt
    pause
    exit /b 1
)

echo.
echo Execution de l'application en mode serialisation...
java -cp "bin;lib/*;." -DPersistenceType=SERIALIZATION gui.MainApplication
pause 
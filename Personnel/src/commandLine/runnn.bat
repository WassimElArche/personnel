

set /p username=Entrez le nom d'utilisateur MySQL: 
set /p password=Entrez le mot de passe MySQL: 



mysql -u root -p 1007200 < temp_update.sql
if %ERRORLEVEL% == 0 (
  echo Base de donnees mise a jour avec succes!
) else (
  echo ERREUR: Echec de la mise a jour de la base de donnees.
  goto end
)

echo.
echo 2. Compilation du projet...
mkdir -p bin
javac -d bin -cp "lib/*;." src/personnel/*.java src/commandLine/*.java src/jdbc/*.java src/serialisation/*.java 2>compilation_errors.txt
type compilation_errors.txt

echo.
echo 3. Execution des tests unitaires...
javac -d bin -cp "lib/*;bin;." src/testsUnitaires/TestNumeroSecuriteSociale.java
java -cp "bin;lib/*" org.junit.platform.console.ConsoleLauncher --scan-classpath --select-class=testsUnitaires.TestNumeroSecuriteSociale

echo.
echo 4. Demarrage de l'application...
echo Utilisez "root" comme identifiant et "toor" comme mot de passe.
java -cp "bin;lib/*;." commandLine.PersonnelConsole

:end
del temp_update.sql
echo.
echo Implementation terminee.
pause
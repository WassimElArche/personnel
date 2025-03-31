# Application de Gestion du Personnel

Cette application permet de gérer le personnel des ligues sportives.

## Prérequis

- Java 8 ou supérieur
- MySQL

## Installation

1. **Base de données**
   - Installer MySQL si ce n'est pas déjà fait
   - Créer la base de données en exécutant le script SQL fourni :
     ```
     mysql -u root -p < script_creation_bd.sql
     ```
   - Vérifier que le fichier `src/jdbc/Credentials.java` contient les bonnes informations de connexion

2. **Installation des bibliothèques**
   - Créer un dossier `lib` à la racine du projet
   - Télécharger et placer dans ce dossier :
     - [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
     - [CommandLineMenus](https://maven.alexandre-mesle.com/maven/CommandLineMenus/CommandLineMenus/2.0/CommandLineMenus-2.0.jar)

## Exécution

### Sous Windows
- Double-cliquer sur `run.bat`
- Ou ouvrir une invite de commande et exécuter `run.bat`

### Sous Linux/Mac
- Donner les droits d'exécution au script : `chmod +x run.sh`
- Exécuter le script : `./run.sh`


## Structure du projet
- `src/personnel` : Classes métier
- `src/commandLine` : Interface en ligne de commande
- `src/jdbc` : Connexion à la base de données
- `src/serialisation` : Persistance des données via sérialisation 
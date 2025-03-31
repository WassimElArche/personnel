-- Création de la base de données
DROP DATABASE IF EXISTS personnel;
CREATE DATABASE personnel;
USE personnel;

-- Création des tables
CREATE TABLE ligue (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE employe (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    mail VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    dateArrivee DATE NOT NULL,
    dateDepart DATE,
    admin BOOLEAN NOT NULL DEFAULT FALSE,
    numeroSecuriteSociale VARCHAR(15) UNIQUE,
    ligue_id INT,
    FOREIGN KEY (ligue_id) REFERENCES ligue(id) ON DELETE CASCADE
);

CREATE TABLE root (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Création de l'utilisateur root pour la connexion
INSERT INTO root (nom, password) VALUES ('root', 'toor');

-- Création d'un nouvel utilisateur MySQL avec des droits limités
CREATE USER 'personnel_user'@'localhost' IDENTIFIED BY 'Personnel2024!';
GRANT SELECT, INSERT, UPDATE, DELETE ON personnel.* TO 'personnel_user'@'localhost';
FLUSH PRIVILEGES;

-- Commentaire explicatif
-- Pour vous connecter à cette base de données, utilisez les paramètres suivants :
-- URL: jdbc:mysql://localhost:3306/personnel
-- Utilisateur: personnel_user
-- Mot de passe: Personnel2024! 
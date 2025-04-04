
CREATE DATABASE projet_;


CREATE TABLE IF NOT EXISTS user ("
					+ "id_user	INTEGER NOT NULL,"
					+ "name_user	TEXT NOT NULL,"
					+ "password_user	TEXT NOT NULL,"
					+ "PRIMARY KEY(id_user AUTOINCREMENT)"
					+ ");

CREATE TABLE IF NOT EXISTS Professeur (
    idProfesseur INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    prenom TEXT NOT NULL,
    tel INTEGER NOT NULL UNIQUE,
    mail TEXT NOT NULL UNIQUE,
    matiere TEXT NOT NULL -- discipline
);

CREATE TABLE IF NOT EXISTS Salle (
    idSalle INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    capacite INTEGER NOT NULL CHECK (capacite > 0)
);

CREATE TABLE IF NOT EXISTS anneScolaire(
    idAnneeScolaire INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    anneScolaire VARCHAR(9) NOT NULL  -- 2021/2022
);

CREATE TABLE IF NOT EXISTS Classe (
    idClasse INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    cycle TEXT NOT NULL  -- premier/second cylce
);

CREATE TABLE IF NOT EXISTS Matiere(
	idMatiere INTEGER NOT NULL,
	nomMatiere VARCHAR(20) NOT NULL,
    idclasse INTEGER NOT NULL,
    nbrHeures INTEGER NOT NULL CHECK (nbrHeures > 0),
    FOREIGN KEY (idClasse) REFERENCES Classe(idClasse),
	PRIMARY KEY(idMatiere AUTOINCREMENT)
);


CREATE TABLE IF NOT EXISTS Cours (
    idCours INTEGER PRIMARY KEY AUTOINCREMENT,
    jour DATE NOT NULL,
    heureDebut TIME NOT NULL,
    heureFin TIME NOT NULL,
    idProfesseur INTEGER NOT NULL,
    idSalle INTEGER NOT NULL,
    idClasse INTEGER NOT NULL,
    idMatiere INTERGER NOT NULL,
    FOREIGN KEY (idProfesseur) REFERENCES Professeur(idProfesseur),
    FOREIGN KEY (idSalle) REFERENCES Salle(idSalle),
    FOREIGN KEY (idClasse) REFERENCES Classe(idClasse),
    FOREIGN KEY (idMatiere) REFERENCES  Matiere(idMatiere) -- un cour correspond a une matiere
    CHECK (heureDebut < heureFin)
);

CREATE TABLE IF NOT EXISTS EmploiTempsProfesseur (
    idEmploiTempsProf INTEGER PRIMARY KEY AUTOINCREMENT,
    idProfesseur INTEGER NOT NULL,
    idAnneeScolaire INTEGER NOT NULL,
    classesParJour INTEGER NOT NULL CHECK (classesParJour >= 0),
    heuresParClasse INTEGER NOT NULL CHECK (heuresParClasse > 0),
    heuresTotalSemaine INTEGER NOT NULL CHECK (heuresTotalSemaine >= 0),
    FOREIGN KEY (idProfesseur) REFERENCES Professeur(idProfesseur),
    FOREIGN KEY (idAnneeScolaire) REFERENCES anneScolaire(idAnneeScolaire) -- un emploie de temps correspond a une année
);

CREATE TABLE IF NOT EXISTS EmploiTempsClasse (
    idEmploiTempsClasse INTEGER PRIMARY KEY AUTOINCREMENT,
    idClasse INTEGER NOT NULL,
    idAnneeScolaire INTEGER NOT NULL,
    lundiCours TEXT,      -- Liste des identifiants des cours sous forme de texte, séparés par des virgules
    mardiCours TEXT,
    mercrediCours TEXT,
    jeudiCours TEXT,
    vendrediCours TEXT,
    FOREIGN KEY (idClasse) REFERENCES Classe(idClasse), -- apartir de la on a acces au cyle
    FOREIGN KEY (idAnneeScolaire) REFERENCES anneScolaire(idAnneeScolaire)
);


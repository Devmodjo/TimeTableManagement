package DBManager;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBManager {
	
	public static Connection connect() {
		
		Connection con = null;
		
		try {
			
			// chargement du pilotes
			Class.forName("org.sqlite.JDBC"); 
			// connection à la base de donnée
			con = DriverManager.getConnection("jdbc:sqlite:config_");
			// Statement permet d'executée des requete statique (ex : creation d'une table) -> un peu comme un cursor
			Statement stmt = con.createStatement();
			
			// creation de la table utilisateurs
			String userTable = "CREATE TABLE IF NOT EXISTS user ("
					+ "id_user	INTEGER NOT NULL ,"
					+ "name_user	TEXT NOT NULL,"
					+ "password_user	TEXT NOT NULL,"
					+ "userStatus TEXT NOT NULL,"
					+ "PRIMARY KEY(id_user AUTOINCREMENT)"
					+ ");";
			stmt.execute(userTable);
			
			// creation de la table matiere
			String matiere = "CREATE TABLE IF NOT EXISTS matiere("
					+ "idMatiere INTEGER NOT NULL,"
					+ "nom_matiere VARCHAR(20) NOT NULL,"
					+ "PRIMARY KEY(idMatiere AUTOINCREMENT)"
					+ ");";
			stmt.execute(matiere);
			
			// creation de la tables classe
			String classTable = "CREATE TABLE IF NOT EXISTS Classe ("
					+ "    idClasse INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "    nom TEXT NOT NULL,"
					+ "    cycle TEXT NOT NULL"
					+ ");";
			stmt.execute(classTable);
			
			// creation de la table de cour
			String courTable = "CREATE TABLE IF NOT EXISTS Cours ("
					+ "    idCours INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "    jour DATE NOT NULL,"
					+ "    heureDebut TIME NOT NULL,"
					+ "    heureFin TIME NOT NULL,"
					+ "    idProfesseur INTEGER NOT NULL,"
					+ "    idSalle INTEGER NOT NULL,"
					+ "    idClasse INTEGER NOT NULL,"
					+ "    idMatiere INTERGER NOT NULL,"
					+ "    FOREIGN KEY (idProfesseur) REFERENCES Professeur(idProfesseur),"
					+ "    FOREIGN KEY (idSalle) REFERENCES Salle(idSalle),"
					+ "    FOREIGN KEY (idClasse) REFERENCES Classe(idClasse),"
					+ "    FOREIGN KEY (idMatiere) REFERENCES  Matiere(idMatiere),"
					+ "	   CHECK (heureDebut < heureFin) "
					+ ");";
			stmt.execute(courTable);
			
			
			// creation de la table classroom(salle de classe)
			String salleTable = "CREATE TABLE IF NOT EXISTS Salle ("
					+ "    idSalle INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "    nom TEXT NOT NULL,\r\n"
					+ "    capacite INTEGER NOT NULL CHECK (capacite > 0)"
					+ ")";
			stmt.execute(salleTable);
			
			// creation de la tables professeur
			String profTable = "CREATE TABLE IF NOT EXISTS Professeur ("
					+ "    idProfesseur VARCHAR(9) UNIQUE,"
					+ "    nom TEXT NOT NULL,"
					+ "    prenom TEXT NOT NULL,"
					+ "	   tel INTEGER NOT NULL UNIQUE,"
					+ "    mail TEXT NOT NULL UNIQUE,"
					+ "    discipline TEXT NOT NULL,"
					+ "	   grade TEXT NOT NULL,"
					+ "    sexe TEXT NOT NULL "
					+ ");";
			stmt.execute(profTable);
			
			
			// creation de la table anneScolaire
			String anneeScolaire = "CREATE TABLE IF NOT EXISTS anneScolaire("
					+ "    idAnneeScolaire INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
					+ "    anneScolaire VARCHAR(9) NOT NULL"
					+ ");";
			stmt.execute(anneeScolaire);
			
			// creation de la table emploie de temps classe
			String emploieTempClasse = "CREATE TABLE IF NOT EXISTS EmploiTempsClasse ("
					+ "    idEmploiTempsClasse INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "    idClasse INTEGER NOT NULL,"
					+ "    idAnneeScolaire INTEGER NOT NULL,"
					+ "    lundiCours TEXT,   "
					+ "    mardiCours TEXT,"
					+ "    mercrediCours TEXT,"
					+ "    jeudiCours TEXT,"
					+ "    vendrediCours TEXT,"
					+ "    FOREIGN KEY (idClasse) REFERENCES Classe(idClasse),"
					+ "    FOREIGN KEY (idAnneeScolaire) REFERENCES anneScolaire(idAnneeScolaire)"
					+ ");";
			stmt.execute(emploieTempClasse);
			
			
			// creation de la table emploie de temps professeur
			String emploieTempsProf = "CREATE TABLE IF NOT EXISTS EmploiTempsProfesseur ("
					+ "    idEmploiTempsProf INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "    idProfesseur INTEGER NOT NULL,"
					+ "    idAnneeScolaire INTEGER NOT NULL,"
					+ "    classesParJour INTEGER NOT NULL CHECK (classesParJour >= 0),"
					+ "    heuresParClasse INTEGER NOT NULL CHECK (heuresParClasse > 0),"
					+ "    heuresTotalSemaine INTEGER NOT NULL CHECK (heuresTotalSemaine >= 0),"
					+ "    FOREIGN KEY (idProfesseur) REFERENCES Professeur(idProfesseur),"
					+ "    FOREIGN KEY (idAnneeScolaire) REFERENCES anneScolaire(idAnneeScolaire) "
					+ ");";
			stmt.execute(emploieTempsProf);
			
			
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return con;
	}

}

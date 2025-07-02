# 🕒 Application de Gestion des Emplois du Temps - JavaFX

## 📌 Description

Cette application de bureau développée en **JavaFX** avec **SQLite** comme base de données locale permet de **gérer efficacement les emplois du temps scolaires** d'un établissement. Elle propose une gestion centralisée des utilisateurs (administrateurs, professeurs, responsables des emplois du temps) avec **interfaces personnalisées selon les rôles**, une **authentification sécurisée**, une **gestion des matières, classes, emplois du temps**, et des **exports en PDF et Excel**.

<p align="center">
  <img src="https://raw.githubusercontent.com/Devmodjo/rapport-soutenance-terminal/main/enregistrementEmploiTempsClasse.png" alt="Interface Admin" width="600">
</p>


---

## 🛠️ Fonctionnalités principales

### ✅ Authentification des utilisateurs
- Connexion sécurisée avec vérification des identifiants
- Interfaces chargées dynamiquement selon le **grade** de l’utilisateur :
  - `Administrateur`
  - `Responsable des emplois du temps`
  - `Professeur`

### 👩‍🏫 Espace Administrateur
- Gestion des comptes utilisateurs
- Ajout/modification/suppression des utilisateurs
- Attribution de rôles (admin, professeur, responsable)

### 🗓️ Espace Responsable des emplois du temps
- Création de matières liées à chaque classe
- Enregistrement des classes et des horaires
- Gestion des salles
- Édition visuelle des emplois du temps hebdomadaires
- **Export PDF** et **génération de fichiers Excel** pour impression

### 👨‍🏫 Espace Professeur
- Consultation des emplois du temps personnels
- Accès restreint aux fonctions lecture uniquement

---

## 🧑‍💻 Technologies utilisées

- **Java JDK 23**
- **JavaFX**
- **SQLite**
- **Scene Builder**
- **iTextPDF**
- **Apache POI** (optionnel pour l’export Excel)
- **JPackage** (création d’un installateur Windows)

---

## 📂 Arborescence du projet

📁 src/
┣ 📁 SceneController/ → Contrôleurs JavaFX (Login, Admin, Emplois du temps…)
┣ 📁 application/FXML_FILES/ → Interfaces FXML
┣ 📁 DBManager/ → Classe de gestion SQLite (DBManager.java)
┣ 📁 ObjectModel/ → Modèles Java (Matiere, Classe, Utilisateur…)
┗ 📁 util/ → Classe utilitaire (UserView.java, DialogBox.java)


---

## ⚙️ Installation & Configuration (Eclipse)

### ✅ Prérequis

- Java JDK **8 ou supérieur**
- Eclipse IDE installé
- JavaFX SDK (OpenJFX) téléchargé
- [iTextPDF 5.5.13.3](https://github.com/itext/itextpdf/releases/tag/5.5.13.3)
- Optionnel : [Apache POI pour l’export Excel](https://archive.apache.org/dist/poi/release/bin/poi-bin-3.9-20121203.zip )

### 📦 Étapes

1. **Cloner le projet**
   ```bash
   git clone https://github.com/Devmodjo/TimeTableManagement.git

2. **Importer dans Eclipse**
   - File → Import → Existing Projects into Workspace
   - Sélectionner le dossier cloné
     
3. **Configurer JavaFX dabs Eclipse**
   - Télécharger [JavaFX SDK](https://openjfx.io)
   - Ajouter --module-path "chemin_vers_javafx/lib" --add-modules javafx.controls,javafx.fxml dans les VM Arguments
   - Ajouter les fichiers .jar de JavaFX dans le Build Path
     
4. **Ajouter les bibliothèques externes**
   - iTextPDF-5.5.13.3.jar → pour la génération PDF
   - Apache POI → pour la generation du fichier excel
   - sqlite-jdbc-3.42.0.1.jar → pour connecter la base de donnée SQLite
     
5. **Lancer l'application**
   - Ouvrir Main.java
   - Cliquer sur "Run"
  
## 🖨️ Impression et export
- 📄 Export PDF de l’emploi du temps hebdomadaire
- 📊 Export Excel (si Apache POI est activé)

## 📋 Base de données
  Le fichier config_(base de donnée) est automatiquement généré avec les tables suivantes :

- Utilisateur(id, nom, motDePasse, grade)
- Classe(id, nom)
- Matiere(id, nom_matiere, nom_classe)
- Salle(id, nom, capacite)
- EmploiTemps(id, idClasse, idProfesseur, jour, heureDebut, heureFin, idMatiere, idSalle)
- un compte administrateur est disponible par defaut(username: `root`, password: `root`)

## ✍️ Auteur
 *👨‍💻 Kamsu Modjo Victor Yvan
* 🎓 Développeur Full-Stack Junior
* 📍 Basé au Cameroun
* 📧 Contact : yvankamsu88@gmail.com
* 🔗 [LinkedIn](https://www.linkedin.com/in/victor-modjo-5933162a3/)

## 🚀 Objectif
Ce projet a été réalisé dans le cadre d’un projet de fin de formation en en Classe de Terminale TI, avec pour objectif d’apporter une solution simple, efficace et moderne à la gestion des emplois du temps dans les établissements scolaires.
*ce projet comporte egalement un rapport Rédiger avec le Language de programmation LateX disponible [ici](https://github.com/Devmodjo/rapport-soutenance-terminal)* 

 ## ⭐ Recruteurs / Collaborateurs
*Je suis à la recherche d'opportunités pour approfondir mes compétences en développement Java, Spring Boot, ReactJS, et gestion de projet.
N'hésitez pas à me contacter ou à suivre ce dépôt pour découvrir mon travail !*

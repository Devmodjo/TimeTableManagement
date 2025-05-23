package SceneController.timeTableController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import java.util.List;
import java.util.Optional;

import DBManager.DBManager;
import util.DialogBox;
import util.EmailValidator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class TimeTableClasseController {
    @FXML
    private ComboBox<String> classname;
    @FXML
    private Label hours_period_1;
    @FXML
    private Label hours_period_2;
    @FXML
    private Label hours_period_4;
    @FXML
    private Label hours_periode_3;
    @FXML
    private Label hours_periode_5;
    @FXML
    private Label hours_periode_6;
    @FXML
    private Label hours_periode_7;
    @FXML
    private ComboBox<String> jeudiCourPeriode1;
    @FXML
    private ComboBox<String> jeudiCourPeriode2;
    @FXML
    private ComboBox<String> jeudiCourPeriode3;
    @FXML
    private ComboBox<String> jeudiCourPeriode4;
    @FXML
    private ComboBox<String> jeudiCourPeriode5;
    @FXML
    private ComboBox<String> jeudiCourPeriode6;
    @FXML
    private ComboBox<String> jeudiCourPeriode7;
    @FXML
    private ColumnConstraints jeudiCoursColumn;
    @FXML
    private Button loadComponent;
    @FXML
    private ComboBox<String> lundiCourPeriode1;
    @FXML
    private ComboBox<String> lundiCourPeriode3;
    @FXML
    private ComboBox<String> lundiCourPeriode4;
    @FXML
    private ComboBox<String> lundiCourPeriode5;
    @FXML
    private ComboBox<String> lundiCourPeriode6;
    @FXML
    private ComboBox<String> lundiCourPeriode7;
    @FXML
    private ColumnConstraints lundiCoursColumn;
    @FXML
    private ComboBox<String> lundiCoursPeriode2;
    @FXML
    private ComboBox<String> mardiCourPeriode1;
    @FXML
    private ComboBox<String> mardiCourPeriode2;
    @FXML
    private ComboBox<String> mardiCourPeriode3;
    @FXML
    private ComboBox<String> mardiCourPeriode4;
    @FXML
    private ComboBox<String> mardiCourPeriode5;
    @FXML
    private ComboBox<String> mardiCourPeriode6;
    @FXML
    private ComboBox<String> mardiCourPeriode7;
    @FXML
    private ColumnConstraints mardiCoursColumn;
    @FXML
    private ComboBox<String> mercrediCourPeriode1;
    @FXML
    private ComboBox<String> mercrediCourPeriode2;
    @FXML
    private ComboBox<String> mercrediCourPeriode3;
    @FXML
    private ComboBox<String> mercrediCourPeriode4;
    @FXML
    private ComboBox<String> mercrediCourPeriode5;
    @FXML
    private ComboBox<String> mercrediCourPeriode6;
    @FXML
    private ComboBox<String> mercrediCourPeriode7;
    @FXML
    private ColumnConstraints mercrediCoursColumn;
    @FXML
    private RowConstraints periode_1;
    @FXML
    private RowConstraints periode_2;
    @FXML
    private RowConstraints periode_3;
    @FXML
    private RowConstraints periode_4;
    @FXML
    private RowConstraints periode_5;
    @FXML
    private RowConstraints periode_6;
    @FXML
    private RowConstraints periode_7;
    @FXML
    private Button saveTimesTable;
    @FXML
    private TextField schoolYear;
    @FXML
    private ComboBox<String> vendrediCourPeriode1;
    @FXML
    private ComboBox<String> vendrediCourPeriode2;
    @FXML
    private ComboBox<String> vendrediCourPeriode3;
    @FXML
    private ComboBox<String> vendrediCourPeriode4;
    @FXML
    private ComboBox<String> vendrediCourPeriode5;
    @FXML
    private ComboBox<String> vendrediCourPeriode6;
    @FXML
    private ComboBox<String> vendrediCourPeriode7;
    @FXML
    private ColumnConstraints vendrediCoursColumn;
    
    public static String[] matOfMonday = new String[7];
    public static String[] matOfTuesday = new String[7];
    public static String[] matOfWednesday = new String[7];
    public static String[] matOfThursday = new String[7];
    public static String[] matOfFriday = new String[7];
    
    
   @FXML
   private void initialize() {
	   classname.getItems().addAll(someClassroom());  
	   loadComponent.setOnAction(evnt->loadComponent());
	   saveTimesTable.setOnAction(evnt->{
		try {
			saveTimeTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	});
   }
   
   public List<String> someClassroom(){
	   List<String> someclasse = new ArrayList<>();
       String sql = "SELECT DISTINCT nom FROM Classe";
       
       try (Connection con = DBManager.connect();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
           
           while (rs.next()) {
               someclasse.add(rs.getString("nom"));
           }
       } catch (Exception e) {
          new DialogBox().errorAlertBox("ERROR", e.getMessage());
       }
       return someclasse;
	   
   }
   
   @FXML
   public void loadComponent() {
       String selectedClass = classname.getValue();
       
       if (selectedClass == null || selectedClass.isEmpty()) {
           new DialogBox().errorAlertBox("Classe non sélectionnée", "Veuillez d'abord sélectionner une classe.");
           return;
       }

       // Récupère les matières pour la classe sélectionnée
       List<String> matieres = new ArrayList<>();
       String sql = "SELECT nom_matiere FROM matiere WHERE nom_classe =?";

       try (Connection con = DBManager.connect();
            PreparedStatement stmt = con.prepareStatement(sql)) {
           
           stmt.setString(1, selectedClass);
           ResultSet rs = stmt.executeQuery();

           while (rs.next()) {
               matieres.add(rs.getString("nom_matiere"));
           }

       } catch (SQLException e) {
           new DialogBox().errorAlertBox("Erreur", "Erreur lors de la récupération des matières : " + e.getMessage());
           return;
       }

       // Remplit toutes les ComboBox des jours avec les matières récupérées
       
       @SuppressWarnings("unchecked")
       ComboBox<String>[] comboBoxes = new ComboBox[]{
           lundiCourPeriode1, lundiCoursPeriode2, lundiCourPeriode3, lundiCourPeriode4, lundiCourPeriode5, lundiCourPeriode6, lundiCourPeriode7,
           mardiCourPeriode1, mardiCourPeriode2, mardiCourPeriode3, mardiCourPeriode4, mardiCourPeriode5, mardiCourPeriode6, mardiCourPeriode7,
           mercrediCourPeriode1, mercrediCourPeriode2, mercrediCourPeriode3, mercrediCourPeriode4, mercrediCourPeriode5, mercrediCourPeriode6, mercrediCourPeriode7,
           jeudiCourPeriode1, jeudiCourPeriode2, jeudiCourPeriode3, jeudiCourPeriode4, jeudiCourPeriode5, jeudiCourPeriode6, jeudiCourPeriode7,
           vendrediCourPeriode1, vendrediCourPeriode2, vendrediCourPeriode3, vendrediCourPeriode4, vendrediCourPeriode5, vendrediCourPeriode6, vendrediCourPeriode7
       };
       List<ComboBox<String>> allComboBoxes = Arrays.asList(comboBoxes);
       
       //ComboBox<String> cb = new ComboBox<String>(); 
       // Nettoie et ajoute les matières à chaque ComboBox
       for (ComboBox<String> cb : allComboBoxes) {
           Optional.ofNullable(cb).ifPresent(c->{
        	   cb.getItems().clear();
               cb.getItems().addAll(matieres);
           });
       }

       new DialogBox().infoAlertBox("Chargement terminé", "Les matières ont été chargées avec succès pour la classe " + selectedClass + ".");
       saveTimesTable.setDisable(false);
   }
   
   public void saveTimeTable() throws SQLException {
	   
	   try(Connection db = DBManager.connect();){
		   // create table for all days comoboBoxes
		   
		   @SuppressWarnings("unchecked")
		   ComboBox<String>[] mondayComboBoxes = new ComboBox[] {
					   lundiCourPeriode1, lundiCoursPeriode2, lundiCourPeriode3, lundiCourPeriode4, lundiCourPeriode5, lundiCourPeriode6, lundiCourPeriode7
		   };
		   int index1 = 0;
		   for(ComboBox<String> cb : mondayComboBoxes) {
			   // get value foreach comboBoxes for add in array
			   if(cb.getValue() !=null) {
				   matOfMonday[index1] = cb.getValue().toString();
			   }else {
				   matOfMonday[index1] = " ";
			   }
			   index1++;
			   
		   }
		    
		   
		   @SuppressWarnings("unchecked")
		   ComboBox<String>[] tuesdayComboBoxes = new ComboBox[] {
				   mardiCourPeriode1, mardiCourPeriode2, mardiCourPeriode3, mardiCourPeriode4, mardiCourPeriode5, mardiCourPeriode6, mardiCourPeriode7
		   };
		   int index2 = 0;
		   for(ComboBox<String> cb : tuesdayComboBoxes) {
			   // get value foreach comboBoxes for add in array
			   if(cb.getValue() !=null) {
				   matOfTuesday[index2] = cb.getValue().toString();
			   }else {
				   matOfTuesday[index2] = " ";
			   }
			   index2++;
			   
		   }
		   
		   @SuppressWarnings("unchecked")
		   ComboBox<String>[] wednesdayComboBoxes = new ComboBox[] {
				   mercrediCourPeriode1, mercrediCourPeriode2, mercrediCourPeriode3, mercrediCourPeriode4, mercrediCourPeriode5, mercrediCourPeriode6, mercrediCourPeriode7
		   };
		   int index3 = 0;
		   for(ComboBox<String> cb : wednesdayComboBoxes) {
			   // get value foreach comboBoxes for add in array
			   if(cb.getValue() !=null) {
				   matOfWednesday[index3] = cb.getValue().toString();
			   }else {
				   matOfWednesday[index3] = " ";
			   }
			   index3++;
			   
		   }
		   
		   @SuppressWarnings("unchecked")
		   ComboBox<String>[] thursdayComboBoxes = new ComboBox[] {
				   jeudiCourPeriode1, jeudiCourPeriode2, jeudiCourPeriode3, jeudiCourPeriode4, jeudiCourPeriode5, jeudiCourPeriode6, jeudiCourPeriode7
		   };
		   int index4 = 0;
		   for(ComboBox<String> cb : thursdayComboBoxes) {
			   // get value foreach comboBoxes for add in array
			   if(cb.getValue() != null) {
				   matOfThursday[index4] = cb.getValue().toString();
			   }else {
				   matOfThursday[index4] = " ";
			   }
			   index4++;
			   
		   }
		   
		   @SuppressWarnings("unchecked")
		   ComboBox<String>[] fridayComboBoxes = new ComboBox[] {
				   vendrediCourPeriode1, vendrediCourPeriode2, vendrediCourPeriode3, vendrediCourPeriode4, vendrediCourPeriode5, vendrediCourPeriode6, vendrediCourPeriode7
		   };
		   int index5 = 0;
		   for(ComboBox<String> cb : fridayComboBoxes) {
			   // get value foreach comboBoxes for add in array
			   if(cb.getValue() != null) {
				   matOfFriday[index5] = cb.getValue().toString();
			   }else {
				   matOfFriday[index5] = " ";
			   }
			   index5++;
			   
		   }
		   
		   // le pentagone en chaleur
		   String[][] week = {matOfMonday, matOfTuesday, matOfWednesday, matOfThursday, matOfFriday};
		   if(schoolYear.getText() != null && EmailValidator.isValidSchoolYears(schoolYear.getText())) {
			   // inclusion de la fonctiond'insertion d'emploie de temps ici
			   insertEmploiTemps(classname.getValue().toString(), week, schoolYear.getText() ,db);
			   new DialogBox().infoAlertBox("SUCCESS", "nouvelle emploie de temps enregistrer avec success");
			   
		   }else {
			   new DialogBox().errorAlertBox("ERREUR", "verifier les format de l'année scolaire");
			   return;
		   }
		
	   }
   }
   
	public void insertEmploiTemps(String className, String[][] week, String schoolYear,Connection db) throws SQLException{
	    // Convertir chaque ligne du tableau en une chaîne de caractères
	    String lundi = String.join(";", week[0]);
	    String mardi = String.join(";", week[1]);
	    String mercredi = String.join(";", week[2]);
	    String jeudi = String.join(";", week[3]);
	    String vendredi = String.join(";", week[4]);
	    
	    // requete pour selectionnez l'identifiant d'une classe
	    String sql0 = "SELECT idClasse FROM Classe WHERE nom=?";
	    PreparedStatement pstmt1 = db.prepareStatement(sql0);
		pstmt1.setString(1, className);
		ResultSet res1 = pstmt1.executeQuery();
		int idClass = res1.getInt("idClasse");
		
	    // Requête SQL pour insérer l'emploi du temps
	    String sql = "INSERT INTO EmploiTempsClasse (idClasse, lundiCours, mardiCours, mercrediCours, jeudiCours, vendrediCours, anneeScolaire) VALUES (?, ?, ?, ?, ?, ?,?)";

	    try (PreparedStatement pstmt = db.prepareStatement(sql)) {
	        pstmt.setInt(1, idClass);
	        pstmt.setString(2, lundi);
	        pstmt.setString(3, mardi);
	        pstmt.setString(4, mercredi);
	        pstmt.setString(5, jeudi);
	        pstmt.setString(6, vendredi);
	        pstmt.setString(7, schoolYear);

	        // Exécuter l'insertion
	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Emploi du temps inséré avec succès !");
	        } else {
	            System.out.println("Erreur lors de l'insertion !");
	        }
	    } catch (SQLException e) {
	        System.out.println("Erreur SQL : " + e.getMessage());
	    }finally {
	    	pstmt1.close();
	    	res1.close();
	    }
	}


}

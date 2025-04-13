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
    
    
    
   @FXML
   private void initialize() {
	   classname.getItems().addAll(someClassroom());  
	   loadComponent.setOnAction(evnt->loadComponent());
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
		   
		   @SuppressWarnings("unchecked")
		   ComboBox<String>[] tuesdayComboBoxes = new ComboBox[] {
				   mardiCourPeriode1, mardiCourPeriode2, mardiCourPeriode3, mardiCourPeriode4, mardiCourPeriode5, mardiCourPeriode6, mardiCourPeriode7
		   };
		   
		   @SuppressWarnings("unchecked")
		   ComboBox<String>[] wednesdayComboBoxes = new ComboBox[] {
				   mercrediCourPeriode1, mercrediCourPeriode2, mercrediCourPeriode3, mercrediCourPeriode4, mercrediCourPeriode5, mercrediCourPeriode6, mercrediCourPeriode7
		   };
		   
		   @SuppressWarnings("unchecked")
		   ComboBox<String>[] thursdayComboBoxes = new ComboBox[] {
				   jeudiCourPeriode1, jeudiCourPeriode2, jeudiCourPeriode3, jeudiCourPeriode4, jeudiCourPeriode5, jeudiCourPeriode6, jeudiCourPeriode7
		   };
		   
		   @SuppressWarnings("unchecked")
		   ComboBox<String>[] fridayComboBoxes = new ComboBox[] {
				   vendrediCourPeriode1, vendrediCourPeriode2, vendrediCourPeriode3, vendrediCourPeriode4, vendrediCourPeriode5, vendrediCourPeriode6, vendrediCourPeriode7
		   };
		   
		
	   }
   }


}

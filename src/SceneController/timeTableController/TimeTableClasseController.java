package SceneController.timeTableController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import java.util.List;
import DBManager.DBManager;
import util.DialogBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private ComboBox<String> jeudiCourPeriode;
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
    private ComboBox<String> vendrediCourPeriode;
    @FXML
    private ComboBox<String> vendrediCourPeriode6;
    @FXML
    private ComboBox<String> vendrediCourPeriode7;
    @FXML
    private ColumnConstraints vendrediCoursColumn;
    
   @FXML
   private void initialize() {
	   classname.getItems().addAll(someClassroom());
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
   
   public void loadComponent() {
	   
   }

}

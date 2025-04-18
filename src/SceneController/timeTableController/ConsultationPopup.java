package SceneController.timeTableController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import util.LigneEmploiTemps;

public class ConsultationPopup {

    @FXML
    private TableColumn<?, ?> numberTimeTableColumn;

    @FXML
    private Button printButton;

    @FXML
    private TableColumn<?, ?> schoolYearColumn;

    @FXML
    private TextField searbarClasse;

    @FXML
    private TableView<?> viewTimeTableClasse;
    
    private LigneEmploiTemps ligne;

    public void setLigneEmploiTemps(LigneEmploiTemps ligne) {
        this.ligne = ligne;
        System.out.println("Classe sélectionnée : " + ligne.getClasse());
       
    }

    @FXML
    public void initialize() {
        
    }

}
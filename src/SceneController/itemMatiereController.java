package SceneController;



import ObjectModel.Matiere;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class itemMatiereController {

    @FXML
    private Label nomMatiere;
    
    @SuppressWarnings("unused")
	private Matiere matiere;
    
    
    public void setData(Matiere matiere) {
    	this.matiere = matiere;
    	nomMatiere.setText(matiere.getNomMatiere());
    }

}

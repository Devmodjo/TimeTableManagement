package SceneController.timeTableController;

import java.io.IOException;

import SceneController.LoginSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainAppTimeController {
	
	@SuppressWarnings("unused")
	private LoginSceneController.User user;
	@FXML
	private AnchorPane MainPane;
	@FXML
	private Label welcomeLabel;
	@FXML
	private Button emploieTempsClassButton;
	@FXML
	private Button emploieTempsProfessorButton;
	@FXML
	private Button logoutLabel;
	@FXML 
	private AnchorPane mainBox;
	
	private Stage stage;
	//private Scene scene;
	//private Parent root;
	
	@FXML
	private void initialize() {
		
	}
	
	public void setUser(LoginSceneController.User user) {
        this.user = user;
        welcomeLabel.setText(" Bienvenue " + user.getUsername().toLowerCase());
    }
	@FXML
	 public void logout(ActionEvent event) {
	    	
			stage = (Stage) MainPane.getScene().getWindow();
		    System.out.println("you succefully logged out");
		    stage.close();
		    
		    try {
		    	Parent root = FXMLLoader.load(getClass().getResource("./../../application/FXML_FILES/LoginScene.fxml"));
				Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.setTitle("Authentification");
				stage.setResizable(false);
	            stage.show();
	            
	            // Récupération de la résolution de l'écran
	            Screen screen = Screen.getPrimary();
	            Rectangle2D bounds = screen.getVisualBounds();

	            // Calcul de la position pour centrer la fenêtre
	            double x = (bounds.getWidth() - stage.getWidth()) / 2;
	            double y = (bounds.getHeight() - stage.getHeight()) / 2;

	            stage.setX(x);
	            stage.setY(y);
	            stage.show();
	            
		    }catch(IOException e) {
		    	e.printStackTrace();
		    }
		    
	    	
	    }

}

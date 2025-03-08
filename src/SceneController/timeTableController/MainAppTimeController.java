package SceneController.timeTableController;

import SceneController.LoginSceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainAppTimeController {
	
	private LoginSceneController.User user;
	@FXML
	private Label welcomeLabel;
	
	public void setUser(LoginSceneController.User user) {
        this.user = user;
        welcomeLabel.setText(" Bienvenue " + user.getUsername());
    }

}

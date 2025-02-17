package util;

import SceneController.LoginSceneController.User;
import SceneController.MainAppController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class UserView {

	/*
	 * cette classe permet de recense chaque interface utilisateru dans des fonctions 
	 * afin de les appelé en fonction des identifiant de connexion (Admin, Professeur, responsable emploie du temps)
	 * */
	
	public void AdminView(User user, TextField usernameInput) {
		
		/* Interface administrateur */
		
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("./../application/FXML_FILES/MainApp.fxml"));
	        Parent root = loader.load();

	        // Pass user data to MainAppController
	        MainAppController mainAppController = loader.getController();
	        mainAppController.setUser(user);

	        Stage stage = (Stage) usernameInput.getScene().getWindow();
	        stage.setTitle("TimesTables Management - " + user.getUserStatus());
	        stage.setScene(new Scene(root));
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void TimesManagerView() {
		/* Interface responsable des emploi de temps */
	}
	
	
	public void ProfessorView() {
		/* Interface Professeur */
	}
	
}

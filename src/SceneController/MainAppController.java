package SceneController;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainAppController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private AnchorPane MainPane;
    @FXML
    private Button AcceuilButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button createClassButton;
    @FXML
    private Button createProfesseurButton;
    @FXML
    private Button createMatiere;
    @FXML
    private AnchorPane mainBox;
    
    private Stage stage;
	private Scene scene;
	private Parent root;

    @SuppressWarnings("unused")
	private LoginSceneController.User user;

    public void setUser(LoginSceneController.User user) {
        this.user = user;
        welcomeLabel.setText(" Bienvenue " + user.getUsername());
    }
    
    
    @FXML
    private void initialize() {
    	
    	// ouvrir la fenetre par defaut
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../application/FXML_FILES/HomeScene.fxml"));
    	try {
			Parent root = loader.load();
			mainBox.getChildren().add(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	createProfesseurButton.setOnAction(event->openProfessorScreen());
    	AcceuilButton.setOnAction(event->openHomeScreen());
    	createMatiere.setOnAction(event->openMatScreen());
    	createClassButton.setOnAction(event ->openClassScreen());
    }
    // ouvrir la fenetre de creation de prof
    private void openProfessorScreen() {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../application/FXML_FILES/createProfessorScene.fxml"));
    	try {
			Parent root = loader.load();
			mainBox.getChildren().clear();
			mainBox.getChildren().add(root);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    // ouvrir la page d'acceuil
    public void openHomeScreen() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../application/FXML_FILES/HomeScene.fxml"));
    	try {
			Parent root = loader.load();
			mainBox.getChildren().clear();
			mainBox.getChildren().add(root);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    // ouvrir la page d'ajout de matiere
    public void openMatScreen() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../application/FXML_FILES/ajoutMatiere.fxml"));
    	try {
			Parent root = loader.load();
			mainBox.getChildren().clear();
			mainBox.getChildren().add(root);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    
    }
    
    public void openClassScreen() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("./../application/FXML_FILES/ajoutCLasse.fxml"));
    	try {
			Parent root = loader.load();
			mainBox.getChildren().clear();
			mainBox.getChildren().add(root);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void switchtoLoginScene(MouseEvent event) throws IOException {
		
		root = FXMLLoader.load(getClass().getResource("./../application/FXML_FILES/CreateaccountScene.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
    
    // logout
    public void logout(ActionEvent event) {
    	
		stage = (Stage) MainPane.getScene().getWindow();
	    System.out.println("you succefully logged out");
	    stage.close();
	    
	    try {
	    	Parent root = FXMLLoader.load(getClass().getResource("./../application/FXML_FILES/LoginScene.fxml"));
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
 
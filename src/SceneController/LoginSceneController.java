package SceneController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DBManager.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import util.DialogBox;
import util.UserView;


public class LoginSceneController {
	
	@FXML
	protected TextField usernameInput;
	@FXML
	protected PasswordField password_input;
	@FXML
	protected Button loginButton;
	protected User user;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	
	@FXML
	private void initialize() {
	    new Thread(() -> {
	        try {
	            createSuperAdmin();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }).start();
	}

	
	public void LoginAccount(ActionEvent event) {
		
		String name_user = usernameInput.getText().toString();
		String password_user = password_input.getText().toString();
		user = login(name_user, password_user);
        if (user != null) {
            System.out.println("Login successful! User ID: " + user.getId() + ", Username: " + user.getUsername() +", userStatus : " + user.getUserStatus());
            // Load main application
            try {
            	// afficher differente interface en fonction des identifiant de connection
              // if(user.getUserStatus() == "Admin")
            	   new UserView().AdminView(user, usernameInput);
               /*if(user.getUserStatus() == "Responsable du temps")
            	   new UserView().TimesManagerView(user, usernameInput);
               if(user.getUserStatus() == "Professeur")
            	   new DialogBox().infoAlertBox("info", "interface indisponible");*/
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Login failed!");
            new DialogBox().errorAlertBox("LoginError", "identifiant incorect");
        }
	}
	
	
	// backend verification handle login
	//@SuppressWarnings("unused")
	private User login(String username, String password) {
	    String sql = "SELECT * FROM user WHERE name_user = ? AND password_user = ?";
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        // Établir la connexion
	        con = DBManager.connect();

	        // Préparer la requête SQL
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, username);
	        pstmt.setString(2, password);

	        // Exécuter la requête
	        rs = pstmt.executeQuery();

	        // Vérifier si un utilisateur est trouvé
	        if (rs.next()) {
	            // Retourner un objet User si l'utilisateur existe
	            return new User(rs.getInt("id_user"), rs.getString("name_user"), rs.getString("userStatus"));
	        } else {
	            // Aucun utilisateur trouvé
	            return null;
	        }

	    } catch (SQLException e) {
	        // Afficher un message d'erreur
	        System.err.println("Erreur lors du login : " + e.getMessage());
	        return null;

	    } finally {
	        // Fermer les ressources dans l'ordre inverse de leur ouverture
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
	        }
	    }
	}

	
	public void createSuperAdmin() {
	    Connection con = null;
	    PreparedStatement pstmt = null;

	    try {
	        if (!verifySuperAdmin()) { // Vérifiez si root existe déjà
	            con = DBManager.connect();
	            pstmt = con.prepareStatement("INSERT INTO user(name_user, password_user, userStatus) VALUES('root', 'root', 'Admin')");
	            pstmt.executeUpdate();
	            System.out.println("Super admin 'root' créé avec succès !");
	        } else {
	            System.out.println("Super admin 'root' existe déjà.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (pstmt != null) pstmt.close();
	            if (con != null) con.close();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	}

	
	public boolean verifySuperAdmin() throws SQLException {
	    boolean exists = false;
	    Connection con = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	        con = DBManager.connect();
	        stmt = con.createStatement();
	        rs = stmt.executeQuery("SELECT * FROM user WHERE name_user = 'root'");
	        
	        if (rs.next()) {
	            exists = true;
	        }
	    } finally {
	        if (rs != null) rs.close();
	        if (stmt != null) stmt.close();
	        if (con != null) con.close();
	    }
	    
	    return exists;
	}

	
	public void switchtoCreateaccountScene(MouseEvent event) throws IOException {
		
		root = FXMLLoader.load(getClass().getResource("./../application/FXML_FILES/CreateaccountScene.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
	public void logout(Stage stage) {
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("logged out");
		alert.setHeaderText("your about logged out !");
		alert.setContentText("ête vous sure d'avoir enregistrer vos donné ?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
	    	System.out.println("you succefully logged out");
	    	stage.close();
		}
    	
    }
	
	public class User{
		
		private int id;
        private String username;
        private String userStatus;

        public User(int id, String username, String userStatus) {
            this.id = id;
            this.username = username;
            this.userStatus = userStatus;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
        
        public String getUserStatus() {
        	return userStatus;
        }
	}
}

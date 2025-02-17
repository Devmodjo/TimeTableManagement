package SceneController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DBManager.DBManager;
import ObjectModel.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import util.DialogBox;

public class homeSceneController{

    @FXML
    private CheckBox checkboxProf;
    @FXML
    private TableView<User> tableViewUser;
    @FXML
    private TableColumn<User, Integer> columnID;
    @FXML
    private TableColumn<User, String> columnPassword;
    @FXML
    private TableColumn<User, String> columnStatus;
    @FXML
    private TableColumn<User, String> columnUser;
    @FXML
    private ComboBox<String> comboNomProfesseur;
    @FXML
    private ComboBox<String> comboStatus;
    @FXML
    private Label lbnomProfesseur;
    @FXML
    private Label lbnomUtilisateur;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button removeButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField usernameField;
    
    private DialogBox boxDialog = new DialogBox();
    
    private List<String> listProf = new ArrayList<>();
    
    @FXML
    private void initialize() {
    	
    	// remplir les comboBox
    	try {
    		comboStatus.getItems().addAll("Admin", "Responsable du temps", "Professeur");
    		comboStatus.setValue("Admin");
    		comboNomProfesseur.getItems().addAll(someProfesseur());
    		
    		//setUserInfo();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	// affecter la fonction de la checkbox
    	checkboxProf.setOnAction(event ->verifyChecked());
    	saveButton.setOnAction(event->{
    		try {
    			addUser();
    		}catch(SQLException e){e.printStackTrace();}
    	});
    }
    
    
    @SuppressWarnings("unused")
	public void addUser() throws SQLException{
    	/* ajout des utilisateur */
    	Connection con = null;
    	PreparedStatement ps = null;
    	try {
    		
    		String user;
    		String pass= passwordField.getText();
    		String userStatus = comboStatus.getValue().toString();
    		String sql = "INSERT INTO user(name_user, password_user, userStatus) VALUES(?,?,?)";
    		
    		con = DBManager.connect();
    		if(verifyChecked() == true) {
    			user = comboNomProfesseur.getValue().toString();
    		} else {
    			user = usernameField.getText();
    		}
    		
    		if(user != null && pass != "") {
    			ps = con.prepareStatement(sql);
            	ps.setString(1, user);
            	ps.setString(2, pass);
            	ps.setString(3, userStatus);
            	ps.execute();
            	boxDialog.infoAlertBox("Save[OK]", "nouvel utilisateur enregistrer aec succes,");
    		} else {
    			boxDialog.errorAlertBox("ERROR", "verifiez vos champs");
    		}
    		
    	}finally {
    		if (ps != null) ps.close();
    		if (con != null) con.close();
    	}
    	
    }
    
    public void setUserInfo(String nomProf) throws SQLException{
    	/* affecter les info utilisateur(professeur)
    	 * dans les champs si son nom est selectionner   */
    	
    	if(verifyChecked() == true) {
    		Connection con = null;
        	Statement stmt = null;
        	ResultSet rs = null;
        	
        	comboStatus.setValue("Professeur");
        	String sql = "SELECT idProfesseur FROM professeur WHERE nom='" + nomProf+ "'";
        	if(!nomProf.isEmpty()) {
        		try {
            		con = DBManager.connect();
            		stmt = con.createStatement();
            		rs = stmt.executeQuery(sql);
            		
            		while(rs.next()) {
            			passwordField.setText(rs.getString("idProfesseur"));
            		}
        	    	
            	} finally {
            		if (stmt != null) stmt.close();
            		if (rs != null) rs.close();
            		if (con != null) con.close();
            		
            	}
        	}
    	}else {
    		System.out.println("cheat");
    	}
    	
    }
    
    public List<String> someProfesseur() throws SQLException{
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	try {
    		String sql = "SELECT DISTINCT nom FROM Professeur";
    		con = DBManager.connect();
    		stmt = con.createStatement();
    		rs = stmt.executeQuery(sql);
    		
    		while (rs.next()) {
    			listProf.add(rs.getString("nom"));
    		}
    		
    	}finally {
    		if (stmt != null) stmt.close();
    		if (rs != null) rs.close();
    		if (con != null) con.close();
    	}
    	
    	return listProf;
    }
    
    public boolean verifyChecked() {
    	/* fonction pour verifier si la checkbox est cochée
    	 * * */
    	
    	if(checkboxProf.isSelected()) {
    		lbnomProfesseur.setDisable(false);
    		comboNomProfesseur.setDisable(false);
    		
    		lbnomUtilisateur.setDisable(true);
    		usernameField.setDisable(true);
    		return true;
    		
    	} else {
    		lbnomProfesseur.setDisable(true);
    		comboNomProfesseur.setDisable(true);
    		
    		lbnomUtilisateur.setDisable(false);
    		usernameField.setDisable(false);
    		return false;
    	}
    }
    // public void
}

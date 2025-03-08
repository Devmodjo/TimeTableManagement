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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    
    private User user;
    
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
    	removeButton.setOnAction(event ->{
    		try {
    			removeUser();
    		} catch(SQLException e) {e.printStackTrace();}
    	});
    	// mise a jour automatique du tablau
    	mouseClicked();
    	updateTableview();
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
    		User us = new User(null, user, userStatus, pass);
    		if(user != null && pass != "") {
    			ps = con.prepareStatement(sql);
            	ps.setString(1, us.getUsername());
            	ps.setString(2, us.getPassword());
            	ps.setString(3, us.getUserStatus());
            	ps.execute();
            	updateTableview();
            	clearField();
            	boxDialog.infoAlertBox("Save[OK]", "nouvel utilisateur enregistrer aec succes,");
    		} else {
    			boxDialog.errorAlertBox("ERROR", "verifiez vos champs");
    		}
    		
    	}finally {
    		if (ps != null) ps.close();
    		if (con != null) con.close();
    	}
    	
    }
    
    public void updateTableview() {
    	/*mise a jour du tableau*/
    	try {
    		ObservableList<User> list = getListUser();
    		// affecetetr les valeur aux champs
    		columnID.setCellValueFactory(new PropertyValueFactory<User, Integer>("idUser"));
    		columnUser.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
    		columnStatus.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
    		columnPassword.setCellValueFactory(new PropertyValueFactory<User,String>("userStatus"));
    		tableViewUser.setItems(list);
    	} catch(Exception e ) {
    		boxDialog.errorAlertBox("ERROR", "une erreur est survenue");
    		e.printStackTrace();
    	}
    }
    
    @FXML
    public void mouseClicked() {
    	try {
    		User us = tableViewUser.getSelectionModel().getSelectedItem();
    		us = new User(us.getIdUser(), us.getUsername(), us.getUserStatus(), us.getPassword());
    		this.user = us;
    		removeButton.setDisable(false);
    	} catch(Exception e ) {
    		// lol
    	}
    }
    
    public void removeUser() throws SQLException{
    	Connection con = null;
    	PreparedStatement ps = null;
    	try {
    		String sql = "DELETE FROM user WHERE id_user=?";
    		con = DBManager.connect();
    		ps = con.prepareStatement(sql);
    		ps.setInt(1, this.user.getIdUser());
    		ps.execute();
    		updateTableview();
    		clearField();
    		boxDialog.infoAlertBox("REMOVE OK", "l'element a bien ete supprimer");
    		
    	} finally {
    		if(ps != null) ps.close();
    		if(con != null) con.close();
    	}
    }
    
    public ObservableList<User> getListUser() throws SQLException{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		ObservableList<User> listUser = FXCollections.observableArrayList();
		try {
			con = DBManager.connect();
			String sql = "SELECT * FROM user";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				User us = new User(
						rs.getInt("id_user"),
						rs.getString("name_user"),
						rs.getString("password_user"),
						rs.getString("userStatus"));
				listUser.add(us);
			}
			
		} finally {
			if (rs != null ) rs.close();
    		if (stmt != null) stmt.close();
    		if (con != null) con.close();
		}
    	return listUser;
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
    public void clearField() {
    	usernameField.setText("");
    	passwordField.setText("");
    	comboNomProfesseur.setValue("");
    	comboStatus.setValue("Admin");
    }
}

package SceneController;

import javafx.scene.control.TextField;
import util.DialogBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

import DBManager.DBManager;
import ObjectModel.Matiere;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

public class ajoutMatiereController implements Initializable{
	 
	@FXML
	private Button btnAddMat;
	@FXML
	private TextField fieldAddMat;
	@FXML
	private ScrollPane scrollpane;
	@FXML
	private Button remove;
	@FXML
	private ComboBox<String> classlist;
	@FXML
	private ListView<String> ListMatiere;
	
	private List<String> listOfMat = new ArrayList<String>();
	private DialogBox boxDialog = new DialogBox();
	
	@FXML
	private void initialize() {
		
		
	}
	
	
	public void ajouterMatiere(ActionEvent ev) {
		
		String fieldMat = fieldAddMat.getText();
		String sql = "INSERT INTO matiere(nom_matiere) VALUES(?)";
		if(!fieldMat.isEmpty()) {
			Matiere mat = new Matiere.MatiereBuilder()
					.withNomMatiere(fieldMat.toUpperCase()).build();
			try {
				
				Connection con = DBManager.connect();
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, mat.getNomMatiere());
				ps.execute();
				fieldAddMat.setText("");
				
				ps.close();
				con.close();
				//updateTableMatiere();
				
				this.boxDialog.infoAlertBox("Save OK", "Nouvelle Matiere enregistrer");
				
			}catch(Exception e) {
				e.printStackTrace();
				this.boxDialog.errorAlertBox("ERROR", e.getMessage());
			}
		} else {
			this.boxDialog.errorAlertBox("FieldError", "Veuillez renseigner le nom de la matiere" );
		}
	}
	
	// recupere toute les classe
	public List<String> someClasse() {
	    List<String> someclasse = new ArrayList<>(); // Nouvelle liste à chaque appel
	    Connection con = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	        String sql = "SELECT DISTINCT nom FROM Classe";
	        con = DBManager.connect();
	        stmt = con.createStatement();
	        rs = stmt.executeQuery(sql);

	        while (rs.next()) {
	            someclasse.add(rs.getString("nom"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // Fermeture des ressources dans le bon ordre
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (con != null) con.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return someclasse;
	}

	
	// recuperer les matiere en base de donnée
	public List<String> getMatiere(){
		Matiere m;
		try {
			Connection con = DBManager.connect();
			PreparedStatement ps = con.prepareStatement("SELECT DISTINCT * FROM  matiere");
			ResultSet rs = ps.executeQuery();
			int i =0;
			while(rs.next()) {
				// ajoute des matiere dans la liste d'element
				m = new Matiere.MatiereBuilder()
						.withNomMatiere(rs.getString("nom_matiere"))
						.build();
				listOfMat.add(m.getNomMatiere());
				System.out.println(listOfMat.get(i));
	
				i++;
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			this.boxDialog.errorAlertBox("DBASE ERROR", "une erreur est survenue");
			e.printStackTrace();
		}
		return listOfMat;
	}
	
	/*Fonction de mise a jour de la table de MAtiere*/
	public void updateTableMatiere() {
		ObservableList<String> list = (ObservableList<String>) getMatiere();
		listOfMat.addAll(list);
	}
	
	
	
	public void removeMatiere(String nomMatiere) {
		try {
			Connection con = DBManager.connect();
			PreparedStatement ps = con.prepareStatement("DELETE FROM matiere WHERE nom_matiere= ?");
			ps.setString(1, nomMatiere);
			ps.execute();
			boxDialog.infoAlertBox("removed", " la matiere " + nomMatiere + " correcement supprimer");
			ps.close();
			con.close();
			updateTableMatiere();
			
		}catch(Exception e) {
			boxDialog.errorAlertBox("Error", "une erreur est survenue");
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/* charger le layout des differents matieres */
		ListMatiere.getItems().addAll(getMatiere());
		ListMatiere.getItems().addAll(getMatiere());
		classlist.getItems().addAll(someClasse());
		ListMatiere.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				remove.setDisable(false);
				remove.setOnAction(event -> removeMatiere(ListMatiere.getSelectionModel().getSelectedItem()));
			}
		});
	}
	
	
}


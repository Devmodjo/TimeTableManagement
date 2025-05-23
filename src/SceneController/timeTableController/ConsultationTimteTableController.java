package SceneController.timeTableController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import DBManager.DBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DialogBox;
import util.LigneEmploiTemps;

public class ConsultationTimteTableController {
	
	// -------------- propriété pour la consultation par classe
	
    @FXML
    private TabPane TabPaneConsultation;

    @FXML
    private TableColumn<String, String> classeColumn;

    @FXML
    private AnchorPane consulationEtablissementScene;

    @FXML
    private Button consultButton;

    @FXML
    private AnchorPane consultationClassroomScene;

    @FXML
    private TableColumn<String, String> disponibiliteColumn;

    @FXML
    private TableColumn<String, String> nbreSaveColumn;

    @FXML
    private TextField searhBar;

    @FXML
    private TableView<LigneEmploiTemps> tableViewListEmploieTemps;
    
    public List<String> listClaroomCell;
    public List<Boolean> listDisponibilitieCell;
    public List<Integer> listsavedTimeTable;
    protected LigneEmploiTemps ligneEmploiTemps;
    
    // bouton pour generer l'emploi de temps de l'etablissement
    @FXML
    public Button generateEtablissement;
    
    
    
    @FXML
    private void initialize() throws SQLException {
    	FillTableView();
    	updateTableView();
    	searhBar.textProperty().addListener((ObservableList, oldValue, newValue)->{
    		filterData(newValue);
    	});
    	consultButton.setOnAction(evnt->{
    		SpawnConsultationPopup(ligneEmploiTemps);
    	});
    	generateEtablissement.setOnAction(evnt->{
    		SpawnGeneratePopup();
    	});
    	tableViewListEmploieTemps.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
    	    if (newSel != null) {
    	        this.ligneEmploiTemps = newSel;
    	        consultButton.setDisable(false);
    	        System.out.println("Ligne sélectionnée : " + newSel.getClasse());
    	    }
    	});
    }
    
    // constructor...
    public ConsultationTimteTableController() throws SQLException {
    	 this.listClaroomCell = getListClassroom();
    	 this.listDisponibilitieCell = getDisponibilitie();
    	 this.listsavedTimeTable = getListTimeTableSaved();
    }
    
    public void FillTableView() throws SQLException{
    	// setp 1: get all classroom for classroom column (information a remplire dans la première colone)
 
    }
    
    public List<String> getListClassroom() throws SQLException{
    	/*
    	 * recuperation de la liste des classe
    	 * */
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet res = null;
    	List<String> listClasse = FXCollections.observableArrayList();
    	try {
    		
    		con = DBManager.connect();
  
    		String sql = "SELECT DISTINCT nom FROM Classe";
    		stmt = con.createStatement();
    		res = stmt.executeQuery(sql);
    		
    		while(res.next()) {
    			listClasse.add(res.getString("nom").toString());
    		}
    		
    	} finally {
    		if (res != null ) res.close();
    		if (stmt != null) stmt.close();
    		if (con != null) con.close();
    	}
    	
    	return listClasse;
    }
    
    public List<Boolean> getDisponibilitie(){
    	List<Boolean> availabilityList = FXCollections.observableArrayList();
        String query = """
            SELECT C.idClasse, 
                   EXISTS (
                       SELECT 1 FROM EmploiTempsClasse E 
                       WHERE E.idClasse = C.idClasse
                   ) AS hasTimetable
            FROM Classe C;
        """;

        try (Connection con = DBManager.connect();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                boolean hasTimetable = rs.getInt("hasTimetable") == 1;
                availabilityList.add(hasTimetable);
            }

        } catch (SQLException e) {
            new DialogBox().errorAlertBox("Erreur", "Échec lors de la vérification des emplois du temps :\n" + e.getMessage());
        }

        return availabilityList;
    }
    
    public List<Integer> getListTimeTableSaved() {
        List<Integer> timetableCounts = FXCollections.observableArrayList();

        String sql = """
            SELECT C.idClasse, COUNT(E.idEmploiTempsClasse) AS count
            FROM Classe C
            LEFT JOIN EmploiTempsClasse E ON C.idClasse = E.idClasse
            GROUP BY C.idClasse
            ORDER BY C.idClasse
        """;

        try (Connection con = DBManager.connect();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int count = rs.getInt("count");
                timetableCounts.add(count);
            }

        } catch (SQLException e) {
            new DialogBox().errorAlertBox("Erreur", "Échec lors du comptage des emplois du temps par classe :\n" + e.getMessage());
        }

        return timetableCounts;
    }
    
    public void updateTableView() {
        if (listClaroomCell.size() != listDisponibilitieCell.size() || listClaroomCell.size() != listsavedTimeTable.size()) {
            new DialogBox().errorAlertBox("Erreur", "Les listes ne sont pas de la même taille.");
            System.out.println("classroom : " + listClaroomCell.size());
            System.out.println("disponibilite : " + listDisponibilitieCell.size());
            System.out.println("enregistrer : " + listsavedTimeTable.size());
            return;
        }

        List<LigneEmploiTemps> lignes = FXCollections.observableArrayList();

        for (int i = 0; i < listClaroomCell.size(); i++) {
            String nomClasse = listClaroomCell.get(i);
            String dispo = listDisponibilitieCell.get(i) ? "Disponible" : "Indisponible";
            String nombre = listsavedTimeTable.get(i).toString();

            lignes.add(new LigneEmploiTemps(nomClasse, dispo, nombre));
        }

        // Remplir le tableau
        tableViewListEmploieTemps.setItems(FXCollections.observableArrayList(lignes));

        // Associer les colonnes aux champs de la classe LigneEmploiTemps
        classeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("classe"));
        disponibiliteColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("disponibilite"));
        nbreSaveColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));
    }
    
    public void filterData(String search) {
        try {
            ObservableList<LigneEmploiTemps> fullList = FXCollections.observableArrayList();

            // On reconstruit toute la liste avec les 3 colonnes
            for (int i = 0; i < listClaroomCell.size(); i++) {
                String classe = listClaroomCell.get(i);
                String disponibilite = listDisponibilitieCell.get(i) ? "Disponible" : "Indisponible";
                String nombre = String.valueOf(listsavedTimeTable.get(i));

                fullList.add(new LigneEmploiTemps(classe, disponibilite, nombre));
            }

            // On filtre la liste selon la recherche
            ObservableList<LigneEmploiTemps> filteredList = FXCollections.observableArrayList();
            for (LigneEmploiTemps ligne : fullList) {
                if (ligne.getClasse().toLowerCase().contains(search.toLowerCase())) {
                    filteredList.add(ligne);
                }
            }

            // On applique le résultat à la TableView
            tableViewListEmploieTemps.setItems(filteredList);

        } catch (Exception e) {
            new DialogBox().errorAlertBox("Erreur", "Une erreur est survenue sur la barre de recherche : " + e.getMessage());
        }
    }
    
    public static String cl;
    public static String dispo;
    public static String nbr;
    
    @FXML
    public void mouClicked() {
    	/* evenement de click sur le tableau */
    	
    	try {
    		LigneEmploiTemps lg = tableViewListEmploieTemps.getSelectionModel().getSelectedItem(); 
    		if(lg != null)
    			this.ligneEmploiTemps = lg;
    			// ecriture de la classe selectionner dans le fichier
    			saveSession(this.ligneEmploiTemps);
    			
	    		consultButton.setDisable(false);
	    		System.out.println("element du tableau selectionné : " + lg.getClasse());
    		
    	} catch(Exception e) {
    		// lol
    	}
    }
    
    public void SpawnConsultationPopup(LigneEmploiTemps lg){
    
    	/*
    	 * affichage de la popup d'impression des emploi par classe
    	 * */
    	
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/FXML_FILES/PopupConsultationScene.fxml"));
    		Parent root = loader.load();
    		
    		// Récupération du contrôleur
    		ConsultationPopup controller = loader.getController();
    		if (this.ligneEmploiTemps != null)
    			controller.setLigneEmploiTemps(this.ligneEmploiTemps);
    		else
    			new DialogBox().errorAlertBox("ERROR", "veuillez selectionnez une ligne avant de consulter le tableau");
    		
    		Scene scene = new Scene(root);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		stage.setTitle("Liste Emploie de Temps - "+ lg.getClasse());
    		stage.setResizable(false);
    		stage.initModality(Modality.APPLICATION_MODAL);
    		stage.showAndWait();
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		new DialogBox().errorAlertBox("Erreur", "Erreur lors de l’ouverture de la popup :\n" + e.getMessage()); 
    	}
    }
    
    public void SpawnGeneratePopup(){
    	/*
    	 * affichage de la popup pour generer l'emploi de l'etablissement
    	 * */
    	
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/FXML_FILES/GeneratePopupScene.fxml"));
    		Parent root = loader.load();
    		
    		// Récupération du contrôleur
    		/*ConsultationPopup controller = loader.getController();
    		if (this.ligneEmploiTemps != null)
    			controller.setLigneEmploiTemps(this.ligneEmploiTemps);
    		else
    			new DialogBox().errorAlertBox("ERROR", "veuillez selectionnez une ligne avant de consulter le tableau");
    		*/
    		
    		Scene scene = new Scene(root);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		stage.setTitle("Générer l'emploie de temps de l'établissement");
    		stage.setResizable(false);
    		stage.initModality(Modality.APPLICATION_MODAL);
    		stage.showAndWait();
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		new DialogBox().errorAlertBox("Erreur", "Erreur lors de l’ouverture de la popup :\n" + e.getMessage()); 
    	}
    }
    
    @SuppressWarnings("unused")
	private void saveSession(LigneEmploiTemps line) throws IOException {
    	// creation du fichier de session
    	try(BufferedWriter writer = new BufferedWriter(new FileWriter("session_temps.txt"))) {
    		// ecrite dans le fichier
    		writer.write(line.getClasse());
    		System.out.println("ecriture dans le fichier effectuer avec succes");
    	} catch(Exception e) {
    		e.printStackTrace();
    		new DialogBox().errorAlertBox("ERROR", "une erreur est survenue " + e.getMessage());
    	}
    }
    
    // -------------- gestionnaire de consultation de l'emploie de temps pour l'etablissement
}

package SceneController.timeTableController;

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
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import util.DialogBox;
import util.LigneEmploiTemps;

public class ConsultationTimteTableController {

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
    public LigneEmploiTemps ligneEmploiTemps;
    
    @FXML
    private void initialize() throws SQLException {
    	FillTableView();
    	updateTableView();
    	mouClicked();
    	searhBar.textProperty().addListener((ObservableList, oldValue, newValue)->{
    		filterData(newValue);
    	});;
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
    	for (int i = 0; i < listClaroomCell.size(); i++) {
            cl = listClaroomCell.get(i);
            dispo = listDisponibilitieCell.get(i) ? "Disponible" : "Indisponible";
            nbr = String.valueOf(listsavedTimeTable.get(i));
        }
    	
    	try {
    		LigneEmploiTemps lg = tableViewListEmploieTemps.getSelectionModel().getSelectedItem(); 
    		lg = new LigneEmploiTemps(cl, dispo, nbr);
    		this.ligneEmploiTemps = lg;
    		
    		consultButton.setDisable(false);
    		System.out.println("element du tableau selectionné");
    		
    	} catch(Exception e) {
    		// lol
    	}
    }

    
}

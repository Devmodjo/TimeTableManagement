package SceneController.timeTableController;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import DBManager.DBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.DialogBox;
import util.GenerateExcel;
import util.LigneEmploiTemps;

public class GeneratePopup {

    @FXML
    private Button generateButton;

    @FXML
    private TableColumn<LigneEmploiTemps, String> schoolYearColumn;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView<LigneEmploiTemps> viewTimeTableEtab;
    public LigneEmploiTemps ligneEmploiTemps;

    
    @FXML
    public void initialize(){
    	
    	updateTableView();
    	searchBar.textProperty().addListener((ObservableList, oldValue, newValue)->{
    		filterData(newValue);
    	});
    	
    	generateButton.setOnAction(event->{
    		try {	
				GenerateTimeTable();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	});
    }
    
    public ObservableList<LigneEmploiTemps> getListYears() throws SQLException, IOException{
    	/*
    	 * recuperation de la liste des année scolaire
    	 * */
    	String sql = "SELECT DISTINCT anneeScolaire "
    			+ "FROM EmploiTempsClasse "
    			+ "ORDER BY anneeScolaire DESC";
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet res = null;
    	ObservableList<LigneEmploiTemps> listYear = FXCollections.observableArrayList();
    	
    	try {
    		con = DBManager.connect();
    		stmt = con.createStatement();
    		res = stmt.executeQuery(sql);
    		
        	while(res.next()) {
        		LigneEmploiTemps lg = new LigneEmploiTemps(res.getString("anneeScolaire"));
        		listYear.add(lg);
        	}
      
    	}finally{
    		if(con != null) con.close();
    		if(stmt != null) stmt.close();
    		if(res != null) res.close();
    	}
    	
    	return listYear;
    }
    
    public void updateTableView(){
    	/* remplissage et mise a jour du tableau */
    	try {
    		ObservableList<LigneEmploiTemps> list =  getListYears();
    		schoolYearColumn.setCellValueFactory(new PropertyValueFactory<LigneEmploiTemps, String>("schoolYear"));
    		viewTimeTableEtab.setItems(list);
    		
    	}catch(Exception  e) {
    		e.printStackTrace();
    		new DialogBox().errorAlertBox("ERROR", "une erreur est survenue lors du chargement: \n " + e.getMessage());
    		
    	}
    }
    
    public void filterData(String search) {
    	try {
    		ObservableList<LigneEmploiTemps> filterData= FXCollections.observableArrayList();
    		ObservableList<LigneEmploiTemps> list = getListYears();
    		
    		for(LigneEmploiTemps lg : list) {
    			if(lg.getSchoolYear().contains(search)) {
    				filterData.add(lg);
    			}
    		}
    		viewTimeTableEtab.setItems(filterData);
    	} catch(Exception e) {
    		new DialogBox().errorAlertBox("ERROR", "une erreur est survenue :"+ e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    @FXML
    public void mouseclickedConsultation()  {
    	
    	try {
    		
    		LigneEmploiTemps lgt = viewTimeTableEtab.getSelectionModel().getSelectedItem();
    		if (lgt == null) {
                System.out.println("Aucune ligne sélectionnée");
                return;
            }
        	lgt = new LigneEmploiTemps(lgt.getSchoolYear());
        	this.ligneEmploiTemps = lgt;
        	System.out.println("recuperation : " + ligneEmploiTemps.getSchoolYear());
        	generateButton.setDisable(false);
        	
    	} catch(Exception  e) {
    		// lol
    	}
    		
    }
    
    public Map<String, String[][]> getEmploieTemps(String anneeScolaire) throws SQLException{
    	
    	PreparedStatement ps = null;
    	ResultSet res = null;
    	Map<String, String[][]> emploisParClasse = new HashMap<>();
    	String[] jours = {"lundiCours", "mardiCours", "mercrediCours", "jeudiCours", "vendrediCours"};
    	
    	try(Connection con = DBManager.connect();){
    		
    		String sql = """
    				SELECT C.nom, E.lundiCours, E.mardiCours, E.mercrediCours, E.jeudiCours, E.vendrediCours
                FROM EmploiTempsClasse E
                JOIN Classe C ON E.idClasse = C.idClasse
                WHERE E.anneeScolaire = ?
                """;
    		
    		ps = con.prepareStatement(sql);
    		ps.setString(1, anneeScolaire);
    		res = ps.executeQuery();
    		
    		
    		while(res.next()) {
    			String nomClasse = res.getString("nom");
    			String[][] emploi = new String[5][7]; // 5jours/ 7periodes
    			
    			for (int jour = 0; jour < 5; jour++) {
                    String coursJour = res.getString(jours[jour]);
                    if (coursJour != null && !coursJour.isEmpty()) {
                        String[] matieres = coursJour.split(";"); 
                        for (int h = 0; h < matieres.length && h < 7; h++) {
                            emploi[jour][h] = matieres[h];
                        }
                    }
                }

				emploisParClasse.put(nomClasse, emploi);
    		}
    		
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}finally{
    		if (ps != null) ps.close();
    		if(res != null) res.close();
    	}
    	
    	return emploisParClasse;
    }
    
    
    public void GenerateTimeTable() throws SQLException {
    	/*
    	 * Fonction pour generer l'emploie de temps de l'etablissement
    	 * */
    	Stage stage = (Stage) generateButton.getScene().getWindow();
    	FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Enregistrer sous...");
		
		// definition de l'extension
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier Excel", "*.xlsx"));
        
        // suggere un nom de fichier
        fileChooser.setInitialFileName("emploi_temps_etablissement"+this.ligneEmploiTemps.getSchoolYear().replace("/", "-")+".xlsx");
        
        // ouvrir la FileChooser
        File selectedFile = fileChooser.showSaveDialog(stage);
        
        if(selectedFile != null) {
        	
			GenerateExcel.printExcel(getEmploieTemps(this.ligneEmploiTemps.getSchoolYear()), selectedFile.getAbsolutePath());
				
        } else {
        	new DialogBox().warningAlertBox("WARNING", "aucun repertoire n'as ete selectionné");
        }
    }
}

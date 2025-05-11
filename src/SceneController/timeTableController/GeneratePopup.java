package SceneController.timeTableController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DBManager.DBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.DialogBox;
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
    public void initialize() {
    	
    	updateTableView();
    	searchBar.textProperty().addListener((ObservableList, oldValue, newValue)->{
    		filterData(newValue);
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
    
    public void GenerateTimeTable() {
    	/*
    	 * Fonction pour generer l'emploie de temps de l'etablissement
    	 * */
    }
}

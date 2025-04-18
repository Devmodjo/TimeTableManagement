package SceneController.timeTableController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.List;
import DBManager.DBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.LigneEmploiTemps;
import util.DialogBox;

public class ConsultationPopup{

	//@FXML
   // private TableColumn<LigneEmploiTemps, String> numberTimeTableColumn;

    @FXML
    private Button printButton;

    @FXML
    private TableColumn<LigneEmploiTemps, String> schoolYearColumn;

    @FXML
    private TextField searbarClasse;

    @FXML
    private TableView<LigneEmploiTemps> viewTimeTableClasse;
    
    @SuppressWarnings("unused")
	private LigneEmploiTemps ligneEmploiTemps;
    
    public void setLigneEmploiTemps(LigneEmploiTemps ligne) {
        this.ligneEmploiTemps = ligne;
        System.out.println("Classe sélectionnée : " + ligne.getClasse());
    }

    @FXML
    public void initialize() throws IOException {
    	updateTableView();
    	searbarClasse.textProperty().addListener((ObservableList, oldValue, newValue)->{
    		filterData(newValue);
    	});
    	
    }
    
    // foncntion permettant de recuperer la liste des anner scolaire
    public ObservableList<LigneEmploiTemps> getConsultList() throws SQLException, IOException{
    	String sql = "SELECT anneeScolaire "
    			+ "FROM EmploiTempsClasse "
    			+ "WHERE idClasse = "
    			+ "( SELECT idClasse FROM Classe WHERE nom=? ) "
    			+ "ORDER BY anneeScolaire DESC";
    	Connection con = null;
    	Statement stmt = null;
    	PreparedStatement ps= null;
    	ResultSet res = null;
    	BufferedReader reader = null;
    	ObservableList<LigneEmploiTemps> listYear = FXCollections.observableArrayList();
    	
    	try {
    		// lecture du fichier
    		reader = new BufferedReader(new FileReader("session_temps.txt"));
        	String nomClasse = reader.readLine();
    		
    		con = DBManager.connect();
        	ps = con.prepareStatement(sql);
        	ps.setString(1, nomClasse);
        	res = ps.executeQuery();
        	System.out.println("consultation de la classe : " + nomClasse);
        	while(res.next()) {
        		LigneEmploiTemps lg = new LigneEmploiTemps(res.getString("anneeScolaire"));
        		listYear.add(lg);
        	}
      
    	}finally{
    		if(con != null) con.close();
    		if(stmt != null) stmt.close();
    		if(res != null) res.close();
    		if(reader != null) reader.close();
    	}
    	
    	return listYear;
    }
    
    public void updateTableView(){
    	/* remplissage et mise a jour du tableau */
    	try {
    		ObservableList<LigneEmploiTemps> list =  getConsultList();
    		schoolYearColumn.setCellValueFactory(new PropertyValueFactory<LigneEmploiTemps, String>("schoolYear"));
    		viewTimeTableClasse.setItems(list);
    		
    	}catch(Exception  e) {
    		e.printStackTrace();
    		new DialogBox().errorAlertBox("ERROR", "une erreur est survenue lors du chargement: \n " + e.getMessage());
    		
    	}
    }
    
    public void filterData(String search) {
    	try {
    		ObservableList<LigneEmploiTemps> filterData= FXCollections.observableArrayList();
    		ObservableList<LigneEmploiTemps> list = getConsultList();
    		
    		for(LigneEmploiTemps lg : list) {
    			if(lg.getSchoolYear().contains(search)) {
    				filterData.add(lg);
    			}
    		}
    		viewTimeTableClasse.setItems(filterData);
    	} catch(Exception e) {
    		new DialogBox().errorAlertBox("ERROR", "une erreur est survenue :"+ e.getMessage());
    		e.printStackTrace();
    	}
    }

}
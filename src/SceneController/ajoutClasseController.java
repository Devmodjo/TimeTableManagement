package SceneController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DBManager.DBManager;
import ObjectModel.Classe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.DialogBox;

public class ajoutClasseController {

    @FXML
    private ComboBox<String> comboCycle;

    @FXML
    private ListView<String> listViewClass;

    @FXML
    private TextField nomClasse;
    
    @FXML
    private TextField searchBarClasse;

    @FXML
    private Button removeClass;
    
    @FXML
    private Button addClass;
    
    @FXML
    private Button viewTimeTable;
    
    @FXML
    private TableView<Classe> tableViewClasse;
    @FXML
    private TableColumn<Classe, String> columnClass;
    
    @FXML
    private TableColumn<Classe, String> columnCycle;
    
    private DialogBox boxdialog = new DialogBox();
    private Classe classe;
    
    @FXML
    public void initialize() {
    	try {
    		comboCycle.getItems().addAll("1er cycle", "2eme cycle");
        	comboCycle.setValue("1er cycle");
        	removeClass.setDisable(true);
        	
        	addClass.setOnAction(event -> {
        		try {
        			registerClass();
        		} catch (Exception e) {e.printStackTrace();}
        	});
        	removeClass.setOnAction(event ->{
        		try {
        			removeClasse();
        		} catch(Exception e) {e.printStackTrace();}
        	});
        	searchBarClasse.textProperty().addListener((ObservableList, oldValue, newValue)->{
        		try { filterData(newValue); } catch (Exception e) { e.printStackTrace(); }
        	});
        	mouseClicked();
        	updateTableView();
        	
    	}catch(Exception e) {
    		boxdialog.errorAlertBox("ERROR", "erreur de chargement");
    		e.printStackTrace();
    	}
    }
    
    public void registerClass() throws SQLException {
    	/* 
    	 * fonction pour ajouter des classes
    	 * */
    	
    	Connection c = null;
        PreparedStatement stmt = null;
        //ResultSet rs = null;
        
        try {
        	String nomclass = nomClasse.getText();
        	String cycle = comboCycle.getValue().toString();
        	String sql = "INSERT INTO Classe(nom,cycle) VALUES(?,?)";
        	c = DBManager.connect();
        	
        	// cree une nouvelle instance d'une classe
        	Classe cl = new Classe.ClasseBuilder()
        			.withNomClasse(nomclass)
        			.withCycle(cycle).build();
        	
        	if(cl.getNomClasse().isEmpty()) {
        		boxdialog.errorAlertBox("FieldEmpty", "veuillez renseignez tous les champs");
        	} else {
        		
        		stmt = c.prepareStatement(sql);
        		stmt.setString(1, cl.getNomClasse());
        		stmt.setString(2, cl.getCyle());
        		stmt.execute();
        		boxdialog.infoAlertBox("Save OK", "nouvelle classe correctement enregistrer");
        		clearField();
        		updateTableView();
        	}
        	
        } finally {
        	if (stmt != null) stmt.close();
        	if (c != null) c.close();
        }
    }
    
    public void removeClasse() throws SQLException{
    	/*
    	 * fonction pour retirer une classe
    	 * */
    	Connection con = null;
    	PreparedStatement ps = null;
    	try {
    		String sql = "DELETE FROM Classe WHERE idClasse=?";
    		con = DBManager.connect();
    		ps = con.prepareStatement(sql);
    		ps.setInt(1, this.classe.getIdClasse());
    		ps.execute();
    		updateTableView();
    		clearField();
    		boxdialog.infoAlertBox("REMOVE OK", "element correctement supprimer");
    		
    	} finally {
    		if (con != null) con.close();
    		if (ps != null) ps.close();
    	}
    	
    	
    }
    
    // ***************** gestion du tableau de classe
    public void updateTableView() {
    	/*
    	 * mise a jour des donnée du table
    	 * */
    	
    	try {
    		ObservableList<Classe> list = getListClasse();
    		
    		// affectation des valeur de chaque colone
    		columnClass.setCellValueFactory(new PropertyValueFactory<Classe, String>("nomClasse"));
    		columnCycle.setCellValueFactory(new PropertyValueFactory<Classe, String>("cyle"));
    		
    		tableViewClasse.setItems(list);
    		
    	}catch(Exception e){
    		boxdialog.errorAlertBox("ERROR", "une erreur est survenue");
    		e.printStackTrace();
    	}
    }
    
    
    public ObservableList<Classe> getListClasse() throws SQLException{
    	
    	/*
    	 * recuperation de la liste des classe
    	 * */
    	
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet res = null;
    	ObservableList<Classe> listClasse = FXCollections.observableArrayList();
    	try {
    		
    		con = DBManager.connect();
  
    		String sql = "SELECT DISTINCT * FROM Classe";
    		stmt = con.createStatement();
    		res = stmt.executeQuery(sql);
    		
    		while(res.next()) {
    			Classe cl = new Classe.ClasseBuilder()
    					.withIdClasse(res.getInt("idClasse"))
    					.withNomClasse(res.getString("nom"))
    					.withCycle(res.getString("cycle"))
    					.build();
    			
    			listClasse.add(cl);
    		}
    		
    	} finally {
    		if (res != null ) res.close();
    		if (stmt != null) stmt.close();
    		if (con != null) con.close();
    	}
    	
    	return listClasse;
    	
    }
    
    @FXML
    public void mouseClicked() {
    	/*
    	 * évenement de click sur le tableau
    	 * */
    	
    	try {
    		Classe cl = tableViewClasse.getSelectionModel().getSelectedItem();
    		cl = new Classe.ClasseBuilder()
    				.withIdClasse(cl.getIdClasse())
    				.withNomClasse(cl.getNomClasse())
    				.withCycle(cl.getCyle())
    				.build();
    		this.classe = cl;
    		
    		nomClasse.setText(cl.getNomClasse());
    		comboCycle.setValue(cl.getCyle());
    		
    		removeClass.setDisable(false);
    		
    	} catch(Exception e) {
    		// lol
    	}
    }
    
    public void filterData(String search) throws SQLException {
    	/*
    	 * filtre de la barre de recherche
    	 * */
    	try {
    		ObservableList<Classe> filterData = FXCollections.observableArrayList();
    		ObservableList<Classe> list = getListClasse();
    		
    		for(Classe cl : list) {
    			if(cl.getNomClasse().contains(search) ||
    					cl.getCyle().contains(search)) {
    				filterData.add(cl);
    			}
    		}
    		
    		tableViewClasse.setItems(filterData);
    	} catch(Exception e) {
    		boxdialog.errorAlertBox("ERROR", "une erreur est survenue");
    		e.printStackTrace();
    	}
    }
    
    public void clearField() {
    	nomClasse.setText("");
    	comboCycle.setValue("1er cycle");
    }
}

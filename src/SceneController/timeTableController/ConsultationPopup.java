package SceneController.timeTableController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;

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
import util.LigneEmploiTemps;
import util.GeneratePDF;
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
    public static String[] arrayMatiere = new String[7];
    private final String SESSION_ANNE_PATH = System.getProperty("user.home") + File.separator + "session_anne.txt";
    
    
    public void setLigneEmploiTemps(LigneEmploiTemps ligne) {
        this.ligneEmploiTemps = ligne;
        System.out.println("Classe sélectionnée : " + ligne.getClasse());
    }

    @FXML
    public void initialize() throws IOException, Exception, DocumentException {
    	updateTableView();
    	//mouseclickedConsultation();
    	searbarClasse.textProperty().addListener((ObservableList, oldValue, newValue)->{
    		filterData(newValue);
    	});

    	printButton.setOnAction(event ->{
    		try {
				printTimeTable();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
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
    
    @FXML
    public void mouseclickedConsultation()  {
    	
    	try {
    		
    		LigneEmploiTemps lgt = viewTimeTableClasse.getSelectionModel().getSelectedItem();
    		if (lgt == null) {
                System.out.println("Aucune ligne sélectionnée");
                return;
            }
        	lgt = new LigneEmploiTemps(lgt.getSchoolYear());
        	this.ligneEmploiTemps = lgt;
        	System.out.println("recuperation : " + ligneEmploiTemps.getSchoolYear());
        	
        	System.out.println(getEmploieTemps(this.ligneEmploiTemps.getSchoolYear()));
        	
        	printButton.setDisable(false);
        	
    	} catch(Exception  e) {
    		// lol
    	}
    		
    }

    @SuppressWarnings({ "static-access", "resource" })
	public List<String> getEmploieTemps(String anneeScolaire) throws SQLException, IOException{
    
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	BufferedReader readerClasse = null;
    	List<String> listMatiere = new ArrayList<String>();
    	
    	try(Connection con = DBManager.connect();){
    	String sql = "SELECT lundiCours, "
    			+ "mardiCours, mercrediCours, jeudiCours,"
    			+ "vendrediCours FROM EmploiTempsClasse  WHERE anneeScolaire = ? "
    			+ "AND idClasse = ( SELECT idClasse FROM Classe WHERE nom=? )";
    	 
    		readerClasse = new BufferedReader(new FileReader("session_temps.txt"));
    		String[] jours = {"lundiCours", "mardiCours", "mercrediCours", "jeudiCours", "vendrediCours"};
    		
    		pstmt = con.prepareStatement(sql);
    		pstmt.setString(1, anneeScolaire);
    		pstmt.setString(2, readerClasse.readLine());
    		
    		rs = pstmt.executeQuery();
    		if(rs.next()) {
    			for (String jour : jours) {
    				String matiere = rs.getString(jour);
    				// convertir chaque matiere recupere en tableau
    				//this.arrayMatiere = matiere.split(";");
    				//System.out.println("matiere du " + jour + ": " + matiere);
    				listMatiere.add(matiere);
    			}
    		}
    		
    		
    		
    	} finally {
    		if (rs != null) rs.close();
    		if(pstmt != null) pstmt.close();
    		if(readerClasse != null) readerClasse.close();
    		
    	}
    	
    	return listMatiere;
    }
    
    @SuppressWarnings("unused")
	private void saveSessionYear(String year) throws IOException {
    	
    	File file = new File(SESSION_ANNE_PATH);
    	// cree d'abord le fichier
    	if(file.createNewFile()) {
    		System.out.println("fichier cree : " + SESSION_ANNE_PATH);
    	}else {
    		System.out.println("fichier deja existant :" + SESSION_ANNE_PATH);
    	}
    	
    	try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
    		writer.write(year);
    	} catch(Exception e) {
    		System.err.println("une erreur est survenue lors de l'ecriture de l'anne dans le fichier");
    	}
    	
    }
    
    @SuppressWarnings("unused")
	private String getSavedSessionYear() throws IOException {
        File file = new File(SESSION_ANNE_PATH);
        if (!file.exists()) {
            throw new FileNotFoundException("Fichier session_anne.txt introuvable à : " + SESSION_ANNE_PATH);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        }
    }
    
    public void printTimeTable() throws DocumentException {
    	try(BufferedReader read = new BufferedReader(new FileReader("session_temps.txt"))) {
    		
    		Stage stage = (Stage) printButton.getScene().getWindow();
    		String classname = read.readLine();
    		
    		FileChooser fileChooser = new FileChooser();
    		fileChooser.setTitle("Enregistrer sous...");
    		
    		// definition de l'extension
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
            
            // suggere un nom de fichier
            fileChooser.setInitialFileName("emploi_temps_"+classname+"_"+this.ligneEmploiTemps.getSchoolYear()+".pdf");
            
            // ouvrir la FileChooser
            File selectedFile = fileChooser.showSaveDialog(stage);
    		
			
			if(selectedFile != null) {
				List<String> getTimeTable = getEmploieTemps(this.ligneEmploiTemps.getSchoolYear());
				GeneratePDF.printByClass(classname, this.ligneEmploiTemps.getSchoolYear() , getTimeTable , selectedFile.getAbsolutePath());
				new DialogBox().infoAlertBox("ENREGISTREMENT OK", "le document a bien été enregistrer");
				
			}else {
				new DialogBox().errorAlertBox("ERROR", "une erreur est survenue");
			}
			
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
    }
    
}
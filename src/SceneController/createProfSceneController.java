package SceneController;

import java.sql.Connection;

import DBManager.DBManager;
import ObjectModel.Professeur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.DialogBox;
import util.EmailValidator;
import util.GenerateMatricule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class createProfSceneController {

	
    @FXML
    private TextField grade;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField telProfesseur;
    @FXML
    private TextField mailProfesseur;
    @FXML
    private TextField searchBar;
    @FXML
    private Button saveProf;
    @FXML
    private Button updateProf;
    @FXML
    private Button deleteProf;
    @FXML
    private Button printExcelFilesButton;
    @FXML
    private Button printPdfFileButton;
    @FXML
    private ComboBox<String> sexeBox = new ComboBox<String>();
    @FXML
    private ComboBox<String> discipline = new ComboBox<String>();

    private DialogBox boxDialog = new DialogBox();
    private List<String> listDiscipline = new ArrayList<String>();

    @FXML
    private TableView<Professeur> tableProfessor;
    @FXML
    private TableColumn<Professeur, String> idColumn;
    @FXML
    private TableColumn<Professeur, String> nameColumn;
    @FXML
    private TableColumn<Professeur, String> surnameColumn;
    @FXML
    private TableColumn <Professeur, Integer> telColumn;
    @FXML
    private TableColumn<Professeur, String> addressColumn;
    @FXML
    private TableColumn<Professeur, String> disciplineColumn;
    @FXML
    private TableColumn<Professeur, String> gradeColumn;
    @FXML
    private TableColumn<Professeur, String> sexeColumn;
    
    private Connection conGlobal = null;
    private PreparedStatement pstmt = null;
    @SuppressWarnings("unused")
	private Professeur professeur;
    @FXML
    private void initialize() {
    	// ajouter les element à la comboBox
    	try 
    	{
    
    		sexeBox.getItems().addAll("neutre", "M", "F");
    		discipline.getItems().addAll(
    				"Français", "Anglais", "Allemand", "Espagnol", 
    				"Chinois", "Italien", "Arabe", "Mathématiques", 
    				"Physique", "Chimie", "Biologie", "SVTEEHB", 
    				"Informatique", "Programmation", "SI", "Reseaux/maintenance", 
    				"Histoire", "Geographie", "ECM", "ALCN", "Dessin", "Eps", "TM");
    		
    		sexeBox.setValue("neutre");
        	discipline.setValue("discipline");
        	updateProf.setDisable(true);
        	deleteProf.setDisable(true);
        	
        	updateProf.setOnAction(event ->{
				try { updateProfessor(); } catch (SQLException e) {e.printStackTrace();}
			});
        	deleteProf.setOnAction(event ->{
        		try { deleteProfessor(); } catch( SQLException e ) { e.printStackTrace();}
        	});
        	searchBar.textProperty().addListener((ObservableList, oldValue, newValue)->{
        		try { filterData(newValue); } catch (Exception e) { e.printStackTrace(); }
        	});
        	mouseClicked();
    		updateTableView();
    		
    		
    	}catch(Exception e) {
    		boxDialog.errorAlertBox("ERROR", "erreur de chargement");
    		e.printStackTrace();
    	}
    }
    
    /* ajout des professeurs */
    public void AddProfessor(ActionEvent evn) throws SQLException {
    	
    	conGlobal = DBManager.connect();
    	String name = this.name.getText();
    	String surname = this.surname.getText();
    	String discipline = this.discipline.getValue().toString();
    	String grade = this.grade.getText();
    	String sexe = this.sexeBox.getValue().toString();
    	String mail = this.mailProfesseur.getText();
    	int tel = Integer.parseInt(telProfesseur.getText());
    	
    	/* Instance d'un nouvel objet Professeur */
    	Professeur professeur = new Professeur.ProfesseurBuilder()
    			.withNomprof(name)
    			.withPrenomprof(surname)
    			.withTel(tel)
    			.withMail(mail)
    			.withDiscipline(discipline)
    			.withGrade(grade)
    			.withSexe(sexe)
    			.build();
    	
    	/* verification des champs */
    	if(name.isEmpty() || surname.isEmpty() || discipline.isEmpty() || grade.isEmpty() || sexe.isEmpty() ||telProfesseur.getText().isEmpty() ) {
    		boxDialog.errorAlertBox("emptyField", "Veuillez remplir tous les champs");
    		
    	} else if(telProfesseur.getText().length() != 9){
    		
    		boxDialog.errorAlertBox("phoneNumberError", "le numero de telephone doit compter 9 chiffres");
    		
    	}else if(!EmailValidator.isValidEmail(mail)) {
    		
    		boxDialog.errorAlertBox("MailError", "l'addresse mail est incorrect");
    		
    	}else if(verifyPhoneNumber(tel) == true) {
    		
    		boxDialog.errorAlertBox("phoneNumberError", "un autre utilisateur utilise ce numero");
    		
    	} else if (verifyMail(mail) == 1) {
    		boxDialog.errorAlertBox("MailError", "un autre utilisateur utilise cette addresse");
    	} else {
    		try {
    		    String matricule = generateMat();
    		    String sql = "INSERT INTO professeur(idProfesseur,nom, prenom, tel, mail, discipline, grade, sexe) "
    		            + "VALUES(?,?,?,?,?,?,?,?)";
    		    pstmt = conGlobal.prepareStatement(sql);
    		    pstmt.setString(1, matricule.toUpperCase());
    		    pstmt.setString(2, professeur.getNomprof().toUpperCase());
    		    pstmt.setString(3, professeur.getPrenomprof().toUpperCase());
    		    pstmt.setInt(4, professeur.getTel());
    		    pstmt.setString(5, professeur.getMail());
    		    pstmt.setString(6, professeur.getDiscipline());
    		    pstmt.setString(7, professeur.getGrade().toUpperCase());
    		    pstmt.setString(8, professeur.getSexe());
    		    pstmt.execute();
    		    
    		    boxDialog.infoAlertBox("SaveOK", "nouveau Professeur enregistré avec succès");
    		    clearField();
    		    updateTableView();

    		} catch (SQLException e) {
    		    boxDialog.errorAlertBox("SQLError", "Une erreur est survenue : " + e.getMessage());
    		    e.printStackTrace();
    		} catch (NumberFormatException e) {
    		    boxDialog.errorAlertBox("NumberError", "Le numéro de téléphone doit comporter uniquement des chiffres");
    		} finally {
    		    try {
    		        if (pstmt != null) pstmt.close();
    		        if (conGlobal != null) conGlobal.close();
    		    } catch (SQLException e) {
    		        e.printStackTrace();
    		    }
    		}

    	}
    		
    }
    /* verification du matricule */
    public String generateMat() throws SQLException {
        String auto = GenerateMatricule.generateMatricule();
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            c = DBManager.connect();
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT DISTINCT idProfesseur FROM Professeur");
            while (rs.next()) {
                if (rs.getString("idProfesseur").equals(auto)) {
                    auto = GenerateMatricule.generateMatricule();
                } else {
                    return auto;
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (c != null) c.close();
        }
        return auto;
    }

    public void clearField() {
    	name.setText("");
    	surname.setText("");
    	grade.setText("");
    	telProfesseur.setText("");
    	mailProfesseur.setText("");
    }
    
    @FXML
    /* fonctionne de mise a jour */
	public void updateProfessor() throws SQLException {
		
		// mise a jour des professeur
		Connection con = null;
		PreparedStatement ps = null;
		String name = this.name.getText();
    	String surname = this.surname.getText();
    	String discipline = this.discipline.getValue().toString();
    	String grade = this.grade.getText();
    	String sexe = this.sexeBox.getValue().toString();
    	String mail = this.mailProfesseur.getText();
    	int tel = Integer.parseInt(telProfesseur.getText());
    	
		String sql = "UPDATE Professeur SET "
				+ "nom = ?,"
				+ "prenom = ?,"
				+ "tel = ?,"
				+ "mail = ?,"
				+ "discipline =?,"
				+ "grade =?,"
				+ "sexe =? WHERE idProfesseur=?";
		try {
			
			if(name.isEmpty() && surname.isEmpty() && discipline.isEmpty() && grade.isEmpty() && sexe.isEmpty() && telProfesseur.getText().isEmpty() ) {
				
	    		boxDialog.errorAlertBox("emptyField", "Veuillez remplir tous les champs");
	    		
	    	} else if(telProfesseur.getText().length() != 9){
	    		
	    		boxDialog.errorAlertBox("phoneNumberError", "le numero de telephone doit compter 9 chiffres");
	    		
	    	}else if(!EmailValidator.isValidEmail(mail)) {
	    		
	    		boxDialog.errorAlertBox("MailError", "l'addresse mail est incorrect");
	    		
	    	}else {
	    		con = DBManager.connect();
				ps = con.prepareStatement(sql);
				ps.setString(1, name);
				ps.setString(2, surname);
				ps.setInt(3, tel);
				ps.setString(4, mail);
				ps.setString(5, discipline);
				ps.setString(6, grade);
				ps.setString(7, sexe);
				ps.setString(8, professeur.getIdProf());
	    		ps.executeUpdate();
	    		updateTableView();
	    		clearField();
	    		boxDialog.infoAlertBox("ModicationOk", "Modification effectuer avec succes");
	    	}
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
		}
		
	}
	/* fonction de suppression */
	public void deleteProfessor() throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = DBManager.connect();
			ps = con.prepareStatement("DELETE FROM Professeur WHERE idProfesseur=?");
			ps.setString(1, this.professeur.getIdProf());
			ps.execute();
			boxDialog.infoAlertBox("DeleteOK", "suppression de l'element effectuer avec succès");
			updateTableView();
			clearField();
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
		}
	}
	
	@FXML
	/* fonction de mise a jours du tableView */
	public void updateTableView() {
		/*
		 *  mise a jour des donnée dans le tableau
		 **/
		try {
			ObservableList<Professeur> list = getProfessorList();
		
			// affectation des valeur de chaque colonne
			idColumn.setCellValueFactory(new PropertyValueFactory<Professeur, String>("idProf"));
			nameColumn.setCellValueFactory(new PropertyValueFactory<Professeur, String>("nomprof"));
			surnameColumn.setCellValueFactory(new PropertyValueFactory<Professeur, String>("prenomprof"));
			telColumn.setCellValueFactory(new PropertyValueFactory<Professeur, Integer>("tel"));
			addressColumn.setCellValueFactory(new PropertyValueFactory<Professeur, String>("mail"));
			disciplineColumn.setCellValueFactory(new PropertyValueFactory<Professeur, String>("discipline"));
			gradeColumn.setCellValueFactory(new PropertyValueFactory<Professeur, String>("grade"));
			sexeColumn.setCellValueFactory(new PropertyValueFactory<Professeur, String>("sexe"));
			
			// ajouter liste d'elemtn dans le tableau
			tableProfessor.setItems(list);
			
		} catch (Exception e) {
			boxDialog.errorAlertBox("ERROR", "une erreur est survenue");
			e.printStackTrace();
		}
		
	}
	
	public ObservableList<Professeur> getProfessorList(){
		/**
		 * ecouteur pour mettre a jour la liste lorsque elle est modifier
		 * */
		Connection con = DBManager.connect();
		ObservableList<Professeur> listProfesseur = FXCollections.observableArrayList();
		try {
	
			String sql = "SELECT * FROM professeur";
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			
			while (res.next()) {
				
				Professeur professeur = new Professeur.ProfesseurBuilder()
						.withIdProf(res.getString("idProfesseur"))
						.withNomprof(res.getString("nom"))
						.withPrenomprof(res.getString("prenom"))
						.withTel(res.getInt("tel"))
						.withMail(res.getString("mail"))
						.withDiscipline(res.getString("discipline"))
						.withGrade(res.getString("grade"))
						.withSexe(res.getString("sexe"))
						.build();
				// ajout des element a la liste du tableau
				listProfesseur.add(professeur);
			}
			
			res.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			boxDialog.errorAlertBox("ERROR", "une erreur est survenue");
			e.printStackTrace();
		}
		
		return listProfesseur;
	}
	
	
	
	@FXML
	/* evenement de la souris sur le tableview */
	public void mouseClicked() {
		/* affecter des valeur au champs lorsque un ligne est selectionner */
		try {
			
			Professeur prof = tableProfessor.getSelectionModel().getSelectedItem();
			prof = new Professeur.ProfesseurBuilder()
					.withIdProf(prof.getIdProf())
					.withNomprof(prof.getNomprof())
					.withPrenomprof(prof.getPrenomprof())
					.withMail(prof.getMail())
					.withDiscipline(prof.getDiscipline())
					.withGrade(prof.getGrade())
					.withSexe(prof.getSexe())
					.withTel(prof.getTel())
					.build();
			this.professeur = prof;
			
			name.setText(prof.getNomprof());
			surname.setText(prof.getPrenomprof());
			telProfesseur.setText(prof.getTel().toString());
			mailProfesseur.setText(prof.getMail());
			discipline.setValue(prof.getDiscipline());
			grade.setText(prof.getGrade());
			sexeBox.setValue(prof.getSexe());
			
			updateProf.setDisable(false);
			deleteProf.setDisable(false);
			
		}catch(Exception e) {
			// lol
		}
	}
	
	/* lister toute les discipline dans le comboBox */
	public List<String> someDiscipline() throws Exception{
    	
    	/* liste toutes les matiere disponible */
		String sql = "SELECT * FROM matiere";
    		
    		Connection con = DBManager.connect();
    		Statement stmt = con.createStatement();
    		ResultSet rs = stmt.executeQuery(sql);
    		
    		while(rs.next()) {
    			String nomMatiere = rs.getString("nom_matiere");
    			listDiscipline.add(nomMatiere);
    		}
    		
    		rs.close();
    		stmt.close();
    		con.close();
			return listDiscipline;
    }
	
	/* filtre de la barre de recherche */
	public void filterData(String search) throws SQLException{
		try {
			ObservableList<Professeur> filterData = FXCollections.observableArrayList();
			ObservableList<Professeur> list = getProfessorList();
			
			for (Professeur prof : list) {
				if (prof.getIdProf().contains(search)
						|| prof.getNomprof().contains(search)
						|| prof.getPrenomprof().contains(search)
						|| prof.getDiscipline().contains(search)
						|| prof.getGrade().contains(search)
						|| prof.getMail().contains(search)
						|| prof.getSexe().contains(search)) {
					filterData.add(prof);
					
				}
			}
			
			tableProfessor.setItems(filterData);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* verification du numero de telephone */
	public boolean verifyPhoneNumber(int phoneNumber) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DBManager.connect();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT tel FROM Professeur");
			while (rs.next()) {
				if (rs.getInt("tel") == phoneNumber) {
					return true;
				}else {
					return false;
				}
			}
		} finally {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (con != null) con.close();
		}
		return null != null;
	}
	
	private int val;
	
	/* verification des addrese */
	public int verifyMail(String mail) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DBManager.connect();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT mail FROM Professeur");
			while (rs.next()) {
				if (rs.getString("mail") == mail) {
					val = 1;
				}else {
					val = 0;
				}
			}
		} finally {
			if (con != null) con.close();
			if(stmt != null) stmt.close();
			if (rs != null ) rs.close();
		}
		
		return val;
	}
}

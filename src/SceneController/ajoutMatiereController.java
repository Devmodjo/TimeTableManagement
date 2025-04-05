package SceneController;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.DialogBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DBManager.DBManager;
import ObjectModel.Matiere;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ajoutMatiereController{
    
    // Constantes pour les messages
    private static final String SAVE_SUCCESS = "Enregistrement réussi";
    private static final String DELETE_SUCCESS = "Suppression réussie";
    private static final String ERROR_TITLE = "Erreur";
    private static final String FIELD_ERROR = "Champ manquant";
    
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
    private TableView<Matiere> tableMatiere;
    @FXML
    private TableColumn<Matiere, String> columnClasse;
    @FXML
    private TableColumn<Matiere, String> columnMatiere;
    
    private DialogBox boxDialog = new DialogBox();
    private Matiere matiere;

    @FXML
    private void initialize() {
        classlist.getItems().addAll(someClasse());
        remove.setOnAction(event -> {
            try {
                removeMatiere();
            } catch (SQLException e) {
                boxDialog.errorAlertBox(ERROR_TITLE, e.getMessage());
            }
        });
        mouseClicked();
        updateTableView();
    }

    public void ajouterMatiere(ActionEvent ev) {
        String fieldMat = fieldAddMat.getText().trim();
        String classfield = classlist.getValue() != null ? classlist.getValue().toString() : "";

        if(!fieldMat.isEmpty() && !classfield.isEmpty()) {
            String sql = "INSERT INTO matiere(nom_matiere, nom_classe) VALUES(?,?)";
            
            try (Connection con = DBManager.connect();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                
                ps.setString(1, fieldMat.toUpperCase());
                ps.setString(2, classfield);
                ps.executeUpdate();
                
                fieldAddMat.clear();
                classlist.setValue(null);
                updateTableView();
                
                boxDialog.infoAlertBox(SAVE_SUCCESS, "Nouvelle matière enregistrée");
                
            } catch(Exception e) {
                boxDialog.errorAlertBox(ERROR_TITLE, e.getMessage());
            }
        } else {
            boxDialog.errorAlertBox(FIELD_ERROR, "Veuillez renseigner tous les champs");
        }
    }

    public List<String> someClasse() {
        List<String> someclasse = new ArrayList<>();
        String sql = "SELECT DISTINCT nom FROM Classe";
        
        try (Connection con = DBManager.connect();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                someclasse.add(rs.getString("nom"));
            }
        } catch (Exception e) {
            boxDialog.errorAlertBox(ERROR_TITLE, e.getMessage());
        }
        return someclasse;
    }

    public void updateTableView() {
        try {
            ObservableList<Matiere> list = getListMatiere();
            
            columnMatiere.setCellValueFactory(new PropertyValueFactory<>("nomMatiere"));
            columnClasse.setCellValueFactory(new PropertyValueFactory<>("nomClasse"));
            
            tableMatiere.setItems(list);
        } catch(Exception e) {
            boxDialog.errorAlertBox(ERROR_TITLE, e.getMessage());
        }
    }

    public void removeMatiere() throws SQLException {
        if(matiere == null) return;
        
        String sql = "DELETE FROM matiere WHERE idMatiere=?";
        try (Connection con = DBManager.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, matiere.getIdMatiere());
            ps.executeUpdate();
            updateTableView();
            boxDialog.infoAlertBox(DELETE_SUCCESS, "Élément supprimé avec succès");
        }
    }

    public ObservableList<Matiere> getListMatiere() throws SQLException {
        ObservableList<Matiere> listMatiere = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT * FROM matiere";
        
        try (Connection con = DBManager.connect();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while(rs.next()) {
                Matiere mat = new Matiere.MatiereBuilder()
                        .withIdMatiere(rs.getInt("idMatiere"))
                        .withNomMatiere(rs.getString("nom_matiere"))
                        .withNomClasse(rs.getString("nom_classe"))
                        .build();
                listMatiere.add(mat);
            }
        }
        return listMatiere;
    }

    public void mouseClicked() {
        Matiere selected = tableMatiere.getSelectionModel().getSelectedItem();
        if(selected != null) {
            this.matiere = new Matiere.MatiereBuilder()
                    .withIdMatiere(selected.getIdMatiere())
                    .withNomMatiere(selected.getNomMatiere())
                    .withNomClasse(selected.getNomClasse())
                    .build();
        }
    }
}

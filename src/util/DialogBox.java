package util;

import javafx.scene.control.Alert;

public class DialogBox {

	// errorBox alert
    public void errorAlertBox(String title, String contain) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(contain);
		alert.showAndWait();
	}
	// infoBox alert
	public void infoAlertBox(String title, String contain) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(contain);
		alert.showAndWait();
	}
	// warningBox alert
	public void warningAlertBox(String title, String contain) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(contain);
		alert.showAndWait();
	}
	// confirmationBox alert
	public void confirmationAlertBox(String title, String contain) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(contain);
		alert.showAndWait();
	}
}

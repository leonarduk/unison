package uk.co.sleonard.unison;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import uk.co.sleonard.unison.gui.generated.UNISoNTabbedFrameFX;

public class UNISoNControllerFX {

	String lineSeparator = System.getProperty("line.separator");

	// Components Variables

	// Reference to the main application
	private UNISoNTabbedFrameFX unisonTabbedFrameFX;

	public UNISoNControllerFX() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	private void initialize() {

	}

	public void setUnisonTabbedFrameFX(UNISoNTabbedFrameFX unisonTabbedFrameFX) {
		this.unisonTabbedFrameFX = unisonTabbedFrameFX;
	}

	@FXML
	private void closeApplication() {
		Platform.exit();
	}

	@FXML
	public void showAboutDialog() {
		// Load the FXML File.
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
			        UNISoNTabbedFrameFX.class.getResource("fxml/AboutDialogLayout.fxml"));
			Pane pane = (Pane) loader.load();
			Scene scene = new Scene(pane);
			Stage aboutDialog = new Stage();
			aboutDialog.setTitle("About");
			aboutDialog.setScene(scene);
			aboutDialog.show();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

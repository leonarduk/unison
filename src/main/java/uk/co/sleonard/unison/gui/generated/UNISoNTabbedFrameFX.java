package uk.co.sleonard.unison.gui.generated;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uk.co.sleonard.unison.UNISoNControllerFX;

/**
 * The class UNISoNTabbedFrameFX
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 */
public class UNISoNTabbedFrameFX extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private TabPane tabs;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("UNISoN");

		initRootLayout();
		showDownloadNewsPanel();
		showMessageStoreViewer();
		showPajekPanel();
	}

	private void initRootLayout() {

		try {
			// Load the FXML File.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(UNISoNTabbedFrameFX.class.getResource("fxml/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			tabs = (TabPane) rootLayout.getChildren().get(2);

			// Show scene
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Shows downloadNewsPanel inside RootLayout.
	public void showDownloadNewsPanel() {
		try {
			// Load the FXML File.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
			        UNISoNTabbedFrameFX.class.getResource("fxml/DownloadNewsPanelLayout.fxml"));
			AnchorPane downloadNewsPanel = (AnchorPane) loader.load();
			tabs.getTabs().get(0).setContent(downloadNewsPanel);

			// Give access of the UNISoNTabbedFrame to Controller.
			UNISoNControllerFX controller = loader.getController();
			controller.setUnisonTabbedFrameFX(this);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Shows messageStoreViewer inside RootLayout.
	public void showMessageStoreViewer() {
		try {
			// Load the FXML File.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
			        UNISoNTabbedFrameFX.class.getResource("fxml/MessageStoreViewerLayout.fxml"));
			AnchorPane messageStoreViewer = (AnchorPane) loader.load();
			tabs.getTabs().get(1).setContent(messageStoreViewer);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Shows PajekPanel inside RootLayout.
	public void showPajekPanel() {
		try {
			// Load the FXML File.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(UNISoNTabbedFrameFX.class.getResource("fxml/PajekPanelLayout.fxml"));
			AnchorPane messageStoreViewer = (AnchorPane) loader.load();
			tabs.getTabs().get(2).setContent(messageStoreViewer);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

}

package uk.co.sleonard.unison.gui.generated;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.gui.SplashScreenFX;

/**
 * The class UNISoNTabbedFrameFX
 * 
 * The class UNISoNTabbedFrameFX is responsible by union of three Stages: DownloadNewsPanel, MessageStoreViewer and PajekPanel.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 */
public class UNISoNTabbedFrameFX extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private TabPane tabs;
	private UNISoNControllerFX unisonController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Loading...");
		showSplashScreen();
	}

	private void showSplashScreen() throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(UNISoNTabbedFrameFX.class.getResource("fxml/SplashScreenLayout.fxml"));
		Pane splashPane = (Pane) loader.load();
		Scene splashScene = new Scene(splashPane);
		primaryStage.setScene(splashScene);
		primaryStage.setTitle("Loading...");
		primaryStage.show();

		SplashScreenFX controller = loader.getController();
		controller.setMainApp(this);
		controller.load();
	}

	public void initRootLayout() {

		try {
			// Load the FXML File.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(UNISoNTabbedFrameFX.class.getResource("fxml/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			tabs = (TabPane) rootLayout.getChildren().get(2);

			unisonController = (UNISoNControllerFX) loader.getController();
			unisonController.setUnisonTabbedFrameFX(this);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showRootLayout() {
		// Show scene
		Scene scene = new Scene(rootLayout);
		primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.setTitle("UNISoN");
		primaryStage.show();

		showDownloadNewsPanel();
		showMessageStoreViewer();
		showPajekPanel();
	}

	// Shows downloadNewsPanel inside RootLayout.
	private void showDownloadNewsPanel() {
		try {
			// Load the FXML File.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
			        UNISoNTabbedFrameFX.class.getResource("fxml/DownloadNewsPanelLayout.fxml"));
			AnchorPane downloadNewsPanel = (AnchorPane) loader.load();
			tabs.getTabs().get(0).setContent(downloadNewsPanel);

			this.unisonController.setUnisonTabbedFrameFX(this);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Shows messageStoreViewer inside RootLayout.
	private void showMessageStoreViewer() {
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
	private void showPajekPanel() {
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

	public UNISoNControllerFX getUnisonController() {
		return this.unisonController;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.exit(1);
	}

	public static void main(String[] args) {
		launch(args);
	}

}

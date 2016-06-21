package uk.co.sleonard.unison.gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.datahandling.UNISoNDatabase;
import uk.co.sleonard.unison.gui.generated.UNISoNTabbedFrameFX;

/**
 * The class SplashScreen
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since 18 de jun de 2016
 *
 */
public class SplashScreenFX {

	@FXML
	private ProgressBar progressBar;

	// Reference to the main application
	private UNISoNTabbedFrameFX main;

	public SplashScreenFX() {
	}

	@FXML
	private void initialize() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				setProgress(0.5);
				return null;
			}

			@Override
			protected void succeeded() {
				Platform.runLater(new Runnable() {
			        public void run() {
				        main.initRootLayout();
				        UNISoNControllerFX controller = main.getUnisonController();
				        final UNISoNDatabase database = controller.getDatabase();
				        setProgress(1.0);
				        main.getPrimStage().close();
				        main.showRootLayout();
			        }
		        });
			}
		};
		new Thread(task).start();
	}

	public void initializeDB() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				UNISoNControllerFX controller = main.getUnisonController();

				final UNISoNDatabase database = controller.getDatabase();
				// database.addObserver(this.downloadNewsPanel1);
		        // database.addObserver(this.messageStoreViewer1);
		        // database.addObserver(this.pajekPanel1);
		        // database.addObserver(this); ADD LATER MY OBSERVERS
		        // database.refreshDataFromDatabase();

				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				setProgress(1.0);
				main.getPrimStage().close();
				main.showRootLayout();
			}
		};

		new Thread(task).start();
	}

	public void setProgress(Double value) {
		progressBar.setProgress(value);
	}

	public Double getProgress() {
		return progressBar.getProgress();
	}

	public void setMainApp(UNISoNTabbedFrameFX main) {

		this.main = main;

	}

}

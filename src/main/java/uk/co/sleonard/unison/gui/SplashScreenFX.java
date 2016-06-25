package uk.co.sleonard.unison.gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.datahandling.UNISoNDatabase;

/**
 * The class SplashScreen
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since 18 de jun de 2016
 *
 */
public class SplashScreenFX {

	// Components Variables
	@FXML
	private ProgressBar progressBar;

	// Reference to the main application
	private UNISoNTabbedFrameFX main;

	public SplashScreenFX() {
	}

	@FXML
	private void initialize() {

	}

	public void load() {
		setProgress(0.2);
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				main.initRootLayout();
				UNISoNControllerFX controller = main.getUnisonController();
				final UNISoNDatabase database = controller.getDatabase();
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
				        setProgress(1.0);
				        main.getPrimStage().close();
				        main.showRootLayout();
			        }
		        });
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

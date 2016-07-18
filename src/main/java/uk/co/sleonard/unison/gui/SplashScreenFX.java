package uk.co.sleonard.unison.gui;

import java.util.Observable;
import java.util.Observer;

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
 * @since 18-Jun-2016
 */
public class SplashScreenFX implements Observer {

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
		Observer o = this; // Reference to SplashScreenFX used inside TASK.
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				SplashScreenFX.this.main.initRootLayout();
				UNISoNControllerFX controller = SplashScreenFX.this.main.getUnisonController();
				final UNISoNDatabase database = controller.getDatabase();
				database.addObserver(o);
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						setProgress(0.8);
						SplashScreenFX.this.main.getPrimStage().close();
						SplashScreenFX.this.main.showRootLayout();
					}
				});
			}
		};
		new Thread(task).start();
	}

	public void setProgress(Double value) {
		this.progressBar.setProgress(value);
	}

	public Double getProgress() {
		return this.progressBar.getProgress();
	}

	public void setMainApp(UNISoNTabbedFrameFX main) {

		this.main = main;

	}

	@Override
	public void update(Observable observable, Object arg) {
		if (observable instanceof UNISoNDatabase) {
			UNISoNControllerFX.getGui().showAlert("GUI has been refreshed from the database");
		}
	}

}

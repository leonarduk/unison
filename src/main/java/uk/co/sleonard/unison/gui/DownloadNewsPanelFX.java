package uk.co.sleonard.unison.gui;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.datahandling.DAO.DownloadRequest.DownloadMode;
import uk.co.sleonard.unison.input.DataHibernatorWorker;
import uk.co.sleonard.unison.input.HeaderDownloadWorker;
import uk.co.sleonard.unison.input.NNTPNewsGroup;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The class DownloadNewsPanelFX, Controller of the Tab Download Messages.
 *
 * @author Elton <elton_12_nunes@hotmail.com>
 * @since 25-Jun-2016
 */
public class DownloadNewsPanelFX implements UNISoNLogger, Observer {

	/** -------------------- Components Variables ------------------ */
	/** The newsgroup field. */
	@FXML
	private TextField newsGroupField;
	/** The host combo. */
	@FXML
	private ComboBox<String> hostCombo;
	/** The available newsgroups. */
	@FXML
	private ListView<NNTPNewsGroup> availableNewsgroups;
	/** The find button. */
	@FXML
	private Button findButton;
	/** The notes area. */
	@FXML
	private TextArea notesArea;
	/** The cancel button. */
	@FXML
	private Button cancelButton;
	/** The download button. */
	@FXML
	private Button downloadButton;
	/** The from date field. */
	@FXML
	private TextField fromDateField;
	/** The from date label. */
	@FXML
	private Label fromDateLabel;
	/** The get location check. */
	@FXML
	private CheckBox getLocationCheck;
	/** The pause button. */
	@FXML
	private Button pauseButton;
	/** The to date field. */
	@FXML
	private TextField toDateField;
	/** The to date label. */
	@FXML
	private Label toDateLabel;
	/** -------------------- Components Variables ------------------ */

	private UNISoNControllerFX controller;

	/** The available groups. */
	private Set<NNTPNewsGroup> availableGroups;

	StringBuffer logText;

	public DownloadNewsPanelFX() {

	}

	public static void main(String[] args) {
		UNISoNTabbedFrameFX.main(args);
	}

	@FXML
	private void initialize() {
		this.controller = UNISoNControllerFX.getInstance();
		logText = new StringBuffer();

		this.controller.getHeaderDownloader().addObserver(this);
		DataHibernatorWorker.setLogger(this);
		this.controller.setDownloadPanel(this);

		List<String> listServers = Arrays.asList(StringUtils.loadServerList());
		ObservableList<String> list = FXCollections.observableList(listServers);
		this.hostCombo.setItems(list);
	}

	@FXML
	private void findGroups() {
		this.controller.setStatusLabel("Loading groups...");
		this.controller.setStatusProgress(-1.0);

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				final String host = hostCombo.getSelectionModel().getSelectedItem().toString()
		                .trim();
				controller.setNntpHost(host);
				final String group = newsGroupField.getText();
				log("Find groups matching : " + group + " on " + controller.getNntpHost());
				downloadEnabled(false);
				if (null != group) {
					try {
						availableGroups = controller.listNewsgroups(group, host);
					}
					catch (UNISoNException e) {
						alert("Problem downloading: " + e.getMessage());
					}
					if ((null == availableGroups) || (availableGroups.size() == 0)) {
						alert("No groups found for string : " + group + " on "
		                        + controller.getNntpHost() + ".\nPerhaps another host?");

					}
					else {
						downloadEnabled(true);
					}
					availableNewsgroups.setItems(getAvailableGroupsItems());
				}
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
				        controller.setStatusLabel("");
				        controller.setStatusProgress(0.0);
			        }
		        });

			}
		};
		new Thread(task).start();
	}

	/**
	 * Download button action performed.
	 */
	@FXML
	private void downloadButton() {
		this.downloadEnabled(false);

		final Object[] items = this.availableNewsgroups.getSelectionModel().getSelectedItems()
		        .toArray();
		final Set<NNTPNewsGroup> groups = new HashSet<>();
		for (final Object item : items) {
			groups.add((NNTPNewsGroup) item);
		}
		if (groups.size() > 0) {
			try {
				this.log("Download : " + groups);

				final Date fromDate = StringUtils.stringToDate(this.fromDateField.getText());
				final Date toDate = StringUtils.stringToDate(this.toDateField.getText());

				DownloadMode mode;
				// FIXME Disable until start improving gui
				// if (this.getTextCheck.isSelected()) {
				// mode = DownloadMode.ALL;
				// }
				// else
				if (this.getLocationCheck.isSelected()) {
					mode = DownloadMode.HEADERS;
				}
				else {
					mode = DownloadMode.BASIC;
				}
				this.controller.quickDownload(groups, fromDate, toDate, this, mode);

				this.log("Done.");
			}
			catch (final UNISoNException e) {
				this.alert("Failed to download. Check your internet connection");
				this.downloadEnabled(true);
			}
			catch (final DateTimeParseException e) {
				this.alert("Failed to parse date : " + e.getMessage());
				this.downloadEnabled(true);
			}
		}
	}

	/**
	 * Pause button action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	@FXML
	private void pauseButton() {
		final HeaderDownloadWorker headerDownloader = this.controller.getHeaderDownloader();
		if (headerDownloader.isDownloading()) {
			headerDownloader.pause();
			this.pauseButton.setText("Resume");
		}
		else {
			this.pauseButton.setText("Pause");
			headerDownloader.resume();
		}
	}

	/**
	 * Cancel button action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	@FXML
	private void cancelButton() {
		this.controller.getHeaderDownloader().fullstop();
	}

	/**
	 * Gets the available groups model.
	 *
	 * @return the available groups model
	 */
	private ObservableList<NNTPNewsGroup> getAvailableGroupsItems() {
		List<NNTPNewsGroup> listGroups = new ArrayList<>(availableGroups);
		return FXCollections.observableList(listGroups);
	}

	/**
	 * Download enabled.
	 *
	 * @param enabled
	 *            the enabled
	 */
	private void downloadEnabled(final boolean enabled) {
		this.downloadButton.setDisable(!enabled);
		this.fromDateLabel.setDisable(!enabled);
		this.toDateLabel.setDisable(!enabled);
		this.fromDateField.setDisable(!enabled);
		this.toDateField.setDisable(!enabled);
		this.getLocationCheck.setDisable(!enabled);

		// these are off when download on & vice versa
		this.cancelButton.setDisable(enabled);
		this.pauseButton.setDisable(enabled);
	}

	@Override
	public void alert(String message) {
		// Changed to platform runlater to ensures what are thread fx
		Platform.runLater(() -> {
			this.log(message);
			UNISoNControllerFX.getGui().showAlert(message);
		});
	}

	@Override
	public void log(String message) {
		// Changed to platform runlater to ensures what are thread fx
		Platform.runLater(() -> {
			this.logText.append(message + "\n");
			this.notesArea.setText(this.logText.toString());
		});

	}

	@Override
	public void update(Observable observable, Object arg) {
		if (observable instanceof HeaderDownloadWorker) {
			final HeaderDownloadWorker headerDownloader = (HeaderDownloadWorker) observable;
			if (!headerDownloader.isDownloading()) {
				this.downloadEnabled(true);
			}
			// } else if (observable instanceof UNISoNController) {
			// UNISoNController controller = (UNISoNController) observable;
		}
	}
}

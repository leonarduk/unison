package uk.co.sleonard.unison.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.UNISoNLogger;
import uk.co.sleonard.unison.input.NNTPNewsGroup;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The class DownloadNewsPanelFX, Controller of the Tab Download Messages.
 * 
 * @author Elton <elton_12_nunes@hotmail.com>
 */
public class DownloadNewsPanelFX implements UNISoNLogger {

	// Components Variables
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
				// this.downloadEnabled(false); TODO Implement later.
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
						// this.downloadEnabled(true); TODO Implement later
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
	 * Gets the available groups model.
	 *
	 * @return the available groups model
	 */
	private ObservableList<NNTPNewsGroup> getAvailableGroupsItems() {
		List<NNTPNewsGroup> listGroups = new ArrayList<>(availableGroups);
		return FXCollections.observableList(listGroups);
	}

	@Override
	public void alert(String message) {
		this.log(message);
		UNISoNControllerFX.getGui().showAlert(message);
	}

	@Override
	public void log(String message) {
		this.logText.append(message + "\n");
		this.notesArea.setText(this.logText.toString());

	}
}

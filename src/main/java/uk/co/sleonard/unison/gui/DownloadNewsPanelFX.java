/**
 * DownloadNewsPanelFX
 *
 * @author ${author}
 * @since 25-Jun-2016
 */
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
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
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
	private TextField			newsGroupField;
	/** The host combo. */
	@FXML
	private ComboBox<String>	hostCombo;
	/** The available newsgroups. */
	@FXML
	private ListView<NewsGroup>	availableNewsgroups;
	/** The find button. */
	@FXML
	private Button				findButton;
	/** The notes area. */
	@FXML
	private TextArea			notesArea;

	private UNISoNControllerFX controller;

	/** The available groups. */
	private Set<NewsGroup> availableGroups;

	StringBuffer logText;

	public static void main(final String[] args) {
		UNISoNTabbedFrameFX.main(args);
	}

	public DownloadNewsPanelFX() {

	}

	@Override
	public void alert(final String message) {
		this.log(message);
		UNISoNControllerFX.getGui().showAlert(message);
	}

	@FXML
	private void findGroups() {
		this.controller.setStatusLabel("Loading groups...");
		this.controller.setStatusProgress(-1.0);

		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				final String host = DownloadNewsPanelFX.this.hostCombo.getSelectionModel()
		                .getSelectedItem().toString().trim();
				DownloadNewsPanelFX.this.controller.setNntpHost(host);
				final String group = DownloadNewsPanelFX.this.newsGroupField.getText();
				DownloadNewsPanelFX.this.log("Find groups matching : " + group + " on "
		                + DownloadNewsPanelFX.this.controller.getNntpHost());
				// this.downloadEnabled(false); TODO Implement later.
				if (null != group) {
					try {
						DownloadNewsPanelFX.this.availableGroups = DownloadNewsPanelFX.this.controller
		                        .listNewsgroups(group, host);
					}
					catch (final UNISoNException e) {
						DownloadNewsPanelFX.this.alert("Problem downloading: " + e.getMessage());
					}
					if ((null == DownloadNewsPanelFX.this.availableGroups)
		                    || (DownloadNewsPanelFX.this.availableGroups.size() == 0)) {
						DownloadNewsPanelFX.this.alert("No groups found for string : " + group
		                        + " on " + DownloadNewsPanelFX.this.controller.getNntpHost()
		                        + ".\nPerhaps another host?");

					}
					else {
						// this.downloadEnabled(true); TODO Implement later
					}
					DownloadNewsPanelFX.this.availableNewsgroups
		                    .setItems(DownloadNewsPanelFX.this.getAvailableGroupsItems());
				}
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				Platform.runLater(() -> {
					DownloadNewsPanelFX.this.controller.setStatusLabel("");
					DownloadNewsPanelFX.this.controller.setStatusProgress(0.0);
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
	private ObservableList<NewsGroup> getAvailableGroupsItems() {
		final List<NewsGroup> listGroups = new ArrayList<>(this.availableGroups);
		return FXCollections.observableList(listGroups);
	}

	@FXML
	private void initialize() {
		this.controller = UNISoNControllerFX.getInstance();
		this.logText = new StringBuffer();

		final List<String> listServers = Arrays.asList(StringUtils.loadServerList());
		final ObservableList<String> list = FXCollections.observableList(listServers);
		this.hostCombo.setItems(list);
	}

	@Override
	public void log(final String message) {
		this.logText.append(message + "\n");
		this.notesArea.setText(this.logText.toString());

	}
}

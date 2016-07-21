/*
 * PajekPanel.java
 *
 * Created on 03 December 2007, 08:48
 */

package uk.co.sleonard.unison.gui;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.hibernate.Session;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uk.co.sleonard.unison.UNISoNControllerFX;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DataQuery;
import uk.co.sleonard.unison.datahandling.UNISoNDatabase;
import uk.co.sleonard.unison.datahandling.DAO.Message;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.output.ExportToCSV;
import uk.co.sleonard.unison.output.PajekNetworkFile;
import uk.co.sleonard.unison.utils.StringUtils;

/**
 * The Class PajekPanel.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class PajekPanelFX implements Observer {

	/** The Constant PAJEK_NETWORK_FILE_SUFFIX. */
	private static final String PAJEK_NETWORK_FILE_SUFFIX = ".net";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 84102596787648747L;

	/** The pajek file. */
	private PajekNetworkFile pajekFile;

	/** The csv exporter. */
	private final ExportToCSV csvExporter = new ExportToCSV();

	/** The session. */
	private Session session;

	/** The main stage. */
	private Stage stage;

	/** The pajek header. */
	// private final Vector<String> pajekHeader;

	/** -------------------- Components Variables ------------------ */

	/** The all radio. */
	@FXML
	private RadioButton allRadio;

	/** The creator radio. */
	@FXML
	private RadioButton creatorRadio;

	/** The csv button. */
	@FXML
	private Button csvButton;

	/** The previous radio. */
	@FXML
	private RadioButton previousRadio;

	/** The file preview area. */
	@FXML
	private TextArea filePreviewArea;

	/** The preview button. */
	@FXML
	private Button previewButton;

	/** The graph scroll pane. */
	@FXML
	private ScrollPane graphScrollPane;

	/** The results matrix table. */
	@FXML
	private TableView<Object> resultsMatrixTable;

	/** The save button. */
	@FXML
	private Button saveButton;

	/** END----------------- Components Variables ---------------END */

	/**
	 * The initialize() method is called (if it is present) after the loading of
	 * the scene graph is complete (so all the GUI objects will have been
	 * instantiated) but before control has returned to your application's
	 * invoking code.
	 * 
	 */
	@FXML
	public void initialize() {
		TableColumn columnSubject = new TableColumn<>("Subject");
		TableColumn columnDate = new TableColumn<>("Date");
		TableColumn columnFrom = new TableColumn<>("FROM");
		TableColumn columnTo = new TableColumn<>("TO");
		this.resultsMatrixTable.getColumns().addAll(columnSubject, columnDate, columnFrom, columnTo);

		try {

			this.session = UNISoNControllerFX.getInstance().getHelper().getHibernateSession();

			// TODO create file to put the no messages on vector
			// TODO create file to put the location as cluster (use country)

			// this.resultsMatrixTable.setDisable(true); TODO Maybe reactivate
			this.getFilePreviewArea().setEditable(false);
			this.previousRadio.setSelected(true);
			this.refreshPajekMatrixTable();
		} catch (final UNISoNException e) {
			UNISoNControllerFX.getInstance();
			UNISoNControllerFX.getGui().showAlert("Error: " + e.getMessage());
		}

	}

	/**
	 * Creates the new row.
	 *
	 * @param rowIndex
	 *            the row index
	 * @param currentMsg
	 *            the current msg
	 * @param originalMsg
	 *            the original msg
	 * @return the vector
	 */
	private Vector<String> createNewRow(final int rowIndex, final Message currentMsg, final Message originalMsg) {
		String toText = "";

		if (null != originalMsg) {
			toText = originalMsg.getPoster().getName() + " [" + originalMsg.getPoster().getEmail() + "]";
		}
		final Vector<String> row = new Vector<>();
		row.add(currentMsg.getSubject());
		row.add("" + currentMsg.getDateCreated());

		final UsenetUser poster = currentMsg.getPoster();

		String fromText = "MISSING";
		if (null != poster) {
			fromText = poster.getName() + " [" + poster.getEmail() + "]";
		}
		row.add(fromText);
		row.add(toText);
		return row;
	}

	/**
	 * Csv button action performed.
	 */
	@FXML
	private void csvButtonFile() {
		/*
		 * try { this.csvExporter.exportTableToCSV(this.resultsMatrixTable,
		 * this.pajekHeader); } catch (final UNISoNException e) {
		 * e.printStackTrace(); }
		 */
	}

	public TextArea getFilePreviewArea() {
		return this.filePreviewArea;
	}

	/**
	 * Gets the latest pajek matrix vector.
	 *
	 * @return the latest pajek matrix vector
	 */
	private Vector<Vector<String>> getLatestPajekMatrixVector() {
		Vector<Vector<String>> tableData;
		final List<Message> messages = UNISoNControllerFX.getInstance().getFilter().getMessagesFilter();
		final HashMap<String, Message> msgMap = new HashMap<>();

		// Load ALL messages into map so can get complete referemce
		final Vector<Message> allMessages = DataQuery.getInstance().getMessages(null, null, this.session, null, null,
				false, null, null);
		for (final Object next : allMessages) {
			final Message msg = (Message) next;
			msgMap.put(msg.getUsenetMessageID(), msg);
		}

		tableData = new Vector<>();
		int rowIndex = 1;
		for (final ListIterator<Message> msgIter = messages.listIterator(); msgIter.hasNext();) {
			final Message next = msgIter.next();
			Message lastMsg = null;

			final List<String> refMsgs = StringUtils.convertStringToList(next.getReferencedMessages(), " ");
			int size;
			try {
				size = refMsgs.size();
			} catch (final Exception e) {
				e.printStackTrace();
				size = 0;
			}
			if ((null != refMsgs) && (size > 0)) {
				final List<String> msgList = StringUtils.convertStringToList(next.getReferencedMessages(), " ");

				if (this.allRadio.isSelected()) {

					final ListIterator<String> iter = msgList.listIterator();
					while (iter.hasNext()) {
						final Message msg = this.getReferencedMessage(msgMap, iter.next(), next);
						tableData.add(this.createNewRow(rowIndex++, next, msg));
					}
				} else {
					if (this.previousRadio.isSelected()) {
						lastMsg = this.getReferencedMessage(msgMap, refMsgs.get(refMsgs.size() - 1), next);
					} else if (this.creatorRadio.isSelected()) {
						lastMsg = this.getReferencedMessage(msgMap, refMsgs.get(0), next);
					}
					tableData.add(this.createNewRow(rowIndex++, next, lastMsg));
				}
			} else {
				tableData.add(this.createNewRow(rowIndex++, next, null));
			}

		}
		return tableData;
	}

	/**
	 * Gets the referenced message.
	 *
	 * @param msgMap
	 *            the msg map
	 * @param key
	 *            the key
	 * @param currentMessage
	 *            the current message
	 * @return the referenced message
	 */
	public Message getReferencedMessage(final HashMap<String, Message> msgMap, final String key,
			final Message currentMessage) {
		Message message = msgMap.get(key);

		// create dummy message for missing ones
		if ((null != key) && key.contains("@") && (null == message)) {
			// This message and poster is not saved to the database
			final UsenetUser poster = new UsenetUser("MISSING", key, "UNKNOWN", null, null);
			message = new Message(null, key, currentMessage.getSubject(), poster, currentMessage.getTopic(), null,
					currentMessage.getReferencedMessages().replaceAll(key, ""), null);
		}
		return message;
	}

	/**
	 * Preview button action performed.
	 */
	@FXML
	private void previewButtonAction() {
		UNISoNControllerFX.getInstance().getDatabase().notifyObservers();
	}

	/**
	 * Refresh pajek matrix table.
	 *
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	private void refreshPajekMatrixTable() throws UNISoNException {
		final ObservableList<Object> model = this.resultsMatrixTable.getItems();
		model.add(this.getLatestPajekMatrixVector());

		// filePreviewArea
		this.pajekFile = new PajekNetworkFile();
		// this.pajekFile.createDirectedLinks(new Vector<>(model));
		// this.graphScrollPane.removeAll(); VERIFY TODO

		final GraphPreviewPanel previewPanel = this.pajekFile.getPreviewPanel();

		// SwingNode test
		SwingNode swingNode = new SwingNode();
		swingNode.setContent(previewPanel);
		Pane pane = new Pane();
		pane.getChildren().add(swingNode);

		this.graphScrollPane.setContent(pane);
		previewPanel.repaint();
		// this.graphScrollPane.setSize(200.0, 200.0);

		// clear down for next set of data
		this.getFilePreviewArea().setText("");
		final OutputStream output = new OutputStream() {

			@Override
			public void write(final int b) throws IOException {
				final char letter = (char) b;
				PajekPanelFX.this.getFilePreviewArea().getText().concat("" + letter);
			}

		};
		final PrintStream stream = new PrintStream(output, true);
		this.pajekFile.writeData(stream);
		previewPanel.repaint();
	}

	/**
	 * Save button action performed.
	 */
	@FXML
	private void saveButtonFile() {
		final FileChooser file = new FileChooser();
		file.setTitle("Save Pajek Network File");
		final String initialValue = "*" + PajekPanelFX.PAJEK_NETWORK_FILE_SUFFIX;
		file.setInitialFileName(initialValue); // set initial filename filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pajek Network File (.NET)",
				"*" + PajekPanelFX.PAJEK_NETWORK_FILE_SUFFIX);
		file.getExtensionFilters().add(extFilter);
		File archive = file.showSaveDialog(this.stage);
		String curFile = null;
		curFile = archive.getName();
		if ((curFile != null) && !curFile.equals(initialValue)) {

			if (!curFile.endsWith(PajekPanelFX.PAJEK_NETWORK_FILE_SUFFIX)) {
				curFile += PajekPanelFX.PAJEK_NETWORK_FILE_SUFFIX;
			}
			final String filename = archive.getAbsolutePath() + curFile;
			final File saveFile = new File(filename); // chooser.getSelectedFile();
			final String fileName = saveFile.getAbsolutePath();
			this.pajekFile.saveToFile(fileName);
			this.showStatus("Saved file " + filename);

		} else {
			this.showStatus("You cancelled.");
		}

	}

	public void setFilePreviewArea(final TextArea filePreviewArea) {
		this.filePreviewArea = filePreviewArea;
	}

	/**
	 * Show status.
	 *
	 * @param string
	 *            the string
	 */
	private void showStatus(final String string) {
		UNISoNControllerFX.getInstance();
		UNISoNControllerFX.getGui().showStatus(string);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(final Observable observable, final Object arg1) {
		if (observable instanceof UNISoNDatabase) {
			// UNISoNController controller = (UNISoNController) observable;
			try {
				this.refreshPajekMatrixTable();
			} catch (final UNISoNException e) {
				e.printStackTrace();
			}
		}
	}

	protected void setStage(Stage stage) {
		this.stage = stage;
	}

}

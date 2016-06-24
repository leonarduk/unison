/**
 * UNISoNGUI
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class UNISoNGUIFX {
	/**
	 * Ask question.
	 *
	 * @param question
	 *            the question
	 * @param options
	 *            the options
	 * @param title
	 *            the title
	 * @param defaultOption
	 *            the default option
	 * @return the int
	 */
	public int askQuestion(final String question, final String[] options, final String title,
	        final String defaultOption) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(question);
		
		List<ButtonType> buttons = new ArrayList<>();
		for (String name : options) {
			ButtonType button = new ButtonType(name);
			buttons.add(button);
		}
		ButtonType cancel = new ButtonType(defaultOption, ButtonData.CANCEL_CLOSE);
		buttons.add(cancel);
		alert.getButtonTypes().setAll(buttons);
		Optional<ButtonType> result = alert.showAndWait();

		for (int i = 0; i < buttons.size(); i++) {
			if (result.get() == buttons.get(i)){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Show alert.
	 *
	 * @param message
	 *            the message
	 */
	public void showAlert(final String messageText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(messageText);
		alert.showAndWait();
	}


}

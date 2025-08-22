package gui.subpanels;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import constants.GUIConstants;
import constants.UserStringConstants;
import persistence.mariaDB.DBManager;

/**
 * Panel showing quiz statistics and information.
 * <p>
 * Displays a HTML formatted overview of all themes along with the number of
 * questions each contains.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuizInfoViewPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private final JEditorPane infoPane;

	/**
	 * Constructs the quiz info view panel with database manager integration.
	 * 
	 * @param dbManager The MariaDB database manager for retrieving statistics
	 */
	public QuizInfoViewPanel(DBManager dbManager) {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout());

		infoPane = new JEditorPane();
		infoPane.setContentType(UserStringConstants.HTML_CONTENT_TYPE);
		infoPane.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(infoPane);
		add(scrollPane, BorderLayout.CENTER);
		showWelcomeMessage();
	}

	/**
	 * Shows the correct answer in the info panel.
	 * 
	 * @param correctAnswer The correct answer text to display
	 */
	public void showCorrectAnswer(String correctAnswer) {
		StringBuilder html = new StringBuilder();
		html.append("<html><body>");
		html.append("<h2 style='color: green;'>Richtige Antwort:</h2>");
		html.append("<p style='font-size: 14px; background-color: #e8f5e8; padding: 10px; border-radius: 5px;'>");
		html.append("<b>").append(correctAnswer != null ? correctAnswer : "Keine Antwort gefunden").append("</b>");
		html.append("</p>");
		html.append("</body></html>");

		infoPane.setText(html.toString());
		infoPane.setCaretPosition(0);
	}

	/**
	 * Shows feedback about the user's answer (correct or incorrect).
	 * 
	 * @param isCorrect true if the answer was correct, false otherwise
	 * @param message the feedback message to display
	 */
	public void showAnswerFeedback(boolean isCorrect, String message) {
		StringBuilder html = new StringBuilder();
		html.append("<html><body>");
		
		if (isCorrect) {
			html.append("<h2 style='color: green;'>✓ Richtig!</h2>");
			html.append("<p style='font-size: 14px; background-color: #e8f5e8; padding: 10px; border-radius: 5px; color: green;'>");
		} else {
			html.append("<h2 style='color: red;'>✗ Falsch!</h2>");
			html.append("<p style='font-size: 14px; background-color: #ffe8e8; padding: 10px; border-radius: 5px; color: red;'>");
		}
		
		html.append("<b>").append(message != null ? message : "").append("</b>");
		html.append("</p>");
		html.append("</body></html>");

		infoPane.setText(html.toString());
		infoPane.setCaretPosition(0);
	}

	/**
	 * Shows a welcome message in the info panel.
	 */
	private void showWelcomeMessage() {
		StringBuilder html = new StringBuilder();
		html.append("<html><body>");
		html.append("<h2>Quiz Info</h2>");
		html.append(
				"<p>Wählen Sie eine Antwort aus und klicken Sie auf 'Antwort zeigen', um die korrekte Lösung zu sehen.</p>");
		html.append("</body></html>");

		infoPane.setText(html.toString());
		infoPane.setCaretPosition(0);
	}
}
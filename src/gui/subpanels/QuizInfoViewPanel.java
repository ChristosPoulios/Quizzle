package gui.subpanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import constants.GUIConstants;
import constants.UserStringConstants;
import persistence.mariaDB.DBManager;
import quizlogic.dto.ThemeDTO;

/**
 * Panel showing quiz information.
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
	private final JComboBox<String> themeComboBox;
	private ThemeSelectionDelegate delegate;

	/**
	 * Interface for handling theme selection events.
	 */
	public interface ThemeSelectionDelegate {
		void onThemeSelected(String themeTitle);
	}

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

		themeComboBox = new JComboBox<>();
		themeComboBox.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH - QUIZ_INFO_COMBO_OFFSET, COMBOBOX_HEIGHT));
		themeComboBox.setBackground(TEXTFIELD_BACKGROUND);
		themeComboBox.setEditable(false);
		themeComboBox.addItem(UserStringConstants.ALL_THEMES_OPTION);

		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		populateThemeComboBox(themes);

		themeComboBox.addActionListener(_ -> {
			String selectedTheme = (String) themeComboBox.getSelectedItem();
			if (delegate != null && selectedTheme != null) {
				delegate.onThemeSelected(selectedTheme);
			}
		});

		JPanel headerPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
		headerPanel.setBackground(BACKGROUND_COLOR);
		headerPanel.setBorder(
				BorderFactory.createEmptyBorder(PANEL_MARGIN_V, PANEL_MARGIN_H, PANEL_MARGIN_V, PANEL_MARGIN_H));
		headerPanel.add(themeComboBox);

		JScrollPane scrollPane = new JScrollPane(infoPane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);

		add(headerPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT));
		setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT));
		setMinimumSize(
				new Dimension(RIGHT_PANEL_WIDTH - PANEL_MARGIN_OFFSET, MAIN_CONTENT_HEIGHT - PANEL_MARGIN_OFFSET));

		showWelcomeMessage();
	}

	/**
	 * Sets the delegate for theme selection events.
	 * 
	 * @param delegate The delegate to set
	 */
	public void setThemeSelectionDelegate(ThemeSelectionDelegate delegate) {
		this.delegate = delegate;
	}

	/**
	 * Updates the theme combo box with the available themes from the database.
	 * 
	 * @param themes The list of theme DTOs to populate the combo box
	 */
	public void updateThemeComboBox(ArrayList<ThemeDTO> themes) {
		themeComboBox.removeAllItems();
		themeComboBox.addItem(UserStringConstants.ALL_THEMES_OPTION);
		populateThemeComboBox(themes);
	}

	/**
	 * Populates the theme combo box with sorted themes.
	 * Themes without descriptions are marked with "*" and listed first,
	 * then themes with descriptions, both groups sorted alphabetically.
	 * 
	 * @param themes The list of theme DTOs to populate the combo box
	 */
	private void populateThemeComboBox(ArrayList<ThemeDTO> themes) {
		// Separate themes with and without descriptions
		ArrayList<ThemeDTO> themesWithoutDescription = new ArrayList<>();
		ArrayList<ThemeDTO> themesWithDescription = new ArrayList<>();
		
		for (ThemeDTO theme : themes) {
			if (theme.getThemeDescription() == null || theme.getThemeDescription().trim().isEmpty()) {
				themesWithoutDescription.add(theme);
			} else {
				themesWithDescription.add(theme);
			}
		}
		
		// Sort both lists alphabetically by title
		Comparator<ThemeDTO> titleComparator = Comparator.comparing(ThemeDTO::getThemeTitle);
		themesWithoutDescription.sort(titleComparator);
		themesWithDescription.sort(titleComparator);
		
		// Add themes without description first (marked with *)
		for (ThemeDTO theme : themesWithoutDescription) {
			themeComboBox.addItem("* " + theme.getThemeTitle());
		}
		
		// Add themes with description
		for (ThemeDTO theme : themesWithDescription) {
			themeComboBox.addItem(theme.getThemeTitle());
		}
	}

	/**
	 * Shows the correct answer in the info panel.
	 * 
	 * @param correctAnswer The correct answer text to display
	 */
	public void showCorrectAnswer(String correctAnswer) {
		StringBuilder html = new StringBuilder();
		html.append("<html><body style='font-family: Helvetica, Arial, sans-serif; font-size: 16px;'>");
		html.append(
				"<h2 style='color: green; font-family: Helvetica, Arial, sans-serif; font-size: 20px; font-weight: bold;'>")
				.append(UserStringConstants.QUIZ_INFO_CORRECT_ANSWER_HEADER).append("</h2>");
		html.append(
				"<p style='font-family: Helvetica, Arial, sans-serif; font-size: 16px; background-color: #e8f5e8; padding: 10px; border-radius: 5px;'>");
		html.append("<b>").append(correctAnswer != null ? correctAnswer : UserStringConstants.QUIZ_INFO_NO_ANSWER_FOUND)
				.append("</b>");
		html.append("</p>");
		html.append("</body></html>");

		infoPane.setText(html.toString());
		infoPane.setCaretPosition(0);
	}

	/**
	 * Shows feedback about the user's answer (correct or incorrect).
	 * 
	 * @param isCorrect true if the answer was correct, false otherwise
	 * @param message   the feedback message to display
	 */
	public void showAnswerFeedback(boolean isCorrect, String message) {
		StringBuilder html = new StringBuilder();
		html.append("<html><body style='font-family: Helvetica, Arial, sans-serif; font-size: 16px;'>");

		if (isCorrect) {
			html.append(
					"<h2 style='color: green; font-family: Helvetica, Arial, sans-serif; font-size: 20px; font-weight: bold;'>")
					.append(UserStringConstants.QUIZ_INFO_FEEDBACK_CORRECT).append("</h2>");
			html.append("<p style='font-family: Helvetica, Arial, sans-serif; font-size: 16px; "
					+ "background-color: #e8f5e8; padding: 10px; border-radius: 5px; color: green;'>");
		} else {
			html.append(
					"<h2 style='color: red; font-family: Helvetica, Arial, sans-serif; font-size: 20px; font-weight: bold;'>")
					.append(UserStringConstants.QUIZ_INFO_FEEDBACK_INCORRECT).append("</h2>");
			html.append("<p style='font-family: Helvetica, Arial, sans-serif; font-size: 16px; "
					+ "background-color: #ffe8e8; padding: 10px; border-radius: 5px; color: red;'>");
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
	public void showWelcomeMessage() {
		StringBuilder html = new StringBuilder();
		html.append("<html><body style='font-family: Helvetica, Arial, sans-serif; font-size: 16px;'>");
		html.append("<h2 style='font-family: Helvetica, Arial, sans-serif; font-size: 20px; font-weight: bold;'>")
				.append(UserStringConstants.QUIZ_INFO_PANEL_HEADER).append("</h2>");
		html.append("<p style='font-family: Helvetica, Arial, sans-serif; font-size: 16px;'>")
				.append(UserStringConstants.QUIZ_INFO_WELCOME_MESSAGE).append("</p>");
		html.append("</body></html>");

		infoPane.setText(html.toString());
		infoPane.setCaretPosition(0);
	}

	/**
	 * Sets the answer text directly in the info panel.
	 * 
	 * @param correctAnswerText The answer text to display
	 */
	public void setAnswerText(String correctAnswerText) {
		infoPane.setText(correctAnswerText);
		infoPane.setCaretPosition(0);
	}
}
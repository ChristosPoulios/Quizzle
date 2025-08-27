package gui.subpanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import constants.GUIConstants;
import constants.UserStringConstants;
import persistence.mariaDB.DBManager;
import quizlogic.dto.ThemeDTO;

/**
 * Panel showing theme information.
 * <p>
 * Displays a HTML formatted overview of theme details including theme title,
 * description, and related statistics.
 * </p>
 * 
 * @author Generated based on QuizInfoViewPanel
 * @version 1.0
 * @since 1.0
 */
public class QuizThemeInfoView extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private final JEditorPane infoPane;
	private DBManager dbManager;

	/**
	 * Constructs the theme info view panel with database manager integration.
	 * 
	 * @param dbManager The MariaDB database manager for retrieving theme
	 *                  information
	 */
	public QuizThemeInfoView(DBManager dbManager) {
		this.dbManager = dbManager;
		setBackground(BACKGROUND_COLOR);
		setOpaque(false);
		setLayout(new BorderLayout());

		Dimension themeInfoSize = new Dimension(RIGHT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT);
		setPreferredSize(themeInfoSize);
		setMaximumSize(themeInfoSize);
		setMinimumSize(themeInfoSize);

		infoPane = new JEditorPane();
		infoPane.setContentType(UserStringConstants.HTML_CONTENT_TYPE);
		infoPane.setEditable(false);
		infoPane.setBackground(BACKGROUND_COLOR);

		JScrollPane scrollPane = new JScrollPane(infoPane);
		scrollPane.setBackground(BACKGROUND_COLOR);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		add(scrollPane, BorderLayout.CENTER);
		showWelcomeMessage();
	}

	/**
	 * Shows information about the selected theme.
	 * 
	 * @param themeTitle The title of the theme to show information for
	 */
	public void showThemeInfo(String themeTitle) {
		StringBuilder html = new StringBuilder();
		html.append("<html><body style='font-family: Helvetica, Arial, sans-serif; font-size: 16px;'>");

		if (UserStringConstants.ALL_THEMES_OPTION.equals(themeTitle)) {
			showAllThemesOverview();
			return;
		}

		// Strip the "*" prefix if present for themes without descriptions
		String actualThemeTitle = themeTitle.startsWith("* ") ? themeTitle.substring(2) : themeTitle;

		ThemeDTO selectedTheme = null;
		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			if (theme.getThemeTitle().equals(actualThemeTitle)) {
				selectedTheme = theme;
				break;
			}
		}

		if (selectedTheme != null) {
			html.append("<h2 style='font-family: Helvetica, Arial, sans-serif; font-size: 18px; font-weight: bold;'>")
					.append(selectedTheme.getThemeTitle()).append("</h2>");

			html.append("<div style='background-color: #f0f8ff; padding: 15px; border-radius: 8px; margin: 10px 0;'>");
			html.append("<h3 style='margin-top: 0; font-size: 16px; font-weight: bold;'>Themeninformation:</h3>");
			
			// Check if theme has a description - if not, leave the field empty but still show the section
			String description = selectedTheme.getThemeDescription();
			if (description != null && !description.trim().isEmpty()) {
				html.append("<p style='font-size: 14px; line-height: 1.6; color: #000000;'>")
						.append(description).append("</p>");
			} else {
				html.append("<p style='font-size: 14px; line-height: 1.6; color: #888888; font-style: italic;'>")
						.append("Keine Beschreibung verfügbar.").append("</p>");
			}
			html.append("</div>");

			int questionCount = dbManager.getQuestionsFor(selectedTheme).size();
			String backgroundColor = questionCount > 0 ? "#e8f5e8" : "#ffe8e8";
			String textColor = questionCount > 0 ? "#006600" : "#cc0000";

			html.append("<div style='background-color: ").append(backgroundColor)
					.append("; padding: 10px; border-radius: 5px; margin: 10px 0;'>");
			html.append("<p style='font-size: 14px; font-weight: bold; color: ").append(textColor).append(";'>");
			html.append("Anzahl Fragen: ").append(questionCount);
			html.append("</p>");
			html.append("</div>");
		} else {
			html.append("<h2>Thema nicht gefunden</h2>");
			html.append("<p>Das ausgewählte Thema konnte nicht gefunden werden.</p>");
		}

		html.append("</body></html>");
		infoPane.setText(html.toString());
		infoPane.setCaretPosition(0);
	}

	/**
	 * Shows an overview of all themes with their question counts.
	 */
	private void showAllThemesOverview() {
		StringBuilder html = new StringBuilder();
		html.append("<html><body style='font-family: Helvetica, Arial, sans-serif; font-size: 16px;'>");
		html.append("<h2 style='font-family: Helvetica, Arial, sans-serif;'>Themenübersicht</h2>");

		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();

		if (themes.isEmpty()) {
			html.append("<p style='font-style: italic; color: #666666;'>Keine Themen verfügbar.</p>");
		} else {
			html.append("<div style='background-color: #f9f9f9; padding: 15px; border-radius: 8px;'>");

			for (ThemeDTO theme : themes) {
				int questionCount = dbManager.getQuestionsFor(theme).size();

				String backgroundColor = questionCount > 0 ? "#e8f5e8" : "#ffe8e8";
				String textColor = questionCount > 0 ? "#006600" : "#cc0000";

				html.append("<div style='border-bottom: 1px solid #ddd; padding: 10px 0; margin-bottom: 10px;'>");
				html.append("<h3 style='color: #000000; margin: 0 0 5px 0; font-size: 16px; font-weight: bold;'>")
						.append(theme.getThemeTitle()).append("</h3>");

				html.append("<p style='font-size: 12px; margin: 5px 0; color: #000000;'>")
						.append(theme.getThemeDescription() != null ? theme.getThemeDescription()
								: "Keine Beschreibung verfügbar.")
						.append("</p>");

				html.append("<span style='font-size: 12px; font-weight: bold; color: ").append(textColor)
						.append("; background-color: ").append(backgroundColor)
						.append("; padding: 2px 6px; border-radius: 3px;'>").append(questionCount).append(" Fragen")
						.append("</span>");

				html.append("</div>");
			}

			html.append("</div>");
		}

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
		html.append("<h2 style='font-family: Helvetica, Arial, sans-serif; color: #0066cc;'>Themeninformationen</h2>");
		html.append("<p style='font-family: Helvetica, Arial, sans-serif; font-size: 16px; color: #666666;'>");
		html.append("Wählen Sie ein Thema aus der Liste aus, um detaillierte Informationen anzuzeigen.");
		html.append("</p>");
		html.append("</body></html>");

		infoPane.setText(html.toString());
		infoPane.setCaretPosition(0);
	}
}
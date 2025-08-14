package gui.subpanels;

import javax.swing.*;
import gui.interfaces.GUIConstants;
import java.awt.*;
import java.util.ArrayList;
import persistence.mariaDB.DBManager;
import quizlogic.dto.ThemeDTO;

/**
 * Panel displaying quiz information and statistics with persistent data
 * integration.
 * 
 * Shows theme selection and provides information display area. Uses MariaDB for
 * data persistence and automatically updates when data changes.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class QuizInfoViewPanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;
	private JComboBox<String> themeComboBox;
	private JTextPane infoPane;
	private DBManager dbManager;

	/**
	 * Constructs the quiz info view panel with database manager integration.
	 * 
	 * @param dbManager The database manager for theme access
	 */
	public QuizInfoViewPanel(DBManager dbManager) {
		this.dbManager = dbManager;
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(PANEL_MARGIN_V, PANEL_MARGIN_H, PANEL_MARGIN_V, PANEL_MARGIN_H));

		themeComboBox = new JComboBox<>();
		themeComboBox.setPreferredSize(new Dimension(300, 30));
		themeComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		fillThemeComboBox();

		add(themeComboBox);
		add(Box.createVerticalStrut(10));

		infoPane = new JTextPane();
		infoPane.setContentType("text/html");
		infoPane.setText("");
		infoPane.setEditable(false);
		infoPane.setFont(DEFAULT_FONT);

		JScrollPane scrollPane = new JScrollPane(infoPane);
		scrollPane.setPreferredSize(new Dimension(350, 400));
		scrollPane.setBackground(BACKGROUND_COLOR);
		add(scrollPane);

		updateInfoDisplay();

		themeComboBox.addActionListener(_ -> updateInfoDisplay());
	}

	/**
	 * Fills the theme combo box with available themes from database.
	 */
	private void fillThemeComboBox() {
		themeComboBox.removeAllItems();
		themeComboBox.addItem("Alle Themen");

		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			themeComboBox.addItem(theme.getThemeTitle());
		}
	}

	/**
	 * Updates the information display based on selected theme.
	 */
	private void updateInfoDisplay() {
		String selectedTheme = (String) themeComboBox.getSelectedItem();
		if (selectedTheme == null) {
			return;
		}

		StringBuilder html = new StringBuilder();
		html.append("<html><body style='font-family: Helvetica; padding: 10px;'>");

		ThemeDTO theme = findThemeByTitle(selectedTheme);
		if (theme != null) {
			html.append("<h2>").append(theme.getThemeTitle()).append("</h2>");

			if (theme.getThemeDescription() != null && !theme.getThemeDescription().trim().isEmpty()) {
				html.append("<p>").append(theme.getThemeDescription()).append("</p>");
			}

			int questionCount = dbManager.getQuestionsFor(theme).size();
			html.append("<p><b>Anzahl Fragen:</b> ").append(questionCount).append("</p>");
		}

		html.append("</body></html>");
		infoPane.setText(html.toString());

	}

	/**
	 * Finds a theme by its title.
	 * 
	 * @param title The theme title to search for
	 * @return The theme DTO or null if not found
	 */
	private ThemeDTO findThemeByTitle(String title) {
		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			if (theme.getThemeTitle().equals(title)) {
				return theme;
			}
		}
		return null;
	}

	/**
	 * Refreshes the data display (called when themes are updated).
	 */
	public void refreshData() {
		fillThemeComboBox();
		updateInfoDisplay();
	}
}
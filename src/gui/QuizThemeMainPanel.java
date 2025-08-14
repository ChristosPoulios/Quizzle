package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.interfaces.GUIConstants;
import gui.interfaces.QuizThemeDelegator;
import gui.subpanels.ThemeButtonPanel;
import gui.subpanels.ThemeListPanel;
import gui.subpanels.ThemePanel;
import persistence.mariaDB.DBManager;
import quizlogic.dto.ThemeDTO;

/**
 * Main panel for theme management with theme selection functionality.
 * 
 * Provides theme creation, editing, and listing with automatic field filling.
 * Uses MariaDB for data persistence.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class QuizThemeMainPanel extends JPanel implements GUIConstants, QuizThemeDelegator {
	private static final long serialVersionUID = 1L;
	private ThemeListPanel themeListPanel;
	private ThemePanel themePanel;
	private ThemeButtonPanel buttonPanel;
	private DBManager dbManager;
	private ThemeChangeListener themeChangeListener;

	/**
	 * Interface for listening to theme changes
	 */
	public interface ThemeChangeListener {
		void onThemeChanged();
	}

	/**
	 * Constructs the quiz theme main panel with selection functionality.
	 * 
	 * @param dbManager the database manager for MariaDB operations
	 */
	public QuizThemeMainPanel(DBManager dbManager) {
		this.dbManager = dbManager;
		setLayout(new BorderLayout());
		setBackground(BACKGROUND_COLOR);

		themePanel = new ThemePanel();
		themeListPanel = new ThemeListPanel(dbManager);
		buttonPanel = new ThemeButtonPanel(BTN_DELETE_THEME, BTN_SAVE_THEME, BTN_ADD_THEME);
		buttonPanel.setDelegate(this);

		themeListPanel.setSelectionListener(selectedTheme -> {
			themePanel.fillFields(selectedTheme.getThemeTitle(), selectedTheme.getText());
			System.out.println("Theme ausgewählt: " + selectedTheme.getThemeTitle());
		});

		add(themePanel, BorderLayout.WEST);
		add(themeListPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Sets the listener for theme changes
	 * 
	 * @param listener the listener to notify when themes change
	 */
	public void setThemeChangeListener(ThemeChangeListener listener) {
		this.themeChangeListener = listener;
	}

	/**
	 * Notifies the theme change listener if set
	 */
	private void notifyThemeChanged() {
		if (themeChangeListener != null) {
			themeChangeListener.onThemeChanged();
		}
	}

	@Override
	public void onNewTheme(String themeTitle) {
		themePanel.clearFields();
		buttonPanel.setMessage("Bereit für neues Thema");
	}

	@Override
	public void onThemeSaved(String themeTitle) {
		String title = themePanel.getTitleText();
		String info = themePanel.getInfoText();
		if (title.isEmpty()) {
			buttonPanel.setMessage("Bitte geben Sie einen Titel für das Thema ein");
			return;
		}

		ThemeDTO newTheme = new ThemeDTO();
		newTheme.setId(-1);
		newTheme.setThemeTitle(title);
		newTheme.setThemeDescription(info);
		newTheme.setQuestions(new ArrayList<>());

		String result = dbManager.saveTheme(newTheme);

		if (result != null && result.contains("successfully")) {
			buttonPanel.setMessage("Thema erfolgreich gespeichert: " + title);
			themePanel.clearFields();
			themeListPanel.updateThemeList();

			notifyThemeChanged();
		} else {
			buttonPanel.setMessage("Fehler beim Speichern: " + (result != null ? result : "Unbekannter Fehler"));
		}
	}

	@Override
	public void onThemeDeleted(String themeTitle) {

		ThemeDTO selectedTheme = themeListPanel.getSelectedTheme();
		String selectedTitle = themeListPanel.getSelectedThemeTitle();

		if (selectedTheme == null || selectedTitle == null) {
			buttonPanel.setMessage("Bitte ein Thema zum Löschen auswählen");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Sind Sie sicher, dass Sie das Thema '" + selectedTitle + "' löschen möchten?\n\n"
						+ "⚠️ Diese Aktion kann nicht rückgängig gemacht werden!",
				"Thema löschen bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			String result = dbManager.deleteTheme(selectedTheme);

			if (result != null && result.contains("successfully")) {
				buttonPanel.setMessage("Thema erfolgreich gelöscht: " + selectedTitle);

				themePanel.clearFields();
				themeListPanel.clearSelection();
				themeListPanel.updateThemeList();

				notifyThemeChanged();

				System.out.println("DEBUG: Theme '" + selectedTitle + "' erfolgreich gelöscht");
			} else {

				buttonPanel.setMessage(
						"Fehler beim Löschen des Themas: " + (result != null ? result : "Unbekannter Fehler"));

				System.err.println("ERROR: Fehler beim Löschen: " + result);
			}
		} else {
			buttonPanel.setMessage("Löschen abgebrochen");
		}
	}
}
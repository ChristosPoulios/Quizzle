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
import persistence.serialization.QuizDataManager;
import quizlogic.dto.ThemeDTO;

/**
 * Main panel for theme management with theme selection functionality.
 * 
 * Provides theme creation, editing, and listing with automatic field filling.
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
	private QuizDataManager dataManager;

	/**
	 * Constructs the quiz theme main panel with selection functionality.
	 * 
	 * @param dataManager2
	 */
	public QuizThemeMainPanel(QuizDataManager dataManager2) {
		this.dataManager = new QuizDataManager();
		setLayout(new BorderLayout());
		setBackground(BACKGROUND_COLOR);

		themePanel = new ThemePanel();
		themeListPanel = new ThemeListPanel(dataManager);
		buttonPanel = new ThemeButtonPanel(BTN_DELETE_THEME, BTN_SAVE_THEME, BTN_ADD_THEME);
		buttonPanel.setDelegate(this);

		themeListPanel.setSelectionListener(selectedTheme -> {
			themePanel.fillFields(selectedTheme.getThemeTitle(), selectedTheme.getText());
			System.out.println("Theme ausgewählt: " + selectedTheme.getThemeTitle());
		});

		add(themePanel, BorderLayout.WEST);
		add(themeListPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		dataManager.addUpdateListener(() -> {
			themeListPanel.updateThemeList();
		});
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

		@SuppressWarnings("unused")
		String result = dataManager.saveTheme(newTheme);

		buttonPanel.setMessage("Thema erfolgreich gespeichert: " + title);

		themePanel.clearFields();
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
			String result = dataManager.deleteTheme(selectedTheme);

			if (result == null) {
			    buttonPanel.setMessage("Thema erfolgreich gelöscht: " + selectedTitle); 

				themePanel.clearFields();
				themeListPanel.clearSelection();

				System.out.println("DEBUG: Theme '" + selectedTitle + "' erfolgreich gelöscht");
			} else {

			    buttonPanel.setMessage("Fehler beim Löschen des Themas: " + result); 

				System.err.println("ERROR: Fehler beim Löschen: " + result);
			}
		} else {
			System.out.println("DEBUG: Löschen abgebrochen vom Benutzer");
		}
	}
}

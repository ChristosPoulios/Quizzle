package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import constants.GUIConstants;
import constants.UserStringConstants;
import gui.interfaces.QuizThemeDelegator;
import gui.subpanels.ThemeButtonPanel;
import gui.subpanels.ThemeListPanel;
import gui.subpanels.ThemePanel;

import persistence.mariaDB.DBManager;

import quizlogic.dto.ThemeDTO;

/**
 * Main panel for managing quiz themes including selection and CRUD operations.
 * <p>
 * Provides functionality to create, edit, list, and delete quiz themes.
 * Integrates MariaDB persistence via {@link DBManager}.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
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
	 * Listener interface for theme change events.
	 */
	public interface ThemeChangeListener {
		/**
		 * Called when themes have been changed (added/modified/deleted).
		 */
		void onThemeChanged();
	}

	/**
	 * Constructs the quiz theme main panel with theme selection support.
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
			themePanel.fillFields(selectedTheme.getThemeTitle(), selectedTheme.getThemeDescription());
			System.out.println("Theme selected: " + selectedTheme.getThemeTitle());
		});

		add(themePanel, BorderLayout.WEST);
		add(themeListPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Sets the listener for theme changes.
	 * 
	 * @param listener the listener to notify when themes change
	 */
	public void setThemeChangeListener(ThemeChangeListener listener) {
		this.themeChangeListener = listener;
	}

	/**
	 * Notifies the registered theme change listener if set.
	 */
	private void notifyThemeChanged() {
		if (themeChangeListener != null) {
			themeChangeListener.onThemeChanged();
		}
	}

	/**
	 * Called when a new theme is created. Clears input fields in preparation.
	 * 
	 * @param themeTitle the new theme title (unused here)
	 */
	@Override
	public void onNewTheme(String themeTitle) {
		themePanel.clearFields();
		buttonPanel.setMessage(UserStringConstants.MSG_READY_FOR_NEW_THEME);
	}

	/**
	 * Called when a theme is saved. Performs validation, persistence, updates UI
	 * and notifies listener.
	 * 
	 * @param themeTitle the title of the theme being saved
	 */
	@Override
	public void onThemeSaved(String themeTitle) {
		String title = themePanel.getTitleText();
		String description = themePanel.getInfoText();

		if (title.isEmpty()) {
			buttonPanel.setMessage(UserStringConstants.MSG_PLEASE_ENTER_THEME_TITLE);
			return;
		}

		// Check if description is empty and show confirmation dialog
		if (description == null || description.trim().isEmpty()) {
			int result = JOptionPane.showConfirmDialog(this,
				UserStringConstants.MSG_CONFIRM_SAVE_WITHOUT_DESCRIPTION,
				UserStringConstants.DIALOG_TITLE_CONFIRM_SAVE_WITHOUT_DESCRIPTION,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
			
			if (result != JOptionPane.OK_OPTION) {
				buttonPanel.setMessage(UserStringConstants.MSG_SAVE_CANCELLED);
				return;
			}
		}

		// Check if theme with this title already exists
		ArrayList<ThemeDTO> allThemes = dbManager.getAllThemes();
		ThemeDTO existingTheme = null;
		for (ThemeDTO theme : allThemes) {
			if (theme.getThemeTitle().equals(title)) {
				existingTheme = theme;
				break;
			}
		}

		ThemeDTO themeToSave;
		if (existingTheme != null) {
			// Update existing theme's description
			existingTheme.setThemeDescription(description);
			themeToSave = existingTheme;
		} else {
			// Create new theme
			themeToSave = new ThemeDTO();
			themeToSave.setId(-1);
			themeToSave.setThemeTitle(title);
			themeToSave.setThemeDescription(description);
			themeToSave.setQuestions(new ArrayList<>());
		}

		String result = dbManager.saveTheme(themeToSave);
		if (result != null && result.toLowerCase().contains("successfully")) {
			buttonPanel.setMessage(String.format(UserStringConstants.MSG_THEME_CREATED_SUCCESS, title));
			themePanel.clearFields();
			themeListPanel.updateThemeList();
			notifyThemeChanged();
		} else {
			buttonPanel.setMessage(String.format(UserStringConstants.MSG_THEME_DELETE_ERROR, (result != null ? result : "Unbekannter Fehler")));
		}
	}

	/**
	 * Called when a theme is deleted. Prompts for confirmation and, if confirmed,
	 * deletes the theme via DBManager, updates UI and notifies listeners.
	 * 
	 * @param themeTitle the title of the theme being deleted
	 */
	@Override
	public void onThemeDeleted(String themeTitle) {
		ThemeDTO selectedTheme = themeListPanel.getSelectedTheme();
		String selectedTitle = themeListPanel.getSelectedThemeTitle();

		if (selectedTheme == null || selectedTitle == null) {
			buttonPanel.setMessage(UserStringConstants.MSG_PLEASE_SELECT_THEME_TO_DELETE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				String.format(UserStringConstants.MSG_CONFIRM_DELETE_THEME, selectedTitle),
				UserStringConstants.DIALOG_TITLE_CONFIRM_DELETE_THEME, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			String result = dbManager.deleteTheme(selectedTheme);
			if (result != null && result.toLowerCase().contains("successfully")) {
				buttonPanel.setMessage(String.format(UserStringConstants.MSG_THEME_DELETED_SUCCESS, selectedTitle));
				themePanel.clearFields();
				themeListPanel.clearSelection();
				themeListPanel.updateThemeList();
				notifyThemeChanged();
				System.out.println("DEBUG: Theme '" + selectedTitle + "' erfolgreich gelöscht");
			} else {
				buttonPanel.setMessage(String.format(UserStringConstants.MSG_THEME_DELETE_ERROR, (result != null ? result : "Unbekannter Fehler")));
				System.err.println("ERROR: Fehler beim Löschen: " + result);
			}
		} else {
			buttonPanel.setMessage(UserStringConstants.MSG_DELETE_CANCELLED);
		}
	}
}
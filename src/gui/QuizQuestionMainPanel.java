package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import constants.GUIConstants;
import gui.interfaces.QuizQuestionDelegator;
import gui.subpanels.AnswerRowPanel;
import gui.subpanels.QuestionButtonPanel;
import gui.subpanels.QuestionListPanel;
import gui.subpanels.QuestionPanel;

import persistence.mariaDB.DBManager;

import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Main panel for managing quiz questions.
 * <p>
 * Coordinates between question editing, question listing, and button actions.
 * Provides a full interface for CRUD operations on quiz questions using MariaDB
 * persistence.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuizQuestionMainPanel extends JPanel implements GUIConstants, QuizQuestionDelegator {

	private static final long serialVersionUID = 1L;

	private final DBManager dbManager;

	private QuestionPanel questionPanel;

	private QuestionListPanel questionListPanel;

	private QuestionButtonPanel buttonPanel;

	/**
	 * Constructs the quiz question main panel setting up UI layout and components.
	 * 
	 * @param dbManager Database manager for MariaDB operations
	 */
	public QuizQuestionMainPanel(DBManager dbManager) {
		this.dbManager = dbManager;
		initializeLayout();
		initializeComponents();
		connectComponents();
	}

	/**
	 * Initializes the layout of the main panel.
	 */
	private void initializeLayout() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));
	}

	/**
	 * Initializes child components like question panel, list panel and button
	 * panel.
	 */
	private void initializeComponents() {
		questionPanel = new QuestionPanel();
		questionPanel.setEditable(true);
		questionPanel.getMetaPanel().getQuestionTextArea().setBackground(BACKGROUND_COLOR);

		questionListPanel = new QuestionListPanel(dbManager);

		buttonPanel = new QuestionButtonPanel(BTN_DELETE_QUESTION, BTN_SAVE_QUESTION, BTN_ADD_QUESTION);
	}

	/**
	 * Connects delegates and adds child components to the main panel.
	 */
	private void connectComponents() {
		questionListPanel.setDelegate(this);
		buttonPanel.setDelegate(this);
		buttonPanel.setPanelReferences(questionPanel, questionListPanel);

		add(questionPanel, BorderLayout.WEST);
		add(questionListPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Responds to a theme being selected by updating question list and enabling
	 * editing.
	 * 
	 * @param themeTitle The selected theme title
	 */
	@Override
	public void onThemeSelected(String themeTitle) {
		questionListPanel.updateQuestionList(themeTitle);
		clearQuestionSelection();
		enableEditingIfThemeSelected(themeTitle);
	}

	/**
	 * Responds to a question selection by loading its data and answers and
	 * displaying them.
	 * 
	 * @param entry The selected question entry text
	 * @param index The index of the selected question
	 */
	@Override
	public void onQuestionSelected(String entry, int index) {
		QuestionDTO question = questionListPanel.getQuestionByIndex(index);
		if (question != null) {
			ArrayList<AnswerDTO> answers = dbManager.getAnswersFor(question);
			question.setAnswers(answers);
			String selectedThemeName = questionListPanel.getSelectedThemeTitle();
			if ("Alle Themen".equals(selectedThemeName)) {
				selectedThemeName = getThemeNameForQuestion(question);
			}
			questionPanel.fillWithQuestionData(question, selectedThemeName);
		}
	}

	/**
	 * Responds to a question delete request by deleting the question and updating
	 * the UI.
	 * 
	 * @param questionTitle The title of the question to delete
	 */
	@Override
	public void onQuestionDeleted(String questionTitle) {
		try {
			int selectedIndex = questionListPanel.getQuestionList().getSelectedIndex();
			if (selectedIndex < 0) {
				buttonPanel.setMessage("Keine Frage zum Löschen ausgewählt");
				return;
			}
			QuestionDTO questionToDelete = questionListPanel.getQuestionByIndex(selectedIndex);
			if (questionToDelete == null) {
				buttonPanel.setMessage("Frage nicht gefunden");
				return;
			}
			String result = dbManager.deleteQuestion(questionToDelete);
			if (result != null && result.contains("successfully")) {
				buttonPanel.setMessage("Frage erfolgreich gelöscht: " + questionToDelete.getQuestionTitle());
				clearQuestionSelection();
				questionListPanel.updateQuestionList(questionListPanel.getSelectedThemeTitle());
			} else {
				buttonPanel.setMessage(
						"Frage konnte nicht gelöscht werden: " + (result != null ? result : "Unbekannter Fehler"));
			}
		} catch (Exception e) {
			buttonPanel.setMessage("Fehler beim Löschen: " + e.getMessage());
		}
	}

	/**
	 * Responds to a request to save a question, including answers and applies
	 * validation. Updates UI messages accordingly.
	 * 
	 * @param questionTitle    The question title
	 * @param themeTitle       The theme title
	 * @param questionType     The question type (not fully used)
	 * @param answerText       The answer text (not fully used)
	 * @param hasCorrectAnswer Whether at least one correct answer exists
	 */
	@Override
	public void onQuestionSaved(String questionTitle, String themeTitle, String questionType, String answerText,
			boolean hasCorrectAnswer) {
		try {
			if (!validateInput(themeTitle)) {
				return;
			}
			ThemeDTO targetTheme = findTheme(themeTitle);
			if (targetTheme == null) {
				buttonPanel.setMessage("Thema nicht gefunden: " + themeTitle);
				return;
			}
			QuestionDTO question = getOrCreateQuestion(targetTheme);
			if (question == null) {
				buttonPanel.setMessage("Frage konnte nicht erstellt werden");
				return;
			}
			updateQuestionData(question);
			String result = dbManager.saveQuestion(question, targetTheme);
			if (result != null && result.contains("successfully")) {
				ArrayList<AnswerDTO> answers = collectAnswers(question);
				if (!validateAnswers(answers)) {
					return;
				}
				boolean allAnswersSaved = true;
				for (AnswerDTO answer : answers) {
					String answerResult = dbManager.saveAnswer(answer, question);
					if (answerResult == null || !answerResult.contains("successfully")) {
						allAnswersSaved = false;
						break;
					}
				}
				if (allAnswersSaved) {
					buttonPanel.setMessage("Frage erfolgreich gespeichert: " + question.getQuestionTitle());
					questionListPanel.updateQuestionList(questionListPanel.getSelectedThemeTitle());
				} else {
					buttonPanel.setMessage("Frage gespeichert, aber Fehler beim Speichern der Antworten");
				}
			} else {
				buttonPanel.setMessage("Fehler beim Speichern: " + (result != null ? result : "Unbekannter Fehler"));
			}
		} catch (Exception e) {
			buttonPanel.setMessage("Fehler beim Speichern: " + e.getMessage());
		}
	}

	/**
	 * Responds to new question creation command by clearing inputs for fresh entry.
	 * 
	 * @param themeTitle   The theme for the new question
	 * @param questionType The type of the new question (not fully used)
	 */
	@Override
	public void onNewQuestion(String themeTitle, String questionType) {
		try {
			if (themeTitle == null || themeTitle.trim().isEmpty()) {
				buttonPanel.setMessage("Bitte wählen Sie ein Thema aus der Liste");
				return;
			}
			questionPanel.fillWithQuestionData(null);
			questionPanel.setEditable(true);
			questionListPanel.getQuestionList().clearSelection();
			questionPanel.getMetaPanel().getTitleField().setText("Neue Frage");
			questionPanel.getMetaPanel().getQuestionTextArea().setText("");
			AnswerRowPanel[] answerRows = questionPanel.getAnswersPanel().getAnswerRows();
			for (AnswerRowPanel row : answerRows) {
				row.getTextField().setText("");
				row.getCheckBox().setSelected(false);
			}
			buttonPanel.setMessage("Neue Frage erstellen für Thema: " + themeTitle);
		} catch (Exception e) {
			buttonPanel.setMessage("Fehler beim Erstellen einer neuen Frage: " + e.getMessage());
		}
	}

	/**
	 * Clears the current question selection and resets the question panel UI.
	 */
	private void clearQuestionSelection() {
		questionListPanel.getQuestionList().clearSelection();
		questionPanel.fillWithQuestionData(null);
	}

	/**
	 * Enables or disables question editing based on whether a valid theme is
	 * selected.
	 * 
	 * @param themeTitle Title of the selected theme
	 */
	private void enableEditingIfThemeSelected(String themeTitle) {
		if (themeTitle != null && !themeTitle.equals("Alle Themen")) {
			questionPanel.setEditable(true);
		} else {
			questionPanel.setEditable(false);
		}
	}

	/**
	 * Validates user input theme title.
	 * 
	 * @param themeTitle The theme title string to validate
	 * @return true if valid, false otherwise
	 */
	private boolean validateInput(String themeTitle) {
		if (themeTitle == null || themeTitle.trim().isEmpty() || themeTitle.equals("Alle Themen")) {
			buttonPanel.setMessage("Bitte wählen Sie ein gültiges Thema aus");
			return false;
		}
		return true;
	}

	/**
	 * Finds and returns the ThemeDTO for a given theme title.
	 * 
	 * @param themeTitle The title of the theme to find
	 * @return The ThemeDTO if found; null otherwise
	 */
	private ThemeDTO findTheme(String themeTitle) {
		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			if (theme.getThemeTitle().equals(themeTitle)) {
				return theme;
			}
		}
		return null;
	}

	/**
	 * Returns the currently selected question or creates a new one.
	 * 
	 * @param theme The theme to associate with a new question if needed
	 * @return Existing or new QuestionDTO
	 */
	private QuestionDTO getOrCreateQuestion(ThemeDTO theme) {
		int selectedIndex = questionListPanel.getQuestionList().getSelectedIndex();
		if (selectedIndex >= 0) {
			return questionListPanel.getQuestionByIndex(selectedIndex);
		} else {
			QuestionDTO newQuestion = new QuestionDTO();
			newQuestion.setId(-1);
			return newQuestion;
		}
	}

	/**
	 * Updates the question data from UI fields.
	 * 
	 * @param question The QuestionDTO to update
	 */
	private void updateQuestionData(QuestionDTO question) {
		String title = questionPanel.getMetaPanel().getTitleField().getText().trim();
		String questionText = questionPanel.getMetaPanel().getQuestionTextArea().getText().trim();
		question.setQuestionTitle(title);
		question.setQuestionText(questionText);
	}

	/**
	 * Collects answers from the answer panel UI.
	 * 
	 * @param question The question to associate with answers
	 * @return List of collected AnswerDTO objects
	 */
	private ArrayList<AnswerDTO> collectAnswers(QuestionDTO question) {
		ArrayList<AnswerDTO> answers = new ArrayList<>();
		AnswerRowPanel[] answerRows = questionPanel.getAnswersPanel().getAnswerRows();
		for (int i = 0; i < answerRows.length; i++) {
			String answerText = answerRows[i].getTextField().getText().trim();
			if (!answerText.isEmpty()) {
				AnswerDTO answer = new AnswerDTO();
				answer.setId(-1);
				answer.setAnswerText(answerText);
				answer.setCorrect(answerRows[i].isCorrect());
				answers.add(answer);
			}
		}
		return answers;
	}

	/**
	 * Checks if the answers list is valid, requiring at least one answer and one
	 * correct answer.
	 * 
	 * @param answers List of answers to validate
	 * @return true if valid, false otherwise
	 */
	private boolean validateAnswers(ArrayList<AnswerDTO> answers) {
		if (answers.isEmpty()) {
			buttonPanel.setMessage("Bitte geben Sie mindestens eine Antwort ein");
			return false;
		}
		boolean hasCorrectAnswer = false;
		for (AnswerDTO answer : answers) {
			if (answer.isCorrect()) {
				hasCorrectAnswer = true;
				break;
			}
		}
		if (!hasCorrectAnswer) {
			buttonPanel.setMessage("Bitte markieren Sie mindestens eine Antwort als korrekt");
			return false;
		}
		return true;
	}

	/**
	 * Determines the theme title for the given question by searching associated
	 * themes.
	 * 
	 * @param question The question to find the theme of
	 * @return The theme title string or empty string if none found
	 */
	private String getThemeNameForQuestion(QuestionDTO question) {
		try {
			String currentTheme = questionListPanel.getSelectedThemeTitle();
			if (currentTheme != null && !"Alle Themen".equals(currentTheme)) {
				return currentTheme;
			}
			ArrayList<ThemeDTO> allThemes = dbManager.getAllThemes();
			for (ThemeDTO theme : allThemes) {
				ArrayList<QuestionDTO> questionsForTheme = dbManager.getQuestionsFor(theme);
				for (QuestionDTO q : questionsForTheme) {
					if (q.getId() == question.getId()) {
						return theme.getThemeTitle();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * Refreshes the theme list displayed in the question list panel.
	 * <p>
	 * Should be called whenever themes have been added, modified, or deleted, to
	 * keep the UI synchronized with the underlying data.
	 */
	public void refreshThemeList() {
		if (questionListPanel != null) {
			questionListPanel.refreshThemeComboBox();
		}
	}

}

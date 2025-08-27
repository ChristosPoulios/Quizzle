package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import constants.ConfigManager;
import constants.GUIConstants;
import constants.UserStringConstants;
import gui.interfaces.QuizQuestionDelegator;
import gui.subpanels.AnswerRowPanel;
import gui.subpanels.QuestionButtonPanel;
import gui.subpanels.QuestionListPanel;
import gui.subpanels.QuestionPanel;

import persistence.QuizDataInterface;

import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Main panel for managing quiz questions.
 * <p>
 * Coordinates between question editing, question listing, and button actions.
 * Provides a full interface for CRUD operations on quiz questions using 
 * the configured data storage (database or file-based with automatic fallback).
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuizQuestionMainPanel extends JPanel implements GUIConstants, QuizQuestionDelegator {

	private static final long serialVersionUID = 1L;

	private final QuizDataInterface dataManager;

	private QuestionPanel questionPanel;

	private QuestionListPanel questionListPanel;

	private QuestionButtonPanel buttonPanel;

	/**
	 * Constructs the quiz question main panel setting up UI layout and components.
	 * 
	 * @param dataManager Data manager for storage operations (database or file-based)
	 */
	public QuizQuestionMainPanel(QuizDataInterface dataManager) {
		this.dataManager = dataManager;
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
		questionPanel.setPreferredSize(new java.awt.Dimension(LEFT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT));

		questionListPanel = new QuestionListPanel(dataManager);
		questionListPanel.setPreferredSize(new java.awt.Dimension(RIGHT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT));

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
		// Strip the "*" prefix if present for themes without descriptions
		String actualThemeTitle = themeTitle.startsWith("* ") ? themeTitle.substring(2) : themeTitle;
		
		questionListPanel.updateQuestionList(actualThemeTitle);
		clearQuestionSelection();
		enableEditingIfThemeSelected(actualThemeTitle);
		questionPanel.fillWithQuestionData(null, actualThemeTitle);
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
			ArrayList<AnswerDTO> answers = dataManager.getAnswersFor(question);
			question.setAnswers(answers);
			String selectedThemeName = questionListPanel.getSelectedThemeTitle();
			if (selectedThemeName.equals(UserStringConstants.ALL_THEMES_OPTION)) {
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
				buttonPanel.setMessage(UserStringConstants.MSG_NO_QUESTION_SELECTED_FOR_DELETE);
				return;
			}
			QuestionDTO questionToDelete = questionListPanel.getQuestionByIndex(selectedIndex);
			if (questionToDelete == null) {
				buttonPanel.setMessage(UserStringConstants.MSG_QUESTION_NOT_FOUND);
				return;
			}
			String result = dataManager.deleteQuestion(questionToDelete);
			if (result != null && result.contains("successfully")) {
				buttonPanel.setMessage(String.format(UserStringConstants.MSG_QUESTION_DELETED_SUCCESS,
						questionToDelete.getQuestionTitle()));
				clearQuestionSelection();
				questionListPanel.updateQuestionList(questionListPanel.getSelectedThemeTitle());
			} else {
				buttonPanel.setMessage(String.format(UserStringConstants.MSG_QUESTION_DELETE_ERROR,
						(result != null ? result : "Unbekannter Fehler")));
			}
		} catch (Exception e) {
			buttonPanel.setMessage(String.format(UserStringConstants.MSG_QUESTION_DELETE_EXCEPTION, e.getMessage()));
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
			ConfigManager.debugPrint("=== DEBUG: onQuestionSaved START ===");

			if (!validateInput(themeTitle)) {
				ConfigManager.debugPrint("DEBUG: Theme validation failed");
				return;
			}
			ThemeDTO targetTheme = findTheme(themeTitle);
			if (targetTheme == null) {
				ConfigManager.debugPrint("DEBUG: Theme not found: " + themeTitle);
				buttonPanel.setMessage(String.format(UserStringConstants.MSG_THEME_NOT_FOUND, themeTitle));
				return;
			}

			ConfigManager.debugPrint("DEBUG: Found theme: " + targetTheme.getThemeTitle());

			QuestionDTO question = getOrCreateQuestion(targetTheme);
			if (question == null) {
				ConfigManager.debugPrint("DEBUG: Failed to create/get question");
				buttonPanel.setMessage(UserStringConstants.MSG_QUESTION_CREATE_ERROR);
				return;
			}

			ConfigManager.debugPrint("DEBUG: Question ID: " + question.getId());

			updateQuestionData(question);
			ConfigManager.debugPrint("DEBUG: Updated question data - Title: " + question.getQuestionTitle());

			ArrayList<AnswerDTO> answers = collectAnswers(question);
			if (!validateAnswers(answers)) {
				ConfigManager.debugPrint("DEBUG: Answer validation failed - not saving question");
				return;
			}

			String result = dataManager.saveQuestion(question, targetTheme);
			ConfigManager.debugPrint("DEBUG: Save question result: " + result);

			if (result != null && result.contains("successfully")) {

				if (question.getId() == -1) {

					ConfigManager.debugPrint("DEBUG: Question ID still -1, trying to refresh from database");
					ArrayList<QuestionDTO> questionsForTheme = dataManager.getQuestionsFor(targetTheme);
					for (QuestionDTO dbQuestion : questionsForTheme) {
						if (dbQuestion.getQuestionTitle().equals(question.getQuestionTitle())
								&& dbQuestion.getText().equals(question.getText())) {
							question.setId(dbQuestion.getId());
							ConfigManager.debugPrint("DEBUG: Found question in DB with ID: " + question.getId());
							break;
						}
					}
				}

				ConfigManager.debugPrint("DEBUG: Question ID after save: " + question.getId());

				QuestionDTO freshQuestion = null;
				if (question.getId() != -1) {
					ArrayList<QuestionDTO> questionsForTheme = dataManager.getQuestionsFor(targetTheme);
					for (QuestionDTO dbQuestion : questionsForTheme) {
						if (dbQuestion.getId() == question.getId()) {
							freshQuestion = dbQuestion;
							ConfigManager.debugPrint("DEBUG: Using fresh question object from database");
							break;
						}
					}
				}

				QuestionDTO questionForAnswers = (freshQuestion != null) ? freshQuestion : question;

				boolean allAnswersSaved = true;
				for (int i = 0; i < answers.size(); i++) {
					AnswerDTO answer = answers.get(i);
					ConfigManager.debugPrint("DEBUG: Saving answer " + (i + 1) + ": '" + answer.getAnswerText()
							+ "' (correct: " + answer.isCorrect() + ")");
					String answerResult = dataManager.saveAnswer(answer, questionForAnswers);
					ConfigManager.debugPrint("DEBUG: Answer save result: " + answerResult);

					if (answerResult == null || !answerResult.contains("successfully")) {
						allAnswersSaved = false;
						ConfigManager.debugPrint("DEBUG: Failed to save answer " + (i + 1));
						break;
					}
				}

				if (allAnswersSaved) {
					ConfigManager.debugPrint("DEBUG: All answers saved successfully");
					buttonPanel.setMessage(
							String.format(UserStringConstants.MSG_QUESTION_SAVED_SUCCESS, question.getQuestionTitle()));
					questionListPanel.updateQuestionList(questionListPanel.getSelectedThemeTitle());
				} else {
					ConfigManager.debugPrint("DEBUG: Some answers failed to save");
					buttonPanel.setMessage(UserStringConstants.MSG_QUESTION_SAVED_ANSWERS_ERROR);
				}
			} else {
				ConfigManager.debugPrint("DEBUG: Question save failed: " + result);
				buttonPanel.setMessage(String.format(UserStringConstants.MSG_QUESTION_SAVE_ERROR,
						(result != null ? result : "Unbekannter Fehler")));
			}
			ConfigManager.debugPrint("=== DEBUG: onQuestionSaved END ===");
		} catch (Exception e) {
			ConfigManager.debugPrint("DEBUG: Exception occurred: " + e.getMessage());
			e.printStackTrace();
			buttonPanel.setMessage(String.format(UserStringConstants.MSG_QUESTION_SAVE_ERROR, e.getMessage()));
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
				buttonPanel.setMessage(UserStringConstants.MSG_PLEASE_SELECT_THEME);
				return;
			}

			questionListPanel.getQuestionList().clearSelection();

			questionPanel.fillWithQuestionData(null);

			questionPanel.setEditable(true);

			questionPanel.getMetaPanel().getTitleField().setText(UserStringConstants.NEW_QUESTION_TEXT);
			questionPanel.getMetaPanel().getQuestionTextArea().setText("");

			AnswerRowPanel[] answerRows = questionPanel.getAnswersPanel().getAnswerRows();
			for (AnswerRowPanel row : answerRows) {
				row.getTextField().setText("");
				row.getTextField().setEditable(true);
				row.getCheckBox().setSelected(false);
				row.getCheckBox().setEnabled(true);
				row.setVisible(true);
			}

			buttonPanel.setMessage(String.format(UserStringConstants.MSG_NEW_QUESTION_FOR_THEME, themeTitle));
		} catch (Exception e) {
			buttonPanel.setMessage(String.format(UserStringConstants.MSG_NEW_QUESTION_CREATE_ERROR, e.getMessage()));
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
		if (themeTitle != null && !themeTitle.equals(UserStringConstants.ALL_THEMES_OPTION)) {
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
		if (themeTitle == null || themeTitle.trim().isEmpty()
				|| themeTitle.equals(UserStringConstants.ALL_THEMES_OPTION)) {
			buttonPanel.setMessage(UserStringConstants.MSG_PLEASE_SELECT_VALID_THEME);
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
		ArrayList<ThemeDTO> themes = dataManager.getAllThemes();
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

		ConfigManager.debugPrint("=== DEBUG: collectAnswers ===");
		ConfigManager.debugPrint("Number of answer rows: " + answerRows.length);

		for (int i = 0; i < answerRows.length; i++) {
			ConfigManager.debugPrint("Row " + i + ":");
			ConfigManager.debugPrint("  - Visible: " + answerRows[i].isVisible());
			ConfigManager.debugPrint("  - Text: '" + answerRows[i].getTextField().getText() + "'");
			ConfigManager.debugPrint("  - Editable: " + answerRows[i].getTextField().isEditable());
			ConfigManager.debugPrint("  - Correct: " + answerRows[i].isCorrect());

			if (answerRows[i].isVisible()) {
				String answerText = answerRows[i].getTextField().getText().trim();
				if (!answerText.isEmpty()) {
					AnswerDTO answer = new AnswerDTO();
					answer.setId(-1);
					answer.setAnswerText(answerText);
					answer.setCorrect(answerRows[i].isCorrect());
					answers.add(answer);
					ConfigManager.debugPrint("  -> Added answer: " + answerText + " (correct: " + answer.isCorrect() + ")");
				} else {
					ConfigManager.debugPrint("  -> Skipped: empty text");
				}
			} else {
				ConfigManager.debugPrint("  -> Skipped: not visible");
			}
		}

		ConfigManager.debugPrint("Total answers collected: " + answers.size());
		ConfigManager.debugPrint("=== END DEBUG ===");

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
			buttonPanel.setMessage(UserStringConstants.MSG_PLEASE_ENTER_AT_LEAST_ONE_ANSWER);
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
			buttonPanel.setMessage(UserStringConstants.MSG_PLEASE_MARK_AT_LEAST_ONE_CORRECT);
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
			if (currentTheme != null && !UserStringConstants.ALL_THEMES_OPTION.equals(currentTheme)) {
				return currentTheme;
			}
			ArrayList<ThemeDTO> allThemes = dataManager.getAllThemes();
			for (ThemeDTO theme : allThemes) {
				ArrayList<QuestionDTO> questionsForTheme = dataManager.getQuestionsFor(theme);
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
	 * </p>
	 */
	public void refreshThemeList() {
		if (questionListPanel != null) {
			questionListPanel.refreshThemeComboBox();
		}
	}

}
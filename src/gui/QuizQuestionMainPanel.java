package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import gui.interfaces.GUIConstants;
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
 * Main panel for quiz question management.
 * 
 * Coordinates between question editing, question listing, and button actions.
 * Provides a complete interface for CRUD operations on quiz questions. Uses
 * MariaDB for data persistence.
 * 
 * @author Christos Poulios
 * @version 2.0
 */
public class QuizQuestionMainPanel extends JPanel implements GUIConstants, QuizQuestionDelegator {

	private static final long serialVersionUID = 1L;

	private final DBManager dbManager;
	private QuestionPanel questionPanel;
	private QuestionListPanel questionListPanel;
	private QuestionButtonPanel buttonPanel;

	public QuizQuestionMainPanel(DBManager dbManager) {
		this.dbManager = dbManager;
		initializeLayout();
		initializeComponents();
		connectComponents();
	}

	private void initializeLayout() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));
	}

	private void initializeComponents() {
		questionPanel = new QuestionPanel();
		questionPanel.setEditable(true);
		questionPanel.getMetaPanel().getQuestionTextArea().setBackground(BACKGROUND_COLOR);

		questionListPanel = new QuestionListPanel(dbManager);

		buttonPanel = new QuestionButtonPanel(BTN_DELETE_QUESTION, BTN_SAVE_QUESTION, BTN_ADD_QUESTION);
	}

	private void connectComponents() {
		questionListPanel.setDelegate(this);
		buttonPanel.setDelegate(this);
		buttonPanel.setPanelReferences(questionPanel, questionListPanel);

		add(questionPanel, BorderLayout.WEST);
		add(questionListPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void onThemeSelected(String themeTitle) {
		questionListPanel.updateQuestionList(themeTitle);
		clearQuestionSelection();
		enableEditingIfThemeSelected(themeTitle);
	}

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

	private void clearQuestionSelection() {
		questionListPanel.getQuestionList().clearSelection();
		questionPanel.fillWithQuestionData(null);
	}

	private void enableEditingIfThemeSelected(String themeTitle) {
		if (themeTitle != null && !themeTitle.equals("Alle Themen")) {
			questionPanel.setEditable(true);
		}
	}

	/**
	 * Refreshes the theme list in the question list panel.
	 * Call this method when themes have been added, modified, or deleted.
	 */
	public void refreshThemeList() {
		questionListPanel.refreshThemeComboBox();
	}

	private boolean validateInput(String themeTitle) {
		if (themeTitle == null || themeTitle.trim().isEmpty() || themeTitle.equals("Alle Themen")) {
			buttonPanel.setMessage("Bitte wählen Sie ein gültiges Thema aus");
			return false;
		}
		return true;
	}

	private ThemeDTO findTheme(String themeTitle) {

		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			if (theme.getThemeTitle().equals(themeTitle)) {
				return theme;
			}
		}
		return null;
	}

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

	private void updateQuestionData(QuestionDTO question) {
		String title = questionPanel.getMetaPanel().getTitleField().getText().trim();
		String questionText = questionPanel.getMetaPanel().getQuestionTextArea().getText().trim();

		question.setQuestionTitle(title);
		question.setQuestionText(questionText);
	}

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
	 * Gets the theme name for a specific question by looking up the theme_id
	 */
	private String getThemeNameForQuestion(QuestionDTO question) {
		try {
			ArrayList<ThemeDTO> allThemes = dbManager.getAllThemes();

			String currentTheme = questionListPanel.getSelectedThemeTitle();
			if (currentTheme != null && !"Alle Themen".equals(currentTheme)) {
				return currentTheme;
			}

			return "";
		} catch (Exception e) {
			return "";
		}
	}
}
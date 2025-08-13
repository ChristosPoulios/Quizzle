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
import persistence.serialization.QuizDataManager;
import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Main panel for quiz question management.
 * 
 * Coordinates between question editing, question listing, and button actions.
 * Provides a complete interface for CRUD operations on quiz questions.
 * 
 * @author Christos Poulios
 * @version 2.0
 */
public class QuizQuestionMainPanel extends JPanel implements GUIConstants, QuizQuestionDelegator {

	private static final long serialVersionUID = 1L;

	// ==================== Components ====================
	private final QuizDataManager dataManager;
	private QuestionPanel questionPanel;
	private QuestionListPanel questionListPanel;
	private QuestionButtonPanel buttonPanel;

	// ==================== Constructor ====================

	public QuizQuestionMainPanel(QuizDataManager dataManager) {
		this.dataManager = dataManager;
		initializeLayout();
		initializeComponents();
		connectComponents();
	}

	// ==================== Initialization ====================

	private void initializeLayout() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));
	}

	private void initializeComponents() {
		questionPanel = new QuestionPanel();
		questionPanel.setEditable(true);
		questionPanel.getMetaPanel().getQuestionTextArea().setBackground(BACKGROUND_COLOR);

		questionListPanel = new QuestionListPanel(dataManager);

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

	// ==================== QuizQuestionDelegator Implementation

	@Override
	public void onThemeSelected(String themeTitle) {
		questionListPanel.updateQuestionList(themeTitle);
		clearQuestionSelection();
		enableEditingIfThemeSelected(themeTitle);
	}

	@Override
	public void onQuestionSelected(String entry, int index) {
		QuestionDTO question = dataManager.getQuestionByGlobalIndex(index);
		displayQuestion(question);
	}

	@Override
	public void onQuestionDeleted(String questionTitle) {
		try {
			int selectedIndex = questionListPanel.getQuestionList().getSelectedIndex();
			if (selectedIndex < 0) {
				buttonPanel.setMessage("Keine Frage zum Löschen ausgewählt");
				return;
			}

			QuestionDTO questionToDelete = dataManager.getQuestionByGlobalIndex(selectedIndex);
			if (questionToDelete == null) {
				buttonPanel.setMessage("Frage nicht gefunden");
				return;
			}

			if (removeQuestionFromTheme(questionToDelete)) {
				buttonPanel.setMessage("Frage erfolgreich gelöscht: " + questionToDelete.getQuestionTitle());
				clearQuestionSelection();
				questionListPanel.updateQuestionList(questionListPanel.getSelectedThemeTitle());
			} else {
				buttonPanel.setMessage("Frage konnte nicht gelöscht werden");
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

			ArrayList<AnswerDTO> answers = collectAnswers(question);
			if (!validateAnswers(answers)) {
				return;
			}

			question.setAnswers(answers);

			String result = dataManager.saveTheme(targetTheme);
			if (result.contains("erfolgreich")) {
				buttonPanel.setMessage("Frage erfolgreich gespeichert: " + question.getQuestionTitle());
				questionListPanel.updateQuestionList(questionListPanel.getSelectedThemeTitle());
			} else {
				buttonPanel.setMessage("Fehler beim Speichern: " + result);
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

	// ==================== Helper Methods ====================

	private void clearQuestionSelection() {
		questionListPanel.getQuestionList().clearSelection();
		questionPanel.fillWithQuestionData(null);
	}

	private void enableEditingIfThemeSelected(String themeTitle) {
		if (themeTitle != null && !themeTitle.equals("Alle Themen")) {
			questionPanel.setEditable(true);
		}
	}

	private void displayQuestion(QuestionDTO question) {
		if (question != null) {
			questionPanel.fillWithQuestionData(question);
			questionPanel.setEditable(true);
		}
	}

	private boolean removeQuestionFromTheme(QuestionDTO questionToDelete) {
		for (ThemeDTO theme : dataManager.getAllThemes()) {
			if (theme.getQuestions() != null) {
				for (int i = 0; i < theme.getQuestions().size(); i++) {
					QuestionDTO q = theme.getQuestions().get(i);
					if (q.getId() == questionToDelete.getId()) {
						theme.getQuestions().remove(i);
						String result = dataManager.saveTheme(theme);
						return result.contains("erfolgreich");
					}
				}
			}
		}
		return false;
	}

	private boolean validateInput(String themeTitle) {
		if (themeTitle == null || themeTitle.trim().isEmpty()) {
			buttonPanel.setMessage("Bitte wählen Sie ein Thema aus");
			return false;
		}

		String questionTitle = questionPanel.getMetaPanel().getTitleField().getText().trim();
		if (questionTitle.isEmpty()) {
			buttonPanel.setMessage("Bitte geben Sie einen Fragetitel ein");
			return false;
		}

		String questionContent = questionPanel.getMetaPanel().getQuestionTextArea().getText().trim();
		if (questionContent.isEmpty()) {
			buttonPanel.setMessage("Bitte geben Sie einen Fragetext ein");
			return false;
		}

		return true;
	}

	private ThemeDTO findTheme(String themeTitle) {
		for (ThemeDTO theme : dataManager.getAllThemes()) {
			if (themeTitle.equals(theme.getThemeTitle())) {
				return theme;
			}
		}
		return null;
	}

	private QuestionDTO getOrCreateQuestion(ThemeDTO targetTheme) {
		int selectedIndex = questionListPanel.getQuestionList().getSelectedIndex();
		boolean isNewQuestion = selectedIndex < 0;

		if (isNewQuestion) {
			QuestionDTO question = new QuestionDTO();
			question.setId(generateNewQuestionId(targetTheme));
			targetTheme.getQuestions().add(question);
			return question;
		} else {
			return dataManager.getQuestionByGlobalIndex(selectedIndex);
		}
	}

	private void updateQuestionData(QuestionDTO question) {
		String questionTitle = questionPanel.getMetaPanel().getTitleField().getText().trim();
		String questionContent = questionPanel.getMetaPanel().getQuestionTextArea().getText().trim();

		question.setQuestionTitle(questionTitle);
		question.setQuestionText(questionContent);
	}

	private ArrayList<AnswerDTO> collectAnswers(QuestionDTO question) {
		ArrayList<AnswerDTO> answers = new ArrayList<>();
		AnswerRowPanel[] answerRows = questionPanel.getAnswersPanel().getAnswerRows();

		for (int i = 0; i < answerRows.length; i++) {
			String answerText = answerRows[i].getAnswerText().trim();
			if (!answerText.isEmpty()) {
				AnswerDTO answer = new AnswerDTO();
				answer.setId(i);
				answer.setAnswerText(answerText);
				answer.setCorrect(answerRows[i].isCorrect());
				answer.setQuestionId(question.getId());
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

		boolean hasCorrectAnswer = answers.stream().anyMatch(AnswerDTO::isCorrect);
		if (!hasCorrectAnswer) {
			buttonPanel.setMessage("Bitte markieren Sie mindestens eine richtige Antwort");
			return false;
		}

		return true;
	}

	private int generateNewQuestionId(ThemeDTO theme) {
		int maxId = 0;
		if (theme.getQuestions() != null) {
			for (QuestionDTO q : theme.getQuestions()) {
				maxId = Math.max(maxId, q.getId());
			}
		}
		return maxId + 1;
	}
}
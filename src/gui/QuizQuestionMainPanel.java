package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import gui.interfaces.GUIConstants;
import gui.interfaces.QuizPanelDelegator;
import gui.interfaces.QuizQuestionDelegator;
import gui.subpanels.QuestionButtonPanel;
import gui.subpanels.QuestionListPanel;
import gui.subpanels.QuestionPanel;
import persistence.serialization.QuizDataManager;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;
import gui.subpanels.AnswerRowPanel;
import quizlogic.dto.AnswerDTO;

/**
 * Main panel for managing quiz questions with persistent data storage.
 * 
 * Displays a question list, question details, and control buttons. Integrates
 * with QuizDataManager for real-time data synchronization.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class QuizQuestionMainPanel extends JPanel implements GUIConstants, QuizQuestionDelegator, QuizPanelDelegator {
	private static final long serialVersionUID = 1L;
	private QuestionListPanel questionListPanel;
	private QuestionPanel questionPanel;
	private QuestionButtonPanel btnPanel;
	private QuizDataManager dataManager;

	/**
	 * Constructs the quiz question main panel with data manager integration.
	 * 
	 * @param dataManager The central data manager for persistence
	 */
	public QuizQuestionMainPanel(QuizDataManager dataManager) {
		this.dataManager = dataManager;
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));
		initPanels();
	}

	/**
	 * Initializes the question panel, list panel, and control buttons.
	 */
	private void initPanels() {
		questionPanel = new QuestionPanel();
		questionPanel.getMetaPanel().getQuestionTextArea().setBackground(BACKGROUND_COLOR);
		questionPanel.setEditable(true);
		questionListPanel = new QuestionListPanel(dataManager);
		questionListPanel.setDelegate(this);

		btnPanel = new QuestionButtonPanel(BTN_DELETE_QUESTION, BTN_SAVE_QUESTION, BTN_ADD_QUESTION);
		btnPanel.setDelegate(this);
		btnPanel.setPanelReferences(questionPanel, questionListPanel);

		add(questionPanel, BorderLayout.WEST);
		add(questionListPanel, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);

	}

	@Override
	public void onThemeSelected(String themeTitle) {
		questionListPanel.updateQuestionList(themeTitle);
		questionListPanel.getQuestionList().clearSelection();
		questionPanel.fillWithQuestionData(null);

		if (themeTitle != null && !themeTitle.equals("Alle Themen")) {
			questionPanel.setEditable(true);
		}
	}

	@Override
	public void onQuestionSelected(String entry, int index) {
		QuestionDTO question = dataManager.getQuestionByGlobalIndex(index);
		if (question != null) {
			questionPanel.fillWithQuestionData(question);
			questionPanel.setEditable(true);
			System.out.println(question);
		} else {
			questionPanel.fillWithQuestionData(null);
			questionPanel.setEditable(true);
		}
	}

	@Override
	public void onQuestionDeleted(String questionText) {
		try {

			int selectedIndex = questionListPanel.getQuestionList().getSelectedIndex();
			if (selectedIndex < 0) {
				btnPanel.setMessage("Keine Frage zum Löschen ausgewählt");
				return;
			}

			QuestionDTO questionToDelete = dataManager.getQuestionByGlobalIndex(selectedIndex);
			if (questionToDelete == null) {
				btnPanel.setMessage("Frage nicht gefunden");
				return;
			}


			boolean deleted = false;
			for (ThemeDTO theme : dataManager.getAllThemes()) {
				if (theme.getQuestions() != null) {
					for (int i = 0; i < theme.getQuestions().size(); i++) {
						QuestionDTO q = theme.getQuestions().get(i);
						if (q.getId() == questionToDelete.getId()) {
							theme.getQuestions().remove(i);
							String result = dataManager.saveTheme(theme);
							if (result.contains("erfolgreich")) {
								deleted = true;
								btnPanel.setMessage("Frage erfolgreich gelöscht: " + questionToDelete.getQuestionTitle());

								questionPanel.fillWithQuestionData(null);
								questionListPanel.updateQuestionList(questionListPanel.getSelectedThemeTitle());
							} else {
								btnPanel.setMessage("Fehler beim Löschen: " + result);
							}
							break;
						}
					}
					if (deleted) break;
				}
			}

			if (!deleted) {
				btnPanel.setMessage("Frage konnte nicht gelöscht werden");
			}
		} catch (Exception e) {
			btnPanel.setMessage("Fehler beim Löschen: " + e.getMessage());
		}
	}

	@Override
	public void onQuestionSaved(String questionText, String themeTitle, String questionType, String answerText,
			boolean isCorrect) {
		try {

			if (themeTitle == null || themeTitle.trim().isEmpty()) {
				btnPanel.setMessage("Bitte wählen Sie ein Thema aus");
				return;
			}

			String questionTitle = questionPanel.getMetaPanel().getTitleField().getText().trim();
			String questionContent = questionPanel.getMetaPanel().getQuestionTextArea().getText().trim();

			if (questionTitle.isEmpty()) {
				btnPanel.setMessage("Bitte geben Sie einen Fragetitel ein");
				return;
			}

			if (questionContent.isEmpty()) {
				btnPanel.setMessage("Bitte geben Sie einen Fragetext ein");
				return;
			}


			ThemeDTO targetTheme = null;
			for (ThemeDTO theme : dataManager.getAllThemes()) {
				if (themeTitle.equals(theme.getThemeTitle())) {
					targetTheme = theme;
					break;
				}
			}

			if (targetTheme == null) {
				btnPanel.setMessage("Thema nicht gefunden: " + themeTitle);
				return;
			}


			QuestionDTO question;
			int selectedIndex = questionListPanel.getQuestionList().getSelectedIndex();
			boolean isNewQuestion = selectedIndex < 0;

			if (isNewQuestion) {

				question = new QuestionDTO();
				question.setId(generateNewQuestionId(targetTheme));
			} else {

				question = dataManager.getQuestionByGlobalIndex(selectedIndex);
				if (question == null) {
					btnPanel.setMessage("Frage nicht gefunden");
					return;
				}
			}


			question.setQuestionTitle(questionTitle);
			question.setQuestionText(questionContent);


			ArrayList<AnswerDTO> answers = new ArrayList<>();
			AnswerRowPanel[] answerRows = questionPanel.getAnswersPanel().getAnswerRows();
			boolean hasCorrectAnswer = false;

			for (int i = 0; i < answerRows.length; i++) {
				String answerTextUI = answerRows[i].getAnswerText().trim();
				if (!answerTextUI.isEmpty()) {
					AnswerDTO answer = new AnswerDTO();
					answer.setId(i);
					answer.setAnswerText(answerTextUI);
					answer.setCorrect(answerRows[i].isCorrect());
					answer.setQuestionId(question.getId()); 
					answers.add(answer);

					if (answerRows[i].isCorrect()) {
						hasCorrectAnswer = true;
					}
				}
			}

			if (answers.isEmpty()) {
				btnPanel.setMessage("Bitte geben Sie mindestens eine Antwort ein");
				return;
			}

			if (!hasCorrectAnswer) {
				btnPanel.setMessage("Bitte markieren Sie mindestens eine richtige Antwort");
				return;
			}

			question.setAnswers(answers);


			if (isNewQuestion) {
				targetTheme.getQuestions().add(question);
			}


			String result = dataManager.saveTheme(targetTheme);
			if (result.contains("erfolgreich")) {
				btnPanel.setMessage("Frage erfolgreich gespeichert: " + questionTitle);

				questionListPanel.updateQuestionList(questionListPanel.getSelectedThemeTitle());
			} else {
				btnPanel.setMessage("Fehler beim Speichern: " + result);
			}

		} catch (Exception e) {
			btnPanel.setMessage("Fehler beim Speichern: " + e.getMessage());
		}
	}

	@Override
	public void onNewQuestion(String themeTitle, String questionType) {
		try {
			if (themeTitle == null || themeTitle.trim().isEmpty()) {
				btnPanel.setMessage("Bitte wählen Sie ein Thema aus der Liste");
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

			btnPanel.setMessage("Neue Frage erstellen für Thema: " + themeTitle);

		} catch (Exception e) {
			btnPanel.setMessage("Fehler beim Erstellen einer neuen Frage: " + e.getMessage());
		}
	}

	@Override
	public void onShowAnswerClicked() {
		// Not used for question tab
	}

	@Override
	public void onSaveAnswerClicked() {
		// Not used for question tab
	}

	@Override
	public void onNextQuestionClicked() {
		// Not used for question tab
	}

	/**
	 * Gets the QuestionListPanel instance.
	 * 
	 * @return The QuestionListPanel instance
	 */
	public QuestionListPanel getQuestionListPanel() {
		return questionListPanel;
	}

	/**
	 * Gets the QuestionPanel instance.
	 * 
	 * @return The QuestionPanel instance
	 */
	public QuestionPanel getQuestionPanel() {
		return questionPanel;
	}

	/**
	 * Generates a new unique question ID for the given theme.
	 */
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
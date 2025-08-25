package gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import constants.GUIConstants;
import constants.UserStringConstants;
import gui.interfaces.QuizPanelDelegator;
import gui.subpanels.QuestionPanel;
import gui.subpanels.QuizButtonPanel;
import gui.subpanels.QuizInfoViewPanel;
import persistence.mariaDB.DBManager;
import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.QuizSessionDTO;
import quizlogic.dto.ThemeDTO;
import quizlogic.dto.UserAnswerDTO;

/**
 * Main panel for the quiz tab providing quiz gameplay interface with session
 * tracking.
 */
public class QuizMainPanel extends JPanel implements GUIConstants, QuizPanelDelegator {
	private static final long serialVersionUID = 1L;

	private QuestionPanel questionPanel;
	private QuizInfoViewPanel quizInfoViewPanel;
	private QuizButtonPanel buttonPanel;
	private DBManager dbManager;
	private JPanel contentPanel;

	private ThemeDTO selectedTheme;
	private QuizSessionDTO currentSession;
	private QuestionDTO currentQuestion;

	public QuizMainPanel(DBManager dbManager) {
		this.dbManager = dbManager;
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));
		setBorder(BorderFactory.createEmptyBorder(PANEL_MARGIN_V, PANEL_MARGIN_H, PANEL_MARGIN_V, PANEL_MARGIN_H));

		initPanels();
		startNewSession();
		loadNextQuestion();
	}

	private void initPanels() {
		questionPanel = new QuestionPanel();
		quizInfoViewPanel = new QuizInfoViewPanel(dbManager);
		
		quizInfoViewPanel.setThemeSelectionDelegate(this::onThemeSelected);

		contentPanel = new JPanel(new java.awt.GridLayout(1, 2, PANEL_MARGIN_H, 0));
		contentPanel.setBackground(BACKGROUND_COLOR);
		contentPanel.add(questionPanel);
		contentPanel.add(quizInfoViewPanel);

		add(contentPanel, BorderLayout.CENTER);

		buttonPanel = new QuizButtonPanel(BTN_SHOW_ANSWER, BTN_SAVE_ANSWER, BTN_NEXT_QUESTION);
		buttonPanel.setDelegate(this);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void startNewSession() {
		currentSession = new QuizSessionDTO();
		currentSession.start();
	}

	private void loadNextQuestion() {
		if (selectedTheme != null) {
			currentQuestion = dbManager.getRandomQuestionFor(selectedTheme);
		} else {
			currentQuestion = dbManager.getRandomQuestion();
		}

		fillWithQuestionData(currentQuestion);
	}

	public void fillWithQuestionData(QuestionDTO question) {
		this.currentQuestion = question;

		if (question != null) {
			List<AnswerDTO> answers = dbManager.getAnswersFor(question);
			question.setAnswers(answers);
		}

		questionPanel.fillWithQuestionData(question);

		if (question == null) {
			buttonPanel.setMessage(UserStringConstants.MSG_NO_QUESTION_AVAILABLE);
		} else {
			buttonPanel.setMessage("");
			questionPanel.setQuizMode(true);
			clearAnswerSelections();
		}
	}

	private void clearAnswerSelections() {
		if (questionPanel != null && questionPanel.getAnswersPanel() != null) {
			for (gui.subpanels.AnswerRowPanel row : questionPanel.getAnswersPanel().getAnswerRows()) {
				row.getCheckBox().setSelected(false);
			}
		}
	}

	public void setSelectedTheme(ThemeDTO theme) {
		this.selectedTheme = theme;
		loadNextQuestion();
	}

	@Override
	public void onShowAnswerClicked() {
		if (currentQuestion == null)
			return;

		List<AnswerDTO> answers = currentQuestion.getAnswers();
		if (answers != null) {
			// Find correct answer text for InfoView
			String correctAnswerText = "";
			for (AnswerDTO answer : answers) {
				if (answer.isCorrect()) {
					correctAnswerText = answer.getAnswerText();
					break;
				}
			}
			
			quizInfoViewPanel.showCorrectAnswer(correctAnswerText);
			buttonPanel.setMessage("Die korrekte Antwort wird in der Info-Ansicht angezeigt.");
		}
	}

	@Override
	public void onSaveAnswerClicked() {
		if (currentQuestion == null || currentSession == null)
			return;

		// Get selected answers from UI
		List<AnswerDTO> answers = currentQuestion.getAnswers();
		if (answers == null || answers.isEmpty())
			return;

		// Find selected answer(s)
		List<Integer> selectedIndices = questionPanel.getAnswersPanel().getSelectedAnswerIndices();
		
		if (selectedIndices.isEmpty()) {
			buttonPanel.setMessage("Bitte wählen Sie mindestens eine Antwort aus.");
			return;
		}

		boolean isCorrect = checkUserAnswers(selectedIndices, answers);
		
		for (Integer index : selectedIndices) {
			if (index < answers.size()) {
				AnswerDTO answer = answers.get(index);
				UserAnswerDTO userAnswer = new UserAnswerDTO(currentSession.getId(), currentQuestion.getId(), answer.getId(),
						true, answer.isCorrect());

				currentSession.addUserAnswer(userAnswer);
			}
		}

		// Show feedback to user
		if (isCorrect) {
			buttonPanel.setMessage("✓ Richtig! Ihre Antwort ist korrekt.");
			quizInfoViewPanel.showAnswerFeedback(true, "Ihre Antwort ist richtig.");
		} else {
			buttonPanel.setMessage("✗ Falsch! Ihre Antwort ist nicht korrekt.");
			quizInfoViewPanel.showAnswerFeedback(false, "Leider falsch. Versuchen Sie es erneut.");
		}
	}

	/**
	 * Checks if the user's selected answers are correct.
	 * 
	 * @param selectedIndices List of selected answer indices
	 * @param answers List of all answers for the question
	 * @return true if all selected answers are correct and no correct answers are missed
	 */
	private boolean checkUserAnswers(List<Integer> selectedIndices, List<AnswerDTO> answers) {
		// Count correct answers in the question
		int totalCorrectAnswers = 0;
		int userCorrectAnswers = 0;
		
		for (int i = 0; i < answers.size(); i++) {
			AnswerDTO answer = answers.get(i);
			if (answer.isCorrect()) {
				totalCorrectAnswers++;
				// Check if user selected this correct answer
				if (selectedIndices.contains(i)) {
					userCorrectAnswers++;
				}
			} else {
				// Check if user incorrectly selected a wrong answer
				if (selectedIndices.contains(i)) {
					return false; // User selected a wrong answer
				}
			}
		}
		

		return userCorrectAnswers == totalCorrectAnswers && userCorrectAnswers > 0;
	}

	@Override
	public void onNextQuestionClicked() {
		loadNextQuestion();
		buttonPanel.setMessage(UserStringConstants.MSG_NEXT_QUESTION);
	}

	@Override
	public void onQuestionSelected(String entry, int index) {
		buttonPanel.setMessage(String.format(UserStringConstants.MSG_QUESTION_SELECTED, entry));
	}

	@Override
	public void onThemeSelected(String themeTitle) {
		buttonPanel.setMessage(String.format(UserStringConstants.MSG_THEME_SELECTED, themeTitle));

		// Find theme by title and set as selected
		if (dbManager != null) {
			List<ThemeDTO> themes = dbManager.getAllThemes();
			for (ThemeDTO theme : themes) {
				if (theme.getThemeTitle().equals(themeTitle)) {
					setSelectedTheme(theme);
					break;
				}
			}
		}
	}

	public QuizSessionDTO getCurrentSession() {
		return currentSession;
	}

	public ThemeDTO getSelectedTheme() {
		return selectedTheme;
	}
}
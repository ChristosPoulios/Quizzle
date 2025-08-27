package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import constants.GUIConstants;
import constants.LogicConstants;
import constants.UserStringConstants;
import gui.interfaces.QuizPanelDelegator;
import gui.subpanels.QuestionPanel;
import gui.subpanels.QuizButtonPanel;
import gui.subpanels.QuizInfoViewPanel;
import persistence.QuizDataInterface;
import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.QuizSessionDTO;
import quizlogic.dto.ThemeDTO;
import quizlogic.dto.UserAnswerDTO;

/**
 * Main panel for the quiz tab providing quiz gameplay interface with session
 * tracking and automatic fallback data storage support.
 */
public class QuizMainPanel extends JPanel implements GUIConstants, QuizPanelDelegator {
	private static final long serialVersionUID = 1L;

	/** Panels */
	private QuestionPanel questionPanel;
	private QuizInfoViewPanel quizInfoViewPanel;
	private QuizButtonPanel buttonPanel;
	private QuizDataInterface dataManager;

	/** State */
	private ThemeDTO selectedTheme;
	private QuizSessionDTO currentSession;
	private QuestionDTO currentQuestion;

	public QuizMainPanel(QuizDataInterface dataManager) {
		this.dataManager = dataManager;
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));

		initPanels();
		startNewSession();
		loadNextQuestion();
	}

	private void initPanels() {
		questionPanel = new QuestionPanel();
		questionPanel.setPreferredSize(new java.awt.Dimension(LEFT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT));

		quizInfoViewPanel = new QuizInfoViewPanel(dataManager);
		quizInfoViewPanel.setPreferredSize(new java.awt.Dimension(RIGHT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT));

		quizInfoViewPanel.setThemeSelectionDelegate(this::onThemeSelected);

		add(questionPanel, BorderLayout.WEST);
		add(quizInfoViewPanel, BorderLayout.CENTER);

		buttonPanel = new QuizButtonPanel(BTN_SHOW_ANSWER, BTN_SAVE_ANSWER, BTN_NEXT_QUESTION);
		buttonPanel.setDelegate(this);
		buttonPanel.setMessage(UserStringConstants.MESSAGE_DEFAULT);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void startNewSession() {
		currentSession = new QuizSessionDTO();
		currentSession.start();
	}

	private void loadNextQuestion() {
		if (selectedTheme != null) {
			currentQuestion = dataManager.getRandomQuestionFor(selectedTheme);
		} else {
			currentQuestion = dataManager.getRandomQuestion();
		}

		fillWithQuestionData(currentQuestion);
		quizInfoViewPanel.showWelcomeMessage();
	}

	public void fillWithQuestionData(QuestionDTO question) {
		this.currentQuestion = question;

		if (question != null) {
			List<AnswerDTO> answers = dataManager.getAnswersFor(question);
			question.setAnswers(answers);
		}

		String themeName = null;
		if (selectedTheme != null) {
			themeName = selectedTheme.getThemeTitle();
		} else {
			themeName = UserStringConstants.ALL_THEMES_OPTION;
		}
		questionPanel.fillWithQuestionData(question, themeName);

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

			List<String> correctAnswers = new ArrayList<>();
			for (AnswerDTO answer : answers) {
				if (answer.isCorrect()) {
					correctAnswers.add(answer.getAnswerText());
				}
			}
			String correctAnswerText = String.join(", ", correctAnswers);
			quizInfoViewPanel.setAnswerText(correctAnswerText);
			
			String feedbackMsg;
			if (correctAnswers.size() == 1) {
				feedbackMsg = String.format(UserStringConstants.QUIZ_INFO_FEEDBACK_SHOW_SINGLE_CORRECT,
						correctAnswerText);
			} else {
				feedbackMsg = String.format(UserStringConstants.QUIZ_INFO_FEEDBACK_SHOW_MULTIPLE_CORRECT,
						correctAnswerText);
			}
			
			buttonPanel.setMessage(feedbackMsg);
			quizInfoViewPanel.showAnswerFeedback(true, feedbackMsg);

			buttonPanel.getButton1().setEnabled(false);
			buttonPanel.getButton2().setEnabled(false);
			buttonPanel.getButton3().setEnabled(true);
		}
	}

	@Override
	public void onSaveAnswerClicked() {
		if (currentQuestion == null || currentSession == null)
			return;

		List<AnswerDTO> answers = currentQuestion.getAnswers();
		if (answers == null || answers.isEmpty())
			return;

		List<Integer> selectedIndices = questionPanel.getAnswersPanel().getSelectedAnswerIndices();

		if (selectedIndices.isEmpty()) {
			buttonPanel.setMessage("Bitte wählen Sie mindestens eine Antwort aus.");
			return;
		}

		boolean isCorrect = checkUserAnswers(selectedIndices, answers);

		for (Integer index : selectedIndices) {
			if (index < answers.size()) {
				AnswerDTO answer = answers.get(index);
				UserAnswerDTO userAnswer = new UserAnswerDTO(currentSession.getId(), currentQuestion.getId(),
						answer.getId(), true, answer.isCorrect());

				currentSession.addUserAnswer(userAnswer);
			}
		}

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
	 * @param answers         List of all answers for the question
	 * @return true if all selected answers are correct and no correct answers are
	 *         missed
	 */
	private boolean checkUserAnswers(List<Integer> selectedIndices, List<AnswerDTO> answers) {

		int totalCorrectAnswers = LogicConstants.MIN_VALID_ID;
		int userCorrectAnswers = LogicConstants.MIN_VALID_ID;

		for (int i = LogicConstants.MIN_VALID_ID; i < answers.size(); i++) {
			AnswerDTO answer = answers.get(i);
			if (answer.isCorrect()) {
				totalCorrectAnswers++;

				if (selectedIndices.contains(i)) {
					userCorrectAnswers++;
				}
			} else {

				if (selectedIndices.contains(i)) {
					return false;
				}
			}
		}

		return userCorrectAnswers == totalCorrectAnswers && userCorrectAnswers > LogicConstants.MIN_VALID_ID;
	}

	@Override
	public void onNextQuestionClicked() {
		loadNextQuestion();
		buttonPanel.setMessage(UserStringConstants.MSG_NEXT_QUESTION);

		buttonPanel.getButton1().setEnabled(true);
		buttonPanel.getButton2().setEnabled(true);
		buttonPanel.getButton3().setEnabled(true);
	}

	@Override
	public void onQuestionSelected(String entry, int index) {
		buttonPanel.setMessage(String.format(UserStringConstants.MSG_QUESTION_SELECTED, entry));
	}

	@Override
	public void onThemeSelected(String themeTitle) {
		buttonPanel.setMessage(String.format(UserStringConstants.MSG_THEME_SELECTED, themeTitle));

		if (UserStringConstants.ALL_THEMES_OPTION.equals(themeTitle)) {
			setSelectedTheme(null);
		} else {
			// Strip the "*" prefix if present for themes without descriptions
			String actualThemeTitle = themeTitle.startsWith("* ") ? themeTitle.substring(2) : themeTitle;

			if (dataManager != null) {
				List<ThemeDTO> themes = dataManager.getAllThemes();
				for (ThemeDTO theme : themes) {
					if (theme.getThemeTitle().equals(actualThemeTitle)) {
						setSelectedTheme(theme);
						return;
					}
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
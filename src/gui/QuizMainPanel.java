package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import constants.ConfigManager;
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
 * <p>
 * This panel coordinates the entire quiz-taking experience, including:
 * <ul>
 * <li>Question display and navigation</li>
 * <li>Answer selection and validation</li>
 * <li>Quiz session tracking and statistics</li>
 * <li>Theme-based and random question selection</li>
 * <li>User progress monitoring</li>
 * </ul>
 * <p>
 * The panel integrates with the configured data storage backend (MariaDB database
 * or file-based serialization with automatic fallback) to provide seamless
 * quiz functionality regardless of the underlying persistence mechanism.
 * <p>
 * <strong>Key Features:</strong>
 * <ul>
 * <li>Real-time session tracking with user answer recording</li>
 * <li>Support for both theme-specific and random quiz modes</li>
 * <li>Automatic question progression and scoring</li>
 * <li>Integration with statistics and performance tracking</li>
 * <li>Responsive UI updates based on user interactions</li>
 * </ul>
 * <p>
 * This panel implements the {@link QuizPanelDelegator} interface to handle
 * quiz-specific UI events and coordinate between different quiz components.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 * @see QuizPanelDelegator
 * @see QuestionPanel
 * @see QuizButtonPanel
 * @see QuizInfoViewPanel
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

	/** Reference to parent frame for statistics updates */
	private QFrame parentFrame;

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

			if (!answers.isEmpty()) {
				AnswerDTO firstAnswer = answers.get(0);
				UserAnswerDTO userAnswer = new UserAnswerDTO(currentSession.getId(), currentQuestion.getId(),
						firstAnswer.getId(), false, false);
				currentSession.addUserAnswer(userAnswer);
			}

			buttonPanel.setMessage("✗ Keine Antwort gegeben - als falsch bewertet.");
			quizInfoViewPanel.showAnswerFeedback(false, "Keine Antwort gegeben. Die Frage wird als falsch bewertet.");

			buttonPanel.getButton1().setEnabled(true);
			buttonPanel.getButton2().setEnabled(false);
			buttonPanel.getButton3().setEnabled(true);

			if (parentFrame != null) {
				parentFrame.updateStatistics();
			}
			return;
		}

		boolean isCorrect = checkUserAnswers(selectedIndices, answers);

		for (Integer index : selectedIndices) {
			if (index < answers.size()) {
				AnswerDTO answer = answers.get(index);
				UserAnswerDTO userAnswer = new UserAnswerDTO(currentSession.getId(), currentQuestion.getId(),
						answer.getId(), true, isCorrect);

				currentSession.addUserAnswer(userAnswer);
			}
		}

		if (isCorrect) {
			buttonPanel.setMessage("✓ Richtig! Ihre Antwort ist korrekt.");
			quizInfoViewPanel.showAnswerFeedback(true, "Ihre Antwort ist richtig.");

			buttonPanel.getButton1().setEnabled(false);
			buttonPanel.getButton2().setEnabled(false);
		} else {
			buttonPanel.setMessage("✗ Falsch! Ihre Antwort ist nicht korrekt.");
			quizInfoViewPanel.showAnswerFeedback(false, "Leider falsch. Versuchen Sie es erneut.");

			buttonPanel.getButton1().setEnabled(true);
			buttonPanel.getButton2().setEnabled(false);
		}

		buttonPanel.getButton3().setEnabled(true);

		if (parentFrame != null) {
			parentFrame.updateStatistics();
		}
	}

	/**
	 * Checks if the user's selected answers are correct.
	 * 
	 * For questions with multiple correct answers (2 or more), ALL correct answers
	 * must be selected and NO incorrect answers must be selected for the question
	 * to be considered correct. If only some of the correct answers are selected,
	 * the question is marked as incorrect.
	 *
	 * @param selectedIndices List of indices of answers selected by the user
	 * @param answers         List of all answers for the question
	 * @return true if all selected answers are correct and no correct answers are
	 *         missed
	 */
	private boolean checkUserAnswers(List<Integer> selectedIndices, List<AnswerDTO> answers) {

		int totalCorrectAnswers = 0;
		int userCorrectAnswers = 0;

		for (int i = 0; i < answers.size(); i++) {
			AnswerDTO answer = answers.get(i);
			if (answer.isCorrect()) {
				totalCorrectAnswers++;

				if (selectedIndices.contains(i)) {
					userCorrectAnswers++;
				}
			} else {

				if (selectedIndices.contains(i)) {
					ConfigManager.debugPrint("DEBUG: User selected incorrect answer at index " + i);
					return false;
				}
			}
		}

		ConfigManager.debugPrint("DEBUG: Total correct answers: " + totalCorrectAnswers);
		ConfigManager.debugPrint("DEBUG: User correct answers: " + userCorrectAnswers);
		ConfigManager.debugPrint("DEBUG: Selected indices: " + selectedIndices);

		if (totalCorrectAnswers >= 2) {
			boolean result = userCorrectAnswers == totalCorrectAnswers;
			ConfigManager.debugPrint("DEBUG: Multiple correct answers case - returning: " + result);
			return result;
		}

		if (totalCorrectAnswers == 1) {
			boolean result = userCorrectAnswers == 1;
			ConfigManager.debugPrint("DEBUG: Single correct answer case - returning: " + result);
			return result;
		}

		ConfigManager.debugPrint("DEBUG: No correct answers found - returning false");
		return false;
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

			String actualThemeTitle = themeTitle.startsWith("* ")
					? themeTitle.substring(LogicConstants.THEME_PREFIX_LENGTH)
					: themeTitle;

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

	/**
	 * Refreshes the theme combo box in the quiz info view panel. This should be
	 * called when new themes are created or questions are added.
	 */
	public void refreshThemeComboBox() {
		if (quizInfoViewPanel != null) {
			quizInfoViewPanel.refreshThemeComboBox(dataManager);
		}
	}

	public QuizSessionDTO getCurrentSession() {
		return currentSession;
	}

	public ThemeDTO getSelectedTheme() {
		return selectedTheme;
	}

	/**
	 * Sets the parent frame reference for statistics updates.
	 * 
	 * @param parentFrame the parent QFrame instance
	 */
	public void setParentFrame(QFrame parentFrame) {
		this.parentFrame = parentFrame;
	}
}
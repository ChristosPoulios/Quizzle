package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import gui.interfaces.GUIConstants;
import gui.interfaces.QuizPanelDelegator;
import gui.subpanels.QuestionPanel;
import gui.subpanels.QuizButtonPanel;
import gui.subpanels.QuizInfoViewPanel;
import persistence.serialization.QuizDataManager;
import quizlogic.dto.QuestionDTO;

/**
 * Main panel for the quiz tab with persistent data management.
 * 
 * Uses QuestionPanel for question display, QuizInfoViewPanel for statistics,
 * and QuizDataManager for data persistence. Supports real-time data updates.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 2.0
 */
public class QuizMainPanel extends JPanel implements GUIConstants, QuizPanelDelegator {
	private static final long serialVersionUID = 1L;
	private QuestionPanel questionPanel;
	private QuizInfoViewPanel quizInfoViewPanel;
	private QuizButtonPanel buttonPanel;
	private QuizDataManager dataManager;
	private JPanel contentPanel;

	/**
	 * Constructs the quiz main panel with data manager integration.
	 * 
	 * @param dataManager The central data manager for persistence
	 */
	public QuizMainPanel(QuizDataManager dataManager) {
		this.dataManager = dataManager;
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));
		setBorder(BorderFactory.createEmptyBorder(PANEL_MARGIN_V, PANEL_MARGIN_H, PANEL_MARGIN_V, PANEL_MARGIN_H));

		initPanels();
		fillWithQuestionData(dataManager.getRandomQuestion());
	}

	/**
	 * Initializes main child panels and their layout with data manager integration.
	 */
	private void initPanels() {
		questionPanel = new QuestionPanel();
		quizInfoViewPanel = new QuizInfoViewPanel(dataManager);

		contentPanel = new JPanel(new GridLayout(1, 2, PANEL_MARGIN_H, 0));
		contentPanel.setBackground(BACKGROUND_COLOR);
		contentPanel.add(questionPanel);
		contentPanel.add(quizInfoViewPanel);
		add(contentPanel, BorderLayout.CENTER);

		buttonPanel = new QuizButtonPanel(BTN_SHOW_ANSWER, BTN_SAVE_ANSWER, BTN_NEXT_QUESTION);
		buttonPanel.setDelegate(this);
		add(buttonPanel, BorderLayout.SOUTH);

		dataManager.addUpdateListener(() -> {
			quizInfoViewPanel.refreshData();
		});
	}

	/**
	 * Fills the QuestionPanel with the provided question data.
	 * 
	 * @param question The quiz question to display, or null to clear
	 */
	public void fillWithQuestionData(QuestionDTO question) {
		questionPanel.fillWithQuestionData(question);
		if (question == null) {
			buttonPanel.setMessage("");
		} else {
			buttonPanel.setMessage("");
		}
	}

	@Override
	public void onShowAnswerClicked() {
	    String correct = questionPanel.getAnswersPanel().getCorrectAnswerText();
	    buttonPanel.setMessage("Correct answer: " + correct);
	}

	@Override
	public void onSaveAnswerClicked() {
	    buttonPanel.setMessage("Answer saved.");  
	}

	@Override
	public void onNextQuestionClicked() {
	    fillWithQuestionData(dataManager.getRandomQuestion());
	    buttonPanel.setMessage("Next question.");
	}

	@Override
	public void onQuestionSelected(String entry, int idx) {
	    QuestionDTO question = dataManager.getQuestionByGlobalIndex(idx);
	    if (question != null) {
	        fillWithQuestionData(question);
	        buttonPanel.setMessage("Question selected: " + question.getQuestionTitle()); 
	        fillWithQuestionData(null);
	        buttonPanel.setMessage("No question found."); 
	    }
	}

	@Override
	public void onThemeSelected(String themeTitle) {
		// Not needed for quiz panel, but must be present due to interface
	}

	/**
	 * Gets the QuestionPanel instance for question display.
	 * 
	 * @return The QuestionPanel instance
	 */
	public QuestionPanel getQuestionPanel() {
		return questionPanel;
	}

	/**
	 * Gets the QuizButtonPanel instance for control buttons.
	 * 
	 * @return The QuizButtonPanel instance
	 */
	public QuizButtonPanel getButtonPanel() {
		return buttonPanel;
	}

	/**
	 * Gets the QuizInfoViewPanel instance for statistics display.
	 * 
	 * @return The QuizInfoViewPanel instance
	 */
	public QuizInfoViewPanel getQuizInfoViewPanel() {
		return quizInfoViewPanel;
	}
}

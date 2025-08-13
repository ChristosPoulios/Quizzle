package gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import gui.interfaces.GUIConstants;
import gui.interfaces.QuizPanelDelegator;
import gui.interfaces.QuizQuestionDelegator;
import gui.subpanels.QuestionButtonPanel;
import gui.subpanels.QuestionListPanel;
import gui.subpanels.QuestionPanel;
import persistence.serialization.QuizDataManager;
import quizlogic.dto.QuestionDTO;

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
		questionListPanel = new QuestionListPanel(dataManager);
		questionListPanel.setDelegate(this);

		btnPanel = new QuestionButtonPanel(BTN_DELETE_QUESTION, BTN_SAVE_QUESTION, BTN_ADD_QUESTION);
		btnPanel.setDelegate(this);

		add(questionPanel, BorderLayout.WEST);
		add(questionListPanel, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);

		dataManager.addUpdateListener(() -> {
			questionListPanel.updateQuestionList(questionListPanel.getSelectedThemeTitle());
		});
	}

	@Override
	public void onThemeSelected(String themeTitle) {
		questionListPanel.updateQuestionList(themeTitle);
		questionListPanel.getQuestionList().clearSelection();
		questionPanel.fillWithQuestionData(null);
	}

	@Override
	public void onQuestionSelected(String entry, int index) {
		QuestionDTO question = dataManager.getQuestionByGlobalIndex(index);
		if (question != null) {
			questionPanel.fillWithQuestionData(question);
			System.out.println(question);
		} else {
			questionPanel.fillWithQuestionData(null);
		}
	}

	@Override
	public void onQuestionDeleted(String questionText) {
	    btnPanel.setMessage("Frage löschen: " + questionText);
	}

	@Override
	public void onQuestionSaved(String questionText, String themeTitle, String questionType, String answerText,
	        boolean isCorrect) {
	    btnPanel.setMessage("Frage speichern: " + questionText + " (Thema: " + themeTitle + ")"); 
	}

	@Override
	public void onNewQuestion(String themeTitle, String questionType) {
	    btnPanel.setMessage("Neue Frage: Thema " + themeTitle + ", Typ " + questionType); 
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
}

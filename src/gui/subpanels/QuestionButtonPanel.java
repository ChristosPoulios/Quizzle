package gui.subpanels;

import gui.interfaces.QuizQuestionDelegator;

/**
 * Button panel for question management operations.
 * 
 * Provides three main actions: Delete Question, Save Question, Add New Question.
 * Uses the Delegate pattern to communicate with the parent panel.
 * 
 * @author Quizzle Team
 * @version 2.0
 */
public class QuestionButtonPanel extends MyButtonPanel {

	private static final long serialVersionUID = 1L;
	
	// ==================== Dependencies ====================
	private QuizQuestionDelegator delegate;
	private QuestionPanel questionPanel;
	private QuestionListPanel questionListPanel;

	// ==================== Constructor ====================
	
	/**
	 * Creates a new QuestionButtonPanel with the specified button labels.
	 * 
	 * @param deleteButtonText Text for the delete button
	 * @param saveButtonText Text for the save button  
	 * @param addButtonText Text for the add new question button
	 */
	public QuestionButtonPanel(String deleteButtonText, String saveButtonText, String addButtonText) {
		super(deleteButtonText, saveButtonText, addButtonText);
		setupButtonActions();
	}

	// ==================== Configuration Methods ====================

	/**
	 * Sets the delegate that will handle button actions.
	 */
	public void setDelegate(QuizQuestionDelegator delegate) {
		this.delegate = delegate;
	}

	/**
	 * Sets references to other panels needed for data access.
	 */
	public void setPanelReferences(QuestionPanel questionPanel, QuestionListPanel questionListPanel) {
		this.questionPanel = questionPanel;
		this.questionListPanel = questionListPanel;
	}

	// ==================== Button Action Setup ====================

	private void setupButtonActions() {

		getButton1().addActionListener(_ -> {
			if (delegate != null) {
				String questionTitle = extractQuestionTitle();
				delegate.onQuestionDeleted(questionTitle);
			}
		});


		getButton2().addActionListener(_ -> {
			if (delegate != null) {
				String questionTitle = extractQuestionTitle();
				String themeTitle = extractThemeTitle();
				String questionType = extractQuestionType();
				String answerText = extractCorrectAnswerText();
				boolean hasCorrectAnswer = checkHasCorrectAnswer();

				delegate.onQuestionSaved(questionTitle, themeTitle, questionType, answerText, hasCorrectAnswer);
			}
		});


		getButton3().addActionListener(_ -> {
			if (delegate != null) {
				String themeTitle = extractThemeTitle();
				String questionType = extractQuestionType();
				delegate.onNewQuestion(themeTitle, questionType);
			}
		});
	}

	// ==================== Data Extraction Methods ====================

	private String extractQuestionTitle() {
		if (questionPanel != null && questionPanel.getMetaPanel() != null) {
			String title = questionPanel.getMetaPanel().getTitleField().getText();
			return title != null ? title.trim() : "";
		}
		return "";
	}

	private String extractThemeTitle() {
		if (questionListPanel != null) {
			String selected = questionListPanel.getSelectedThemeTitle();
			return (selected != null && !selected.equals("Alle Themen")) ? selected : "";
		}
		return "";
	}

	private String extractQuestionType() {

		return "Multiple Choice";
	}

	private String extractCorrectAnswerText() {
		if (questionPanel != null && questionPanel.getAnswersPanel() != null) {
			String correctAnswer = questionPanel.getAnswersPanel().getCorrectAnswerText();
			return correctAnswer != null ? correctAnswer : "";
		}
		return "";
	}

	private boolean checkHasCorrectAnswer() {
		if (questionPanel != null && questionPanel.getAnswersPanel() != null) {
			AnswerRowPanel[] rows = questionPanel.getAnswersPanel().getAnswerRows();
			for (AnswerRowPanel row : rows) {
				if (row.isCorrect()) {
					return true;
				}
			}
		}
		return false;
	}
}
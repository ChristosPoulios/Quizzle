package gui.subpanels;

import gui.interfaces.QuizQuestionDelegator;

/**
 * Button panel for question management operations.
 * <p>
 * Provides three main actions: Delete Question, Save Question, Add New
 * Question. Uses the delegate pattern to communicate with the parent panel.
 * </p>
 * 
 * @author Quizzle Team
 * @version 2.0
 */
public class QuestionButtonPanel extends MyButtonPanel {

	private static final long serialVersionUID = 1L;

	private QuizQuestionDelegator delegate;
	private QuestionPanel questionPanel;
	private QuestionListPanel questionListPanel;

	/**
	 * Creates a new QuestionButtonPanel with the specified button labels.
	 * 
	 * @param deleteButtonText Text for the delete button
	 * @param saveButtonText   Text for the save button
	 * @param addButtonText    Text for the add new question button
	 */
	public QuestionButtonPanel(String deleteButtonText, String saveButtonText, String addButtonText) {
		super(deleteButtonText, saveButtonText, addButtonText);
		setupButtonActions();
	}

	/**
	 * Sets the delegate that will handle button actions.
	 * 
	 * @param delegate the delegate to be set
	 */
	public void setDelegate(QuizQuestionDelegator delegate) {
		this.delegate = delegate;
	}

	/**
	 * Sets references to other panels needed for data access.
	 * 
	 * @param questionPanel     Reference to the question panel
	 * @param questionListPanel Reference to the question list panel
	 */
	public void setPanelReferences(QuestionPanel questionPanel, QuestionListPanel questionListPanel) {
		this.questionPanel = questionPanel;
		this.questionListPanel = questionListPanel;
	}

	/**
	 * Configures action listeners for buttons to delegate events.
	 */
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

	/**
	 * Extracts the question title text from the UI.
	 * 
	 * @return The question title or empty string if none
	 */
	private String extractQuestionTitle() {
		if (questionPanel != null && questionPanel.getMetaPanel() != null) {
			String title = questionPanel.getMetaPanel().getTitleField().getText();
			return title != null ? title.trim() : "";
		}
		return "";
	}

	/**
	 * Extracts the theme title currently selected.
	 * 
	 * @return The theme title or empty string if none
	 */
	private String extractThemeTitle() {
		if (questionListPanel != null) {
			String selected = questionListPanel.getSelectedThemeTitle();
			return (selected != null && !selected.equals("Alle Themen")) ? selected : "";
		}
		return "";
	}

	/**
	 * Extracts the question type.
	 * <p>
	 * Currently hardcoded as "Multiple Choice".
	 * 
	 * @return The question type
	 */
	private String extractQuestionType() {
		return "Multiple Choice";
	}

	/**
	 * Extracts the correct answer text from the answers panel.
	 * 
	 * @return The correct answer text or empty string if none
	 */
	private String extractCorrectAnswerText() {
		if (questionPanel != null && questionPanel.getAnswersPanel() != null) {
			String correctAnswer = questionPanel.getAnswersPanel().getCorrectAnswerText();
			return correctAnswer != null ? correctAnswer : "";
		}
		return "";
	}

	/**
	 * Checks if at least one answer is marked as correct.
	 * 
	 * @return true if at least one answer is marked correct, false otherwise
	 */
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

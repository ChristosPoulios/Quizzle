package gui.subpanels;

import gui.interfaces.QuizQuestionDelegator;

/**
 * Spezialisierte ButtonPanel-Variante für das Fragen-Panel.
 * 
 * Nutzt QuizQuestionDelegator und ruft dessen Methoden auf bei Button-Events.
 * 
 * @author ...
 */

public class QuestionButtonPanel extends MyButtonPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QuizQuestionDelegator delegate;
	private QuestionPanel questionPanel;
	private QuestionListPanel questionListPanel;

	public QuestionButtonPanel(String btnName1, String btnName2, String btnName3) {
		super(btnName1, btnName2, btnName3);

		getButton1().addActionListener(_ -> {
			if (delegate != null) {
				delegate.onQuestionDeleted(getCurrentQuestionTitle());
			}
		});

		getButton2().addActionListener(_ -> {
			if (delegate != null) {
				@SuppressWarnings("unused")
				String questionText = getCurrentQuestionText();
				String questionTitle = getCurrentQuestionTitle();
				String themeTitle = getCurrentThemeTitle();
				String questionType = getCurrentQuestionType();
				String answerText = getCurrentAnswerText();
				boolean isCorrect = getCurrentAnswerIsCorrect();

				delegate.onQuestionSaved(questionTitle, themeTitle, questionType, answerText, isCorrect);
			}
		});

		getButton3().addActionListener(_ -> {
			if (delegate != null) {
				String themeTitle = getCurrentThemeTitle();
				String questionType = getCurrentQuestionType();
				delegate.onNewQuestion(themeTitle, questionType);
			}
		});
	}

	public void setDelegate(QuizQuestionDelegator delegate) {
		this.delegate = delegate;
	}

	public void setPanelReferences(QuestionPanel questionPanel, QuestionListPanel questionListPanel) {
		this.questionPanel = questionPanel;
		this.questionListPanel = questionListPanel;
	}

	private String getCurrentQuestionTitle() {
		if (questionPanel != null && questionPanel.getMetaPanel() != null) {
			return questionPanel.getMetaPanel().getTitleField().getText();
		}
		return "";
	}

	private String getCurrentQuestionText() {
		if (questionPanel != null && questionPanel.getMetaPanel() != null) {
			return questionPanel.getMetaPanel().getQuestionTextArea().getText();
		}
		return "";
	}

	private String getCurrentThemeTitle() {
		if (questionListPanel != null) {
			String selected = questionListPanel.getSelectedThemeTitle();
			return selected != null && !selected.equals("Alle Themen") ? selected : "";
		}
		return "";
	}

	private String getCurrentQuestionType() {
		return "Multiple Choice";
	}

	private String getCurrentAnswerText() {
		if (questionPanel != null && questionPanel.getAnswersPanel() != null) {
			return questionPanel.getAnswersPanel().getCorrectAnswerText();
		}
		return "";
	}

	private boolean getCurrentAnswerIsCorrect() {
		if (questionPanel != null && questionPanel.getAnswersPanel() != null) {
			for (AnswerRowPanel row : questionPanel.getAnswersPanel().getAnswerRows()) {
				if (row.isCorrect()) {
					return true;
				}
			}
		}
		return false;
	}
}
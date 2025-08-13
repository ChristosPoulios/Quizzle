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

	public QuestionButtonPanel(String btnName1, String btnName2, String btnName3) {
		super(btnName1, btnName2, btnName3);

		getButton1().addActionListener(_ -> {

			delegate.onQuestionDeleted(getCurrentQuestionTitle());
		});

		getButton2().addActionListener(_ -> {
			String questionText = getCurrentQuestionTitle();
			String themeTitle = getCurrentThemeTitle();
			String questionType = getCurrentQuestionType();
			String answerText = getCurrentAnswerText();
			boolean isCorrect = getCurrentAnswerIsCorrect();

			delegate.onQuestionSaved(questionText, themeTitle, questionType, answerText, isCorrect);
		});

		getButton3().addActionListener(_ -> {

			String themeTitle = getCurrentThemeTitle();
			String questionType = getCurrentQuestionType();
			delegate.onNewQuestion(themeTitle, questionType);
		});
	}

	public void setDelegate(QuizQuestionDelegator delegate) {
		this.delegate = delegate;
	}

	private String getCurrentQuestionTitle() {
		// TODO: Frage-Titel aus dem zugehörigen Frage-Panel holen
		return ""; // Platzhalter
	}

	private String getCurrentThemeTitle() {
		// TODO: Theme-Titel aus dem UI holen
		return ""; // Platzhalter
	}

	private String getCurrentQuestionType() {
		// TODO: Fragetyp aus dem UI (Dropdown o.ä.) holen
		return ""; // Platzhalter
	}

	private String getCurrentAnswerText() {
		// TODO: Antworttext aus dem UI holen
		return ""; // Platzhalter
	}

	private boolean getCurrentAnswerIsCorrect() {
		// TODO: Korrekt-Status aus UI (z.B. Checkbox) holen
		return false; // Platzhalter
	}
}

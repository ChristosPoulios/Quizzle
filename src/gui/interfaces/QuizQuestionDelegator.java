package gui.interfaces;

/**
 * Delegate interface for actions performed on the question panel, such as
 * deleting, saving, or selecting questions.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface QuizQuestionDelegator {
	void onQuestionDeleted(String questionText);

	void onQuestionSaved(String questionTitle, String themeTitle, String questionType, String answerText,
			boolean isCorrect);

	void onQuestionSelected(String entry, int index);

	void onNewQuestion(String themeTitle, String questionType);

	void onThemeSelected(String themeTitle);
}
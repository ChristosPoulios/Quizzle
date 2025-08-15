package gui.interfaces;

/**
 * Delegate interface for actions performed on the question panel, such as
 * deleting, saving, selecting questions, or creating new ones.
 * <p>
 * This interface enables communication between UI components and their
 * controlling logic related to quiz question management.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface QuizQuestionDelegator {

	/**
	 * Called when a question is deleted.
	 * 
	 * @param questionText The text or title of the question being deleted
	 */
	void onQuestionDeleted(String questionText);

	/**
	 * Called when a question is saved.
	 * 
	 * @param questionTitle The question's title
	 * @param themeTitle    The theme title the question belongs to
	 * @param questionType  The type of the question
	 * @param answerText    The answer text
	 * @param isCorrect     Whether the answer is correct
	 */
	void onQuestionSaved(String questionTitle, String themeTitle, String questionType, String answerText,
			boolean isCorrect);

	/**
	 * Called when a question is selected from the list.
	 * 
	 * @param entry The selected entry text
	 * @param index The index of the selected question
	 */
	void onQuestionSelected(String entry, int index);

	/**
	 * Called when a new question is created.
	 * 
	 * @param themeTitle   The theme to associate the new question with
	 * @param questionType The type/category of the new question
	 */
	void onNewQuestion(String themeTitle, String questionType);

	/**
	 * Called when a theme is selected.
	 * 
	 * @param themeTitle The selected theme's title
	 */
	void onThemeSelected(String themeTitle);
}

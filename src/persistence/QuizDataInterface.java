package persistence;

import java.util.ArrayList;

import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Interface defining the contract for quiz data persistence operations.
 * <p>
 * Implementations can use various storage backends such as:
 * <ul>
 * <li>MariaDB relational database (see: persistence.mariaDB package)</li>
 * <li>File-based serialization (see: persistence.serialization package)</li>
 * </ul>
 * This interface provides CRUD operations for:
 * <ul>
 * <li>Themes</li>
 * <li>Questions</li>
 * <li>Answers</li>
 * </ul>
 * as well as retrieval and random selection methods.
 * <p>
 * All return types use DTO classes from the {@code quizlogic.dto} package.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface QuizDataInterface {

	/**
	 * Gets a random question from all available questions in storage.
	 *
	 * @return a random {@link QuestionDTO} or null if no questions are available
	 */
	QuestionDTO getRandomQuestion();

	/**
	 * Gets a random question from a specific theme.
	 *
	 * @param theme the {@link ThemeDTO} to select a question from
	 * @return a random {@link QuestionDTO} or null if no questions are available
	 *         for that theme
	 */
	QuestionDTO getRandomQuestionFor(ThemeDTO theme);

	/**
	 * Retrieves all available quiz themes from storage.
	 *
	 * @return an {@link ArrayList} of {@link ThemeDTO} containing all themes
	 */
	ArrayList<ThemeDTO> getAllThemes();

	/**
	 * Saves a theme to persistent storage.
	 * <p>
	 * If the theme is new (ID not set), it will be created; otherwise, it will be
	 * updated.
	 *
	 * @param theme the {@link ThemeDTO} to save
	 * @return a status/result message, or null if successful without message
	 */
	String saveTheme(ThemeDTO theme);

	/**
	 * Deletes a theme from storage.
	 *
	 * @param theme the {@link ThemeDTO} to delete
	 * @return a status/result message, or null if deleted successfully
	 */
	String deleteTheme(ThemeDTO theme);

	/**
	 * Retrieves all questions associated with a given theme.
	 *
	 * @param theme the {@link ThemeDTO} to fetch questions for
	 * @return an {@link ArrayList} of {@link QuestionDTO} belonging to that theme
	 */
	ArrayList<QuestionDTO> getQuestionsFor(ThemeDTO theme);

	/**
	 * Saves a quiz question to storage.
	 * <p>
	 * If new, it will be inserted; if it already exists, it will be updated.
	 *
	 * @param question the {@link QuestionDTO} to save
	 * @return a status/result message, or null if successful without message
	 */
	String saveQuestion(QuestionDTO question);

	/**
	 * Deletes a quiz question from storage.
	 *
	 * @param question the {@link QuestionDTO} to delete
	 * @return a status/result message, or null if deleted successfully
	 */
	String deleteQuestion(QuestionDTO question);

	/**
	 * Retrieves all answers associated with a given question.
	 *
	 * @param question the {@link QuestionDTO} to fetch answers for
	 * @return an {@link ArrayList} of {@link AnswerDTO} belonging to that question
	 */
	ArrayList<AnswerDTO> getAnswersFor(QuestionDTO question);
}

package constants;

/**
 * Central constants for quiz logic and data management. Contains constants for
 * default values, data validation, and business logic.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface LogicConstants {

	// Default answer configuration
	/** Default number of answers per question */
	int DEFAULT_ANSWERS_PER_QUESTION = 4;

	/** Default index for the correct answer (first answer) */
	int DEFAULT_CORRECT_ANSWER_INDEX = 0;

	/** Starting counter value for questions */
	int QUESTION_COUNTER_START = 1;

	// ID and validation constants
	/** Invalid ID indicator */
	int INVALID_ID = -1;

	/** Minimum valid ID value */
	int MIN_VALID_ID = 0;

	/** ID increment value for new entities */
	int ID_INCREMENT = 1;

	// Default text values
	/** Default answer text prefix */
	String DEFAULT_ANSWER_TEXT_PREFIX = "Antwort ";

	/** Default fallback text for questions without title */
	String DEFAULT_QUESTION_TITLE_PREFIX = "Frage ";

	/** Default text for questions without content */
	String DEFAULT_QUESTION_NO_TEXT = "[Kein Fragetext]";
}
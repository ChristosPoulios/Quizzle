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


	/** Default number of answers per question */
	int DEFAULT_ANSWERS_PER_QUESTION = 4;

	/** Default index for the correct answer (first answer) */
	int DEFAULT_CORRECT_ANSWER_INDEX = 0;

	/** Starting counter value for questions */
	int QUESTION_COUNTER_START = 1;


	/** Invalid ID indicator */
	int INVALID_ID = -1;

	/** Minimum valid ID value */
	int MIN_VALID_ID = 0;

	/** ID increment value for new entities */
	int ID_INCREMENT = 1;
	
	/** Length of the theme prefix "* " used for themes without descriptions */
	int THEME_PREFIX_LENGTH = 2;
}
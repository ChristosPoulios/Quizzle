package constants;

/**
 * Constants for data validation limits used throughout the DTO classes.
 * Centralizes all validation constraints for themes, questions, and answers.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface ValidationConstants {

	// Theme validation constants

	/** Maximum allowed length for theme title in characters */
	int THEME_TITLE_MAX_LENGTH = 100;

	/** Maximum allowed length for theme description in characters */
	int THEME_DESCRIPTION_MAX_LENGTH = 500;

	// Question validation constants

	/** Maximum allowed length for question text in characters */
	int QUESTION_TEXT_MAX_LENGTH = 1000;

	/** Maximum allowed length for question title in characters */
	int QUESTION_TITLE_MAX_LENGTH = 200;

	// Answer validation constants

	/** Maximum allowed length for answer text in characters */
	int ANSWER_TEXT_MAX_LENGTH = 500;

	// Default values

	/** Default question ID value for new answers */
	int DEFAULT_QUESTION_ID = -1;
}
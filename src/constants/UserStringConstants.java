package constants;

/**
 * Central constants for all user-displayed strings in the quiz application.
 * This interface contains all German text labels, button texts, messages, and
 * other strings that are visible to the end user.
 *
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface UserStringConstants {

	// ========================================
	// General Constants
	// ========================================
	/** Title displayed in the main application window */
	String WINDOW_TITLE = "Quizzle";

	/** Default message text for message panels */
	String MESSAGE_DEFAULT = "Meldungen";

	/** Content type for HTML display */
	String HTML_CONTENT_TYPE = "text/html";

	// ========================================
	// Theme Labels
	// ========================================
	/** Label text for theme fields */
	String THEME_LABEL = "Thema:";

	/** Header text for new theme creation */
	String NEW_THEME_HEADER = "Neues Thema";

	/** Label for theme title field */
	String THEME_TITLE_LABEL = "Titel";

	/** Label for theme information field */
	String THEME_INFO_LABEL = "Information zum Thema";

	/** Header for theme list */
	String THEMES_HEADER = "Themen";

	/** Option for selecting all themes */
	String ALL_THEMES_OPTION = "Alle Themen";

	// ========================================
	// Question Labels
	// ========================================
	/** Label for question title field */
	String QUESTION_TITLE_LABEL = "Titel";

	/** Label for question text field */
	String QUESTION_TEXT_LABEL = "Frage";

	/** Label for question lists */
	String QUESTIONLIST_LABEL = "Fragen zum Thema";

	/** Text for new question creation */
	String NEW_QUESTION_TEXT = "Neue Frage";

	/** Label for the possible answers section */
	String POSSIBLE_ANSWERS_LABEL = "Mögliche Antworten";

	/** Label for correct answer indication */
	String ANSWER_LABEL = "Richtig";

	/** Prefix used for answer option labels */
	String ANSWER_PREFIX = "Antwort ";

	// ========================================
	// Button Texts
	// ========================================
	/** Button text for adding new themes */
	String BTN_ADD_THEME = "Neues Thema";

	/** Button text for saving themes */
	String BTN_SAVE_THEME = "Thema speichern";

	/** Button text for deleting themes */
	String BTN_DELETE_THEME = "Thema löschen";

	/** Button text for adding new questions */
	String BTN_ADD_QUESTION = "Neue Frage";

	/** Button text for saving questions */
	String BTN_SAVE_QUESTION = "Frage speichern";

	/** Button text for deleting questions */
	String BTN_DELETE_QUESTION = "Frage löschen";

	/** Button text for showing answers */
	String BTN_SHOW_ANSWER = "Antwort zeigen";

	/** Button text for saving answers */
	String BTN_SAVE_ANSWER = "Antwort speichern";

	/** Button text for next question navigation */
	String BTN_NEXT_QUESTION = "Nächste Frage";

	/** Button text for switching to theme info view */
	String BTN_SHOW_THEMES = "Themeninfos";

	/** Button text for switching to question list view */
	String BTN_SHOW_QUESTIONS = "Fragenliste";



	// ========================================
	// Validation Error Messages
	// ========================================
	/** Error message when theme title is null or empty */
	String ERROR_THEME_TITLE_NULL_OR_EMPTY = "Thementitel darf nicht null oder leer sein";

	/** Error message when theme title exceeds maximum length */
	String ERROR_THEME_TITLE_TOO_LONG = "Thementitel darf nicht länger als %d Zeichen sein";

	/** Error message when theme description exceeds maximum length */
	String ERROR_THEME_DESCRIPTION_TOO_LONG = "Themenbeschreibung darf nicht länger als %d Zeichen sein";

	/** Error message when question text is null or empty */
	String ERROR_QUESTION_TEXT_NULL_OR_EMPTY = "Fragetext darf nicht null oder leer sein";

	/** Error message when question text exceeds maximum length */
	String ERROR_QUESTION_TEXT_TOO_LONG = "Fragetext darf nicht länger als %d Zeichen sein";

	/** Error message when question title exceeds maximum length */
	String ERROR_QUESTION_TITLE_TOO_LONG = "Fragetitel darf nicht länger als %d Zeichen sein";

	/** Error message when answer text is null or empty */
	String ERROR_ANSWER_TEXT_NULL_OR_EMPTY = "Antworttext darf nicht null oder leer sein";

	/** Error message when answer text exceeds maximum length */
	String ERROR_ANSWER_TEXT_TOO_LONG = "Antworttext darf nicht länger als %d Zeichen sein";

	// ========================================
	// Theme Management Messages
	// ========================================
	/** Message when ready for new theme */
	String MSG_READY_FOR_NEW_THEME = "Bereit für neues Thema";

	/** Message requesting theme title input */
	String MSG_PLEASE_ENTER_THEME_TITLE = "Bitte geben Sie einen Titel für das Thema ein";

	/** Message for theme creation success */
	String MSG_THEME_CREATED_SUCCESS = "Thema erfolgreich erstellt: %s";

	/** Message when no theme selected for deletion */
	String MSG_PLEASE_SELECT_THEME_TO_DELETE = "Bitte ein Thema zum Löschen auswählen";

	/** Confirmation dialog for theme deletion */
	String MSG_CONFIRM_DELETE_THEME = "Sind Sie sicher, dass Sie das Thema '%s' löschen möchten?\n\n⚠️ Diese Aktion kann nicht rückgängig gemacht werden!";

	/** Title for theme deletion confirmation dialog */
	String DIALOG_TITLE_CONFIRM_DELETE_THEME = "Thema löschen bestätigen";

	/** Success message for theme deletion */
	String MSG_THEME_DELETED_SUCCESS = "Thema erfolgreich gelöscht: %s";

	/** Error message for theme deletion failure */
	String MSG_THEME_DELETE_ERROR = "Fehler beim Löschen des Themas: %s";

	/** Message when theme deletion is cancelled */
	String MSG_DELETE_CANCELLED = "Löschen abgebrochen";

	/** Message when save is cancelled */
	String MSG_SAVE_CANCELLED = "Speichern abgebrochen";

	/** Confirmation message for saving theme without description */
	String MSG_CONFIRM_SAVE_WITHOUT_DESCRIPTION = "Sie haben keine Beschreibung für das Thema eingegeben.\n\nMöchten Sie das Thema trotzdem speichern?";

	/** Title for save without description confirmation dialog */
	String DIALOG_TITLE_CONFIRM_SAVE_WITHOUT_DESCRIPTION = "Thema ohne Beschreibung speichern";

	/** Message when theme is not found */
	String MSG_THEME_NOT_FOUND = "Thema nicht gefunden: %s";

	// ========================================
	// Question Management Messages
	// ========================================
	/** Message when no question selected for deletion */
	String MSG_NO_QUESTION_SELECTED_FOR_DELETE = "Keine Frage zum Löschen ausgewählt";

	/** Success message for question deletion */
	String MSG_QUESTION_DELETED_SUCCESS = "Frage erfolgreich gelöscht: %s";

	/** Error message for question deletion failure */
	String MSG_QUESTION_DELETE_ERROR = "Frage konnte nicht gelöscht werden: %s";

	/** Error message for question deletion exception */
	String MSG_QUESTION_DELETE_EXCEPTION = "Fehler beim Löschen: %s";

	/** Message requesting theme selection */
	String MSG_PLEASE_SELECT_THEME = "Bitte wählen Sie ein Thema aus der Liste";

	/** Message for new question creation */
	String MSG_NEW_QUESTION_FOR_THEME = "Neue Frage erstellen für Thema: %s";

	/** Message requesting valid theme selection */
	String MSG_PLEASE_SELECT_VALID_THEME = "Bitte wählen Sie ein gültiges Thema aus";

	/** Message when question is not found */
	String MSG_QUESTION_NOT_FOUND = "Frage nicht gefunden";

	/** Message when question could not be created */
	String MSG_QUESTION_CREATE_ERROR = "Frage konnte nicht erstellt werden";

	/** Success message for question save */
	String MSG_QUESTION_SAVED_SUCCESS = "Frage erfolgreich gespeichert: %s";

	/** Message when question saved but answers had errors */
	String MSG_QUESTION_SAVED_ANSWERS_ERROR = "Frage gespeichert, aber Fehler beim Speichern der Antworten";

	/** Error message for question save failure */
	String MSG_QUESTION_SAVE_ERROR = "Fehler beim Speichern: %s";

	/** Error message for new question creation */
	String MSG_NEW_QUESTION_CREATE_ERROR = "Fehler beim Erstellen einer neuen Frage: %s";

	// ========================================
	// Answer Validation Messages
	// ========================================
	/** Message requiring at least one answer */
	String MSG_PLEASE_ENTER_AT_LEAST_ONE_ANSWER = "Bitte geben Sie mindestens eine Antwort ein";

	/** Message requiring at least one correct answer */
	String MSG_PLEASE_MARK_AT_LEAST_ONE_CORRECT = "Bitte markieren Sie mindestens eine Antwort als korrekt";

	// ========================================
	// Quiz Play Messages
	// ========================================
	/** Message when no question is available */
	String MSG_NO_QUESTION_AVAILABLE = "Keine Frage verfügbar";

	/** Message showing correct answer */
	String MSG_CORRECT_ANSWER = "Richtige Antwort: %s";

	/** Message when answer is saved */
	String MSG_ANSWER_SAVED = "Antwort gespeichert.";

	/** Message for next question */
	String MSG_NEXT_QUESTION = "Nächste Frage.";

	/** Message when question is selected */
	String MSG_QUESTION_SELECTED = "Frage ausgewählt: %s";

	/** Message when theme is selected */
	String MSG_THEME_SELECTED = "Thema ausgewählt: %s";

	// ========================================
	// Quiz Info View Panel Messages
	// ========================================
	/** Header for correct answer display */
	String QUIZ_INFO_CORRECT_ANSWER_HEADER = "Richtige Antwort:";

	/** Fallback text when no answer is found */
	String QUIZ_INFO_NO_ANSWER_FOUND = "Keine Antwort gefunden";

	/** Feedback message for correct answers */
	String QUIZ_INFO_FEEDBACK_CORRECT = "✓ Richtig!";

	/** Feedback message for incorrect answers */
	String QUIZ_INFO_FEEDBACK_INCORRECT = "✗ Falsch!";

	/** Header for quiz info panel */
	String QUIZ_INFO_PANEL_HEADER = "Quiz Info";

	/** Welcome message for quiz info panel */
	String QUIZ_INFO_WELCOME_MESSAGE = "Wählen Sie eine Antwort aus und klicken Sie auf 'Antwort zeigen', um die korrekte Lösung zu sehen.";

	/** Feedback message for showing all correct answers */
	String QUIZ_INFO_FEEDBACK_SHOW_ALL_CORRECT = "✓ Die korrekten Antworten sind: %s";

	/** Feedback message for showing one correct answer */
	String QUIZ_INFO_FEEDBACK_SHOW_SINGLE_CORRECT = "✓ Die korrekte Antwort ist: %s";

	/** Feedback message for showing multiple correct answers */
	String QUIZ_INFO_FEEDBACK_SHOW_MULTIPLE_CORRECT = "✓ Die korrekten Antworten sind: %s";

	// ========================================
	// Database Operation Messages
	// ========================================
	/** Database connection error */
	String DB_ERROR_CONNECTION_FAILED = "Failed to connect to database";

	/** Database disconnection error */
	String DB_ERROR_DISCONNECT_FAILED = "Failed to disconnect from database";

	/** Validation failure message */
	String DB_ERROR_VALIDATION_FAILED = "Validation failed: %s";

	/** Database error message */
	String DB_ERROR_DATABASE = "Database error: %s";

	/** General error message */
	String DB_ERROR_GENERAL = "Error: %s";

	/** Database connection test success */
	String DB_MSG_CONNECTION_SUCCESS = "Database connection successful";

	/** Database connection test failure */
	String DB_MSG_CONNECTION_FAILED = "Database connection failed";

	/** Database connection test failure with details */
	String DB_MSG_CONNECTION_FAILED_DETAILS = "Database connection failed: %s";

	/** Theme creation success */
	String DB_MSG_THEME_CREATED_SUCCESS = "Theme successfully created";

	/** Theme update success */
	String DB_MSG_THEME_UPDATED_SUCCESS = "Theme successfully updated";

	/** Theme save failure */
	String DB_ERROR_THEME_SAVE_FAILED = "Failed to save theme";

	/** Theme not found in database */
	String DB_ERROR_THEME_NOT_FOUND = "Theme not found in database";

	/** Theme deletion success */
	String DB_MSG_THEME_DELETED_SUCCESS = "Theme successfully deleted";

	/** Theme not found for deletion */
	String DB_ERROR_THEME_NOT_FOUND_FOR_DELETE = "Theme not found";

	/** Question not associated with theme */
	String DB_ERROR_QUESTION_NOT_ASSOCIATED = "Question not associated with a theme. Use saveQuestion with theme parameter.";

	/** Question creation success */
	String DB_MSG_QUESTION_CREATED_SUCCESS = "Question successfully created";

	/** Question update success */
	String DB_MSG_QUESTION_UPDATED_SUCCESS = "Question successfully updated";

	/** Question save failure */
	String DB_ERROR_QUESTION_SAVE_FAILED = "Failed to save question";

	/** Question not found in database */
	String DB_ERROR_QUESTION_NOT_FOUND = "Question not found in database";

	/** Question deletion success */
	String DB_MSG_QUESTION_DELETED_SUCCESS = "Question successfully deleted";

	/** Question not found for deletion */
	String DB_ERROR_QUESTION_NOT_FOUND_FOR_DELETE = "Question not found";

	/** Answer creation success */
	String DB_MSG_ANSWER_CREATED_SUCCESS = "Answer successfully created";

	/** Answer update success */
	String DB_MSG_ANSWER_UPDATED_SUCCESS = "Answer successfully updated";

	/** Answer save failure */
	String DB_ERROR_ANSWER_SAVE_FAILED = "Failed to save answer";

	// ========================================
	// Database Connection Error Messages
	// ========================================
	/** Database connection error dialog message */
	String DB_CONNECTION_ERROR_MESSAGE = "Bitte prüfen Sie:\n• MariaDB-Server läuft\n• Datenbank 'quizzle_db' existiert\n• Verbindungsparameter sind korrekt\n\nDie Anwendung wird mit eingeschränkter Funktionalität fortgesetzt.";

	/** Database connection failed message prefix */
	String DB_CONNECTION_FAILED_PREFIX = "Verbindung zur MariaDB-Datenbank fehlgeschlagen.\n\nFehler: %s\n\n";

	/** Database connection error dialog title */
	String DIALOG_TITLE_DB_CONNECTION_ERROR = "Datenbankverbindungsfehler";

	// ========================================
	// Tab Labels
	// ========================================
	/** Tab label for quiz themes */
	String TAB_QUIZ_THEMES = "Quizthemen";

	/** Tab label for quiz questions */
	String TAB_QUIZ_QUESTIONS = "Quizfragen";

	/** Tab label for quiz play */
	String TAB_QUIZ = "Quiz";

	/** Tab label for statistics */
	String TAB_STATISTICS = "Statistik";

	// ========================================
	// Statistics Labels
	// ========================================
	/** Label for statistics header */
	String STATISTICS_HEADER = "Quiz-Statistiken";
	
	/** Label for correct answers count */
	String STATISTICS_CORRECT_ANSWERS = "Richtige Antworten";
	
	/** Label for wrong answers count */
	String STATISTICS_WRONG_ANSWERS = "Falsche Antworten";
	
	/** Label for success rate */
	String STATISTICS_SUCCESS_RATE = "Erfolgsquote";
	
	/** Label for total questions answered */
	String STATISTICS_TOTAL_QUESTIONS = "Beantwortete Fragen";
	
	/** Reset statistics button text */
	String BTN_RESET_STATISTICS = "Statistiken zurücksetzen";
	
	/** No statistics message */
	String STATISTICS_NO_DATA = "Noch keine Quiz-Daten vorhanden. Starte ein Quiz, um Statistiken zu sammeln.";

	/** Label for historical statistics section */
	String STATISTICS_HISTORICAL_HEADER = "Historische Statistiken";
	
	/** Label for total sessions */
	String STATISTICS_TOTAL_SESSIONS = "Gesamt Sessions";
	
	/** Label for overall success rate */
	String STATISTICS_OVERALL_SUCCESS_RATE = "Gesamt Erfolgsquote";
	
	/** Label for recent sessions */
	String STATISTICS_RECENT_SESSIONS = "Letzte Sessions";
	
	/** Label for total correct answers across all sessions */
	String STATISTICS_TOTAL_CORRECT_ANSWERS = "Gesamt richtige Antworten";
	
	/** Label for total wrong answers across all sessions */
	String STATISTICS_TOTAL_WRONG_ANSWERS = "Gesamt falsche Antworten";
	
	/** Label for total questions across all sessions */
	String STATISTICS_TOTAL_QUESTIONS_OVERALL = "Gesamt beantwortete Fragen";

	// ========================================
	// HTML Content
	// ========================================
	/** HTML header for theme overview */
	String HTML_THEME_OVERVIEW_HEADER = "<html><body><h2>Themenübersicht</h2><ul>";
}
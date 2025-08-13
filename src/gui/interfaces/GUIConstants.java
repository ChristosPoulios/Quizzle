package gui.interfaces;

import java.awt.*;

/**
 * Central GUI constants for the entire quiz application. Provides frame
 * dimensions, colors, fonts, sizes, text labels, etc.
 * 
 * These are used throughout the GUI to maintain consistent UI design.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface GUIConstants {
	// --- Frame & Layout ---
	int FRAME_X = 300;
	int FRAME_Y = 100;
	int FRAME_WIDTH = 1024;
	int FRAME_HEIGHT = 768;
	int PANEL_MARGIN_H = 10;
	int PANEL_MARGIN_V = 10;
	int GAP_BETWEEN_INPUTS = 8;
	int BUTTON_PANEL_GAP = 100;

	// --- Colors ---
	Color BACKGROUND_COLOR = Color.WHITE;
	Color THEME_PANEL_COLOR = new Color(245, 245, 245);
	Color BUTTON_COLOR = new Color(240, 240, 240);
	Color LABEL_COLOR = Color.BLACK;
	Color TEXTFIELD_BACKGROUND = Color.WHITE;
	Color TEXTFIELD_BORDER_COLOR = Color.GRAY;
	Color CHECKBOX_COLOR = Color.BLACK;
	Color QUESTION_TEXT_AREA = new Color(240, 240, 240);

	// --- Fonts ---
	Font DEFAULT_FONT = new Font("Helvetica", Font.PLAIN, 15);
	Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 17);
	Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 15);
	Font TAB_FONT = new Font("Helvetica", Font.BOLD, 15);
	Font CORRECT_LABEL_FONT = new Font("Helvetica", Font.ITALIC, 13);

	// --- Insets & sizes ---
	Insets DEFAULT_INSETS = new Insets(2, 5, 2, 5);
	int TEXTFIELD_COLUMNS = 25;
	int MSG_TEXTFIELD_COLUMNS = 40;
	int QUESTIONAREA_ROWS = 6;
	int QUESTIONAREA_COLUMNS = 25;
	int CHECKBOX_LABEL_OFFSET = 105;

	// --- Strings / Labels ---
	String WINDOW_TITLE = "Quizzle";
	String THEME_LABEL = "Thema:";
	String POSSIBLE_ANSWERS_LABEL = "Mögliche Antworten";
	String ANSWER_LABEL = "Richtig";
	String ANSWER_PREFIX = "Antwort ";
	String MESSAGE_DEFAULT = "Meldungen";

	// --- Button texts ---
	String BTN_SHOW_ANSWER = "Antwort zeigen";
	String BTN_SAVE_ANSWER = "Antwort speichern";
	String BTN_NEXT_QUESTION = "Nächste Frage";
	String BTN_DELETE_THEME = "Thema löschen";
	String BTN_SAVE_THEME = "Thema speichern";
	String BTN_ADD_THEME = "Neues Thema";
	String BTN_DELETE_QUESTION = "Frage löschen";
	String BTN_SAVE_QUESTION = "Frage speichern";
	String BTN_ADD_QUESTION = "Neue Frage";
	
	// --- Others ---
	int ANSWERS_COUNT = 4;
}

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

	// Frame and panel dimensions
	int FRAME_X = 300;
	int FRAME_Y = 100;
	int FRAME_WIDTH = 850;
	int FRAME_HEIGHT = 600;
	int PANEL_MARGIN_H = 10;
	int PANEL_MARGIN_V = 10;
	int GAP_BETWEEN_INPUTS = 8;
	int BUTTON_PANEL_GAP = 100;

	// Colors and fonts used in the GUI
	Color BACKGROUND_COLOR = Color.WHITE;
	Color THEME_PANEL_COLOR = new Color(245, 245, 245);
	Color BUTTON_COLOR = new Color(240, 240, 240);
	Color LABEL_COLOR = Color.BLACK;
	Color TEXTFIELD_BACKGROUND = Color.WHITE;
	Color TEXTFIELD_BORDER_COLOR = Color.GRAY;
	Color CHECKBOX_COLOR = Color.BLACK;
	Color QUESTION_TEXT_AREA = new Color(240, 240, 240);

	Font DEFAULT_FONT = new Font("Helvetica", Font.PLAIN, 15);
	Font THEME_LABEL_FONT = new Font("Helvetica", Font.BOLD, 20);
	Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 18);
	Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 15);
	Font TAB_FONT = new Font("Helvetica", Font.BOLD, 15);
	Font CORRECT_LABEL_FONT = new Font("Helvetica", Font.ITALIC, 13);

	// Sizes for various components
	int VERTICAL_STRUT_SMALL = 10;
	int VERTICAL_STRUT_MEDIUM = 16;
	int VERTICAL_STRUT_LARGE = 35;

	
	int THEME_TEXTAREA_WIDTH = 400;
	int THEME_TEXTAREA_HEIGHT = 120;
	int THEME_LIST_WIDTH = 300;
	int THEME_LIST_HEIGHT = 383;
	int COMBOBOX_HEIGHT = 30;
	int QUIZ_PANEL_WIDTH = 350;
	int QUIZ_PANEL_HEIGHT = 400;

	
	int QUESTION_LIST_WIDTH = 400;
	int QUESTION_LIST_HEIGHT = 350;
	int QUESTION_LIST_MIN_WIDTH = 300;
	int QUESTION_TEXT_MAX_LENGTH = 50;
	int QUESTION_TEXT_TRUNCATE_LENGTH = 47;

	
	int ANSWER_LABEL_WIDTH = 300;
	int ANSWER_LABEL_HEIGHT = 20;

	// Insets for various components
	int BORDER_TOP = 5;
	int BORDER_BOTTOM = 10;
	int BORDER_LEFT = 10;
	int BORDER_RIGHT = 0;

	Insets DEFAULT_INSETS = new Insets(2, 5, 2, 5);
	int TEXTFIELD_COLUMNS = 25;
	int MSG_TEXTFIELD_COLUMNS = 40;
	int QUESTIONAREA_ROWS = 6;
	int QUESTIONAREA_COLUMNS = 25;
	int CHECKBOX_LABEL_OFFSET = 105;

	// Text labels and button texts used in the GUI
	String WINDOW_TITLE = "Quizzle";
	String THEME_LABEL = "Thema:";
	String POSSIBLE_ANSWERS_LABEL = "Mögliche Antworten";
	String ANSWER_LABEL = "Richtig";
	String ANSWER_PREFIX = "Antwort ";
	String MESSAGE_DEFAULT = "Meldungen";
	String QUESTIONLIST_LABEL = "Fragen zum Thema";

	String BTN_SHOW_ANSWER = "Antwort zeigen";
	String BTN_SAVE_ANSWER = "Antwort speichern";
	String BTN_NEXT_QUESTION = "Nächste Frage";
	String BTN_DELETE_THEME = "Thema löschen";
	String BTN_SAVE_THEME = "Thema speichern";
	String BTN_ADD_THEME = "Neues Thema";
	String BTN_DELETE_QUESTION = "Frage löschen";
	String BTN_SAVE_QUESTION = "Frage speichern";
	String BTN_ADD_QUESTION = "Neue Frage";

	int ANSWERS_COUNT = 4;
}
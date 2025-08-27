package constants;

import java.awt.*;

/**
 * Central GUI constants for the entire quiz application. Provides frame
 * dimensions, colors, fonts, sizes, etc. (excluding user-displayed strings).
 * 
 * User-displayed strings are now located in {@link UserStringConstants}.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface GUIConstants extends UserStringConstants {

	// Frame and panel dimensions

	/** Default X position for the main application window */
	int FRAME_X = 300;

	/** Default Y position for the main application window */
	int FRAME_Y = 100;

	/** Width of the main application window in pixels */
	int FRAME_WIDTH = 850;

	/** Height of the main application window in pixels */
	int FRAME_HEIGHT = 650;

	/** Horizontal margin spacing for panels in pixels */
	int PANEL_MARGIN_H = 10;

	/** Vertical margin spacing for panels in pixels */
	int PANEL_MARGIN_V = 10;

	/** Gap between input components in pixels */
	int GAP_BETWEEN_INPUTS = 8;

	/** Gap spacing for button panels in pixels */
	int BUTTON_PANEL_GAP = 100;

	// Colors and fonts used in the GUI

	/** Default background color for panels and components */
	Color BACKGROUND_COLOR = Color.WHITE;

	/** Background color for theme-related panels */
	Color THEME_PANEL_COLOR = new Color(245, 245, 245);

	/** Background color for buttons */
	Color BUTTON_COLOR = new Color(240, 240, 240);

	/** Color for text labels */
	Color LABEL_COLOR = Color.BLACK;

	/** Background color for text fields */
	Color TEXTFIELD_BACKGROUND = Color.WHITE;

	/** Border color for text fields */
	Color TEXTFIELD_BORDER_COLOR = Color.GRAY;

	/** Color for checkboxes */
	Color CHECKBOX_COLOR = Color.BLACK;

	/** Background color for question text areas */
	Color QUESTION_TEXT_AREA = new Color(240, 240, 240);

	/** Default font used throughout the application */
	Font DEFAULT_FONT = new Font("Helvetica", Font.PLAIN, 15);

	/** Font for theme labels with bold styling */
	Font THEME_LABEL_FONT = new Font("Helvetica", Font.BOLD, 20);

	/** Font for title text with bold styling */
	Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 18);

	/** Font for buttons with bold styling */
	Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 15);

	/** Font for tab headers with bold styling */
	Font TAB_FONT = new Font("Helvetica", Font.BOLD, 15);

	/** Font for correct answer labels with italic styling */
	Font CORRECT_LABEL_FONT = new Font("Helvetica", Font.ITALIC, 13);

	// Sizes for various components

	/** Small vertical spacing between components */
	int VERTICAL_STRUT_SMALL = 10;

	/** Medium vertical spacing between components */
	int VERTICAL_STRUT_MEDIUM = 16;

	/** Large vertical spacing between components */
	int VERTICAL_STRUT_LARGE = 35;

	/** Width of theme text areas in pixels */
	int THEME_TEXTAREA_WIDTH = 400;

	/** Height of theme text areas in pixels */
	int THEME_TEXTAREA_HEIGHT = 120;

	/** Width of theme lists in pixels */
	int THEME_LIST_WIDTH = 300;

	/** Height of theme lists in pixels */
	int THEME_LIST_HEIGHT = 383;
	
	/** Width of the ComboBoxes in pixels */
	int COMBOBOX_WIDTH = 250;

	/** Height of combo boxes in pixels */
	int COMBOBOX_HEIGHT = 30;

	/** Width of quiz panels in pixels */
	int QUIZ_PANEL_WIDTH = 350;

	/** Height of quiz panels in pixels */
	int QUIZ_PANEL_HEIGHT = 400;

	/** Width of question lists in pixels */
	int QUESTION_LIST_WIDTH = 400;

	/** Height of question lists in pixels */
	int QUESTION_LIST_HEIGHT = 350;

	/** Minimum width of question lists in pixels */
	int QUESTION_LIST_MIN_WIDTH = 300;

	/** Maximum length for question text display */
	int QUESTION_TEXT_MAX_LENGTH = 50;

	/** Length at which question text gets truncated */
	int QUESTION_TEXT_TRUNCATE_LENGTH = 47;

	/** Width of answer labels in pixels */
	int ANSWER_LABEL_WIDTH = 300;

	/** Height of answer labels in pixels */
	int ANSWER_LABEL_HEIGHT = 20;

	/** Rigid area spacing for question list panels */
	int QUESTION_LIST_RIGID_AREA_HEIGHT = 8;

	// Insets for various components

	/** Top border inset value */
	int BORDER_TOP = 5;

	/** Bottom border inset value */
	int BORDER_BOTTOM = 10;

	/** Left border inset value */
	int BORDER_LEFT = 10;

	/** Right border inset value */
	int BORDER_RIGHT = 10;

	/** Default insets for components */
	Insets DEFAULT_INSETS = new Insets(2, 5, 2, 5);

	/** Standard number of columns for text fields */
	int TEXTFIELD_COLUMNS = 25;

	/** Number of columns for message text fields */
	int MSG_TEXTFIELD_COLUMNS = 40;

	/** Number of rows for question text areas */
	int QUESTIONAREA_ROWS = 6;

	/** Number of columns for question text areas */
	int QUESTIONAREA_COLUMNS = 25;

	/** Offset for checkbox labels in pixels */
	int CHECKBOX_LABEL_OFFSET = 105;

	/** Number of answer options available per question */
	int ANSWERS_COUNT = 4;
}
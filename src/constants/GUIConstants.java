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

	/**
	 * Configuration keys for window settings - should match ConfigManager constants
	 */
	String WINDOW_WIDTH_CONFIG = "window.width";
	String WINDOW_HEIGHT_CONFIG = "window.height";
	String WINDOW_MAXIMIZED_CONFIG = "window.maximized";

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
	Color BACKGROUND_COLOR = new Color(248, 250, 252);

	/** Background color for theme-related panels */
	Color THEME_PANEL_COLOR = Color.WHITE;

	/** Background color for buttons */
	Color BUTTON_COLOR = new Color(236, 242, 248);

	/** Hover color for buttons */
	Color BUTTON_HOVER_COLOR = new Color(219, 234, 254);

	/** Active/pressed button color */
	Color BUTTON_ACTIVE_COLOR = new Color(191, 219, 254);

	/** Color for text labels - dark gray */
	Color LABEL_COLOR = new Color(55, 65, 81);

	/** Background color for text fields - white with subtle border */
	Color TEXTFIELD_BACKGROUND = Color.WHITE;

	/** Border color for text fields - soft gray-blue */
	Color TEXTFIELD_BORDER_COLOR = new Color(209, 213, 219);

	/** Focus border color for text fields - blue */
	Color TEXTFIELD_FOCUS_BORDER_COLOR = new Color(59, 130, 246);

	/** Color for checkboxes - blue */
	Color CHECKBOX_COLOR = new Color(59, 130, 246);

	/** Background color for question text areas - very light gray */
	Color QUESTION_TEXT_AREA = new Color(249, 250, 251);

	/** Header background color - slightly darker than main background */
	Color HEADER_BACKGROUND_COLOR = new Color(243, 244, 246);

	/** Border color for panels - subtle gray */
	Color PANEL_BORDER_COLOR = new Color(229, 231, 235);

	/** Success color - soft green */
	Color SUCCESS_COLOR = new Color(34, 197, 94);

	/** Error color - soft red */
	Color ERROR_COLOR = new Color(239, 68, 68);

	/** Warning color - soft orange */
	Color WARNING_COLOR = new Color(245, 158, 11);

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
	
	/** Small horizontal spacing between components */
	int HORIZONTAL_STRUT_SMALL = 10;
	
	/** Medium horizontal spacing between components */
	int HORIZONTAL_STRUT_MEDIUM = 16;
	
	/** Large horizontal spacing between components */
	int HORIZONTAL_STRUT_LARGE = 35;
	
	/** Very large horizontal spacing between components */
	int HORIZONTAL_STRUT_VERY_LARGE = 233;

	/** Width of theme text areas in pixels */
	int THEME_TEXTAREA_WIDTH = 400;

	/** Height of theme text areas in pixels */
	int THEME_TEXTAREA_HEIGHT = 130;

	/** Width of theme lists in pixels */
	int THEME_LIST_WIDTH = 300;

	/** Height of theme lists in pixels */
	int THEME_LIST_HEIGHT = 387;

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
	int QUESTION_LIST_HEIGHT = 387;

	/** Minimum width of question lists in pixels */
	int QUESTION_LIST_MIN_WIDTH = 300;

	/** Maximum length for question text display */
	int QUESTION_TEXT_MAX_LENGTH = 50;

	/** Length at which question text gets truncated */
	int QUESTION_TEXT_TRUNCATE_LENGTH = 47;

	/** Maximum length for text preview display */
	int TEXT_PREVIEW_MAX_LENGTH = 50;

	/** Truncation point for text preview (leaves room for "...") */
	int TEXT_PREVIEW_TRUNCATE_LENGTH = 47;

	/** Suffix for truncated text */
	String TEXT_TRUNCATE_SUFFIX = "...";

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
	int CHECKBOX_LABEL_OFFSET = 225;

	/** Number of answer options available per question */
	int ANSWERS_COUNT = 4;

	// Unified panel sizes for consistent layout across all tabs

	/** Standard width for left panels (question/theme editing panels) */
	int LEFT_PANEL_WIDTH = 400;

	/** Standard width for right panels (lists and info views) */
	int RIGHT_PANEL_WIDTH = 400;

	/** Standard height for main content panels */
	int MAIN_CONTENT_HEIGHT = 450;

	// Magic numbers from GUI components
	/** Panel margin offset for minimum size calculations */
	int PANEL_MARGIN_OFFSET = 50;

	/** Header panel height */
	int HEADER_PANEL_HEIGHT = 80;

	/** Label panel maximum height */
	int LABEL_PANEL_MAX_HEIGHT = 30;

	/** Controls panel maximum height */
	int CONTROLS_PANEL_MAX_HEIGHT = 40;

	/** Theme combo box width in header */
	int HEADER_THEME_COMBO_WIDTH = 200;

	/** Standard component height for combo boxes and buttons */
	int STANDARD_COMPONENT_HEIGHT = 30;

	/** Quiz info combo box width offset */
	int QUIZ_INFO_COMBO_OFFSET = 100;

	/** Scroll bar unit increment for smooth scrolling */
	int SCROLL_UNIT_INCREMENT = 16;

	/** Flow layout horizontal gap */
	int FLOW_LAYOUT_HGAP = 10;

	/** Flow layout vertical gap */
	int FLOW_LAYOUT_VGAP = 5;

	// Additional constants for ThemePanel
	/** Theme header panel height */
	int THEME_HEADER_PANEL_HEIGHT = 40;

	/** Small panel size offset for calculations */
	int PANEL_SIZE_OFFSET_SMALL = 20;

	/** Large panel size offset for calculations */
	int PANEL_SIZE_OFFSET_LARGE = 50;
}
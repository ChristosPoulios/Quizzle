package gui.subpanels;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.GUIConstants;

/**
 * Panel for displaying the current theme of the active quiz question.
 * <p>
 * Shows a label and the current theme value in a horizontal row.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class ThemeHeaderPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private final JLabel themeLabel;
	private final JLabel valueLabel;

	/**
	 * Constructs the theme header panel and its UI elements.
	 */
	public ThemeHeaderPanel() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new FlowLayout(FlowLayout.CENTER, PANEL_MARGIN_H, PANEL_MARGIN_V));

		themeLabel = new JLabel(GUIConstants.THEME_LABEL);
		themeLabel.setFont(THEME_LABEL_FONT);

		valueLabel = new JLabel("");
		valueLabel.setFont(TITLE_FONT);

		add(themeLabel);
		add(valueLabel);
	}

	/**
	 * Sets the theme name to be displayed.
	 * 
	 * @param themeName The name of the theme to display
	 */
	public void setTheme(String themeName) {
		if (themeName != null && !themeName.trim().isEmpty()) {
			valueLabel.setText(themeName);
		} else {
			valueLabel.setText("");
		}
	}

	/**
	 * Clears the theme display, removing any shown theme name.
	 */
	public void clearTheme() {
		valueLabel.setText("");
	}
}

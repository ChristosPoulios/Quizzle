package gui.subpanels;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.interfaces.GUIConstants;

/**
 * Panel for displaying the current theme of the active quiz question.
 * 
 * Shows a label and value in a horizontal row.
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
	 * Constructs the theme header panel and its elements.
	 */
	public ThemeHeaderPanel() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new FlowLayout(FlowLayout.CENTER, PANEL_MARGIN_H, PANEL_MARGIN_V));
		themeLabel = new JLabel(GUIConstants.THEME_LABEL);
		themeLabel.setFont(TITLE_FONT);

		valueLabel = new JLabel();
		valueLabel.setFont(TITLE_FONT);

		add(themeLabel);
		add(valueLabel);
	}

	/**
	 * Sets the displayed theme text.
	 * 
	 * @param theme The current theme as string
	 */
	public void setTheme(String theme) {
		valueLabel.setText(theme == null ? "" : theme);
	}

	public Object getThemeTitle() {
		return themeLabel.getText();
	}
}

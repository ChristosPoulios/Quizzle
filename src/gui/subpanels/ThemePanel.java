package gui.subpanels;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import constants.GUIConstants;

/**
 * Panel for creating and editing theme information.
 * <p>
 * Provides text fields for theme title and description with functionality to
 * clear and fill fields.
 * </p>
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class ThemePanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private final JLabel titleLabel;
	private final JTextField titleField;
	private final JLabel infoLabel;
	private final JTextArea infoArea;

	/**
	 * Constructs the theme panel with input fields.
	 */
	public ThemePanel() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(PANEL_MARGIN_V, PANEL_MARGIN_H, PANEL_MARGIN_V, PANEL_MARGIN_H));

		JLabel header = new JLabel("Neues Thema");
		header.setFont(TITLE_FONT);
		header.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(header);

		add(Box.createVerticalStrut(VERTICAL_STRUT_MEDIUM));

		titleLabel = new JLabel("Titel");
		titleLabel.setFont(DEFAULT_FONT);
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(titleLabel);

		titleField = new JTextField("", TEXTFIELD_COLUMNS);
		titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, titleField.getPreferredSize().height));
		titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(titleField);

		add(Box.createVerticalStrut(GAP_BETWEEN_INPUTS));

		infoLabel = new JLabel("Information zum Thema");
		infoLabel.setFont(DEFAULT_FONT);
		infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(infoLabel);

		infoArea = new JTextArea("", QUESTIONAREA_ROWS, QUESTIONAREA_COLUMNS);
		infoArea.setLineWrap(true);
		infoArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(infoArea);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setPreferredSize(new Dimension(THEME_TEXTAREA_WIDTH, THEME_TEXTAREA_HEIGHT));
		add(scrollPane);
	}

	/**
	 * Gets the title text from the input field.
	 * 
	 * @return The title text
	 */
	public String getTitleText() {
		return titleField.getText();
	}

	/**
	 * Gets the info text from the input area.
	 * 
	 * @return The info text
	 */
	public String getInfoText() {
		return infoArea.getText();
	}

	/**
	 * Gets the trimmed theme title.
	 * 
	 * @return The trimmed theme title
	 */
	public String getThemeTitle() {
		return titleField.getText().trim();
	}

	/**
	 * Clears all input fields.
	 */
	public void clearFields() {
		titleField.setText("");
		infoArea.setText("");
	}

	/**
	 * Sets the title text field.
	 * 
	 * @param title The title to set
	 */
	public void setTitleText(String title) {
		titleField.setText(title != null ? title : "");
	}

	/**
	 * Sets the info text area.
	 * 
	 * @param info The info text to set
	 */
	public void setInfoText(String info) {
		infoArea.setText(info != null ? info : "");
	}

	/**
	 * Fills all fields with the given theme data.
	 * 
	 * @param title The theme title
	 * @param info  The theme description
	 */
	public void fillFields(String title, String info) {
		setTitleText(title);
		setInfoText(info);
	}
}

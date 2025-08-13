package gui.subpanels;

import javax.swing.*;
import gui.interfaces.GUIConstants;
import java.awt.*;
import java.util.ArrayList;
import persistence.serialization.QuizDataManager;

/**
 * Panel displaying quiz information and statistics with persistent data
 * integration.
 * 
 * Shows theme selection and provides information display area. Automatically
 * updates when data changes.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class QuizInfoViewPanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;
	private JComboBox<String> themeComboBox;
	private JTextPane infoPane;
	private QuizDataManager dataManager;

	/**
	 * Constructs the quiz info view panel with data manager integration.
	 * 
	 * @param dataManager The data manager for theme access
	 */
	public QuizInfoViewPanel(QuizDataManager dataManager) {
		this.dataManager = dataManager;
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(PANEL_MARGIN_V, PANEL_MARGIN_H, PANEL_MARGIN_V, PANEL_MARGIN_H));

		themeComboBox = new JComboBox<>();
		themeComboBox.setPreferredSize(new Dimension(300, 30));
		themeComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		fillThemeComboBox();

		add(themeComboBox);
		add(Box.createVerticalStrut(10));

		infoPane = new JTextPane();
		infoPane.setContentType("text/html");
		infoPane.setText("");
		infoPane.setEditable(false);
		infoPane.setFont(DEFAULT_FONT);

		JScrollPane scrollPane = new JScrollPane(infoPane);
		scrollPane.setPreferredSize(new Dimension(320, 140));
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(scrollPane);
	}

	/**
	 * Fills the theme combo box with available themes.
	 */
	private void fillThemeComboBox() {
		themeComboBox.removeAllItems();
		themeComboBox.addItem("Alle Themen");
		if (dataManager != null) {
			ArrayList<String> themeTitles = dataManager.getThemeTitles();
			for (String themeTitle : themeTitles) {
				themeComboBox.addItem(themeTitle);
			}
		}
	}

	/**
	 * Refreshes the panel data from persistent storage. Called when data changes
	 * are detected.
	 */
	public void refreshData() {
		fillThemeComboBox();
	}

	/**
	 * Gets the theme combo box component.
	 * 
	 * @return The JComboBox for theme selection
	 */
	public JComboBox<String> getThemeComboBox() {
		return themeComboBox;
	}

	/**
	 * Sets the information text to display.
	 * 
	 * @param htmlText The HTML-formatted text to display
	 */
	public void setInfoText(String htmlText) {
		infoPane.setText(htmlText);
	}
}

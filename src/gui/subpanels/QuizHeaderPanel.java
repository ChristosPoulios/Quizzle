package gui.subpanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.GUIConstants;
import constants.UserStringConstants;
import persistence.QuizDataInterface;
import quizlogic.dto.ThemeDTO;

/**
 * Separate header panel containing theme selection combo box and switch button.
 * This panel is reusable and ensures consistent header display across different
 * views.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuizHeaderPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private JComboBox<String> themeComboBox;
	private JButton switchButton;
	private QuizDataInterface dataManager;

	public interface ThemeSelectionListener {
		void onThemeSelected(String themeName);
	}

	public interface ViewSwitchListener {
		void onViewSwitchRequested();
	}

	private ThemeSelectionListener themeListener;
	private ViewSwitchListener switchListener;

	/**
	 * Constructs the header panel with data manager integration.
	 * 
	 * @param dataManager The data manager for theme data access (database or file-based)
	 */
	public QuizHeaderPanel(QuizDataInterface dataManager) {
		this.dataManager = dataManager;
		initializeComponents();

	}

	/**
	 * Initializes the header components.
	 */
	private void initializeComponents() {
		setLayout(new BorderLayout());
		setBackground(BACKGROUND_COLOR);
		setPreferredSize(new Dimension(Integer.MAX_VALUE, HEADER_PANEL_HEIGHT));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, HEADER_PANEL_HEIGHT));
		setMinimumSize(new Dimension(0, HEADER_PANEL_HEIGHT));

		JPanel mainContainer = new JPanel();
		mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
		mainContainer.setBackground(BACKGROUND_COLOR);
		mainContainer.setOpaque(false);

		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
		labelPanel.setBackground(BACKGROUND_COLOR);
		labelPanel.setOpaque(false);
		labelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, LABEL_PANEL_MAX_HEIGHT));

		JLabel themeLabel = new JLabel(QUESTIONLIST_LABEL);
		themeLabel.setFont(TITLE_FONT);
		labelPanel.add(themeLabel);

		JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, FLOW_LAYOUT_HGAP, FLOW_LAYOUT_VGAP));
		controlsPanel.setBackground(BACKGROUND_COLOR);
		controlsPanel.setOpaque(false);
		controlsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, CONTROLS_PANEL_MAX_HEIGHT));

		themeComboBox = new JComboBox<>();
		themeComboBox.setPreferredSize(new Dimension(HEADER_THEME_COMBO_WIDTH, STANDARD_COMPONENT_HEIGHT));
		themeComboBox.setEditable(false);
		themeComboBox.setBackground(TEXTFIELD_BACKGROUND);

		loadThemes();

		themeComboBox.addActionListener(_ -> {
			String selectedTheme = (String) themeComboBox.getSelectedItem();
			if (selectedTheme != null && themeListener != null) {
				themeListener.onThemeSelected(selectedTheme);
			}
		});

		switchButton = new JButton("Themeninfo");
		switchButton.setPreferredSize(new Dimension(120, STANDARD_COMPONENT_HEIGHT));
		switchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (switchListener != null) {
					switchListener.onViewSwitchRequested();
				}
			}
		});

		controlsPanel.add(themeComboBox);
		controlsPanel.add(switchButton);

		mainContainer.add(labelPanel);
		mainContainer.add(controlsPanel);

		add(mainContainer, BorderLayout.CENTER);
	}

	/**
	 * Loads themes from database into the combo box.
	 */
	private void loadThemes() {
		themeComboBox.removeAllItems();
		themeComboBox.addItem(UserStringConstants.ALL_THEMES_OPTION);

		ArrayList<ThemeDTO> themes = dataManager.getAllThemes();
		populateThemeComboBox(themes);
	}

	/**
	 * Populates the theme combo box with sorted themes.
	 * Themes without descriptions are marked with "*" and listed first,
	 * then themes with descriptions, both groups sorted alphabetically.
	 * 
	 * @param themes The list of theme DTOs to populate the combo box
	 */
	private void populateThemeComboBox(ArrayList<ThemeDTO> themes) {
		// Separate themes with and without descriptions
		ArrayList<ThemeDTO> themesWithoutDescription = new ArrayList<>();
		ArrayList<ThemeDTO> themesWithDescription = new ArrayList<>();
		
		for (ThemeDTO theme : themes) {
			if (theme.getThemeDescription() == null || theme.getThemeDescription().trim().isEmpty()) {
				themesWithoutDescription.add(theme);
			} else {
				themesWithDescription.add(theme);
			}
		}
		
		// Sort both lists alphabetically by title
		Comparator<ThemeDTO> titleComparator = Comparator.comparing(ThemeDTO::getThemeTitle);
		themesWithoutDescription.sort(titleComparator);
		themesWithDescription.sort(titleComparator);
		
		// Add themes without description first (marked with *)
		for (ThemeDTO theme : themesWithoutDescription) {
			themeComboBox.addItem("* " + theme.getThemeTitle());
		}
		
		// Add themes with description
		for (ThemeDTO theme : themesWithDescription) {
			themeComboBox.addItem(theme.getThemeTitle());
		}
	}

	/**
	 * Refreshes the theme combo box with current themes from database.
	 */
	public void refreshThemes() {
		String currentSelection = (String) themeComboBox.getSelectedItem();
		loadThemes();

		if (currentSelection != null) {
			for (int i = 0; i < themeComboBox.getItemCount(); i++) {
				if (currentSelection.equals(themeComboBox.getItemAt(i))) {
					themeComboBox.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	/**
	 * Sets the theme selection listener.
	 * 
	 * @param listener The listener to be notified of theme selections
	 */
	public void setThemeSelectionListener(ThemeSelectionListener listener) {
		this.themeListener = listener;
	}

	/**
	 * Sets the view switch listener.
	 * 
	 * @param listener The listener to be notified of view switch requests
	 */
	public void setViewSwitchListener(ViewSwitchListener listener) {
		this.switchListener = listener;
	}

	/**
	 * Updates the switch button text.
	 * 
	 * @param text The new button text
	 */
	public void setSwitchButtonText(String text) {
		switchButton.setText(text);
	}

	/**
	 * Gets the currently selected theme.
	 * 
	 * @return The selected theme name
	 */
	public String getSelectedTheme() {
		return (String) themeComboBox.getSelectedItem();
	}

	/**
	 * Sets the selected theme in the combo box.
	 * 
	 * @param themeName The theme name to select
	 */
	public void setSelectedTheme(String themeName) {
		themeComboBox.setSelectedItem(themeName);
	}

	/**
	 * Enables or disables the theme combo box.
	 * 
	 * @param enabled true to enable, false to disable
	 */
	public void setThemeComboBoxEnabled(boolean enabled) {
		themeComboBox.setEnabled(enabled);
	}
}
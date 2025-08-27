package gui.subpanels;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import constants.GUIConstants;
import constants.UserStringConstants;
import persistence.mariaDB.DBManager;
import quizlogic.dto.ThemeDTO;

/**
 * Panel displaying a list of available themes with selection functionality.
 * <p>
 * Automatically updates when themes are added and notifies listeners about
 * theme selections. Uses MariaDB for data persistence.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class ThemeListPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private DefaultListModel<String> listModel;
	private JList<String> themeList;
	private JScrollPane scrollPane;

	private DBManager dbManager;

	private ThemeSelectionListener selectionListener;

	/**
	 * Interface for theme selection events.
	 */
	public interface ThemeSelectionListener {
		/**
		 * Called when a theme is selected.
		 * 
		 * @param theme The selected ThemeDTO
		 */
		void onThemeSelected(ThemeDTO theme);
	}

	/**
	 * Constructs the theme list panel with database manager integration.
	 * 
	 * @param dbManager The database manager for theme access
	 */
	public ThemeListPanel(DBManager dbManager) {
		this.dbManager = dbManager;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(BACKGROUND_COLOR);

		add(Box.createRigidArea(new Dimension(0, VERTICAL_STRUT_SMALL)));
		createHeader();
		add(Box.createRigidArea(new Dimension(0, VERTICAL_STRUT_LARGE)));
		createThemeList();
		updateThemeList();
	}

	/**
	 * Creates the header section with theme list title.
	 */
	private void createHeader() {
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(BACKGROUND_COLOR);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));

		JLabel headerLabel = new JLabel(UserStringConstants.THEMES_HEADER);
		headerLabel.setFont(TITLE_FONT);

		headerPanel.add(headerLabel);

		add(headerPanel);
	}

	/**
	 * Creates the scrollable theme list with selection listener.
	 */
	private void createThemeList() {
		listModel = new DefaultListModel<>();
		themeList = new JList<>(listModel);
		themeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		themeList.setBackground(TEXTFIELD_BACKGROUND);

		themeList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && selectionListener != null) {
				String selectedThemeTitle = themeList.getSelectedValue();
				if (selectedThemeTitle != null) {
					ThemeDTO selectedTheme = findThemeByTitle(selectedThemeTitle);
					if (selectedTheme != null) {
						selectionListener.onThemeSelected(selectedTheme);
					}
				}
			}
		});

		scrollPane = new JScrollPane(themeList);
		scrollPane.setBackground(BACKGROUND_COLOR);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, THEME_LIST_HEIGHT));

		add(scrollPane);
		add(Box.createVerticalGlue());

		setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT));
		setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT));
		setMinimumSize(
				new Dimension(RIGHT_PANEL_WIDTH - PANEL_MARGIN_OFFSET, MAIN_CONTENT_HEIGHT - PANEL_MARGIN_OFFSET));
	}

	/**
	 * Finds a theme by its title (handles titles with * prefix).
	 * 
	 * @param displayTitle The display title from the list (may include *)
	 * @return The matching ThemeDTO or null if not found
	 */
	private ThemeDTO findThemeByTitle(String displayTitle) {
		// Remove * prefix if present
		String actualTitle = displayTitle.startsWith("* ") ? displayTitle.substring(2) : displayTitle;

		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			if (theme.getThemeTitle().equals(actualTitle)) {
				return theme;
			}
		}
		return null;
	}

	/**
	 * Updates the theme list from the database with custom sorting. Themes without
	 * description are marked with * and sorted first.
	 */
	public void updateThemeList() {
		listModel.clear();
		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();

		ArrayList<String> themesWithoutDescription = new ArrayList<>();
		ArrayList<String> themesWithDescription = new ArrayList<>();

		for (ThemeDTO theme : themes) {
			String title = theme.getThemeTitle();
			String description = theme.getThemeDescription();

			if (description == null || description.trim().isEmpty()) {
				themesWithoutDescription.add("* " + title);
			} else {
				themesWithDescription.add(title);
			}
		}

		themesWithoutDescription.sort(String.CASE_INSENSITIVE_ORDER);
		themesWithDescription.sort(String.CASE_INSENSITIVE_ORDER);

		for (String theme : themesWithoutDescription) {
			listModel.addElement(theme);
		}
		for (String theme : themesWithDescription) {
			listModel.addElement(theme);
		}
	}

	/**
	 * Sets the selection listener for theme selection events.
	 * 
	 * @param listener The listener to notify on theme selection
	 */
	public void setSelectionListener(ThemeSelectionListener listener) {
		this.selectionListener = listener;
	}

	/**
	 * Returns the theme list component.
	 * 
	 * @return The JList containing themes
	 */
	public JList<String> getThemeList() {
		return themeList;
	}

	/**
	 * Returns the currently selected theme.
	 * 
	 * @return The selected ThemeDTO or null if nothing selected
	 */
	public ThemeDTO getSelectedTheme() {
		String selectedTitle = themeList.getSelectedValue();
		if (selectedTitle != null) {
			return findThemeByTitle(selectedTitle);
		}
		return null;
	}

	/**
	 * Returns the title of the currently selected theme.
	 * 
	 * @return The title string or null if none selected
	 */
	public String getSelectedThemeTitle() {
		return themeList.getSelectedValue();
	}

	/**
	 * Clears the current selection.
	 */
	public void clearSelection() {
		themeList.clearSelection();
	}
}
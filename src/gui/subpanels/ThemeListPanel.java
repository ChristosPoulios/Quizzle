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
import gui.interfaces.GUIConstants;
import persistence.serialization.QuizDataManager;
import quizlogic.dto.ThemeDTO;

/**
 * Panel displaying a list of available themes with selection functionality.
 * 
 * Automatically updates when themes are added and notifies when themes are
 * selected.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class ThemeListPanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;
	private DefaultListModel<String> listModel;
	private JList<String> themeList;
	private JScrollPane scrollPane;
	private QuizDataManager dataManager;
	private ThemeSelectionListener selectionListener;

	/**
	 * Interface für Theme-Auswahl Events.
	 */
	public interface ThemeSelectionListener {
		void onThemeSelected(ThemeDTO theme);
	}

	/**
	 * Constructs the theme list panel with data manager integration.
	 * 
	 * @param dataManager The data manager for theme access
	 */
	public ThemeListPanel(QuizDataManager dataManager) {
		this.dataManager = dataManager;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(BACKGROUND_COLOR);
		createHeader();
		add(Box.createRigidArea(new Dimension(0, 5)));
		createThemeList();
		updateThemeList();
	}

	/**
	 * Creates the header panel with title.
	 */
	private void createHeader() {
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(BACKGROUND_COLOR);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		JLabel headerLabel = new JLabel("Themes");
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
		scrollPane.setPreferredSize(new Dimension(300, 400));

		add(scrollPane);
		add(Box.createVerticalGlue());
	}

	/**
	 * Findet ein Theme anhand des Titels.
	 * 
	 * @param title Der Titel des gesuchten Themes
	 * @return Das Theme-Objekt oder null falls nicht gefunden
	 */
	private ThemeDTO findThemeByTitle(String title) {
		ArrayList<ThemeDTO> themes = dataManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			if (theme.getThemeTitle().equals(title)) {
				return theme;
			}
		}
		return null;
	}

	/**
	 * Updates the theme list from persistent storage.
	 */
	public void updateThemeList() {
		listModel.clear();
		ArrayList<String> themeNames = dataManager.getThemeTitles();
		for (String themeName : themeNames) {
			listModel.addElement(themeName);
		}
	}

	/**
	 * Sets the selection listener for theme selection events.
	 * 
	 * @param listener The listener to notify when themes are selected
	 */
	public void setSelectionListener(ThemeSelectionListener listener) {
		this.selectionListener = listener;
	}

	/**
	 * Gets the theme list component.
	 * 
	 * @return The JList containing themes
	 */
	public JList<String> getThemeList() {
		return themeList;
	}

	/**
	 * Gets the currently selected theme from the list.
	 * 
	 * @return The selected theme or null if none selected
	 */
	public ThemeDTO getSelectedTheme() {
		String selectedTitle = themeList.getSelectedValue();
		if (selectedTitle != null) {
			return findThemeByTitle(selectedTitle);
		}
		return null;
	}

	/**
	 * Gets the title of the currently selected theme.
	 * 
	 * @return The selected theme title or null if none selected
	 */
	public String getSelectedThemeTitle() {
		return themeList.getSelectedValue();
	}

	/**
	 * Clears the current selection in the theme list.
	 */
	public void clearSelection() {
		themeList.clearSelection();
	}
}

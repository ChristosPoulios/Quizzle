package gui.subpanels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import gui.interfaces.GUIConstants;
import gui.interfaces.QuizPanelDelegator;
import persistence.serialization.QuizDataManager;

/**
 * Panel displaying a list of questions with theme filtering and persistent data
 * integration.
 * 
 * Provides theme selection and question browsing with automatic updates.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class QuestionListPanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;
	private JComboBox<String> themeComboBox;
	private DefaultListModel<String> listModel;
	private JList<String> questionList;
	private JScrollPane scrollPane;
	private QuizDataManager dataManager;
	private QuizPanelDelegator delegate;

	/**
	 * Constructs the question list panel with data manager integration.
	 * 
	 * @param dataManager The data manager for question access
	 */
	public QuestionListPanel(QuizDataManager dataManager) {
		this.dataManager = dataManager;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(BACKGROUND_COLOR);

		createHeaderPanel();
		add(Box.createRigidArea(new Dimension(0, 5)));
		createQuestionList();
		updateQuestionList("Alle Themen");
	}

	/**
	 * Sets the delegate for handling user interactions.
	 * 
	 * @param delegate The delegate to notify about selections
	 */
	public void setDelegate(QuizPanelDelegator delegate) {
		this.delegate = delegate;
	}

	/**
	 * Creates the header panel with theme selection.
	 */
	private void createHeaderPanel() {
		JPanel headerContainer = new JPanel();
		headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
		headerContainer.setBackground(BACKGROUND_COLOR);

		JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, PANEL_MARGIN_H, 0));
		comboPanel.setBackground(BACKGROUND_COLOR);

		themeComboBox = new JComboBox<>();
		themeComboBox.setPreferredSize(new Dimension(300, 30));
		themeComboBox.addItem("Alle Themen");

		ArrayList<String> themeTitles = dataManager.getThemeTitles();
		for (String themeTitle : themeTitles) {
			themeComboBox.addItem(themeTitle);
		}

		themeComboBox.setBackground(TEXTFIELD_BACKGROUND);
		themeComboBox.addActionListener(_ -> {
			String selectedTheme = getSelectedThemeTitle();
			if (delegate != null) {
				delegate.onThemeSelected(selectedTheme);
			}
		});

		comboPanel.add(themeComboBox);
		headerContainer.add(comboPanel);
		headerContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, headerContainer.getPreferredSize().height));
		add(headerContainer);
	}

	/**
	 * Creates the scrollable question list.
	 */
	private void createQuestionList() {
		listModel = new DefaultListModel<>();
		questionList = new JList<>(listModel);
		questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		questionList.setBackground(TEXTFIELD_BACKGROUND);

		questionList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && delegate != null) {
				int idx = questionList.getSelectedIndex();
				String entry = questionList.getSelectedValue();
				delegate.onQuestionSelected(entry, idx);
			}
		});

		scrollPane = new JScrollPane(questionList);
		scrollPane.setBackground(BACKGROUND_COLOR);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(300, 400));

		add(scrollPane);
		add(Box.createVerticalGlue());
	}

	/**
	 * Updates the question list based on selected theme.
	 * 
	 * @param selectedThemeTitle The theme to filter by, or "Alle Themen" for all
	 */
	public void updateQuestionList(String selectedThemeTitle) {
		listModel.clear();
		ArrayList<String> entries = dataManager.getQuestionListEntries(selectedThemeTitle);
		for (String entry : entries) {
			listModel.addElement(entry);
		}

		// Update combo box as well
		themeComboBox.removeAllItems();
		themeComboBox.addItem("Alle Themen");
		ArrayList<String> themeTitles = dataManager.getThemeTitles();
		for (String themeTitle : themeTitles) {
			themeComboBox.addItem(themeTitle);
		}
	}

	/**
	 * Gets the question list component.
	 * 
	 * @return The JList containing questions
	 */
	public JList<String> getQuestionList() {
		return questionList;
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
	 * Gets the currently selected theme title.
	 * 
	 * @return The selected theme title
	 */
	public String getSelectedThemeTitle() {
		return (String) themeComboBox.getSelectedItem();
	}
}

package gui.subpanels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import constants.GUIConstants;
import gui.interfaces.QuizQuestionDelegator;
import persistence.mariaDB.DBManager;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;
import constants.UserStringConstants;

/**
 * Panel displaying a list of questions with theme filtering and persistent data
 * integration.
 * <p>
 * Provides UI for theme selection and browsing questions, with connection to
 * MariaDB to fetch question data.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuestionListPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private JComboBox<String> themeComboBox;
	private JButton switchButton;
	private DefaultListModel<String> listModel;
	private JList<String> questionList;
	private JScrollPane scrollPane;

	private DBManager dbManager;

	private QuizQuestionDelegator delegate;

	private ArrayList<QuestionDTO> currentQuestions;
	private ArrayList<ThemeDTO> currentThemes;
	private boolean showingThemes = false;

	/**
	 * Constructs the question list panel with database manager integration.
	 * 
	 * @param dbManager The database manager for question data access
	 */
	public QuestionListPanel(DBManager dbManager) {
		this.dbManager = dbManager;
		this.currentQuestions = new ArrayList<>();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(BACKGROUND_COLOR);
		createHeaderPanel();
		add(Box.createRigidArea(new Dimension(0, QUESTION_LIST_RIGID_AREA_HEIGHT)));
		createQuestionList();
		add(Box.createVerticalGlue());
		updateQuestionList(UserStringConstants.ALL_THEMES_OPTION);
	}

	/**
	 * Sets the delegate for handling user interactions such as selections.
	 * 
	 * @param delegate The delegate to notify
	 */
	public void setDelegate(QuizQuestionDelegator delegate) {
		this.delegate = delegate;
	}

	/**
	 * Creates the header panel with theme selection combo box and switch button.
	 */
	private void createHeaderPanel() {
		JPanel headerContainer = new JPanel();
		headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
		headerContainer.setBackground(BACKGROUND_COLOR);

		JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, PANEL_MARGIN_H, 0));
		comboPanel.setBackground(BACKGROUND_COLOR);

		JLabel themeLabel = new JLabel(QUESTIONLIST_LABEL);
		themeLabel.setFont(TITLE_FONT);
		themeLabel.setBorder(BorderFactory.createEmptyBorder(BORDER_TOP, BORDER_RIGHT, BORDER_BOTTOM, BORDER_RIGHT));

		themeComboBox = new JComboBox<>();
		themeComboBox.setPreferredSize(new Dimension(COMBOBOX_WIDTH, COMBOBOX_HEIGHT));
		themeComboBox.setEditable(false);
		themeComboBox.addItem(UserStringConstants.ALL_THEMES_OPTION);

		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			themeComboBox.addItem(theme.getThemeTitle());
		}

		themeComboBox.setBackground(TEXTFIELD_BACKGROUND);
		themeComboBox.addActionListener(_ -> {
			if (!showingThemes) {
				String selectedTheme = (String) themeComboBox.getSelectedItem();
				if (selectedTheme != null && delegate != null) {
					delegate.onThemeSelected(selectedTheme);
				}
			}
		});

		// Create switch button
		switchButton = new JButton(UserStringConstants.BTN_SHOW_THEMES);
		switchButton.setPreferredSize(new Dimension(120, COMBOBOX_HEIGHT));
		switchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleListView();
			}
		});

		comboPanel.add(themeLabel);
		comboPanel.add(themeComboBox);
		comboPanel.add(switchButton);
		headerContainer.add(comboPanel);
		add(headerContainer);
	}

	/**
	 * Creates the scrollable question list component.
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

		Dimension listSize = new Dimension(QUESTION_LIST_WIDTH, QUESTION_LIST_HEIGHT);
		scrollPane.setPreferredSize(listSize);
		scrollPane.setMaximumSize(listSize);
		scrollPane.setMinimumSize(new Dimension(QUESTION_LIST_MIN_WIDTH, QUESTION_LIST_HEIGHT));

		add(scrollPane);
	}

	/**
	 * Updates the question list based on selected theme.
	 * 
	 * @param selectedThemeTitle Theme to filter by, "Alle Themen" for all questions
	 */
	public void updateQuestionList(String selectedThemeTitle) {
		listModel.clear();
		currentQuestions.clear();

		if (UserStringConstants.ALL_THEMES_OPTION.equals(selectedThemeTitle)) {
			ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
			for (ThemeDTO theme : themes) {
				ArrayList<QuestionDTO> questions = dbManager.getQuestionsFor(theme);
				currentQuestions.addAll(questions);
			}
		} else {
			ThemeDTO selectedTheme = findThemeByTitle(selectedThemeTitle);
			if (selectedTheme != null) {
				currentQuestions = dbManager.getQuestionsFor(selectedTheme);
			}
		}

		for (QuestionDTO question : currentQuestions) {
			String displayText = question.getQuestionTitle();
			if (displayText == null || displayText.trim().isEmpty()) {
				displayText = question.getQuestionText();
			}
			if (displayText.length() > QUESTION_TEXT_MAX_LENGTH) {
				displayText = displayText.substring(0, QUESTION_TEXT_TRUNCATE_LENGTH) + "...";
			}
			listModel.addElement(displayText);
		}

		updateThemeComboBox();
	}

	/**
	 * Updates the theme combo box with current themes from database.
	 */
	private void updateThemeComboBox() {
		String currentSelection = (String) themeComboBox.getSelectedItem();
		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();

		themeComboBox.removeAllItems();
		themeComboBox.addItem(UserStringConstants.ALL_THEMES_OPTION);
		for (ThemeDTO theme : themes) {
			themeComboBox.addItem(theme.getThemeTitle());
		}

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
	 * Forces a refresh of the theme combo box from the database. Call this when
	 * themes have changed.
	 */
	public void refreshThemeComboBox() {
		updateThemeComboBox();
	}

	/**
	 * Finds a theme by its title.
	 * 
	 * @param title The theme title
	 * @return The ThemeDTO or null if not found
	 */
	private ThemeDTO findThemeByTitle(String title) {
		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			if (theme.getThemeTitle().equals(title)) {
				return theme;
			}
		}
		return null;
	}

	/**
	 * Returns the JList component containing questions.
	 * 
	 * @return JList of questions
	 */
	public JList<String> getQuestionList() {
		return questionList;
	}

	/**
	 * Returns the currently selected theme title.
	 * 
	 * @return The selected theme title
	 */
	public String getSelectedThemeTitle() {
		return (String) themeComboBox.getSelectedItem();
	}

	/**
	 * Returns the QuestionDTO at the specified index.
	 * 
	 * @param index The index of the question
	 * @return The QuestionDTO or null if index is invalid
	 */
	public QuestionDTO getQuestionByIndex(int index) {
		if (index >= 0 && index < currentQuestions.size()) {
			return currentQuestions.get(index);
		}
		return null;
	}

	/**
	 * Toggles between showing themes and questions in the list.
	 */
	private void toggleListView() {
		showingThemes = !showingThemes;
		
		if (showingThemes) {
			switchButton.setText(UserStringConstants.BTN_SHOW_QUESTIONS);
			themeComboBox.setEnabled(false);
			updateThemeList();
		} else {
			switchButton.setText(UserStringConstants.BTN_SHOW_THEMES);
			themeComboBox.setEnabled(true);
			String selectedTheme = (String) themeComboBox.getSelectedItem();
			if (selectedTheme != null) {
				updateQuestionList(selectedTheme);
			}
		}
	}
	
	/**
	 * Updates the list to show themes instead of questions.
	 */
	private void updateThemeList() {
		listModel.clear();
		currentThemes = dbManager.getAllThemes();
		
		for (ThemeDTO theme : currentThemes) {
			listModel.addElement(theme.getThemeTitle());
		}
	}
	
	/**
	 * Returns the ThemeDTO at the specified index when showing themes.
	 * 
	 * @param index The index of the theme
	 * @return The ThemeDTO or null if index is invalid or not showing themes
	 */
	public ThemeDTO getThemeByIndex(int index) {
		if (showingThemes && currentThemes != null && index >= 0 && index < currentThemes.size()) {
			return currentThemes.get(index);
		}
		return null;
	}
	
	/**
	 * Returns whether the list is currently showing themes.
	 * 
	 * @return true if showing themes, false if showing questions
	 */
	public boolean isShowingThemes() {
		return showingThemes;
	}
}
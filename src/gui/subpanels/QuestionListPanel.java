package gui.subpanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import constants.GUIConstants;
import constants.UserStringConstants;
import gui.interfaces.QuizQuestionDelegator;
import persistence.QuizDataInterface;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Panel displaying a list of questions with theme filtering and persistent data
 * integration.
 * <p>
 * Provides UI for theme selection and browsing questions, with connection to
 * MariaDB to fetch question data. Now uses QuizThemeInfoView for theme
 * information display instead of a simple list.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.1
 * @since 1.0
 */
public class QuestionListPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private QuizHeaderPanel headerPanel;
	private DefaultListModel<String> listModel;
	private JList<String> questionList;
	private JScrollPane scrollPane;
	private QuizThemeInfoView themeInfoView;

	private QuizDataInterface dataManager;

	private QuizQuestionDelegator delegate;

	private ArrayList<QuestionDTO> currentQuestions;
	private boolean showingThemes = false;

	/**
	 * Constructs the question list panel with data manager integration.
	 * 
	 * @param dataManager Data manager for retrieving questions (database or
	 *                    file-based)
	 */
	public QuestionListPanel(QuizDataInterface dataManager) {
		this.dataManager = dataManager;
		this.currentQuestions = new ArrayList<>();
		setLayout(new BorderLayout());
		setBackground(BACKGROUND_COLOR);

		createHeaderPanel();
		createQuestionList();
		createThemeInfoView();

		add(headerPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

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
	 * Creates the header panel with theme selection and switch functionality.
	 */
	private void createHeaderPanel() {
		headerPanel = new QuizHeaderPanel(dataManager);

		headerPanel.setThemeSelectionListener(selectedTheme -> {
			if (showingThemes) {

				themeInfoView.showThemeInfo(selectedTheme);
			}

			if (delegate != null) {
				delegate.onThemeSelected(selectedTheme);
			}
		});

		headerPanel.setViewSwitchListener(() -> {
			toggleListView();
		});

		headerPanel.setSwitchButtonText(UserStringConstants.BTN_SHOW_THEMES);
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

		Dimension listSize = new Dimension(RIGHT_PANEL_WIDTH, MAIN_CONTENT_HEIGHT - 100);
		scrollPane.setPreferredSize(listSize);
		scrollPane.setMaximumSize(listSize);
		scrollPane.setMinimumSize(new Dimension(RIGHT_PANEL_WIDTH - 100, MAIN_CONTENT_HEIGHT - 100));
	}

	/**
	 * Creates the theme info view component.
	 */
	private void createThemeInfoView() {
		themeInfoView = new QuizThemeInfoView(dataManager);
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
			ArrayList<ThemeDTO> themes = dataManager.getAllThemes();
			for (ThemeDTO theme : themes) {
				ArrayList<QuestionDTO> questions = dataManager.getQuestionsFor(theme);
				currentQuestions.addAll(questions);
			}
		} else {
			ThemeDTO selectedTheme = findThemeByTitle(selectedThemeTitle);
			if (selectedTheme != null) {
				currentQuestions = dataManager.getQuestionsFor(selectedTheme);
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

		headerPanel.refreshThemes();
	}

	/**
	 * Forces a refresh of the theme combo box from the database. Call this when
	 * themes have changed.
	 */
	public void refreshThemeComboBox() {
		headerPanel.refreshThemes();
	}

	/**
	 * Finds a theme by its title.
	 * 
	 * @param title The theme title
	 * @return The ThemeDTO or null if not found
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
		return headerPanel.getSelectedTheme();
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
	 * Toggles between showing themes and questions in the display area.
	 */
	private void toggleListView() {
		showingThemes = !showingThemes;

		if (showingThemes) {

			headerPanel.setSwitchButtonText(UserStringConstants.BTN_SHOW_QUESTIONS);
			remove(scrollPane);
			add(themeInfoView, BorderLayout.CENTER);

			String selectedTheme = headerPanel.getSelectedTheme();
			if (selectedTheme != null) {
				themeInfoView.showThemeInfo(selectedTheme);
			} else {
				themeInfoView.showWelcomeMessage();
			}
		} else {

			headerPanel.setSwitchButtonText(UserStringConstants.BTN_SHOW_THEMES);
			remove(themeInfoView);
			add(scrollPane, BorderLayout.CENTER);

			String selectedTheme = headerPanel.getSelectedTheme();
			if (selectedTheme != null) {
				updateQuestionList(selectedTheme);
			}
		}

		revalidate();
		repaint();
	}

	/**
	 * Returns whether the panel is currently showing themes.
	 * 
	 * @return true if showing themes, false if showing questions
	 */
	public boolean isShowingThemes() {
		return showingThemes;
	}
}
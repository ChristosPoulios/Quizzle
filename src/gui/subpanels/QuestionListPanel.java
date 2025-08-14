package gui.subpanels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import gui.interfaces.GUIConstants;
import gui.interfaces.QuizQuestionDelegator;
import persistence.mariaDB.DBManager;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Panel displaying a list of questions with theme filtering and persistent data
 * integration.
 * 
 * Provides theme selection and question browsing with MariaDB integration.
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
	private DBManager dbManager;
	private QuizQuestionDelegator delegate;
	private ArrayList<QuestionDTO> currentQuestions; // Cache for current questions

	/**
	 * Constructs the question list panel with database manager integration.
	 * 
	 * @param dbManager The database manager for question access
	 */
	public QuestionListPanel(DBManager dbManager) {
		this.dbManager = dbManager;
		this.currentQuestions = new ArrayList<>();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(BACKGROUND_COLOR);

		createHeaderPanel();

		add(Box.createRigidArea(new Dimension(0, 8)));
		createQuestionList();

		add(Box.createVerticalGlue());

		updateQuestionList("Alle Themen");
	}

	/**
	 * Sets the delegate for handling user interactions.
	 * 
	 * @param delegate The delegate to notify about selections
	 */
	public void setDelegate(QuizQuestionDelegator delegate) {
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

		JLabel themeLabel = new JLabel(QUESTIONLIST_LABEL);
		themeLabel.setFont(TITLE_FONT);
		themeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
		themeComboBox = new JComboBox<>();
		themeComboBox.setPreferredSize(new Dimension(300, 30));
		themeComboBox.setEditable(false);
		themeComboBox.addItem("Alle Themen");

		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
		for (ThemeDTO theme : themes) {
			themeComboBox.addItem(theme.getThemeTitle());
		}

		themeComboBox.setBackground(TEXTFIELD_BACKGROUND);
		themeComboBox.addActionListener(_ -> {
			String selectedTheme = (String) themeComboBox.getSelectedItem();
			if (selectedTheme != null && delegate != null) {
				delegate.onThemeSelected(selectedTheme);
			}
		});

		comboPanel.add(themeLabel);
		comboPanel.add(themeComboBox);
		headerContainer.add(comboPanel);
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

		Dimension listSize = new Dimension(400, 350);
		scrollPane.setPreferredSize(listSize);
		scrollPane.setMaximumSize(listSize);
		scrollPane.setMinimumSize(new Dimension(300, 350));

		add(scrollPane);
	}

	/**
	 * Updates the question list based on selected theme.
	 * 
	 * @param selectedThemeTitle The theme to filter by, or "Alle Themen" for all
	 */
	public void updateQuestionList(String selectedThemeTitle) {
		listModel.clear();
		currentQuestions.clear();

		if ("Alle Themen".equals(selectedThemeTitle)) {

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
				if (displayText.length() > 50) {
					displayText = displayText.substring(0, 47) + "...";
				}
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
		themeComboBox.addItem("Alle Themen");
		
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
	 * Forces a refresh of the theme combo box from the database.
	 * Call this method when themes have been added/modified/deleted.
	 */
	public void refreshThemeComboBox() {
		updateThemeComboBox();
	}

	/**
	 * Finds a theme by its title.
	 * 
	 * @param title The theme title to search for
	 * @return The theme DTO or null if not found
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
	 * Gets the question list component.
	 * 
	 * @return The JList containing questions
	 */
	public JList<String> getQuestionList() {
		return questionList;
	}

	/**
	 * Gets the selected theme title from the combo box.
	 * 
	 * @return The selected theme title
	 */
	public String getSelectedThemeTitle() {
		return (String) themeComboBox.getSelectedItem();
	}

	/**
	 * Gets a question by its index in the current list.
	 * 
	 * @param index The index of the question
	 * @return The question DTO or null if index is invalid
	 */
	public QuestionDTO getQuestionByIndex(int index) {
		if (index >= 0 && index < currentQuestions.size()) {
			return currentQuestions.get(index);
		}
		return null;
	}
}
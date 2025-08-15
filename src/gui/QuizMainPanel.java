package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import gui.interfaces.GUIConstants;
import gui.interfaces.QuizPanelDelegator;
import gui.subpanels.QuestionPanel;
import gui.subpanels.QuizButtonPanel;
import gui.subpanels.QuizInfoViewPanel;

import persistence.mariaDB.DBManager;

import quizlogic.dto.QuestionDTO;

/**
 * Main panel for the quiz tab providing quiz gameplay interface.
 * <p>
 * This panel integrates the question display, quiz statistics, and controls for
 * navigating the quiz. Data is persisted and managed via a shared MariaDB
 * database manager. This class implements {@link QuizPanelDelegator} to handle
 * user interactions with quiz controls.
 * </p>
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 2.0
 */
public class QuizMainPanel extends JPanel implements GUIConstants, QuizPanelDelegator {

	private static final long serialVersionUID = 1L;

	private QuestionPanel questionPanel;
	private QuizInfoViewPanel quizInfoViewPanel;
	private QuizButtonPanel buttonPanel;
	private DBManager dbManager;
	private JPanel contentPanel;

	/**
	 * Constructs the quiz main panel integrating all subcomponents and connecting
	 * to the MariaDB persistence layer.
	 * 
	 * @param dbManager the MariaDB database manager for quiz data persistence
	 */
	public QuizMainPanel(DBManager dbManager) {
		this.dbManager = dbManager;
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));
		setBorder(BorderFactory.createEmptyBorder(PANEL_MARGIN_V, PANEL_MARGIN_H, PANEL_MARGIN_V, PANEL_MARGIN_H));
		initPanels();
		fillWithQuestionData(dbManager.getRandomQuestion());
	}

	/**
	 * Initializes the child panels and their layout, including question display,
	 * quiz info, and control buttons.
	 */
	private void initPanels() {
		questionPanel = new QuestionPanel();
		quizInfoViewPanel = new QuizInfoViewPanel(dbManager);
		contentPanel = new JPanel(new GridLayout(1, 2, PANEL_MARGIN_H, 0));
		contentPanel.setBackground(BACKGROUND_COLOR);
		contentPanel.add(questionPanel);
		contentPanel.add(quizInfoViewPanel);
		add(contentPanel, BorderLayout.CENTER);

		buttonPanel = new QuizButtonPanel(BTN_SHOW_ANSWER, BTN_SAVE_ANSWER, BTN_NEXT_QUESTION);
		buttonPanel.setDelegate(this);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Updates questionPanel with a new {@link QuestionDTO}. Shows a message in the
	 * button panel if no question is available.
	 * 
	 * @param question The quiz question to display, or null to clear
	 */
	public void fillWithQuestionData(QuestionDTO question) {
		questionPanel.fillWithQuestionData(question);
		if (question == null) {
			buttonPanel.setMessage("Keine Frage verfügbar");
		} else {
			buttonPanel.setMessage("");
		}
	}

	/**
	 * Handler for "Show Answer" button click. Displays the correct answer for the
	 * current question.
	 */
	@Override
	public void onShowAnswerClicked() {
		String correct = questionPanel.getAnswersPanel().getCorrectAnswerText();
		buttonPanel.setMessage("Correct answer: " + correct);
	}

	/**
	 * Handler for "Save Answer" button click. Displays a confirmation message.
	 */
	@Override
	public void onSaveAnswerClicked() {
		buttonPanel.setMessage("Answer saved.");
	}

	/**
	 * Handler for "Next Question" button click. Loads a new random question from
	 * the database.
	 */
	@Override
	public void onNextQuestionClicked() {
		fillWithQuestionData(dbManager.getRandomQuestion());
		buttonPanel.setMessage("Next question.");
	}

	/**
	 * Handler for question selection from a list. Displays the selected question
	 * info.
	 * 
	 * @param entry the selected entry text
	 * @param index the index of the selected question
	 */
	@Override
	public void onQuestionSelected(String entry, int index) {
		buttonPanel.setMessage("Question selected: " + entry);
	}

	/**
	 * Handler for theme selection from a theme list. Displays the selected theme
	 * info.
	 * 
	 * @param themeTitle the selected theme's title
	 */
	@Override
	public void onThemeSelected(String themeTitle) {
		buttonPanel.setMessage("Theme selected: " + themeTitle);
	}
}

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
 * Main panel for the quiz tab with persistent data management.
 * 
 * Uses QuestionPanel for question display, QuizInfoViewPanel for statistics,
 * and DBManager for MariaDB data persistence. Supports real-time data updates.
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
	 * Constructs the quiz main panel with database manager integration.
	 * 
	 * @param dbManager The MariaDB database manager for persistence
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
	 * Initializes main child panels and their layout with database manager
	 * integration.
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
	 * Fills the QuestionPanel with the provided question data.
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

	@Override
	public void onShowAnswerClicked() {
		String correct = questionPanel.getAnswersPanel().getCorrectAnswerText();
		buttonPanel.setMessage("Correct answer: " + correct);
	}

	@Override
	public void onSaveAnswerClicked() {
		buttonPanel.setMessage("Answer saved.");
	}

	@Override
	public void onNextQuestionClicked() {
		fillWithQuestionData(dbManager.getRandomQuestion());
		buttonPanel.setMessage("Next question.");
	}

	@Override
	public void onQuestionSelected(String entry, int index) {
		buttonPanel.setMessage("Question selected: " + entry);
	}

	@Override
	public void onThemeSelected(String themeTitle) {
		buttonPanel.setMessage("Theme selected: " + themeTitle);
	}
}
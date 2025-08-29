package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import constants.GUIConstants;
import constants.UserStringConstants;
import gui.subpanels.SessionsTrendChart;
import gui.subpanels.StatisticsBarChart;
import persistence.QuizDataInterface;
import quizlogic.dto.QuizSessionDTO;
import quizlogic.dto.UserAnswerDTO;

/**
 * Main panel for displaying quiz statistics. Shows current session statistics
 * including correct answers, wrong answers, and success rate.
 */
public class QuizStatisticsMainPanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;

	/** Labels for displaying statistics */
	private JLabel correctAnswersLabel;
	private JLabel wrongAnswersLabel;
	private JLabel successRateLabel;

	/** Current quiz session reference */
	private QuizSessionDTO currentSession;

	/** Data manager for accessing historical sessions */
	private QuizDataInterface dataManager;

	/** Labels for historical statistics */
	private JLabel totalSessionsLabel;
	private JLabel overallSuccessRateLabel;

	/** Text area for displaying recent sessions */
	private JTextArea recentSessionsArea;

	/** Labels for overall totals */
	private JLabel totalCorrectAnswersLabel;
	private JLabel totalWrongAnswersLabel;
	private JLabel totalQuestionsOverallLabel;

	/** Bar chart for current session visualization */
	private StatisticsBarChart currentSessionBarChart;

	/** Trend chart for recent sessions */
	private SessionsTrendChart sessionsTrendChart;

	public QuizStatisticsMainPanel(QuizDataInterface dataManager) {
		this.dataManager = dataManager;
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));

		initComponents();
		updateDisplay();
	}

	private void initComponents() {
		// Header
		JLabel headerLabel = new JLabel(UserStringConstants.STATISTICS_HEADER, SwingConstants.CENTER);
		headerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
		add(headerLabel, BorderLayout.NORTH);

		JPanel statsPanel = createStatisticsPanel();
		JScrollPane mainScrollPane = new JScrollPane(statsPanel);
		mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		add(mainScrollPane, BorderLayout.CENTER);

	}

	private JPanel createStatisticsPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(BACKGROUND_COLOR);

		JPanel currentPanel = createCurrentSessionPanel();
		mainPanel.add(currentPanel, BorderLayout.NORTH);

		JPanel historicalPanel = createHistoricalPanel();
		mainPanel.add(historicalPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private JPanel createCurrentSessionPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(BACKGROUND_COLOR);
		panel.setBorder(BorderFactory.createTitledBorder("Aktuelle Session"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 20, 10, 20);
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel correctLabel = new JLabel(UserStringConstants.STATISTICS_CORRECT_ANSWERS + ":");
		correctLabel.setFont(DEFAULT_FONT);
		panel.add(correctLabel, gbc);

		gbc.gridx = 1;
		correctAnswersLabel = new JLabel("0");
		correctAnswersLabel.setFont(DEFAULT_FONT);
		correctAnswersLabel.setForeground(SUCCESS_COLOR);
		panel.add(correctAnswersLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel wrongLabel = new JLabel(UserStringConstants.STATISTICS_WRONG_ANSWERS + ":");
		wrongLabel.setFont(DEFAULT_FONT);
		panel.add(wrongLabel, gbc);

		gbc.gridx = 1;
		wrongAnswersLabel = new JLabel("0");
		wrongAnswersLabel.setFont(DEFAULT_FONT);
		wrongAnswersLabel.setForeground(ERROR_COLOR);
		panel.add(wrongAnswersLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel rateLabel = new JLabel(UserStringConstants.STATISTICS_SUCCESS_RATE + ":");
		rateLabel.setFont(DEFAULT_FONT);
		panel.add(rateLabel, gbc);

		gbc.gridx = 1;
		successRateLabel = new JLabel("0%");
		successRateLabel.setFont(DEFAULT_FONT);
		panel.add(successRateLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		currentSessionBarChart = new StatisticsBarChart();
		panel.add(currentSessionBarChart, gbc);

		return panel;
	}

	private JPanel createHistoricalPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(BACKGROUND_COLOR);
		panel.setBorder(BorderFactory.createTitledBorder(UserStringConstants.STATISTICS_HISTORICAL_HEADER));

		JPanel summaryPanel = new JPanel(new GridBagLayout());
		summaryPanel.setBackground(BACKGROUND_COLOR);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 20, 10, 20);
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel sessionsLabel = new JLabel(UserStringConstants.STATISTICS_TOTAL_SESSIONS + ":");
		sessionsLabel.setFont(DEFAULT_FONT);
		summaryPanel.add(sessionsLabel, gbc);

		gbc.gridx = 1;
		totalSessionsLabel = new JLabel("0");
		totalSessionsLabel.setFont(DEFAULT_FONT);
		summaryPanel.add(totalSessionsLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel overallLabel = new JLabel(UserStringConstants.STATISTICS_OVERALL_SUCCESS_RATE + ":");
		overallLabel.setFont(DEFAULT_FONT);
		summaryPanel.add(overallLabel, gbc);

		gbc.gridx = 1;
		overallSuccessRateLabel = new JLabel("0%");
		overallSuccessRateLabel.setFont(DEFAULT_FONT);
		summaryPanel.add(overallSuccessRateLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel totalQuestionsOverallLabelText = new JLabel(
				UserStringConstants.STATISTICS_TOTAL_QUESTIONS_OVERALL + ":");
		totalQuestionsOverallLabelText.setFont(DEFAULT_FONT);
		summaryPanel.add(totalQuestionsOverallLabelText, gbc);

		gbc.gridx = 1;
		totalQuestionsOverallLabel = new JLabel("0");
		totalQuestionsOverallLabel.setFont(DEFAULT_FONT);
		summaryPanel.add(totalQuestionsOverallLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel totalCorrectLabelText = new JLabel(UserStringConstants.STATISTICS_TOTAL_CORRECT_ANSWERS + ":");
		totalCorrectLabelText.setFont(DEFAULT_FONT);
		summaryPanel.add(totalCorrectLabelText, gbc);

		gbc.gridx = 1;
		totalCorrectAnswersLabel = new JLabel("0");
		totalCorrectAnswersLabel.setFont(DEFAULT_FONT);
		totalCorrectAnswersLabel.setForeground(SUCCESS_COLOR);
		summaryPanel.add(totalCorrectAnswersLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel totalWrongLabelText = new JLabel(UserStringConstants.STATISTICS_TOTAL_WRONG_ANSWERS + ":");
		totalWrongLabelText.setFont(DEFAULT_FONT);
		summaryPanel.add(totalWrongLabelText, gbc);

		gbc.gridx = 1;
		totalWrongAnswersLabel = new JLabel("0");
		totalWrongAnswersLabel.setFont(DEFAULT_FONT);
		totalWrongAnswersLabel.setForeground(ERROR_COLOR);
		summaryPanel.add(totalWrongAnswersLabel, gbc);

		panel.add(summaryPanel, BorderLayout.NORTH);

		JPanel recentSessionsPanel = new JPanel(new BorderLayout());
		recentSessionsPanel.setBackground(BACKGROUND_COLOR);
		recentSessionsPanel.setBorder(BorderFactory.createTitledBorder(UserStringConstants.STATISTICS_RECENT_SESSIONS));

		JPanel sessionsContentPanel = new JPanel(new BorderLayout());
		sessionsContentPanel.setBackground(BACKGROUND_COLOR);

		recentSessionsArea = new JTextArea(12, 30);
		recentSessionsArea.setEditable(false);
		recentSessionsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		recentSessionsArea.setBackground(BACKGROUND_COLOR);

		JScrollPane scrollPane = new JScrollPane(recentSessionsArea);
		sessionsContentPanel.add(scrollPane, BorderLayout.CENTER);

		sessionsTrendChart = new SessionsTrendChart();
		sessionsContentPanel.add(sessionsTrendChart, BorderLayout.EAST);

		recentSessionsPanel.add(sessionsContentPanel, BorderLayout.CENTER);

		panel.add(recentSessionsPanel, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Updates the current session and refreshes the display.
	 * 
	 * @param session the current quiz session
	 */
	public void updateSession(QuizSessionDTO session) {
		this.currentSession = session;
		updateDisplay();
	}

	/**
	 * Updates the statistics display based on the current session.
	 */
	private void updateDisplay() {
		updateCurrentSessionDisplay();
		updateHistoricalDisplay();
	}

	/**
	 * Updates the current session statistics display.
	 */
	private void updateCurrentSessionDisplay() {
		if (currentSession == null || currentSession.getUserAnswers() == null
				|| currentSession.getUserAnswers().isEmpty()) {

			correctAnswersLabel.setText("0");
			wrongAnswersLabel.setText("0");
			successRateLabel.setText("0%");
			successRateLabel.setForeground(LABEL_COLOR);
			return;
		}

		List<UserAnswerDTO> userAnswers = currentSession.getUserAnswers();
		int totalQuestions = countUniqueQuestions(userAnswers);
		int correctQuestions = countCorrectQuestions(userAnswers);

		double successRate = totalQuestions > 0 ? (double) correctQuestions / totalQuestions * 100 : 0;

		correctAnswersLabel.setText(String.valueOf(correctQuestions));
		wrongAnswersLabel.setText(String.valueOf(totalQuestions - correctQuestions));
		successRateLabel.setText(String.format("%.1f%%", successRate));

		if (successRate >= 80) {
			successRateLabel.setForeground(SUCCESS_COLOR);
		} else if (successRate >= 60) {
			successRateLabel.setForeground(WARNING_COLOR);
		} else {
			successRateLabel.setForeground(ERROR_COLOR);
		}

		currentSessionBarChart.updateData(correctQuestions, totalQuestions - correctQuestions);
	}

	/**
	 * Updates the historical statistics display.
	 */
	private void updateHistoricalDisplay() {
		if (dataManager == null) {
			totalSessionsLabel.setText("0");
			overallSuccessRateLabel.setText("0%");
			recentSessionsArea.setText("Keine historischen Daten verfügbar.");
			return;
		}

		List<QuizSessionDTO> allSessions = dataManager.getAllQuizSessions();

		if (allSessions.isEmpty()) {
			totalSessionsLabel.setText("0");
			overallSuccessRateLabel.setText("0%");
			recentSessionsArea.setText("Noch keine abgeschlossenen Sessions vorhanden.");
			return;
		}

		int totalSessions = allSessions.size();
		int totalCorrectQuestions = 0;
		int totalAnsweredQuestions = 0;

		for (QuizSessionDTO session : allSessions) {
			if (session.getUserAnswers() != null && !session.getUserAnswers().isEmpty()) {
				int sessionQuestions = countUniqueQuestions(session.getUserAnswers());
				int sessionCorrect = countCorrectQuestions(session.getUserAnswers());

				totalAnsweredQuestions += sessionQuestions;
				totalCorrectQuestions += sessionCorrect;
			}
		}

		double overallSuccessRate = totalAnsweredQuestions > 0
				? (double) totalCorrectQuestions / totalAnsweredQuestions * 100
				: 0;

		totalSessionsLabel.setText(String.valueOf(totalSessions));
		overallSuccessRateLabel.setText(String.format("%.1f%%", overallSuccessRate));

		totalQuestionsOverallLabel.setText(String.valueOf(totalAnsweredQuestions));
		totalCorrectAnswersLabel.setText(String.valueOf(totalCorrectQuestions));
		totalWrongAnswersLabel.setText(String.valueOf(totalAnsweredQuestions - totalCorrectQuestions));

		if (overallSuccessRate >= 80) {
			overallSuccessRateLabel.setForeground(SUCCESS_COLOR);
		} else if (overallSuccessRate >= 60) {
			overallSuccessRateLabel.setForeground(WARNING_COLOR);
		} else {
			overallSuccessRateLabel.setForeground(ERROR_COLOR);
		}

		updateRecentSessionsList(allSessions);
	}

	/**
	 * Updates the recent sessions list display.
	 */
	private void updateRecentSessionsList(List<QuizSessionDTO> allSessions) {
		StringBuilder recentText = new StringBuilder();
		recentText.append(String.format("%-20s %-8s %-8s %-8s%n", "Datum", "Fragen", "Richtig", "Rate"));
		recentText.append("─".repeat(50)).append("\n");

		List<QuizSessionDTO> recentSessions = dataManager.getRecentQuizSessions(10);

		for (QuizSessionDTO session : recentSessions) {
			if (session.getUserAnswers() != null && !session.getUserAnswers().isEmpty()) {
				int questions = countUniqueQuestions(session.getUserAnswers());
				int correct = countCorrectQuestions(session.getUserAnswers());
				double rate = questions > 0 ? (double) correct / questions * 100 : 0;

				String dateStr = session.getTimestamp().toString().substring(0, 19);
				recentText.append(String.format("%-20s %-8d %-8d %6.1f%%%n", dateStr, questions, correct, rate));
			}
		}

		recentSessionsArea.setText(recentText.toString());
		recentSessionsArea.setCaretPosition(0);

		if (sessionsTrendChart != null) {
			sessionsTrendChart.updateSessions(recentSessions);
		}
	}

	/**
	 * Counts unique questions that have been answered. Multiple answers to the same
	 * question count as one question.
	 */
	private int countUniqueQuestions(List<UserAnswerDTO> userAnswers) {
		return (int) userAnswers.stream().mapToInt(UserAnswerDTO::getQuestionId).distinct().count();
	}

	/**
	 * Counts questions that were answered completely correctly. A question is
	 * considered correct only if the user's selected answers match the correct
	 * answers.
	 */
	private int countCorrectQuestions(List<UserAnswerDTO> userAnswers) {
		return (int) userAnswers.stream().collect(java.util.stream.Collectors.groupingBy(UserAnswerDTO::getQuestionId))
				.values().stream().mapToLong(answersForQuestion -> {

					boolean hasSelectedAnswers = answersForQuestion.stream().anyMatch(UserAnswerDTO::isSelected);

					if (!hasSelectedAnswers) {

						return 0;
					}

					boolean allSelectedAreCorrect = answersForQuestion.stream().filter(UserAnswerDTO::isSelected)
							.allMatch(UserAnswerDTO::isCorrect);

					boolean noIncorrectSelected = answersForQuestion.stream()
							.noneMatch(answer -> answer.isSelected() && !answer.isCorrect());

					return (allSelectedAreCorrect && noIncorrectSelected) ? 1 : 0;
				}).sum();
	}

	/**
	 * Gets the current quiz session.
	 * 
	 * @return the current session or null if no session is active
	 */
	public QuizSessionDTO getCurrentSession() {
		return currentSession;
	}
}

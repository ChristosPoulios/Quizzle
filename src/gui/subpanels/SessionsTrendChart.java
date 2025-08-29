package gui.subpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import constants.GUIConstants;
import quizlogic.dto.QuizSessionDTO;
import quizlogic.dto.UserAnswerDTO;

/**
 * Compact trend chart showing success rates of recent quiz sessions. Displays a
 * small bar for each session with color-coded success rate.
 */
public class SessionsTrendChart extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;

	private List<QuizSessionDTO> sessions;

	private static final int BAR_WIDTH = 12;
	private static final int BAR_SPACING = 2;
	private static final int CHART_PADDING = 10;
	private static final int MAX_SESSIONS = 15;

	public SessionsTrendChart() {
		setBackground(BACKGROUND_COLOR);
		setPreferredSize(new Dimension(250, 200));
		setMinimumSize(new Dimension(200, 150));
	}

	/**
	 * Updates the chart with new session data.
	 * 
	 * @param sessions list of recent sessions to display
	 */
	public void updateSessions(List<QuizSessionDTO> sessions) {
		this.sessions = sessions;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (sessions == null || sessions.isEmpty()) {
			drawEmptyMessage(g);
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int width = getWidth();
		int height = getHeight();

		g2d.setColor(LABEL_COLOR);
		g2d.setFont(DEFAULT_FONT);
		g2d.drawString("Trend", CHART_PADDING, 20);

		int chartHeight = height - 40;
		int chartWidth = width - 2 * CHART_PADDING;

		int sessionsToShow = Math.min(sessions.size(), MAX_SESSIONS);
		int totalBarsWidth = sessionsToShow * BAR_WIDTH + (sessionsToShow - 1) * BAR_SPACING;

		if (totalBarsWidth > chartWidth) {
			sessionsToShow = (chartWidth + BAR_SPACING) / (BAR_WIDTH + BAR_SPACING);
		}

		int startX = CHART_PADDING;

		for (int i = 0; i < sessionsToShow && i < sessions.size(); i++) {
			QuizSessionDTO session = sessions.get(i);
			double successRate = calculateSuccessRate(session);

			int barHeight = (int) (chartHeight * successRate / 100.0);
			int barY = height - CHART_PADDING - barHeight;

			Color barColor = getSuccessRateColor(successRate);
			g2d.setColor(barColor);

			g2d.fillRect(startX, barY, BAR_WIDTH, barHeight);

			g2d.setColor(PANEL_BORDER_COLOR);
			g2d.drawRect(startX, barY, BAR_WIDTH, barHeight);

			startX += BAR_WIDTH + BAR_SPACING;
		}

		g2d.setColor(PANEL_BORDER_COLOR);
		g2d.drawLine(CHART_PADDING, height - CHART_PADDING, CHART_PADDING + totalBarsWidth, height - CHART_PADDING);

		drawPercentageMarkers(g2d, width, height, chartHeight);
	}

	private void drawEmptyMessage(Graphics g) {
		g.setColor(LABEL_COLOR);
		g.setFont(DEFAULT_FONT);
		String message = "Keine Sessions";
		int x = (getWidth() - g.getFontMetrics().stringWidth(message)) / 2;
		int y = getHeight() / 2;
		g.drawString(message, x, y);
	}

	private void drawPercentageMarkers(Graphics2D g2d, int width, int height, int chartHeight) {
		g2d.setColor(LABEL_COLOR.brighter());
		g2d.setFont(DEFAULT_FONT.deriveFont(9f));

		int y100 = height - CHART_PADDING - chartHeight;
		g2d.drawString("100%", width - 35, y100 + 5);

		int y50 = height - CHART_PADDING - chartHeight / 2;
		g2d.drawString("50%", width - 30, y50 + 5);

		int y0 = height - CHART_PADDING;
		g2d.drawString("0%", width - 25, y0 - 2);
	}

	private double calculateSuccessRate(QuizSessionDTO session) {
		if (session.getUserAnswers() == null || session.getUserAnswers().isEmpty()) {
			return 0.0;
		}

		int totalQuestions = countUniqueQuestions(session.getUserAnswers());
		int correctQuestions = countCorrectQuestions(session.getUserAnswers());

		return totalQuestions > 0 ? (double) correctQuestions / totalQuestions * 100 : 0;
	}

	private Color getSuccessRateColor(double rate) {
		if (rate >= 80) {
			return SUCCESS_COLOR;
		} else if (rate >= 60) {
			return WARNING_COLOR;
		} else {
			return ERROR_COLOR;
		}
	}

	private int countUniqueQuestions(List<UserAnswerDTO> userAnswers) {
		return (int) userAnswers.stream().mapToInt(UserAnswerDTO::getQuestionId).distinct().count();
	}

	private int countCorrectQuestions(List<UserAnswerDTO> userAnswers) {
		return (int) userAnswers.stream().collect(java.util.stream.Collectors.groupingBy(UserAnswerDTO::getQuestionId))
				.values().stream().mapToLong(answersForQuestion -> {
					boolean allSelectedAreCorrect = answersForQuestion.stream().filter(UserAnswerDTO::isSelected)
							.allMatch(UserAnswerDTO::isCorrect);

					boolean anyIncorrectSelected = answersForQuestion.stream()
							.anyMatch(answer -> answer.isSelected() && !answer.isCorrect());

					return (allSelectedAreCorrect && !anyIncorrectSelected) ? 1 : 0;
				}).sum();
	}
}
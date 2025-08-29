package gui.subpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import constants.GUIConstants;

/**
 * Custom panel that displays a segmented bar chart for quiz statistics. Shows
 * correct and wrong answers as colored segments within a single bar.
 */
public class StatisticsBarChart extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;

	/** Intial Value for correct answers */
	private int correctAnswers = 0;

	/** Intial Value for wrong answers */
	private int wrongAnswers = 0;

	/** Chart title */
	private String title = "Antworten";

	/** Chart styling constants */
	private static final int CHART_PADDING = 20;
	private static final int BAR_HEIGHT = 40;
	private static final int TITLE_HEIGHT = 25;
	private static final int LEGEND_HEIGHT = 20;

	public StatisticsBarChart() {
		setBackground(BACKGROUND_COLOR);
		setPreferredSize(new Dimension(400, 120));
	}

	/**
	 * Updates the chart data and repaints the component.
	 * 
	 * @param correctAnswers number of correct answers
	 * @param wrongAnswers   number of wrong answers
	 */
	public void updateData(int correctAnswers, int wrongAnswers) {
		this.correctAnswers = correctAnswers;
		this.wrongAnswers = wrongAnswers;
		repaint();
	}

	/**
	 * Sets a custom title for the chart.
	 * 
	 * @param title the chart title
	 */
	public void setTitle(String title) {
		this.title = title;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		int totalAnswers = correctAnswers + wrongAnswers;

		if (totalAnswers == 0) {
			drawEmptyChart(g2d);
		} else {
			drawChart(g2d, totalAnswers);
		}

		g2d.dispose();
	}

	/**
	 * Draws the complete chart with segmented bar and legend.
	 */
	private void drawChart(Graphics2D g2d, int totalAnswers) {
		int width = getWidth();
		@SuppressWarnings("unused")
		int height = getHeight();

		g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		g2d.setColor(LABEL_COLOR);
		FontMetrics titleFm = g2d.getFontMetrics();
		int titleX = (width - titleFm.stringWidth(title)) / 2;
		g2d.drawString(title, titleX, CHART_PADDING + titleFm.getAscent());

		int barY = CHART_PADDING + TITLE_HEIGHT;
		int barWidth = width - (2 * CHART_PADDING);

		double correctRatio = (double) correctAnswers / totalAnswers;
		int correctWidth = (int) (barWidth * correctRatio);
		int wrongWidth = barWidth - correctWidth;

		g2d.setColor(new Color(240, 240, 240));
		g2d.fillRoundRect(CHART_PADDING, barY, barWidth, BAR_HEIGHT, 8, 8);
		g2d.setColor(new Color(200, 200, 200));
		g2d.drawRoundRect(CHART_PADDING, barY, barWidth, BAR_HEIGHT, 8, 8);

		if (correctWidth > 0) {
			g2d.setColor(SUCCESS_COLOR);
			g2d.fillRoundRect(CHART_PADDING, barY, correctWidth, BAR_HEIGHT, 8, 8);
		}

		if (wrongWidth > 0) {
			g2d.setColor(ERROR_COLOR);

			if (correctWidth > 0) {
				g2d.fillRect(CHART_PADDING + correctWidth, barY, wrongWidth, BAR_HEIGHT);

				g2d.fillRoundRect(CHART_PADDING + correctWidth + wrongWidth - 8, barY, 8, BAR_HEIGHT, 8, 8);
			} else {

				g2d.fillRoundRect(CHART_PADDING, barY, wrongWidth, BAR_HEIGHT, 8, 8);
			}
		}

		drawSegmentValues(g2d, barY, correctWidth, wrongWidth, totalAnswers);

		drawLegend(g2d, barY + BAR_HEIGHT + 10);
	}

	/**
	 * Draws the values on the bar segments.
	 */
	private void drawSegmentValues(Graphics2D g2d, int barY, int correctWidth, int wrongWidth, int totalAnswers) {
		g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		FontMetrics fm = g2d.getFontMetrics();
		int textY = barY + (BAR_HEIGHT + fm.getAscent()) / 2;

		if (correctAnswers > 0 && correctWidth > 30) {
			String correctText = String.valueOf(correctAnswers);
			int correctTextX = CHART_PADDING + (correctWidth - fm.stringWidth(correctText)) / 2;
			g2d.setColor(Color.WHITE);
			g2d.drawString(correctText, correctTextX, textY);
		}

		if (wrongAnswers > 0 && wrongWidth > 30) {
			String wrongText = String.valueOf(wrongAnswers);
			int wrongTextX = CHART_PADDING + correctWidth + (wrongWidth - fm.stringWidth(wrongText)) / 2;
			g2d.setColor(Color.WHITE);
			g2d.drawString(wrongText, wrongTextX, textY);
		}

		g2d.setColor(LABEL_COLOR);
		g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		FontMetrics smallFm = g2d.getFontMetrics();

		String totalText = "Gesamt: " + totalAnswers;
		double percentage = totalAnswers > 0 ? (double) correctAnswers / totalAnswers * 100 : 0;
		String percentageText = String.format("(%.1f%% richtig)", percentage);

		int totalTextX = CHART_PADDING;
		int percentageTextX = getWidth() - CHART_PADDING - smallFm.stringWidth(percentageText);
		int textAboveY = barY - 5;

		g2d.drawString(totalText, totalTextX, textAboveY);
		g2d.drawString(percentageText, percentageTextX, textAboveY);
	}

	/**
	 * Draws the legend below the bar.
	 */
	private void drawLegend(Graphics2D g2d, int legendY) {
		g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		FontMetrics fm = g2d.getFontMetrics();

		int boxSize = 12;
		int legendSpacing = 20;

		String correctLegend = "Richtig (" + correctAnswers + ")";
		String wrongLegend = "Falsch (" + wrongAnswers + ")";
		int totalLegendWidth = boxSize + 5 + fm.stringWidth(correctLegend) + legendSpacing + boxSize + 5
				+ fm.stringWidth(wrongLegend);
		int legendX = (getWidth() - totalLegendWidth) / 2;

		g2d.setColor(SUCCESS_COLOR);
		g2d.fillRect(legendX, legendY, boxSize, boxSize);
		g2d.setColor(SUCCESS_COLOR.darker());
		g2d.drawRect(legendX, legendY, boxSize, boxSize);

		g2d.setColor(LABEL_COLOR);
		g2d.drawString(correctLegend, legendX + boxSize + 5, legendY + boxSize);

		int wrongLegendX = legendX + boxSize + 5 + fm.stringWidth(correctLegend) + legendSpacing;
		g2d.setColor(ERROR_COLOR);
		g2d.fillRect(wrongLegendX, legendY, boxSize, boxSize);
		g2d.setColor(ERROR_COLOR.darker());
		g2d.drawRect(wrongLegendX, legendY, boxSize, boxSize);

		g2d.setColor(LABEL_COLOR);
		g2d.drawString(wrongLegend, wrongLegendX + boxSize + 5, legendY + boxSize);
	}

	/**
	 * Draws an empty chart with a message when no data is available.
	 */
	private void drawEmptyChart(Graphics2D g2d) {
		g2d.setFont(DEFAULT_FONT);
		g2d.setColor(LABEL_COLOR.brighter());

		String message = "Noch keine Daten verfügbar";
		FontMetrics fm = g2d.getFontMetrics();
		int messageX = (getWidth() - fm.stringWidth(message)) / 2;
		int messageY = getHeight() / 2;

		g2d.drawString(message, messageX, messageY);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, CHART_PADDING * 2 + TITLE_HEIGHT + BAR_HEIGHT + LEGEND_HEIGHT + 20);
	}
}
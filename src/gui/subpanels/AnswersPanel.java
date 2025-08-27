package gui.subpanels;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.GUIConstants;
import quizlogic.dto.AnswerDTO;

/**
 * Panel displaying all possible answers as individual answer rows. Contains
 * multiple AnswerRowPanel components and updates them according to the current
 * question.
 */
public class AnswersPanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;

	private final AnswerRowPanel[] answerRows = new AnswerRowPanel[GUIConstants.ANSWERS_COUNT];
	private boolean showingCorrectAnswers = false;

	public AnswersPanel() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Header
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, PANEL_MARGIN_H, 0));
		headerPanel.setBackground(BACKGROUND_COLOR);
		JLabel answerLabel = new JLabel(GUIConstants.POSSIBLE_ANSWERS_LABEL);
		answerLabel.setPreferredSize(new java.awt.Dimension(ANSWER_LABEL_WIDTH, ANSWER_LABEL_HEIGHT));
		JLabel correctLabel = new JLabel(GUIConstants.ANSWER_LABEL);
		headerPanel.add(answerLabel);
		headerPanel.add(correctLabel);
		add(headerPanel);

		// Answer rows
		for (int i = 0; i < GUIConstants.ANSWERS_COUNT; i++) {
			answerRows[i] = new AnswerRowPanel(i + 1);
			add(answerRows[i]);
		}
	}

	public void setAnswers(List<AnswerDTO> answers) {
		setAnswers(answers, false); // Default to not showing correct answers
	}

	public void setAnswers(List<AnswerDTO> answers, boolean showCorrectAnswers) {
		clearAnswers();
		this.showingCorrectAnswers = showCorrectAnswers;
		if (answers == null)
			return;

		for (int i = 0; i < Math.min(answers.size(), answerRows.length); i++) {
			AnswerDTO answer = answers.get(i);
			// Show correct answers only if in edit mode (QuizFragen tab)
			answerRows[i].setAnswer(answer.getAnswerText(), showCorrectAnswers ? answer.isCorrect() : false);
			answerRows[i].setVisible(true); // Make sure the row is visible
		}

		// Hide unused rows
		for (int i = answers.size(); i < answerRows.length; i++) {
			answerRows[i].setVisible(false);
		}
		
		// Ensure panel is refreshed
		revalidate();
		repaint();
	}

	public void clearAnswers() {
		for (AnswerRowPanel row : answerRows) {
			row.setAnswer("", false);
			row.setVisible(false);
		}
	}

	/**
	 * Prepares the panel for creating a new question by showing empty answer fields.
	 */
	public void prepareForNewQuestion() {
		for (int i = 0; i < answerRows.length; i++) {
			answerRows[i].setAnswer("", false);
			answerRows[i].setVisible(true); // Show all rows for new question creation
		}
		
		// Ensure panel is refreshed
		revalidate();
		repaint();
	}

	public String getCorrectAnswerText() {
		// Find the first correct answer to display
		for (AnswerRowPanel row : answerRows) {
			if (row.isVisible() && row.isCorrect()) {
				return row.getAnswerText();
			}
		}
		return "Keine korrekte Antwort gefunden";
	}

	public void showCorrectAnswers(List<AnswerDTO> answers) {
		if (answers == null)
			return;

		for (int i = 0; i < Math.min(answers.size(), answerRows.length); i++) {
			AnswerDTO answer = answers.get(i);
			answerRows[i].setAnswer(answer.getAnswerText(), answer.isCorrect());
		}
	}

	public AnswerRowPanel[] getAnswerRows() {
		return answerRows;
	}

	public List<Integer> getSelectedAnswerIndices() {
		List<Integer> selectedIndices = new java.util.ArrayList<>();
		for (int i = 0; i < answerRows.length; i++) {
			if (answerRows[i].isVisible() && answerRows[i].getCheckBox().isSelected()) {
				selectedIndices.add(i);
			}
		}
		return selectedIndices;
	}
}
package gui.subpanels;

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

	public AnswersPanel() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		headerPanel.setBackground(BACKGROUND_COLOR);

		headerPanel.add(javax.swing.Box.createHorizontalStrut(PANEL_MARGIN_H));

		JLabel answerLabel = new JLabel(GUIConstants.POSSIBLE_ANSWERS_LABEL);
		answerLabel.setFont(answerLabel.getFont().deriveFont(java.awt.Font.BOLD));
		headerPanel.add(answerLabel);

		headerPanel.add(javax.swing.Box.createHorizontalStrut(HORIZONTAL_STRUT_VERY_LARGE));

		JLabel correctLabel = new JLabel(GUIConstants.ANSWER_LABEL);
		correctLabel.setFont(correctLabel.getFont().deriveFont(java.awt.Font.BOLD));
		headerPanel.add(correctLabel);

		headerPanel.add(javax.swing.Box.createHorizontalStrut(HORIZONTAL_STRUT_LARGE));

		headerPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, ANSWER_LABEL_HEIGHT + 2));

		add(headerPanel);

		add(javax.swing.Box.createVerticalStrut(VERTICAL_STRUT_MEDIUM));

		for (int i = 0; i < GUIConstants.ANSWERS_COUNT; i++) {
			answerRows[i] = new AnswerRowPanel(i + 1);
			add(answerRows[i]);

			if (i < GUIConstants.ANSWERS_COUNT - 1) {
				add(javax.swing.Box.createVerticalStrut(VERTICAL_STRUT_SMALL));
			}
		}

		setPreferredSize(new java.awt.Dimension(LEFT_PANEL_WIDTH - 20, 280));
		setMaximumSize(new java.awt.Dimension(LEFT_PANEL_WIDTH, 320));
	}

	public void setAnswers(List<AnswerDTO> answers) {
		setAnswers(answers, false);
	}

	public void setAnswers(List<AnswerDTO> answers, boolean showCorrectAnswers) {
		clearAnswers();
		if (answers == null)
			return;

		for (int i = 0; i < Math.min(answers.size(), answerRows.length); i++) {
			AnswerDTO answer = answers.get(i);

			answerRows[i].setAnswer(answer.getAnswerText(), showCorrectAnswers ? answer.isCorrect() : false);
			answerRows[i].setVisible(true);
		}

		for (int i = answers.size(); i < answerRows.length; i++) {
			answerRows[i].setVisible(false);
		}

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
	 * Prepares the panel for creating a new question by showing empty answer
	 * fields.
	 */
	public void prepareForNewQuestion() {
		for (int i = 0; i < answerRows.length; i++) {
			answerRows[i].setAnswer("", false);
			answerRows[i].setVisible(true);
		}

		revalidate();
		repaint();
	}

	public String getCorrectAnswerText() {

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
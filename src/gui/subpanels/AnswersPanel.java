package gui.subpanels;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.GUIConstants;

/**
 * Panel displaying all possible answers as individual answer rows.
 * <p>
 * Contains multiple {@link AnswerRowPanel} components and updates them
 * according to the current question. Used to show the answer options and
 * indicate which answers are marked as correct.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class AnswersPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private final AnswerRowPanel[] answerRows = new AnswerRowPanel[GUIConstants.ANSWERS_COUNT];

	/**
	 * Constructs the AnswersPanel and initializes all answer rows.
	 */
	public AnswersPanel() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, PANEL_MARGIN_H, 0));
		headerPanel.setBackground(BACKGROUND_COLOR);

		JLabel answerLabel = new JLabel(GUIConstants.POSSIBLE_ANSWERS_LABEL);
		answerLabel.setPreferredSize(new java.awt.Dimension(ANSWER_LABEL_WIDTH, ANSWER_LABEL_HEIGHT));
		JLabel correctLabel = new JLabel(GUIConstants.ANSWER_LABEL);

		headerPanel.add(answerLabel);
		headerPanel.add(correctLabel);

		add(headerPanel);

		for (int i = 0; i < GUIConstants.ANSWERS_COUNT; i++) {
			answerRows[i] = new AnswerRowPanel(i + 1);
			add(answerRows[i]);
		}
	}

	/**
	 * Sets the list of answers to be displayed in each row.
	 * 
	 * @param answers List of answers to display; must match the number of answer
	 *                rows
	 */
	public void setAnswers(List<quizlogic.dto.AnswerDTO> answers) {
		for (int i = 0; i < answerRows.length; i++) {
			if (answers != null && i < answers.size() && answers.get(i) != null) {
				answerRows[i].setAnswer(answers.get(i).getAnswerText(),
						Boolean.TRUE.equals(answers.get(i).isCorrect()));
			} else {
				answerRows[i].setAnswer("", false);
			}
		}
	}

	/**
	 * Returns the array of all answer rows.
	 * 
	 * @return Array of {@link AnswerRowPanel}
	 */
	public AnswerRowPanel[] getAnswerRows() {
		return answerRows;
	}

	/**
	 * Gets the text of the answer marked as correct.
	 * 
	 * @return The correct answer text, or null if none is marked correct
	 */
	public String getCorrectAnswerText() {
		for (AnswerRowPanel row : answerRows) {
			if (row.isCorrect()) {
				return row.getAnswerText();
			}
		}
		return null;
	}

	/**
	 * Clears all answers from the panel, resetting text and unchecking boxes.
	 */
	public void clearAnswers() {
		for (AnswerRowPanel row : answerRows) {
			row.setAnswer("", false);
		}
	}
}

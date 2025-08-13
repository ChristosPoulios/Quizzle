package gui.subpanels;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.interfaces.GUIConstants;

/**
 * Panel displaying all possible answers as rows.
 * 
 * Contains multiple AnswerRowPanels and updates them according to the current
 * question. Used to show and indicate which answer(s) are marked as correct.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class AnswersPanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;
	private final AnswerRowPanel[] answerRows = new AnswerRowPanel[GUIConstants.ANSWERS_COUNT];

	/**
	 * Constructs the AnswersPanel, initializing all AnswerRowPanels.
	 */
	public AnswersPanel() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, PANEL_MARGIN_H, 0));
		headerPanel.setBackground(BACKGROUND_COLOR);

		JLabel answerLabel = new JLabel(GUIConstants.POSSIBLE_ANSWERS_LABEL);
		answerLabel.setPreferredSize(new java.awt.Dimension(250, 20));
		answerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel correctLabel = new JLabel(GUIConstants.ANSWER_LABEL);
		correctLabel.setPreferredSize(new java.awt.Dimension(100, 20));
		
		headerPanel.add(answerLabel);
		headerPanel.add(correctLabel);
		add(headerPanel);
		
		for (int i = 0; i < GUIConstants.ANSWERS_COUNT; i++) {
			answerRows[i] = new AnswerRowPanel(i + 1);
			add(answerRows[i]);
		}
	}

	/**
	 * Set the list of answers for all rows (used when displaying a new question).
	 * 
	 * @param answers List of answers (must match ANSWERS_COUNT)
	 */
	public void setAnswers(List<quizlogic.dto.AnswerDTO> answers) {
		for (int i = 0; i < answerRows.length; i++) {
			if (answers != null && i < answers.size() && answers.get(i) != null) {
				answerRows[i].setAnswer(answers.get(i).getAnswerText(), Boolean.TRUE.equals(answers.get(i).isCorrect()));
			} else {
				answerRows[i].setAnswer("", false);
			}
		}
	}

	/**
	 * Gets all AnswerRowPanels.
	 * 
	 * @return Array of AnswerRowPanel
	 */
	public AnswerRowPanel[] getAnswerRows() {
		return answerRows;
	}

	/**
	 * Gets the answer text for the row marked as correct.
	 * 
	 * @return The correct answer text, or null if none marked correct
	 */
	public String getCorrectAnswerText() {
		for (AnswerRowPanel row : answerRows) {
			if (row.isCorrect()) {
				return row.getAnswerText();
			}
		}
		return null;
	}
}

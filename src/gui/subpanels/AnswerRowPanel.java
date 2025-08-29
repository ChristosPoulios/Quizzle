package gui.subpanels;

import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import constants.GUIConstants;

/**
 * Panel representing a single answer option row in the answer section of quiz questions.
 * <p>
 * This panel provides a complete interface for displaying and interacting with individual
 * answer options, including:
 * <ul>
 * <li>Answer text display and editing capabilities</li>
 * <li>Correctness indicator (checkbox) for marking correct answers</li>
 * <li>Visual feedback for user selection and answer validation</li>
 * <li>Support for both quiz-taking and question-editing modes</li>
 * <li>Responsive layout that adapts to different content lengths</li>
 * </ul>
 * <p>
 * The panel consists of three main components arranged horizontally:
 * <ul>
 * <li><strong>Answer Label:</strong> Displays the answer identifier (A, B, C, D, etc.)</li>
 * <li><strong>Answer Text Field:</strong> Shows the answer content text</li>
 * <li><strong>Correctness Checkbox:</strong> Indicates/allows selection of correct answers</li>
 * </ul>
 * <p>
 * <strong>Usage Modes:</strong>
 * <ul>
 * <li><strong>Quiz Mode:</strong> Text field is read-only, checkbox is used for user answer selection</li>
 * <li><strong>Edit Mode:</strong> Text field is editable, checkbox marks the correct answer(s)</li>
 * <li><strong>Review Mode:</strong> Shows both user selections and correct answers with visual feedback</li>
 * </ul>
 * <p>
 * The panel automatically handles visual styling and feedback based on the current mode
 * and selection state, providing clear visual cues to users about their choices and
 * the correctness of answers.
 * <p>
 * This component is typically used as part of an {@link AnswersPanel} collection to
 * display all answer options for a quiz question in a consistent and organized manner.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 * @see AnswersPanel
 * @see QuestionPanel
 * @see quizlogic.dto.AnswerDTO
 */
public class AnswerRowPanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;

	private final JLabel answerLabel;
	private final JTextField answerTextField;
	private final JCheckBox checkBox;

	public AnswerRowPanel(int number) {
		setBackground(BACKGROUND_COLOR);
		setLayout(new FlowLayout(FlowLayout.CENTER, PANEL_MARGIN_H, 0));

		answerLabel = new JLabel(GUIConstants.ANSWER_PREFIX + number);
		answerLabel.setFont(answerLabel.getFont().deriveFont(java.awt.Font.BOLD));
		answerTextField = new JTextField("", GUIConstants.TEXTFIELD_COLUMNS);
		answerTextField.setEditable(true);
		answerTextField.setPreferredSize(
				new java.awt.Dimension(LEFT_PANEL_WIDTH - 150, answerTextField.getPreferredSize().height));

		checkBox = new JCheckBox();
		checkBox.setBackground(BACKGROUND_COLOR);

		add(answerLabel);
		add(answerTextField);
		add(checkBox);

		setPreferredSize(new java.awt.Dimension(LEFT_PANEL_WIDTH - 20, 35));
		setMaximumSize(new java.awt.Dimension(LEFT_PANEL_WIDTH, 40));
	}

	public void setAnswer(String text, boolean correct) {
		answerTextField.setText(text == null ? "" : text);
		checkBox.setSelected(correct);
	}

	public JTextField getTextField() {
		return answerTextField;
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public boolean isCorrect() {
		return checkBox.isSelected();
	}

	public String getAnswerText() {
		return answerTextField.getText() == null ? "" : answerTextField.getText();
	}
}
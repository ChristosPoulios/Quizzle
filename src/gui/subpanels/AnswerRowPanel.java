package gui.subpanels;

import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.interfaces.GUIConstants;

/**
 * Panel representing a single answer option row in the answer section.
 * 
 * Displays the answer label, answer text field (read-only), and a checkbox
 * indicating correctness. Used within the AnswersPanel to build a full list of
 * answer options for a question.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class AnswerRowPanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;

	private final JLabel answerLabel;
	private final JTextField answerTextField;
	private final JCheckBox box;

	/**
	 * Constructs an AnswerRowPanel for the given answer number.
	 * 
	 * @param number The answer number/index (for label)
	 */
	public AnswerRowPanel(int number) {
		setBackground(BACKGROUND_COLOR);
		setLayout(new FlowLayout(FlowLayout.CENTER, PANEL_MARGIN_H, 0));
		answerLabel = new JLabel(GUIConstants.ANSWER_PREFIX + number);
		answerTextField = new JTextField("", GUIConstants.TEXTFIELD_COLUMNS);
		answerTextField.setEditable(false);
		box = new JCheckBox();
		box.setBackground(BACKGROUND_COLOR);
		add(answerLabel);
		add(answerTextField);
		add(box);
	}

	/**
	 * Sets the answer text and correctness state for this row.
	 * 
	 * @param text    The answer text
	 * @param correct true if correct, false otherwise
	 */
	public void setAnswer(String text, boolean correct) {
		answerTextField.setText(text == null ? "" : text);
		box.setSelected(correct);
	}

	/**
	 * Returns the answer text field.
	 * 
	 * @return The JTextField for answer text (read-only)
	 */
	public JTextField getTextField() {
		return answerTextField;
	}

	/**
	 * Returns the checkbox indicating correctness.
	 * 
	 * @return The JCheckBox for marking correctness
	 */
	public JCheckBox getCheckBox() {
		return box;
	}

	/**
	 * Whether this answer row is marked as correct.
	 * 
	 * @return true if correct, false otherwise
	 */
	public boolean isCorrect() {
		return box.isSelected();
	}

	/**
	 * Gets the answer text.
	 * 
	 * @return The string value from the text field
	 */
	public String getAnswerText() {
		return answerTextField.getText() == null ? "" : answerTextField.getText();
	}
}

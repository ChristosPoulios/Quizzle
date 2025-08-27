package gui.subpanels;

import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import constants.GUIConstants;

/**
 * Panel representing a single answer option row in the answer section. Displays
 * the answer label, answer text field, and a checkbox for selection.
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
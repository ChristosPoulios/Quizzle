package gui.subpanels;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import constants.GUIConstants;

/**
 * Panel for displaying a question's meta-information including the title and
 * main question text.
 * <p>
 * Consists of a title field and a read-only text area for displaying the
 * question content.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuestionMetaPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private final JTextField titleField;

	private final JTextArea questionTextArea;

	/**
	 * Constructs the metadata panel for a quiz question.
	 */
	public QuestionMetaPanel() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(Box.createVerticalStrut(VERTICAL_STRUT_MEDIUM));

		JLabel titleLabel = new JLabel(QUESTION_TITLE_LABEL);
		titleLabel.setFont(titleLabel.getFont().deriveFont(java.awt.Font.BOLD));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(titleLabel);

		titleField = new JTextField("", TEXTFIELD_COLUMNS);
		titleField.setEditable(false);
		titleField
				.setPreferredSize(new java.awt.Dimension(LEFT_PANEL_WIDTH - 100, titleField.getPreferredSize().height));
		titleField.setMaximumSize(new java.awt.Dimension(LEFT_PANEL_WIDTH - 100, titleField.getPreferredSize().height));
		titleField.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(titleField);

		add(Box.createVerticalStrut(VERTICAL_STRUT_VERY_LARGE));

		JLabel questionLabel = new JLabel(QUESTION_TEXT_LABEL);
		questionLabel.setFont(questionLabel.getFont().deriveFont(java.awt.Font.BOLD));
		questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(questionLabel);

		questionTextArea = new JTextArea("", QUESTIONAREA_ROWS, QUESTIONAREA_COLUMNS);
		questionTextArea.setBackground(QUESTION_TEXT_AREA);
		questionTextArea.setLineWrap(true);
		questionTextArea.setWrapStyleWord(true);
		questionTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(questionTextArea);
		scrollPane.setPreferredSize(new java.awt.Dimension(LEFT_PANEL_WIDTH - 80, 120));
		scrollPane.setMaximumSize(new java.awt.Dimension(LEFT_PANEL_WIDTH - 80, 120));
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(scrollPane);

		setPreferredSize(new java.awt.Dimension(LEFT_PANEL_WIDTH - 20, 180));
		setMaximumSize(new java.awt.Dimension(LEFT_PANEL_WIDTH, 200));
	}

	/**
	 * Sets the question title text.
	 * 
	 * @param title Question title to display
	 */
	public void setTitle(String title) {
		titleField.setText(title == null ? "" : title);
	}

	/**
	 * Sets the question full text content.
	 * 
	 * @param text The question text to display
	 */
	public void setQuestionText(String text) {
		questionTextArea.setText(text == null ? "" : text);
	}

	/**
	 * Returns the title text field component.
	 * 
	 * @return The {@link JTextField} containing the question title
	 */
	public JTextField getTitleField() {
		return titleField;
	}

	/**
	 * Returns the question text area component.
	 * 
	 * @return The {@link JTextArea} containing the question content
	 */
	public JTextArea getQuestionTextArea() {
		return questionTextArea;
	}
}
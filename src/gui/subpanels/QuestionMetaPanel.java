package gui.subpanels;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gui.interfaces.GUIConstants;

/**
 * Panel for displaying a question's meta-information (title and main text).
 * 
 * Consists of a title field and a read-only text area for the question itself.
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

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.setBackground(BACKGROUND_COLOR);

		JLabel titleLabel = new JLabel("Titel");
		titleField = new JTextField("", GUIConstants.TEXTFIELD_COLUMNS);
		titleField.setEditable(false);

		titlePanel.add(titleLabel);
		titlePanel.add(titleField);

		JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		questionPanel.setBackground(BACKGROUND_COLOR);

		JLabel questionLabel = new JLabel("Frage");
		questionTextArea = new JTextArea("", GUIConstants.QUESTIONAREA_ROWS, GUIConstants.QUESTIONAREA_COLUMNS);
		questionTextArea.setBackground(GUIConstants.QUESTION_TEXT_AREA);
		questionTextArea.setLineWrap(true);
		questionTextArea.setWrapStyleWord(true);
		questionTextArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(questionTextArea);
		questionPanel.add(questionLabel);
		questionPanel.add(scrollPane);

		add(titlePanel);
		add(questionPanel);
	}

	/**
	 * Sets the question title field.
	 * 
	 * @param title Question title
	 */
	public void setTitle(String title) {
		titleField.setText(title == null ? "" : title);
	}

	/**
	 * Sets the question text area.
	 * 
	 * @param text Full question text
	 */
	public void setQuestionText(String text) {
		questionTextArea.setText(text == null ? "" : text);
	}

	/**
	 * Gets the title text field for further customization.
	 * 
	 * @return JTextField with title
	 */
	public JTextField getTitleField() {
		return titleField;
	}

	/**
	 * Gets the question text area field for further customization.
	 * 
	 * @return JTextArea with question content
	 */
	public JTextArea getQuestionTextArea() {
		return questionTextArea;
	}
}

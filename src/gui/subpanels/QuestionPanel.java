package gui.subpanels;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.interfaces.GUIConstants;

import quizlogic.dto.QuestionDTO;

/**
 * Main panel for displaying a quiz question, including theme header, meta
 * information, and answers.
 * <p>
 * MessagePanel has been moved to button panels for centralized message
 * handling.
 * </p>
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class QuestionPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private final ThemeHeaderPanel headerPanel;

	private final QuestionMetaPanel metaPanel;

	private final AnswersPanel answersPanel;

	/**
	 * Constructs the main question display panel and its subpanels.
	 */
	public QuestionPanel() {
		setLayout(new BorderLayout(PANEL_MARGIN_H, PANEL_MARGIN_V));
		setBackground(BACKGROUND_COLOR);

		headerPanel = new ThemeHeaderPanel();
		metaPanel = new QuestionMetaPanel();
		answersPanel = new AnswersPanel();

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(BACKGROUND_COLOR);

		centerPanel.add(metaPanel);
		centerPanel.add(answersPanel);

		add(headerPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
	}

	/**
	 * Fills all subpanels with the given question's data.
	 * 
	 * @param question The quiz question to display
	 */
	public void fillWithQuestionData(QuestionDTO question) {
		if (question == null) {
			headerPanel.clearTheme();
			metaPanel.getTitleField().setText("");
			metaPanel.getQuestionTextArea().setText("");
			answersPanel.clearAnswers();
			return;
		}

		if (question.getText() != null) {
			metaPanel.getQuestionTextArea().setText(question.getText());
		} else {
			metaPanel.getQuestionTextArea().setText("");
		}

		metaPanel.setTitle(question.getQuestionTitle());
		metaPanel.setQuestionText(question.getText());
		answersPanel.setAnswers(question.getAnswers());
		revalidate();
		repaint();
	}

	/**
	 * Fills the panel with question data and sets the theme in the header.
	 * 
	 * @param question  The question to display
	 * @param themeName The name of the theme to display in header
	 */
	public void fillWithQuestionData(QuestionDTO question, String themeName) {
		fillWithQuestionData(question);
		if (themeName != null && !themeName.trim().isEmpty()) {
			headerPanel.setTheme(themeName);
		} else {
			headerPanel.clearTheme();
		}
	}

	/**
	 * Sets the editable state of all input fields in the panel.
	 * 
	 * @param editable true to enable editing, false to disable
	 */
	public void setEditable(boolean editable) {
		metaPanel.getTitleField().setEditable(editable);
		metaPanel.getQuestionTextArea().setEditable(editable);

		for (AnswerRowPanel row : answersPanel.getAnswerRows()) {
			row.getTextField().setEditable(editable);
			row.getCheckBox().setEnabled(editable);
		}
	}

	/**
	 * Returns the theme header panel.
	 * 
	 * @return The ThemeHeaderPanel instance
	 */
	public ThemeHeaderPanel getHeaderPanel() {
		return headerPanel;
	}

	/**
	 * Returns the question meta info panel.
	 * 
	 * @return The QuestionMetaPanel instance
	 */
	public QuestionMetaPanel getMetaPanel() {
		return metaPanel;
	}

	/**
	 * Returns the answers display panel.
	 * 
	 * @return The AnswersPanel instance
	 */
	public AnswersPanel getAnswersPanel() {
		return answersPanel;
	}
}

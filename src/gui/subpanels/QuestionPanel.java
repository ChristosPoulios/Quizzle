package gui.subpanels;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import gui.interfaces.GUIConstants;
import quizlogic.dto.QuestionDTO;

/**
 * Main panel for displaying a quiz question, including theme, meta info and
 * answers.
 * 
 * MessagePanel has been moved to button panels for centralized message
 * handling.
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
	 * Gets the theme header panel.
	 *
	 * @return ThemeHeaderPanel instance
	 */
	public ThemeHeaderPanel getHeaderPanel() {
		return headerPanel;
	}

	/**
	 * Gets the question meta info panel.
	 *
	 * @return QuestionMetaPanel instance
	 */
	public QuestionMetaPanel getMetaPanel() {
		return metaPanel;
	}

	/**
	 * Gets the answers display panel.
	 *
	 * @return AnswersPanel instance
	 */
	public AnswersPanel getAnswersPanel() {
		return answersPanel;
	}
}

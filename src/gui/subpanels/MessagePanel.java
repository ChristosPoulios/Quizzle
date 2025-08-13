package gui.subpanels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import gui.interfaces.GUIConstants;

/**
 * Panel for displaying messages or notifications to the user at the bottom of
 * the question view. Uses a non-editable JTextArea to show status or info about
 * the current question.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class MessagePanel extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1L;
	private final JTextArea messageArea;

	/**
	 * Constructs the MessagePanel with default settings.
	 */
	public MessagePanel() {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(0, 0));

		messageArea = new JTextArea(GUIConstants.MESSAGE_DEFAULT, 1, GUIConstants.MSG_TEXTFIELD_COLUMNS);
		messageArea.setBackground(BACKGROUND_COLOR);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		messageArea.setEditable(false);

		add(messageArea, BorderLayout.CENTER);
	}

	/**
	 * Set a new message to display to the user.
	 * 
	 * @param msg The message string
	 */
	public void setMessage(String msg) {
		messageArea.setText(msg == null ? "" : msg);
	}

	/**
	 * Get the JTextArea displaying messages.
	 * 
	 * @return The message area component
	 */
	public JTextArea getMessageArea() {
		return messageArea;
	}
}

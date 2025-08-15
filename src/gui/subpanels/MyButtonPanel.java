package gui.subpanels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import gui.interfaces.GUIConstants;

/**
 * Panel containing control buttons with integrated message display.
 * <p>
 * Features a {@link MessagePanel} above the buttons that spans the full width.
 * Used as a base class for all button panels in the application.
 * </p>
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class MyButtonPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private JButton btn1, btn2, btn3;
	private MessagePanel messagePanel;

	/**
	 * Constructs the button panel with default (empty) button labels.
	 */
	public MyButtonPanel() {
		this("", "", "");
	}

	/**
	 * Constructs the button panel with custom button labels and integrated message
	 * panel.
	 * 
	 * @param btnName1 Name for the first button
	 * @param btnName2 Name for the second button
	 * @param btnName3 Name for the third button
	 */
	public MyButtonPanel(String btnName1, String btnName2, String btnName3) {
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(0, PANEL_MARGIN_V));

		messagePanel = new MessagePanel();
		messagePanel.setBorder(BorderFactory.createEmptyBorder(BORDER_RIGHT, BORDER_LEFT, BORDER_RIGHT, BORDER_RIGHT));
		add(messagePanel, BorderLayout.NORTH);

		JPanel buttonContainer = new JPanel();
		buttonContainer.setBackground(BACKGROUND_COLOR);
		buttonContainer.setLayout(new GridLayout(1, 3, BUTTON_PANEL_GAP, 0));

		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftPanel.setBackground(BACKGROUND_COLOR);
		btn1 = new JButton(btnName1);
		leftPanel.add(btn1);

		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerPanel.setBackground(BACKGROUND_COLOR);
		btn2 = new JButton(btnName2);
		centerPanel.add(btn2);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.setBackground(BACKGROUND_COLOR);
		btn3 = new JButton(btnName3);
		rightPanel.add(btn3);

		buttonContainer.add(leftPanel);
		buttonContainer.add(centerPanel);
		buttonContainer.add(rightPanel);

		add(buttonContainer, BorderLayout.CENTER);
	}

	/**
	 * Returns the first button component.
	 * 
	 * @return The first {@link JButton}
	 */
	public JButton getButton1() {
		return btn1;
	}

	/**
	 * Returns the second button component.
	 * 
	 * @return The second {@link JButton}
	 */
	public JButton getButton2() {
		return btn2;
	}

	/**
	 * Returns the third button component.
	 * 
	 * @return The third {@link JButton}
	 */
	public JButton getButton3() {
		return btn3;
	}

	/**
	 * Returns the integrated message panel for displaying messages over the
	 * buttons.
	 * 
	 * @return The {@link MessagePanel} instance
	 */
	public MessagePanel getMessagePanel() {
		return messagePanel;
	}

	/**
	 * Convenience method to set a message to the integrated message panel.
	 * 
	 * @param message The message string to display
	 */
	public void setMessage(String message) {
		if (messagePanel != null) {
			messagePanel.setMessage(message);
		}
	}
}

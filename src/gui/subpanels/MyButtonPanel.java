package gui.subpanels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import gui.interfaces.GUIConstants;

/**
 * Panel containing control buttons with integrated message display.
 * 
 * Features a MessagePanel above the buttons that spans the full width.
 * Used as base class for all button panels in the application.
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
     * Constructs the ButtonPanel with default button labels.
     */
    public MyButtonPanel() {
        this("", "", "");
    }

    /**
     * Constructs the ButtonPanel with custom button names and integrated MessagePanel.
     *
     * @param btnName1 Name for the first button
     * @param btnName2 Name for the second button 
     * @param btnName3 Name for the third button
     */
    public MyButtonPanel(String btnName1, String btnName2, String btnName3) {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, PANEL_MARGIN_V));

        messagePanel = new MessagePanel();
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
     * Gets the first button.
     *
     * @return JButton instance
     */
    public JButton getButton1() {
        return btn1;
    }

    /**
     * Gets the second button.
     *
     * @return JButton instance
     */
    public JButton getButton2() {
        return btn2;
    }

    /**
     * Gets the third button.
     *
     * @return JButton instance
     */
    public JButton getButton3() {
        return btn3;
    }

    /**
     * Gets the integrated MessagePanel.
     *
     * @return MessagePanel instance
     */
    public MessagePanel getMessagePanel() {
        return messagePanel;
    }

    /**
     * Convenience method to set a message.
     *
     * @param message The message to display
     */
    public void setMessage(String message) {
        if (messagePanel != null) {
            messagePanel.setMessage(message);
        }
    }
}

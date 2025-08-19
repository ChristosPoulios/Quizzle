package gui.subpanels;

import javax.swing.JTabbedPane;

import constants.GUIConstants;

/**
 * Custom tabbed pane for the application UI.
 * <p>
 * Configures background and font for tab headers, designed to group various
 * panels under separate tabs.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 */
public class TabPane extends JTabbedPane implements GUIConstants {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the TabPane with application-specific look and feel.
	 */
	public TabPane() {
		super(JTabbedPane.TOP);
		setFont(TAB_FONT);
	}
}

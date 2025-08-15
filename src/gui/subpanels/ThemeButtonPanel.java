package gui.subpanels;

import gui.interfaces.QuizThemeDelegator;

/**
 * Button panel for theme operations with theme selection correction.
 * <p>
 * Provides buttons for deleting, saving, and adding new themes.
 * Uses delegate pattern to communicate user actions.
 * </p>
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class ThemeButtonPanel extends MyButtonPanel {

    private static final long serialVersionUID = 1L;

    private QuizThemeDelegator delegate;

    /**
     * Constructs the theme button panel with specified button labels.
     * 
     * @param btnName1 Label for the delete button
     * @param btnName2 Label for the save button
     * @param btnName3 Label for the add new theme button
     */
    public ThemeButtonPanel(String btnName1, String btnName2, String btnName3) {
        super(btnName1, btnName2, btnName3);

        getButton1().addActionListener(_ -> {
            delegate.onThemeDeleted("");
        });

        getButton2().addActionListener(_ -> {
            delegate.onThemeSaved("");
        });

        getButton3().addActionListener(_ -> {
            delegate.onNewTheme("");
        });
    }

    /**
     * Sets the delegate for handling theme button actions.
     * 
     * @param delegate The delegate to notify
     */
    public void setDelegate(QuizThemeDelegator delegate) {
        this.delegate = delegate;
    }
}

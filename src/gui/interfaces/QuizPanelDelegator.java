package gui.interfaces;

/**
 * Delegate interface for panel actions such as button clicks and list selections.
 * Used by GUI components to notify about user actions.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface QuizPanelDelegator {
    /**
     * Called when "Show Answer" button is clicked.
     */
    void onShowAnswerClicked();

    /**
     * Called when "Save Answer" button is clicked.
     */
    void onSaveAnswerClicked();

    /**
     * Called when "Next Question" button is clicked.
     */
    void onNextQuestionClicked();

    /**
     * Called when a question is selected from the list.
     * 
     * @param entry The selected entry text
     * @param index The index of the selected question
     */
    void onQuestionSelected(String entry, int index);

    /**
     * Called when a theme is selected from the theme list.
     * 
     * @param themeTitle The selected theme's title
     */
    void onThemeSelected(String themeTitle);
}

package gui.interfaces;

/**
 * Delegate interface for actions related to quiz themes, such as deleting,
 * saving, or adding new themes.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface QuizThemeDelegator {
	void onThemeDeleted(String themeTitle);

	void onThemeSaved(String themeTitle);

	void onNewTheme(String themeTitle);
}

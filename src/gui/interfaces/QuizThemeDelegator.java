package gui.interfaces;

/**
 * Delegate interface for actions related to quiz themes, such as deleting,
 * saving, or adding new themes.
 * <p>
 * Enables communication between theme-related UI components and their
 * controlling logic.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public interface QuizThemeDelegator {

	/**
	 * Called when a theme is deleted.
	 * 
	 * @param themeTitle The title of the theme being deleted
	 */
	void onThemeDeleted(String themeTitle);

	/**
	 * Called when a theme is saved.
	 * 
	 * @param themeTitle The title of the theme being saved
	 */
	void onThemeSaved(String themeTitle);

	/**
	 * Called when a new theme is created.
	 * 
	 * @param themeTitle The title of the new theme
	 */
	void onNewTheme(String themeTitle);
}

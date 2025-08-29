package constants;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * ThemeManager handles the application's Look and Feel (theme) settings.
 * <p>
 * This class provides methods to set and change the UI theme based on
 * configuration settings. It supports various Look and Feel options including
 * Nimbus, System default, and others.
 * </p>
 * 
 * @author Generated for Quizzle Application
 * @version 1.0
 * @since 1.0
 */
public class ThemeManager {

	/** Available theme constants */
	public static final String THEME_NIMBUS = "Nimbus";
	public static final String THEME_SYSTEM = "System";
	public static final String THEME_METAL = "Metal";
	public static final String THEME_MOTIF = "Motif";
	public static final String THEME_WINDOWS = "Windows";

	/** Configuration key for default theme setting */
	public static final String THEME_DEFAULT = "theme.default";

	/**
	 * Applies the Look and Feel based on the configuration. This method should be
	 * called before creating any GUI components.
	 */
	public static void applyTheme() {
		ConfigManager config = ConfigManager.getInstance();
		String themeName = config.getProperty(THEME_DEFAULT, THEME_NIMBUS);
		applyTheme(themeName);
	}

	/**
	 * Applies the specified Look and Feel.
	 * 
	 * @param themeName the name of the theme to apply
	 * @return true if the theme was applied successfully, false otherwise
	 */
	public static boolean applyTheme(String themeName) {
		try {
			switch (themeName) {
			case THEME_NIMBUS:
				return setNimbusLookAndFeel();
			case THEME_SYSTEM:
				return setSystemLookAndFeel();
			case THEME_METAL:
				return setMetalLookAndFeel();
			case THEME_MOTIF:
				return setMotifLookAndFeel();
			case THEME_WINDOWS:
				return setWindowsLookAndFeel();
			default:
				System.err.println("Unknown theme: " + themeName + ". Falling back to Nimbus.");
				return setNimbusLookAndFeel();
			}
		} catch (Exception e) {
			System.err.println("Failed to apply theme '" + themeName + "': " + e.getMessage());
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				System.out.println("Applied system default Look and Feel as fallback.");
				return true;
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException fallbackException) {
				System.err.println("Failed to apply fallback theme: " + fallbackException.getMessage());
				return false;
			}
		}
	}

	/**
	 * Sets the Nimbus Look and Feel.
	 * 
	 * @return true if successful, false otherwise
	 */
	private static boolean setNimbusLookAndFeel() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					System.out.println("Applied Nimbus Look and Feel successfully.");
					return true;
				}
			}
			System.err.println("Nimbus Look and Feel not found. Using system default.");
			return setSystemLookAndFeel();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("Failed to set Nimbus Look and Feel: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Sets the system default Look and Feel.
	 * 
	 * @return true if successful, false otherwise
	 */
	private static boolean setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			System.out.println("Applied System Look and Feel successfully.");
			return true;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("Failed to set System Look and Feel: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Sets the Metal Look and Feel.
	 * 
	 * @return true if successful, false otherwise
	 */
	private static boolean setMetalLookAndFeel() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			System.out.println("Applied Metal Look and Feel successfully.");
			return true;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("Failed to set Metal Look and Feel: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Sets the Motif Look and Feel.
	 * 
	 * @return true if successful, false otherwise
	 */
	private static boolean setMotifLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			System.out.println("Applied Motif Look and Feel successfully.");
			return true;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("Failed to set Motif Look and Feel: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Sets the Windows Look and Feel (only available on Windows).
	 * 
	 * @return true if successful, false otherwise
	 */
	private static boolean setWindowsLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			System.out.println("Applied Windows Look and Feel successfully.");
			return true;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("Failed to set Windows Look and Feel: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Gets a list of all available Look and Feel names.
	 * 
	 * @return array of available theme names
	 */
	public static String[] getAvailableThemes() {
		UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
		String[] themes = new String[lookAndFeels.length + 1]; // +1 for "System"

		themes[0] = THEME_SYSTEM;
		for (int i = 0; i < lookAndFeels.length; i++) {
			themes[i + 1] = lookAndFeels[i].getName();
		}

		return themes;
	}

	/**
	 * Gets the currently active Look and Feel name.
	 * 
	 * @return the name of the current Look and Feel
	 */
	public static String getCurrentTheme() {
		return UIManager.getLookAndFeel().getName();
	}

	/**
	 * Changes the theme and saves it to configuration.
	 * 
	 * @param themeName the new theme name
	 * @return true if the theme was changed successfully, false otherwise
	 */
	public static boolean changeTheme(String themeName) {
		if (applyTheme(themeName)) {
			ConfigManager config = ConfigManager.getInstance();
			config.setProperty(THEME_DEFAULT, themeName);
			config.saveConfiguration();
			return true;
		}
		return false;
	}
}
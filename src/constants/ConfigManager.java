package constants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * ConfigManager handles application configuration settings.
 * <p>
 * This class manages loading and saving configuration from/to external files.
 * It supports both properties files and provides fallback to default values.
 * <p>
 * The configuration file is stored in the application's directory as
 * 'config.properties'. If no configuration file exists, default values are used
 * and a new file is created.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class ConfigManager {

	/** Singleton instance */
	private static ConfigManager instance;

	/** Properties object to store configuration */
	private Properties properties;

	/** Default configuration file name */
	private static final String CONFIG_FILE_NAME = "config.properties";

	/** Path to the configuration file */
	private Path configFilePath;

	/** Configuration keys */
	public static final String DB_URL = "database.url";
	public static final String DB_USER = "database.user";
	public static final String DB_PASSWORD = "database.password";
	public static final String DB_DRIVER = "database.driver";
	public static final String APP_TITLE = "application.title";
	public static final String APP_VERSION = "application.version";
	public static final String WINDOW_WIDTH = "window.width";
	public static final String WINDOW_HEIGHT = "window.height";
	public static final String WINDOW_MAXIMIZED = "window.maximized";
	public static final String DEBUG_MODE = "debug.enabled";

	/**
	 * Private constructor for singleton pattern.
	 */
	private ConfigManager() {
		properties = new Properties();
		configFilePath = Paths.get(CONFIG_FILE_NAME);
		loadConfiguration();
	}

	/**
	 * Returns the singleton instance of ConfigManager.
	 * 
	 * @return ConfigManager instance
	 */
	public static ConfigManager getInstance() {
		if (instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}

	/**
	 * Loads configuration from the properties file. If the file doesn't exist,
	 * creates it with default values.
	 */
	private void loadConfiguration() {
		try {
			if (Files.exists(configFilePath)) {
				try (FileInputStream fis = new FileInputStream(configFilePath.toFile())) {
					properties.load(fis);
					System.out.println("Configuration loaded from: " + configFilePath.toAbsolutePath());
				}
			} else {
				System.out.println("Configuration file not found. Creating with default values...");
				setDefaultValues();
				saveConfiguration();
			}
		} catch (IOException e) {
			System.err.println("Error loading configuration: " + e.getMessage());
			setDefaultValues();
		}
	}

	/**
	 * Sets default configuration values.
	 */
	private void setDefaultValues() {
		properties.setProperty(DB_URL, "url_to_database");
		properties.setProperty(DB_USER, "user");
		properties.setProperty(DB_PASSWORD, "password");
		properties.setProperty(DB_DRIVER, "org.mariadb.jdbc.Driver");

		properties.setProperty(APP_TITLE, "Quizzle");
		properties.setProperty(APP_VERSION, "1.0");

		properties.setProperty(WINDOW_WIDTH, "850");
		properties.setProperty(WINDOW_HEIGHT, "650");
		properties.setProperty(WINDOW_MAXIMIZED, "false");

		properties.setProperty(DEBUG_MODE, "false");
	}

	/**
	 * Saves the current configuration to the properties file.
	 * 
	 * @return true if saved successfully, false otherwise
	 */
	public boolean saveConfiguration() {
		try (FileOutputStream fos = new FileOutputStream(configFilePath.toFile())) {
			properties.store(fos, "Quizzle Application Configuration - Generated on " + new java.util.Date());
			System.out.println("Configuration saved to: " + configFilePath.toAbsolutePath());
			return true;
		} catch (IOException e) {
			System.err.println("Error saving configuration: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Gets a string property value.
	 * 
	 * @param key the property key
	 * @return the property value or null if not found
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Gets a string property value with a default fallback.
	 * 
	 * @param key          the property key
	 * @param defaultValue the default value if key is not found
	 * @return the property value or default value
	 */
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Sets a property value.
	 * 
	 * @param key   the property key
	 * @param value the property value
	 */
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	/**
	 * Gets an integer property value.
	 * 
	 * @param key          the property key
	 * @param defaultValue the default value if key is not found or invalid
	 * @return the integer property value
	 */
	public int getIntProperty(String key, int defaultValue) {
		try {
			String value = properties.getProperty(key);
			return value != null ? Integer.parseInt(value) : defaultValue;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Sets an integer property value.
	 * 
	 * @param key   the property key
	 * @param value the integer value
	 */
	public void setIntProperty(String key, int value) {
		properties.setProperty(key, String.valueOf(value));
	}

	/**
	 * Gets a boolean property value.
	 * 
	 * @param key          the property key
	 * @param defaultValue the default value if key is not found
	 * @return the boolean property value
	 */
	public boolean getBooleanProperty(String key, boolean defaultValue) {
		String value = properties.getProperty(key);
		return value != null ? Boolean.parseBoolean(value) : defaultValue;
	}

	/**
	 * Sets a boolean property value.
	 * 
	 * @param key   the property key
	 * @param value the boolean value
	 */
	public void setBooleanProperty(String key, boolean value) {
		properties.setProperty(key, String.valueOf(value));
	}

	/**
	 * Removes a property.
	 * 
	 * @param key the property key to remove
	 * @return the previous value of the key or null if not found
	 */
	public String removeProperty(String key) {
		return (String) properties.remove(key);
	}

	/**
	 * Checks if a property exists.
	 * 
	 * @param key the property key
	 * @return true if the property exists, false otherwise
	 */
	public boolean hasProperty(String key) {
		return properties.containsKey(key);
	}

	/**
	 * Reloads configuration from file.
	 */
	public void reload() {
		properties.clear();
		loadConfiguration();
	}

	/**
	 * Resets configuration to default values.
	 */
	public void resetToDefaults() {
		properties.clear();
		setDefaultValues();
	}

	/**
	 * Gets the database URL.
	 * 
	 * @return database URL
	 */
	public String getDatabaseUrl() {
		return getProperty(DB_URL);
	}

	/**
	 * Gets the database user.
	 * 
	 * @return database user
	 */
	public String getDatabaseUser() {
		return getProperty(DB_USER);
	}

	/**
	 * Gets the database password.
	 * 
	 * @return database password
	 */
	public String getDatabasePassword() {
		return getProperty(DB_PASSWORD);
	}

	/**
	 * Gets the database driver class name.
	 * 
	 * @return database driver class name
	 */
	public String getDatabaseDriver() {
		return getProperty(DB_DRIVER);
	}

	/**
	 * Gets the application title.
	 * 
	 * @return application title
	 */
	public String getApplicationTitle() {
		return getProperty(APP_TITLE);
	}

	/**
	 * Gets the application version.
	 * 
	 * @return application version
	 */
	public String getApplicationVersion() {
		return getProperty(APP_VERSION);
	}

	/**
	 * Gets the window width.
	 * 
	 * @return window width
	 */
	public int getWindowWidth() {
		return getIntProperty(WINDOW_WIDTH, 850);
	}

	/**
	 * Sets the window width.
	 * 
	 * @param width window width
	 */
	public void setWindowWidth(int width) {
		setIntProperty(WINDOW_WIDTH, width);
	}

	/**
	 * Gets the window height.
	 * 
	 * @return window height
	 */
	public int getWindowHeight() {
		return getIntProperty(WINDOW_HEIGHT, 650);
	}

	/**
	 * Sets the window height.
	 * 
	 * @param height window height
	 */
	public void setWindowHeight(int height) {
		setIntProperty(WINDOW_HEIGHT, height);
	}

	/**
	 * Checks if the window should be maximized.
	 * 
	 * @return true if maximized, false otherwise
	 */
	public boolean isWindowMaximized() {
		return getBooleanProperty(WINDOW_MAXIMIZED, false);
	}

	/**
	 * Sets whether the window should be maximized.
	 * 
	 * @param maximized true to maximize, false otherwise
	 */
	public void setWindowMaximized(boolean maximized) {
		setBooleanProperty(WINDOW_MAXIMIZED, maximized);
	}

	/**
	 * Checks if debug mode is enabled.
	 * 
	 * @return true if debug mode is enabled, false otherwise
	 */
	public boolean isDebugMode() {
		return getBooleanProperty(DEBUG_MODE, false);
	}

	/**
	 * Sets debug mode enabled or disabled.
	 * 
	 * @param enabled true to enable debug mode, false to disable
	 */
	public void setDebugMode(boolean enabled) {
		setBooleanProperty(DEBUG_MODE, enabled);
	}

	/**
	 * Prints debug message to console only if debug mode is enabled.
	 * 
	 * @param message the debug message to print
	 */
	public static void debugPrint(String message) {
		if (getInstance().isDebugMode()) {
			System.out.println(message);
		}
	}
}
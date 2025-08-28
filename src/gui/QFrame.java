package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import constants.GUIConstants;
import constants.ThemeManager;
import constants.UserStringConstants;
import gui.subpanels.TabPane;
import persistence.DataManager;

/**
 * Main application frame that contains the primary window and tabbed panels for
 * the quiz application.
 * <p>
 * Manages the lifecycle of data storage with automatic fallback from MariaDB
 * database to file-based serialization if database connection fails.
 * The main window includes tabs for managing quiz themes, quiz questions,
 * playing the quiz, and viewing statistics.
 * </p>
 * <p>
 * This frame handles UI initialization, data storage connectivity with fallback,
 * and global event dispatching for theme changes.
 * </p>
 *
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QFrame extends JFrame implements GUIConstants {

	private static final long serialVersionUID = 1L;

	/** Smart data manager with automatic fallback capability */
	private DataManager dataManager;

	/** Panel managing quiz themes */
	private QuizThemeMainPanel themeMainPanel;

	/** Panel managing quiz questions */
	private QuizQuestionMainPanel questionMainPanel;

	/** Panel managing quiz gameplay */
	private QuizMainPanel quizMainPanel;

	/**
	 * Constructs the main application window.
	 * <p>
	 * Initializes window properties, attempts to connect to the MariaDB database,
	 * and automatically falls back to file-based storage if database connection fails.
	 * Sets up tabbed subpanels for themes, questions, quiz, and statistics.
	 * </p>
	 */
	public QFrame() {
		super(WINDOW_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);

		dataManager = DataManager.getInstance();

		String storageMethod = dataManager.getStorageMethodDescription();
		System.out.println("Using storage method: " + storageMethod);

		if (!dataManager.isUsingDatabase()) {
			System.err.println("Database connection failed. The application will continue but database functionality will not work.");
			System.err.println("Please ensure:");
			System.err.println("1. MariaDB server is running");
			System.err.println("2. Database 'quizzle_db' exists");
			System.err.println("3. Check your database settings in config.properties");

			javax.swing.JOptionPane.showMessageDialog(null,
					"Datenbankverbindung fehlgeschlagen!\n\n" +
					"Die Anwendung verwendet jetzt dateibasierte Speicherung.\n" +
					"Bitte überprüfen Sie:\n\n" +
					"1. MariaDB Server läuft\n" +
					"2. Datenbank 'quizzle_db' existiert\n" +
					"3. Datenbankeinstellungen in config.properties\n\n" +
					"Alle Funktionen bleiben verfügbar.",
					"Datenbankverbindung - Fallback zu Dateispeicherung", 
					javax.swing.JOptionPane.WARNING_MESSAGE);
		}


		themeMainPanel = new QuizThemeMainPanel(dataManager);
		questionMainPanel = new QuizQuestionMainPanel(dataManager);
		quizMainPanel = new QuizMainPanel(dataManager);

		questionMainPanel.setParentFrame(this);

		themeMainPanel.setThemeChangeListener(this::onThemeChanged);

		TabPane tabPane = new TabPane();
		tabPane.addTab(UserStringConstants.TAB_QUIZ_THEMES, themeMainPanel);
		tabPane.addTab(UserStringConstants.TAB_QUIZ_QUESTIONS, questionMainPanel);
		tabPane.addTab(UserStringConstants.TAB_QUIZ, quizMainPanel);
		tabPane.addTab(UserStringConstants.TAB_STATISTICS, new JPanel());

		add(tabPane);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (dataManager != null) {
				dataManager.close();
				System.out.println("Data storage connections closed");
			}
		}));

		setVisible(true);
	}

	/**
	 * Handles theme change notifications by refreshing all panels.
	 */
	private void onThemeChanged() {
		if (questionMainPanel != null) {
			questionMainPanel.refreshThemeList();
		}

		if (quizMainPanel != null) {
			quizMainPanel.refreshThemeComboBox();
		}
	}

	/**
	 * Handles question save notifications by refreshing the quiz panel's theme combobox.
	 * This ensures that themes without descriptions appear in the combobox after questions are added.
	 */
	public void onQuestionSaved() {
		if (quizMainPanel != null) {
			quizMainPanel.refreshThemeComboBox();
		}
	}

	/**
	 * Returns the data manager instance for external access.
	 *
	 * @return the DataManager instance
	 */
	public DataManager getDataManager() {
		return dataManager;
	}

	/**
	 * Attempts to reconnect to the database if currently using file storage.
	 *
	 * @return true if successfully connected to database, false if still using files
	 */
	public boolean tryReconnectToDatabase() {
		return dataManager.tryReconnectToDatabase();
	}

	/**
	 * Main method to start the application.
	 * 
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		ThemeManager.applyTheme(ThemeManager.THEME_NIMBUS);
		new QFrame();
	}
}
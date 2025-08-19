package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import constants.GUIConstants;
import constants.UserStringConstants;
import gui.subpanels.TabPane;
import persistence.mariaDB.DBManager;

/**
 * Main application frame that contains the primary window and tabbed panels for
 * the quiz application.
 * <p>
 * Manages the lifecycle of the MariaDB database connection and centralizes
 * database access through a shared {@link DBManager} instance. The main window
 * includes tabs for managing quiz themes, quiz questions, playing the quiz, and
 * viewing statistics.
 * </p>
 * <p>
 * This frame handles UI initialization, database connectivity, and global event
 * dispatching for theme changes.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QFrame extends JFrame implements GUIConstants {

	private static final long serialVersionUID = 1L;

	/** Centralized database manager used across all quiz panels */
	private DBManager dbManager;

	/** Panel managing quiz themes */
	private QuizThemeMainPanel themeMainPanel;

	/** Panel managing quiz questions */
	private QuizQuestionMainPanel questionMainPanel;

	/** Panel managing quiz gameplay */
	private QuizMainPanel quizMainPanel;

	/**
	 * Constructs the main application window.
	 * <p>
	 * Initializes window properties, connects to the MariaDB database, and sets up
	 * tabbed subpanels for themes, questions, quiz, and statistics. Displays error
	 * dialogs if database connection cannot be established.
	 * </p>
	 */
	public QFrame() {
		super(WINDOW_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);

		dbManager = DBManager.getInstance();

		try {
			dbManager.connect();
			System.out.println("Database connection established successfully");
		} catch (RuntimeException e) {
			System.err.println("Database connection failed: " + e.getMessage());
			System.err.println("The application will continue but database functionality will not work.");
			System.err.println("Please ensure:");
			System.err.println("1. MariaDB server is running");
			System.err.println("2. Database 'quizzle_db' exists");

			javax.swing.JOptionPane.showMessageDialog(null,
					String.format(UserStringConstants.DB_CONNECTION_FAILED_PREFIX, e.getMessage())
							+ UserStringConstants.DB_CONNECTION_ERROR_MESSAGE,
					UserStringConstants.DIALOG_TITLE_DB_CONNECTION_ERROR, javax.swing.JOptionPane.WARNING_MESSAGE);

			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				if (dbManager != null && dbManager.isConnected()) {
					dbManager.disconnect();
					System.out.println("Database connection closed");
				}
			}));
		}

		themeMainPanel = new QuizThemeMainPanel(dbManager);
		questionMainPanel = new QuizQuestionMainPanel(dbManager);
		quizMainPanel = new QuizMainPanel(dbManager);

		themeMainPanel.setThemeChangeListener(this::onThemeChanged);

		TabPane tabPane = new TabPane();
		tabPane.addTab(UserStringConstants.TAB_QUIZ_THEMES, themeMainPanel);
		tabPane.addTab(UserStringConstants.TAB_QUIZ_QUESTIONS, questionMainPanel);
		tabPane.addTab(UserStringConstants.TAB_QUIZ, quizMainPanel);
		tabPane.addTab(UserStringConstants.TAB_STATISTICS, new JPanel());

		add(tabPane);

		setVisible(true);
	}

	/**
	 * Callback method invoked when quiz themes are changed. Notifies dependent
	 * panels to refresh their theme-related components.
	 */
	private void onThemeChanged() {
		if (questionMainPanel != null) {
			questionMainPanel.refreshThemeList();
			System.out.println("Theme change notification sent to all panels");
		}
	}

	/**
	 * Application entry point.
	 * 
	 * @param args Command line arguments (not used)
	 */
	public static void main(String[] args) {
		new QFrame();
	}
}
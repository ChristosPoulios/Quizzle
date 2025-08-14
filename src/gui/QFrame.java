package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import gui.interfaces.GUIConstants;
import gui.subpanels.TabPane;
import persistence.mariaDB.DBManager;

/**
 * Main application frame with centralized MariaDB data management.
 * 
 * Creates the main window with tabs for themes, questions, quiz, and
 * statistics. Provides a single DBManager instance to all panels for consistent
 * MariaDB data handling.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class QFrame extends JFrame implements GUIConstants {
	private static final long serialVersionUID = 1L;

	/** Central database manager shared across all panels */
	private DBManager dbManager;

	/** Reference to main panels for cross-tab communication */
	private QuizThemeMainPanel themeMainPanel;
	private QuizQuestionMainPanel questionMainPanel;
	private QuizMainPanel quizMainPanel;

	/**
	 * Constructs the main frame with centralized MariaDB data management. Sets up
	 * size, layout and adds tabbed panels with shared database manager.
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
			System.err.println("3. MariaDB JDBC driver (mariadb-java-client.jar) is in classpath");

			javax.swing.JOptionPane.showMessageDialog(null,
					"Failed to connect to MariaDB database.\n\n" + "Error: " + e.getMessage() + "\n\n"
							+ "Please check:\n" + "• MariaDB server is running\n" + "• Database 'quizzle_db' exists\n"
							+ "• JDBC driver is in classpath\n\n"
							+ "The application will continue with limited functionality.",
					"Database Connection Error", javax.swing.JOptionPane.WARNING_MESSAGE);
		}

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (dbManager != null && dbManager.isConnected()) {
				dbManager.disconnect();
				System.out.println("Database connection closed");
			}
		}));

		themeMainPanel = new QuizThemeMainPanel(dbManager);
		questionMainPanel = new QuizQuestionMainPanel(dbManager);
		quizMainPanel = new QuizMainPanel(dbManager);

		themeMainPanel.setThemeChangeListener(this::onThemeChanged);

		TabPane tabPane = new TabPane();
		tabPane.addTab("Quizthemen", themeMainPanel);
		tabPane.addTab("Quizfragen", questionMainPanel);
		tabPane.addTab("Quiz", quizMainPanel);
		tabPane.addTab("Statistik", new JPanel());

		add(tabPane);
		setVisible(true);
	}

	/**
	 * Called when themes are changed (added, modified, or deleted). Notifies all
	 * relevant panels to refresh their theme-related components.
	 */
	private void onThemeChanged() {

		if (questionMainPanel != null) {
			questionMainPanel.refreshThemeList();
		}

		System.out.println("Theme change notification sent to all panels");
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
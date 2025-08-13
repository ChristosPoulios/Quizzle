package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import gui.interfaces.GUIConstants;
import gui.subpanels.TabPane;
import persistence.serialization.QuizDataManager;

/**
 * Main application frame with centralized data management.
 * 
 * Creates the main window with tabs for themes, questions, quiz, and
 * statistics. Provides a single QuizDataManager instance to all panels for
 * consistent data handling.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class QFrame extends JFrame implements GUIConstants {
	private static final long serialVersionUID = 1L;

	/** Central data manager shared across all panels */
	private QuizDataManager dataManager;

	/**
	 * Constructs the main frame with centralized data management. Sets up size,
	 * layout and adds tabbed panels with shared data manager.
	 */
	public QFrame() {
		super(WINDOW_TITLE);

		dataManager = new QuizDataManager();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);

		TabPane tabPane = new TabPane();
		tabPane.addTab("Quizthemen", new QuizThemeMainPanel(dataManager));
		tabPane.addTab("Quizfragen", new QuizQuestionMainPanel(dataManager));
		tabPane.addTab("Quiz", new QuizMainPanel(dataManager));
		tabPane.addTab("Statistik", new JPanel());

		add(tabPane);
		setVisible(true);
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

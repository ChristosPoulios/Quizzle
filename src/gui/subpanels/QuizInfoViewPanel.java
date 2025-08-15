package gui.subpanels;

import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.interfaces.GUIConstants;
import persistence.mariaDB.DBManager;
import quizlogic.dto.ThemeDTO;

/**
 * Panel showing quiz statistics and information.
 * <p>
 * Displays a HTML formatted overview of all themes along with the number of
 * questions each contains.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 2.0
 */
public class QuizInfoViewPanel extends JPanel implements GUIConstants {

	private static final long serialVersionUID = 1L;

	private final DBManager dbManager;
	private final JEditorPane infoPane;

	/**
	 * Constructs the quiz info view panel with database manager integration.
	 * 
	 * @param dbManager The MariaDB database manager for retrieving statistics
	 */
	public QuizInfoViewPanel(DBManager dbManager) {
		this.dbManager = dbManager;

		setBackground(BACKGROUND_COLOR);
		setLayout(new java.awt.BorderLayout());

		infoPane = new JEditorPane();
		infoPane.setContentType("text/html");
		infoPane.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(infoPane);
		add(scrollPane, java.awt.BorderLayout.CENTER);

//		updateInfo();
	}

//	/**
//	 * Updates the info pane with current statistics and theme/question overview.
//	 */
//	public void updateInfo() {
//		StringBuilder html = new StringBuilder();
//		html.append("<html><body><h2>Themenübersicht</h2><ul>");
//
//		ArrayList<ThemeDTO> themes = dbManager.getAllThemes();
//		for (ThemeDTO theme : themes) {
//			int questionCount = dbManager.getQuestionsFor(theme).size();
//			html.append("<li><b>").append(theme.getThemeTitle()).append("</b><br>");
//			html.append(theme.getThemeDescription()).append("<br>");
//			html.append("Anzahl Fragen: ").append(questionCount).append("</li>");
//		}
//		html.append("</ul></body></html>");
//
//		infoPane.setText(html.toString());
//		infoPane.setCaretPosition(0);
//	}
}

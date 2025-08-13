package gui.subpanels;

import gui.interfaces.QuizThemeDelegator;

/**
 * Button panel for theme operations with corrected theme selection.
 * 
 * @author Christos Poulios
 * @version 2.0
 * @since 1.0
 */
public class ThemeButtonPanel extends MyButtonPanel {
	private static final long serialVersionUID = 1L;
	private QuizThemeDelegator delegate;

	public ThemeButtonPanel(String btnName1, String btnName2, String btnName3) {
		super(btnName1, btnName2, btnName3);

		getButton1().addActionListener(_ -> {

			delegate.onThemeDeleted("");
		});

		getButton2().addActionListener(_ -> {

			delegate.onThemeSaved("");
		});

		getButton3().addActionListener(_ -> {

			delegate.onNewTheme("");
		});
	}

	public void setDelegate(QuizThemeDelegator delegate) {
		this.delegate = delegate;
	}
}

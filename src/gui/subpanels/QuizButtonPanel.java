package gui.subpanels;

import gui.interfaces.QuizPanelDelegator;

/**
 * Button panel for quiz controls in the quiz tab.
 * <p>
 * Provides buttons for showing the answer, saving the answer, and proceeding to
 * the next question. Uses the delegate pattern to communicate button events to
 * listeners.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuizButtonPanel extends MyButtonPanel {

	private static final long serialVersionUID = 1L;

	private QuizPanelDelegator delegate;

	/**
	 * Constructs the QuizButtonPanel with given button names.
	 * 
	 * @param btnName1 Label for the "Show Answer" button
	 * @param btnName2 Label for the "Save Answer" button
	 * @param btnName3 Label for the "Next Question" button
	 */
	public QuizButtonPanel(String btnName1, String btnName2, String btnName3) {
		super(btnName1, btnName2, btnName3);

		getButton1().addActionListener(_ -> {
			if (delegate != null) {
				delegate.onShowAnswerClicked();
			}
		});

		getButton2().addActionListener(_ -> {
			if (delegate != null) {
				delegate.onSaveAnswerClicked();
			}
		});

		getButton3().addActionListener(_ -> {
			if (delegate != null) {
				delegate.onNextQuestionClicked();
			}
		});
	}

	/**
	 * Sets the delegate to notify when buttons are clicked.
	 * 
	 * @param delegate the delegate implementing quiz panel actions
	 */
	public void setDelegate(QuizPanelDelegator delegate) {
		this.delegate = delegate;
	}
}

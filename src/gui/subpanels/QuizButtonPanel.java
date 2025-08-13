package gui.subpanels;

import gui.interfaces.QuizPanelDelegator;

public class QuizButtonPanel extends MyButtonPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QuizPanelDelegator delegate;

	public QuizButtonPanel(String btnName1, String btnName2, String btnName3) {
		super(btnName1, btnName2, btnName3);
		getButton1().addActionListener(_ -> {

			delegate.onShowAnswerClicked();
		});
		getButton2().addActionListener(_ -> {

			delegate.onSaveAnswerClicked();
		});

		getButton3().addActionListener(_ -> {

			delegate.onNextQuestionClicked();
		});
	}

	public void setDelegate(QuizPanelDelegator delegate) {
		this.delegate = delegate;
	}
}

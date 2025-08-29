package quizlogic.dto;

import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a user's answer in a quiz session.
 * Tracks which answer was selected and whether it was correct.
 */
public class UserAnswerDTO extends DataTransportObject {
	private static final long serialVersionUID = 1L;

	private int quizSessionId;
	private int questionId;
	private int answerId;
	private boolean isSelected;
	private boolean isCorrect;

	public UserAnswerDTO() {
		super();
		this.isSelected = false;
		this.isCorrect = false;
	}

	public UserAnswerDTO(int id) {
		super(id);
		this.isSelected = false;
		this.isCorrect = false;
	}

	public UserAnswerDTO(int quizSessionId, int questionId, int answerId, boolean isSelected, boolean isCorrect) {
		super();
		this.quizSessionId = quizSessionId;
		this.questionId = questionId;
		this.answerId = answerId;
		this.isSelected = isSelected;
		this.isCorrect = isCorrect;
	}

	public int getQuizSessionId() {
		return quizSessionId;
	}

	public void setQuizSessionId(int quizSessionId) {
		this.quizSessionId = quizSessionId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean correct) {
		isCorrect = correct;
	}

	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof UserAnswerDTO))
			return false;
		UserAnswerDTO that = (UserAnswerDTO) other;
		return this.quizSessionId == that.quizSessionId && this.questionId == that.questionId
				&& this.answerId == that.answerId && this.isSelected == that.isSelected
				&& this.isCorrect == that.isCorrect;
	}

	@Override
	protected int contentHashCode() {
		return Objects.hash(quizSessionId, questionId, answerId, isSelected, isCorrect);
	}

	@Override
	protected String getContentString() {
		return String.format("sessionId=%d, questionId=%d, answerId=%d, selected=%s, correct=%s", quizSessionId,
				questionId, answerId, isSelected, isCorrect);
	}

	@Override
	protected void validate() {
		if (quizSessionId <= 0) {
			throw new IllegalArgumentException("Valid quiz session ID is required");
		}
		if (questionId <= 0) {
			throw new IllegalArgumentException("Valid question ID is required");
		}
		if (answerId <= 0) {
			throw new IllegalArgumentException("Valid answer ID is required");
		}
	}

	public int getSelectedAnswerId() {
		return answerId;
	}

	public void setSelectedAnswerId(int selectedAnswerId) {
		this.answerId = selectedAnswerId;
		this.isSelected = true;
	}
}
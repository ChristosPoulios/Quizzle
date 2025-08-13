package quizlogic.dto;

import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object for quiz answers.
 * Supports both serialization and database persistence.
 */
public class AnswerDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	private String answerText;
	private boolean correct;
	private int questionId;

	// ==================== Constructors ====================
	
	/**
	 * Default constructor
	 */
	public AnswerDTO() {
		super();
		this.correct = false;
		this.questionId = -1;
	}

	/**
	 * Constructor with ID
	 */
	public AnswerDTO(int id) {
		super(id);
		this.correct = false;
		this.questionId = -1;
	}

	/**
	 * Full constructor for creating answers
	 */
	public AnswerDTO(String answerText, boolean correct) {
		super();
		this.answerText = answerText;
		this.correct = correct;
		this.questionId = -1;
	}

	/**
	 * Complete constructor with all fields
	 */
	public AnswerDTO(int id, String answerText, boolean correct, int questionId) {
		super(id);
		this.answerText = answerText;
		this.correct = correct;
		this.questionId = questionId;
	}

	// ==================== Core Getters/Setters ====================

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	// ==================== Legacy Support Methods ====================

	/**
	 * @deprecated Use getAnswerText() instead
	 */
	@Deprecated
	public String getText() {
		return getAnswerText();
	}

	/**
	 * @deprecated Use setAnswerText() instead
	 */
	@Deprecated
	public void setText(String text) {
		setAnswerText(text);
	}

	// ==================== Utility Methods ====================

	public boolean isWrong() {
		return !correct;
	}

	public void toggleCorrect() {
		this.correct = !this.correct;
	}

	// ==================== DataTransportObject Implementation ====================

	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof AnswerDTO))
			return false;

		AnswerDTO that = (AnswerDTO) other;
		return Objects.equals(this.answerText, that.answerText) && 
		       this.correct == that.correct &&
		       this.questionId == that.questionId;
	}

	@Override
	protected int contentHashCode() {
		return Objects.hash(answerText, correct, questionId);
	}

	@Override
	protected String getContentString() {
		return String.format("answerText='%s', correct=%s, questionId=%d", 
		                     answerText, correct, questionId);
	}

	@Override
	protected void validate() {
		if (answerText == null || answerText.trim().isEmpty()) {
			throw new IllegalArgumentException("Answer text cannot be null or empty");
		}
		if (answerText.length() > 500) {
			throw new IllegalArgumentException("Answer text cannot exceed 500 characters");
		}
	}
}
package quizlogic.dto;

import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object for quiz answers. Extends DataTransportObject for
 * consistent DTO behavior.
 */
public class AnswerDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	private String answerText;
	private boolean correct;

	/**
	 * Default constructor
	 */
	public AnswerDTO() {
		super();
		this.correct = false;
	}

	/**
	 * Constructor with ID
	 */
	public AnswerDTO(int id) {
		super(id);
		this.correct = false;
	}

	/**
	 * Full constructor
	 */
	public AnswerDTO(String answerText, boolean correct) {
		super();
		this.answerText = answerText;
		this.correct = correct;
	}

	/**
	 * Constructor with ID and values
	 */
	public AnswerDTO(int id, String answerText, boolean correct) {
		super(id);
		this.answerText = answerText;
		this.correct = correct;
	}

	// ==================== Getters/Setters ====================

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

	// ==================== Legacy-Kompatibilität ====================

	/**
	 * Legacy method - delegates to getAnswerText()
	 */
	public String getText() {
		return getAnswerText();
	}

	/**
	 * Legacy method - delegates to setAnswerText()
	 */
	public void setText(String text) {
		setAnswerText(text);
	}

	// ==================== Convenience Methods ====================

	/**
	 * Checks if this answer is wrong
	 * 
	 * @return true if answer is not correct
	 */
	public boolean isWrong() {
		return !correct;
	}

	/**
	 * Toggles the correct status
	 */
	public void toggleCorrect() {
		this.correct = !this.correct;
	}

	/**
	 * Gets explanation (for now same as answer text)
	 * 
	 * @return explanation text
	 */
	public String getExplanation() {
		return answerText;
	}

	// ==================== Template Method Implementations ====================

	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof AnswerDTO))
			return false;

		AnswerDTO that = (AnswerDTO) other;
		return Objects.equals(this.answerText, that.answerText) && this.correct == that.correct;
	}

	@Override
	protected int contentHashCode() {
		return Objects.hash(answerText, correct);
	}

	@Override
	protected String getContentString() {
		return "text='" + answerText + "', correct=" + correct;
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

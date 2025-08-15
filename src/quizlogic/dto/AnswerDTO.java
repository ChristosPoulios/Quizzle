package quizlogic.dto;

import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a quiz answer.
 * <p>
 * This DTO can be serialized and persisted in storage (e.g., database). Each
 * answer contains:
 * <ul>
 * <li>The answer text</li>
 * <li>A flag indicating if it is the correct answer</li>
 * <li>The ID of the associated question</li>
 * </ul>
 * <p>
 * Implements common DTO functionality from {@link DataTransportObject}.
 *
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class AnswerDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	/** The text content of the answer */
	private String answerText;

	/** Flag indicating whether this is the correct answer */
	private boolean correct;

	/** ID of the question this answer belongs to */
	private int questionId;

	/**
	 * Default constructor for creating a new AnswerDTO. Initializes correctness to
	 * false and questionId to -1.
	 */
	public AnswerDTO() {
		super();
		this.correct = false;
		this.questionId = -1;
	}

	/**
	 * Constructor for creating an AnswerDTO for an existing persisted answer
	 * record.
	 *
	 * @param id the unique ID of the answer
	 */
	public AnswerDTO(int id) {
		super(id);
		this.correct = false;
		this.questionId = -1;
	}

	/**
	 * Constructor for creating a new answer with text and correctness.
	 *
	 * @param answerText the text content of the answer
	 * @param correct    true if this answer is correct, false otherwise
	 */
	public AnswerDTO(String answerText, boolean correct) {
		super();
		this.answerText = answerText;
		this.correct = correct;
		this.questionId = -1;
	}

	/**
	 * Constructor for creating a complete AnswerDTO with all fields.
	 *
	 * @param id         the unique ID of the answer
	 * @param answerText the text content of the answer
	 * @param correct    true if this answer is correct
	 * @param questionId the ID of the question this answer belongs to
	 */
	public AnswerDTO(int id, String answerText, boolean correct, int questionId) {
		super(id);
		this.answerText = answerText;
		this.correct = correct;
		this.questionId = questionId;
	}

	/**
	 * Gets the text of this answer.
	 *
	 * @return the answer text
	 */
	public String getAnswerText() {
		return answerText;
	}

	/**
	 * Sets the text of this answer.
	 *
	 * @param answerText the new answer text
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	/**
	 * Checks if this answer is correct.
	 *
	 * @return true if correct, false otherwise
	 */
	public boolean isCorrect() {
		return correct;
	}

	/**
	 * Sets whether this answer is correct.
	 *
	 * @param correct true if correct, false otherwise
	 */
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	/**
	 * Gets the ID of the question this answer belongs to.
	 *
	 * @return the question ID
	 */
	public int getQuestionId() {
		return questionId;
	}

	/**
	 * Sets the ID of the question this answer belongs to.
	 *
	 * @param questionId the question ID
	 */
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	/**
	 * Compares the content of this AnswerDTO to another DTO for equality. Used when
	 * both entities are new and have no assigned ID.
	 *
	 * @param other another DataTransportObject to compare against
	 * @return true if the answer text, correctness, and question ID match
	 */
	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof AnswerDTO))
			return false;
		AnswerDTO that = (AnswerDTO) other;
		return Objects.equals(this.answerText, that.answerText) && this.correct == that.correct
				&& this.questionId == that.questionId;
	}

	/**
	 * Computes a hash code based on the answer's content. Used when the answer is a
	 * new entity with no ID.
	 *
	 * @return hash code from answerText, correct, and questionId
	 */
	@Override
	protected int contentHashCode() {
		return Objects.hash(answerText, correct, questionId);
	}

	/**
	 * Returns a string summary of this answer's content for debugging/logging
	 * purposes.
	 *
	 * @return String in the format:
	 *         {@code answerText='...', correct=..., questionId=...}
	 */
	@Override
	protected String getContentString() {
		return String.format("answerText='%s', correct=%s, questionId=%d", answerText, correct, questionId);
	}

	/**
	 * Validates that the answer has valid data.
	 * <ul>
	 * <li>Answer text must not be null or empty</li>
	 * <li>Answer text must not exceed 500 characters</li>
	 * </ul>
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
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

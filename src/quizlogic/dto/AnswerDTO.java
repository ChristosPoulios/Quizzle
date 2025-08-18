package quizlogic.dto;

import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a single answer option in a quiz
 * system.
 * <p>
 * This DTO can be serialized and persisted in storage, such as a database. It
 * encapsulates the answer text, indicates whether the answer is correct, and
 * associates the answer with a specific question by its ID.
 * <p>
 * This class extends {@link DataTransportObject} to inherit common DTO
 * behaviors and contract implementations including validation, equality checks,
 * and serialization support.
 * 
 * <p>
 * <b>Note:</b> This class is designed for use in the quiz logic layer to
 * transfer answer data between application tiers or store and retrieve from
 * persistent storage.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class AnswerDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	/** The textual content of this answer. */
	private String answerText;

	/**
	 * Flag indicating if this answer is the correct one for the associated
	 * question.
	 */
	private boolean correct;

	/** The unique identifier of the question this answer belongs to. */
	private int questionId;

	/**
	 * Default constructor creating a new, unsaved AnswerDTO instance.
	 * <p>
	 * Initializes the 'correct' flag to false and the question ID to -1, indicating
	 * not yet associated with a persisted question.
	 */
	public AnswerDTO() {
		super();
		this.correct = false;
		this.questionId = -1;
	}

	/**
	 * Constructor for creating an AnswerDTO representing an existing answer record.
	 * <p>
	 * The object is identified by a unique ID but the correctness is initialized as
	 * false and questionId as -1 until explicitly set.
	 *
	 * @param id the unique identifier of the persisted answer
	 */
	public AnswerDTO(int id) {
		super(id);
		this.correct = false;
		this.questionId = -1;
	}

	/**
	 * Constructs a new AnswerDTO with the given answer text and correctness status.
	 * <p>
	 * The questionId is initialized to -1, indicating the answer is not yet
	 * attached to any question.
	 *
	 * @param answerText the textual content of this answer
	 * @param correct    true if this answer is the correct choice, false otherwise
	 */
	public AnswerDTO(String answerText, boolean correct) {
		super();
		this.answerText = answerText;
		this.correct = correct;
		this.questionId = -1;
	}

	/**
	 * Constructs a fully specified AnswerDTO including its ID, answer text,
	 * correctness status, and associated question ID.
	 *
	 * @param id         the unique identifier of the answer
	 * @param answerText the textual content of this answer
	 * @param correct    true if this answer is correct, false otherwise
	 * @param questionId the ID of the question to which this answer belongs
	 */
	public AnswerDTO(int id, String answerText, boolean correct, int questionId) {
		super(id);
		this.answerText = answerText;
		this.correct = correct;
		this.questionId = questionId;
	}

	/**
	 * Retrieves the text content of this answer.
	 *
	 * @return the answer text as a String
	 */
	public String getAnswerText() {
		return answerText;
	}

	/**
	 * Updates the text content of this answer.
	 *
	 * @param answerText the new answer text to set
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	/**
	 * Checks if this answer is marked as the correct choice.
	 *
	 * @return true if this is the correct answer, false otherwise
	 */
	public boolean isCorrect() {
		return correct;
	}

	/**
	 * Sets the correctness flag for this answer.
	 *
	 * @param correct true to mark this answer as correct, false to mark incorrect
	 */
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	/**
	 * Retrieves the unique identifier of the question associated with this answer.
	 *
	 * @return the question ID as an integer
	 */
	public int getQuestionId() {
		return questionId;
	}

	/**
	 * Sets the question ID that this answer belongs to.
	 *
	 * @param questionId the unique ID of the question to associate with this answer
	 */
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	/**
	 * Compares the content of this AnswerDTO with another DataTransportObject to
	 * determine equality when both objects are new (unsaved).
	 * <p>
	 * The comparison checks equality of answer text, correctness flag, and
	 * associated question ID.
	 *
	 * @param other another DTO to compare against
	 * @return true if both have identical content, false otherwise
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
	 * Computes a hash code derived from the main content of this answer.
	 * <p>
	 * Used for new (unsaved) entities to support collections and hashing.
	 *
	 * @return hash code computed from answerText, correctness, and questionId
	 */
	@Override
	protected int contentHashCode() {
		return Objects.hash(answerText, correct, questionId);
	}

	/**
	 * Generates a concise string representation of this answer's main content.
	 * <p>
	 * Useful for debugging and logging, indicates answer text, correctness, and
	 * question association.
	 *
	 * @return a formatted string summarizing the answer content
	 */
	@Override
	protected String getContentString() {
		return String.format("answerText='%s', correct=%s, questionId=%d", answerText, correct, questionId);
	}

	/**
	 * Validates the internal state of this AnswerDTO.
	 * <p>
	 * This method should ensure that the answer text is not null or empty,
	 * correctness flag and question ID are within expected constraints.
	 * <p>
	 * Throws IllegalArgumentException if validation fails.
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

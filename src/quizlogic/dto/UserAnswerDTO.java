package quizlogic.dto;

import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a user's answer selection in a quiz
 * session.
 * <p>
 * This DTO captures the relationship between a user's quiz session, a specific
 * question, and the answer they selected, along with metadata about whether
 * their selection was correct.
 * <p>
 * Each user answer contains:
 * <ul>
 * <li>The quiz session ID this answer belongs to</li>
 * <li>The question ID that was answered</li>
 * <li>The answer ID that was selected</li>
 * <li>Whether this answer was selected by the user</li>
 * <li>Whether the selected answer was correct</li>
 * </ul>
 * <p>
 * This class extends {@link DataTransportObject} for consistent ID management
 * and validation handling across the application.
 * <p>
 * User answers are used for tracking quiz progress, calculating scores, and
 * providing feedback to users about their performance.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class UserAnswerDTO extends DataTransportObject {

	/**
	 * Serialization version UID for ensuring compatibility during serialization.
	 */
	private static final long serialVersionUID = 1L;

	/** The ID of the quiz session this answer belongs to */
	private int quizSessionId;

	/** The ID of the question that was answered */
	private int questionId;

	/** The ID of the answer that was selected */
	private int answerId;

	/** Flag indicating whether this answer was selected by the user */
	private boolean isSelected;

	/** Flag indicating whether the selected answer was correct */
	private boolean isCorrect;

	/**
	 * Default constructor for creating a new user answer.
	 * <p>
	 * Initializes with default values:
	 * <ul>
	 * <li>isSelected = false</li>
	 * <li>isCorrect = false</li>
	 * <li>All ID fields remain at their default values (0)</li>
	 * </ul>
	 */
	public UserAnswerDTO() {
		super();
		this.isSelected = false;
		this.isCorrect = false;
	}

	/**
	 * Constructor for creating a user answer representing an existing persisted
	 * record.
	 * <p>
	 * Initializes with default boolean values (false for both isSelected and
	 * isCorrect). The provided ID indicates this user answer already exists in
	 * persistent storage.
	 *
	 * @param id the unique identifier of an existing user answer record
	 */
	public UserAnswerDTO(int id) {
		super(id);
		this.isSelected = false;
		this.isCorrect = false;
	}

	/**
	 * Constructor for creating a complete user answer with all field values.
	 * <p>
	 * Creates a new user answer (ID = -1) with all the provided field values.
	 *
	 * @param quizSessionId the ID of the quiz session this answer belongs to
	 * @param questionId    the ID of the question that was answered
	 * @param answerId      the ID of the answer that was selected
	 * @param isSelected    true if this answer was selected by the user
	 * @param isCorrect     true if the selected answer was correct
	 */
	public UserAnswerDTO(int quizSessionId, int questionId, int answerId, boolean isSelected, boolean isCorrect) {
		super();
		this.quizSessionId = quizSessionId;
		this.questionId = questionId;
		this.answerId = answerId;
		this.isSelected = isSelected;
		this.isCorrect = isCorrect;
	}

	/**
	 * Gets the ID of the quiz session this answer belongs to.
	 *
	 * @return the quiz session ID
	 */
	public int getQuizSessionId() {
		return quizSessionId;
	}

	/**
	 * Sets the ID of the quiz session this answer belongs to.
	 *
	 * @param quizSessionId the quiz session ID
	 */
	public void setQuizSessionId(int quizSessionId) {
		this.quizSessionId = quizSessionId;
	}

	/**
	 * Gets the ID of the question that was answered.
	 *
	 * @return the question ID
	 */
	public int getQuestionId() {
		return questionId;
	}

	/**
	 * Sets the ID of the question that was answered.
	 *
	 * @param questionId the question ID
	 */
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	/**
	 * Gets the ID of the answer that was selected.
	 *
	 * @return the answer ID
	 */
	public int getAnswerId() {
		return answerId;
	}

	/**
	 * Sets the ID of the answer that was selected.
	 *
	 * @param answerId the answer ID
	 */
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	/**
	 * Checks whether this answer was selected by the user.
	 *
	 * @return true if selected by the user, false otherwise
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * Sets whether this answer was selected by the user.
	 *
	 * @param selected true if selected by the user, false otherwise
	 */
	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	/**
	 * Checks whether the selected answer was correct.
	 *
	 * @return true if the answer was correct, false otherwise
	 */
	public boolean isCorrect() {
		return isCorrect;
	}

	/**
	 * Sets whether the selected answer was correct.
	 *
	 * @param correct true if the answer was correct, false otherwise
	 */
	public void setCorrect(boolean correct) {
		isCorrect = correct;
	}

	/**
	 * Compares the content of this UserAnswerDTO to another DTO for equality.
	 * <p>
	 * Used when both entities are new and have no assigned ID. Compares all content
	 * fields: quizSessionId, questionId, answerId, isSelected, and isCorrect.
	 *
	 * @param other another DataTransportObject to compare against
	 * @return true if all content fields match, false otherwise
	 */
	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof UserAnswerDTO))
			return false;
		UserAnswerDTO that = (UserAnswerDTO) other;
		return this.quizSessionId == that.quizSessionId && this.questionId == that.questionId
				&& this.answerId == that.answerId && this.isSelected == that.isSelected
				&& this.isCorrect == that.isCorrect;
	}

	/**
	 * Computes a hash code based on the user answer's content.
	 * <p>
	 * Used when the user answer is a new entity with no ID. Hash is computed from
	 * all content fields: quizSessionId, questionId, answerId, isSelected, and
	 * isCorrect.
	 *
	 * @return hash code from all content fields
	 */
	@Override
	protected int contentHashCode() {
		return Objects.hash(quizSessionId, questionId, answerId, isSelected, isCorrect);
	}

	/**
	 * Returns a string summary of this user answer's content for debugging/logging
	 * purposes.
	 *
	 * @return String in the format:
	 *         {@code sessionId=..., questionId=..., answerId=..., selected=..., correct=...}
	 */
	@Override
	protected String getContentString() {
		return String.format("sessionId=%d, questionId=%d, answerId=%d, selected=%s, correct=%s", quizSessionId,
				questionId, answerId, isSelected, isCorrect);
	}

	/**
	 * Validates that the user answer has valid data.
	 * <ul>
	 * <li>Quiz session ID must be greater than 0</li>
	 * <li>Question ID must be greater than 0</li>
	 * <li>Answer ID must be greater than 0</li>
	 * </ul>
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
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

	/**
	 * Gets the ID of the answer that was selected by the user.
	 * <p>
	 * This is an alias method for {@link #getAnswerId()}.
	 *
	 * @return the selected answer ID
	 */
	public int getSelectedAnswerId() {
		return answerId;
	}

	/**
	 * Sets the ID of the answer selected by the user and marks it as selected.
	 * <p>
	 * This is a convenience method that sets both the answerId and marks isSelected
	 * as true in a single operation.
	 *
	 * @param selectedAnswerId the ID of the answer that was selected
	 */
	public void setSelectedAnswerId(int selectedAnswerId) {
		this.answerId = selectedAnswerId;
		this.isSelected = true;
	}
}
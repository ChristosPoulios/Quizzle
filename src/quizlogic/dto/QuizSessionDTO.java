package quizlogic.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a quiz session.
 * <p>
 * A quiz session tracks a user's progress through a quiz, including:
 * <ul>
 * <li>Session timestamp for when the quiz was started</li>
 * <li>The user ID of the person taking the quiz</li>
 * <li>A collection of all user answers provided during the session</li>
 * </ul>
 * <p>
 * This class extends {@link DataTransportObject} for consistent ID management
 * and validation handling across the application.
 * <p>
 * Quiz sessions can be persisted to track user performance and provide
 * statistical analysis of quiz results.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuizSessionDTO extends DataTransportObject {

	/**
	 * Serialization version UID for ensuring compatibility during serialization.
	 */
	private static final long serialVersionUID = 1L;

	/** The timestamp when this quiz session was created or started */
	private Date timestamp;

	/** The ID of the user participating in this quiz session */
	private int userId;

	/** List of all user answers collected during this quiz session */
	private List<UserAnswerDTO> userAnswers;

	/**
	 * Default constructor for creating a new quiz session.
	 * <p>
	 * Initializes the session with:
	 * <ul>
	 * <li>Current timestamp</li>
	 * <li>Default user ID of -1 (indicating no user assigned yet)</li>
	 * <li>Empty list of user answers</li>
	 * </ul>
	 */
	public QuizSessionDTO() {
		super();
		this.timestamp = new Date();
		this.userId = -1;
		this.userAnswers = new ArrayList<>();
	}

	/**
	 * Constructor for creating a quiz session representing an existing persisted
	 * session.
	 * <p>
	 * Initializes with current timestamp, default user ID, and empty answer list.
	 * The provided ID indicates this session already exists in persistent storage.
	 *
	 * @param id the unique identifier of an existing quiz session
	 */
	public QuizSessionDTO(int id) {
		super(id);
		this.timestamp = new Date();
		this.userId = -1;
		this.userAnswers = new ArrayList<>();
	}

	/**
	 * Constructor for creating a quiz session with specific timestamp and user.
	 * <p>
	 * Creates a new session (ID = -1) with the provided timestamp and user ID.
	 * Initializes with an empty list of user answers.
	 *
	 * @param timestamp the timestamp when this session was created/started
	 * @param userId    the ID of the user participating in this session
	 */
	public QuizSessionDTO(Date timestamp, int userId) {
		super();
		this.timestamp = timestamp;
		this.userId = userId;
		this.userAnswers = new ArrayList<>();
	}

	/**
	 * Gets the timestamp of when this quiz session was created or started.
	 *
	 * @return the session timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp for this quiz session.
	 *
	 * @param timestamp the new timestamp for the session
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets the ID of the user participating in this quiz session.
	 *
	 * @return the user ID, or -1 if no user is assigned
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets the ID of the user participating in this quiz session.
	 *
	 * @param userId the user ID to assign to this session
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Gets the list of all user answers collected during this quiz session.
	 *
	 * @return the list of user answers, never null
	 */
	public List<UserAnswerDTO> getUserAnswers() {
		return userAnswers;
	}

	/**
	 * Sets the list of user answers for this quiz session.
	 * <p>
	 * If the provided list is null, it will be replaced with an empty ArrayList.
	 *
	 * @param userAnswers the list of user answers to set
	 */
	public void setUserAnswers(List<UserAnswerDTO> userAnswers) {
		this.userAnswers = userAnswers != null ? userAnswers : new ArrayList<>();
	}

	/**
	 * Adds a user answer to this quiz session.
	 * <p>
	 * If the user answers list is null, it will be initialized before adding the
	 * answer.
	 *
	 * @param userAnswer the user answer to add to this session
	 */
	public void addUserAnswer(UserAnswerDTO userAnswer) {
		if (userAnswers == null) {
			userAnswers = new ArrayList<>();
		}
		userAnswers.add(userAnswer);
	}

	/**
	 * Starts or restarts this quiz session by setting the timestamp to the current
	 * time.
	 * <p>
	 * This method can be called to mark the beginning of a quiz session or to
	 * update the session timestamp.
	 */
	public void start() {
		this.timestamp = new Date();
	}

	/**
	 * Ends this quiz session.
	 * <p>
	 * Currently this method serves as a placeholder for any session cleanup logic
	 * that might be needed in the future.
	 */
	public void end() {
		// Session end logic if needed
	}

	/**
	 * Compares the content of this QuizSessionDTO to another DTO for equality.
	 * <p>
	 * Used when both entities are new and have no assigned ID. Compares timestamp
	 * and user ID for equality.
	 *
	 * @param other another DataTransportObject to compare against
	 * @return true if the timestamp and user ID match, false otherwise
	 */
	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof QuizSessionDTO))
			return false;
		QuizSessionDTO that = (QuizSessionDTO) other;
		return Objects.equals(this.timestamp, that.timestamp) && this.userId == that.userId;
	}

	/**
	 * Computes a hash code based on the session's content.
	 * <p>
	 * Used when the session is a new entity with no ID. Hash is computed from
	 * timestamp and user ID.
	 *
	 * @return hash code from timestamp and userId
	 */
	@Override
	protected int contentHashCode() {
		return Objects.hash(timestamp, userId);
	}

	/**
	 * Returns a string summary of this quiz session's content for debugging/logging
	 * purposes.
	 *
	 * @return String in the format:
	 *         {@code timestamp='...', userId=..., answers=...}
	 */
	@Override
	protected String getContentString() {
		return String.format("timestamp='%s', userId=%d, answers=%d", timestamp, userId,
				userAnswers != null ? userAnswers.size() : 0);
	}

	/**
	 * Validates that the quiz session has valid data.
	 * <ul>
	 * <li>Timestamp must not be null</li>
	 * <li>User ID must be non-negative (0 or positive)</li>
	 * </ul>
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	@Override
	protected void validate() {
		if (timestamp == null) {
			throw new IllegalArgumentException("Quiz session timestamp cannot be null");
		}
		if (userId < 0) {
			throw new IllegalArgumentException("Valid user ID is required");
		}
	}
}
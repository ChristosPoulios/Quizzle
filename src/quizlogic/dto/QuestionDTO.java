package quizlogic.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import constants.UserStringConstants;
import constants.ValidationConstants;
import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a quiz question.
 * <p>
 * Each question contains:
 * <ul>
 * <li>The main question text</li>
 * <li>An optional title or short description</li>
 * <li>A list of possible answers</li>
 * </ul>
 * This class extends {@link DataTransportObject} for consistent ID and
 * validation handling.
 * <p>
 * Used in both in-memory operations and persistent storage layers.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuestionDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	/** The main question text to be displayed */
	private String questionText;

	/** Optional short title for the question */
	private String questionTitle;

	/** List of possible answers to this question */
	private List<AnswerDTO> answers;

	/**
	 * Default constructor for creating a new QuestionDTO. Initializes an empty
	 * answer list.
	 */
	public QuestionDTO() {
		super();
		this.answers = new ArrayList<>();
	}

	/**
	 * Constructor for creating a QuestionDTO with an existing ID (persisted
	 * entity).
	 *
	 * @param id the unique ID of the question
	 */
	public QuestionDTO(int id) {
		super(id);
		this.answers = new ArrayList<>();
	}

	/**
	 * Constructor for creating a new question with text and title.
	 *
	 * @param questionText  the main question text
	 * @param questionTitle the question title
	 */
	public QuestionDTO(String questionText, String questionTitle) {
		super();
		this.questionText = questionText;
		this.questionTitle = questionTitle;
		this.answers = new ArrayList<>();
	}

	/**
	 * Gets the main question text.
	 *
	 * @return the question text
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * Sets the main question text.
	 *
	 * @param questionText the new question text
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/**
	 * Gets the question title.
	 *
	 * @return the question title
	 */
	public String getQuestionTitle() {
		return questionTitle;
	}

	/**
	 * Sets the question title.
	 *
	 * @param questionTitle the new question title
	 */
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	/**
	 * Gets the list of answers for this question.
	 *
	 * @return list of answers
	 */
	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	/**
	 * Sets the list of answers for this question.
	 *
	 * @param answers list of answers, if null an empty list will be assigned
	 */
	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers != null ? answers : new ArrayList<>();
	}

	/**
	 * Legacy method - alias for {@link #getQuestionText()}.
	 *
	 * @return the question text
	 */
	public String getText() {
		return getQuestionText();
	}

	/**
	 * Legacy method - alias for {@link #setQuestionText(String)}.
	 *
	 * @param text question text
	 */
	public void setText(String text) {
		setQuestionText(text);
	}

	/**
	 * Legacy method - alias for {@link #getQuestionTitle()}.
	 *
	 * @return question title
	 */
	public String getTitle() {
		return getQuestionTitle();
	}

	/**
	 * Legacy method - alias for {@link #setQuestionTitle(String)}.
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		setQuestionTitle(title);
	}

	/**
	 * Checks if this question has at least one correct answer.
	 *
	 * @return true if there is at least one answer marked as correct
	 */
	public boolean hasCorrectAnswer() {
		return answers.stream().anyMatch(AnswerDTO::isCorrect);
	}

	/**
	 * Compares the main content of this question to another for equality.
	 * <p>
	 * Used for comparing new entities without an assigned ID.
	 * Also used to identify questions by title even if IDs differ.
	 *
	 * @param other DTO to compare against
	 * @return true if question text and title match
	 */
	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof QuestionDTO))
			return false;
		QuestionDTO that = (QuestionDTO) other;
		return Objects.equals(this.questionText, that.questionText)
				&& Objects.equals(this.questionTitle, that.questionTitle);
	}

	/**
	 * Enhanced equals method that also considers title matching for database mapping.
	 * This ensures that questions with the same title are treated as the same entity
	 * even if one has an ID and the other doesn't.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		DataTransportObject that = (DataTransportObject) obj;
		
		// Standard DTO comparison from parent class
		if (!this.isNew() && !that.isNew()) {
			return this.getId() == that.getId();
		}
		if (this.isNew() && that.isNew()) {
			return this.contentEquals(that);
		}
		
		// Enhanced comparison: if one is new and other is persisted,
		// compare by title if both are QuestionDTOs
		if (obj instanceof QuestionDTO) {
			QuestionDTO otherQuestion = (QuestionDTO) obj;
			// If titles match and are not null/empty, consider them the same question
			if (this.questionTitle != null && otherQuestion.questionTitle != null 
				&& !this.questionTitle.trim().isEmpty() && !otherQuestion.questionTitle.trim().isEmpty()) {
				return Objects.equals(this.questionTitle, otherQuestion.questionTitle);
			}
		}
		
		return false;
	}

	/**
	 * Enhanced hashCode that prioritizes title over ID for database mapping consistency.
	 */
	@Override
	public int hashCode() {
		// If we have a meaningful title, use it for hashing to ensure consistency
		if (questionTitle != null && !questionTitle.trim().isEmpty()) {
			return Objects.hash(questionTitle);
		}
		// Fallback to parent implementation
		return isNew() ? contentHashCode() : Objects.hash(getId());
	}

	/**
	 * Computes a hash code based on question content.
	 * <p>
	 * Used when the entity is new and not yet persisted.
	 *
	 * @return hash based on question text and title
	 */
	@Override
	protected int contentHashCode() {
		return Objects.hash(questionText, questionTitle);
	}

	/**
	 * Returns a string summary of question content for debugging.
	 *
	 * @return formatted string showing title and number of answers
	 */
	@Override
	protected String getContentString() {
		return "title='" + questionTitle + "', answers=" + answers.size();
	}

	/**
	 * Validates the question content.
	 * <ul>
	 * <li>Question text must not be null or empty</li>
	 * <li>Question text must not exceed
	 * {@value ValidationConstants#QUESTION_TEXT_MAX_LENGTH} characters</li>
	 * <li>Question title, if present, must not exceed
	 * {@value ValidationConstants#QUESTION_TITLE_MAX_LENGTH} characters</li>
	 * </ul>
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	@Override
	protected void validate() {
		if (questionText == null || questionText.trim().isEmpty()) {
			throw new IllegalArgumentException(UserStringConstants.ERROR_QUESTION_TEXT_NULL_OR_EMPTY);
		}
		if (questionText.length() > ValidationConstants.QUESTION_TEXT_MAX_LENGTH) {
			throw new IllegalArgumentException(String.format(UserStringConstants.ERROR_QUESTION_TEXT_TOO_LONG,
					ValidationConstants.QUESTION_TEXT_MAX_LENGTH));
		}
		if (questionTitle != null && questionTitle.length() > ValidationConstants.QUESTION_TITLE_MAX_LENGTH) {
			throw new IllegalArgumentException(String.format(UserStringConstants.ERROR_QUESTION_TITLE_TOO_LONG,
					ValidationConstants.QUESTION_TITLE_MAX_LENGTH));
		}
	}
}
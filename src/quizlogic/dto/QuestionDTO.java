package quizlogic.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a quiz question.
 * <p>
 * This DTO contains the main question text, an optional short title, and a list
 * of possible answers. It is intended to be used both for in-memory operations
 * during quiz execution and for persistence in databases.
 * <p>
 * This class extends {@link DataTransportObject} and inherits standard DTO
 * behavior such as validation, equality checks based on content or ID, and
 * serialization support.
 * <p>
 * Each question can have multiple answer options stored inside the answers
 * list.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuestionDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	/** The main textual content of the question to be presented to the user. */
	private String questionText;

	/** An optional short title or summary for the question. */
	private String questionTitle;

	/**
	 * A list containing the possible answers for this question. Typically, this
	 * list holds objects of type {@link AnswerDTO}.
	 */
	private List<AnswerDTO> answers;

	/**
	 * Default constructor initializes an empty list of answers.
	 */
	public QuestionDTO() {
		super();
		this.answers = new ArrayList<>();
	}

	/**
	 * Constructs a QuestionDTO with a specific ID for representing an existing
	 * persisted question. Initializes the answers list to empty.
	 *
	 * @param id the unique identifier of the persisted question
	 */
	public QuestionDTO(int id) {
		super(id);
		this.answers = new ArrayList<>();
	}

	/**
	 * Constructs a QuestionDTO with the specified question text and title.
	 * Initializes the answers list to empty.
	 *
	 * @param questionText  the main text of the question
	 * @param questionTitle an optional title for the question
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
	 * Retrieves the short title of the question, if any.
	 *
	 * @return question title string or null if not set
	 */
	public String getQuestionTitle() {
		return questionTitle;
	}

	/**
	 * Sets the short title for this question.
	 *
	 * @param questionTitle the title to assign to this question
	 */
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	/**
	 * Returns the list of answers associated with this question.
	 *
	 * @return a list holding answer DTOs for this question
	 */
	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	/**
	 * Sets the list of answers for this question.
	 *
	 * @param answers a list containing {@link AnswerDTO} objects representing the
	 *                possible answers
	 */
	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers != null ? answers : new ArrayList<>();
	}

	/**
	 * Legacy method - alias for {@link #getQuestionText()}.
	 *
	 * @return question text
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
	 * Compares the content of this QuestionDTO to another DTO for equality when
	 * both are new (unsaved).
	 * <p>
	 * Equality is based on matching the question text and the question title.
	 *
	 * @param other another DataTransportObject to compare against
	 * @return true if question text and title are equal, false otherwise
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
	 * Computes a hash code based on the question content.
	 * <p>
	 * Used when the entity is new and not yet persisted.
	 *
	 * @return hash code computed from question text and title
	 */
	@Override
	protected int contentHashCode() {
		return Objects.hash(questionText, questionTitle);
	}

	/**
	 * Generates a concise string summary of the question content.
	 * <p>
	 * This includes the question title and the number of associated answers.
	 *
	 * @return a formatted string for debugging/logging purposes
	 */
	@Override
	protected String getContentString() {
		return "title='" + questionTitle + "', answers=" + answers.size();
	}

	/**
	 * Validates the question content.
	 * <p>
	 * Ensures that the main question text is not null or empty, does not exceed
	 * 1000 characters, and the optional question title is within 200 characters if
	 * provided.
	 * <p>
	 * Throws {@link IllegalArgumentException} if any validation rule is violated.
	 */
	@Override
	protected void validate() {
		if (questionText == null || questionText.trim().isEmpty()) {
			throw new IllegalArgumentException("Question text cannot be null or empty");
		}
		if (questionText.length() > 1000) {
			throw new IllegalArgumentException("Question text cannot exceed 1000 characters");
		}
		if (questionTitle != null && questionTitle.length() > 200) {
			throw new IllegalArgumentException("Question title cannot exceed 200 characters");
		}
	}
}

package persistence.serialization.dao;

import java.io.Serializable;
import java.util.Objects;
import persistence.DataAccessObject;
import quizlogic.dto.AnswerDTO;

/**
 * Serializable Data Access Object (DAO) representing a quiz answer for use with
 * file-based serialization persistence.
 * 
 * <p>
 * This DAO mirrors the functionality of {@code AnswerDAO_MariaDB} but is
 * designed for serialization-based storage rather than relational database
 * persistence. It maintains the same interface and validation logic for
 * consistency across storage backends.
 * 
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Store answer text, correctness flag, and question association</li>
 * <li>Provide validation for answer content</li>
 * <li>Convert between DAO and DTO representations</li>
 * <li>Support serialization for file-based persistence</li>
 * </ul>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class AnswerDAO_Serializable extends DataAccessObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The text content of the answer */
	private String text;

	/** Flag indicating whether this is the correct answer */
	private boolean correct;

	/** ID of the question this answer belongs to */
	private int questionId;

	/**
	 * Default constructor for creating a new answer DAO. Initializes with empty
	 * text, incorrect flag, and invalid question ID.
	 */
	public AnswerDAO_Serializable() {
		super();
		this.text = "";
		this.correct = false;
		this.questionId = -1;
	}

	/**
	 * Constructor for creating an answer DAO with an existing ID.
	 * 
	 * @param id the unique identifier of the answer
	 */
	public AnswerDAO_Serializable(int id) {
		super();
		setId(id);
		this.text = "";
		this.correct = false;
		this.questionId = -1;
	}

	/**
	 * Constructor for creating a complete answer DAO.
	 * 
	 * @param text       the answer text content
	 * @param correct    true if this is the correct answer
	 * @param questionId the ID of the associated question
	 */
	public AnswerDAO_Serializable(String text, boolean correct, int questionId) {
		super();
		this.text = text != null ? text : "";
		this.correct = correct;
		this.questionId = questionId;
	}

	/**
	 * Gets the answer text.
	 * 
	 * @return the answer text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the answer text.
	 * 
	 * @param text the answer text to set
	 */
	public void setText(String text) {
		this.text = text != null ? text : "";
	}

	/**
	 * Checks if this answer is marked as correct.
	 * 
	 * @return true if the answer is correct, false otherwise
	 */
	public boolean isCorrect() {
		return correct;
	}

	/**
	 * Sets whether this answer is correct.
	 * 
	 * @param correct true if the answer is correct, false otherwise
	 */
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	/**
	 * Returns the ID of the associated question.
	 * 
	 * @return the question ID
	 */
	public int getQuestionId() {
		return questionId;
	}

	/**
	 * Sets the ID of the associated question.
	 * 
	 * @param questionId the question ID to set
	 */
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	/**
	 * Validates the answer data ensuring text is not empty and question ID is
	 * valid.
	 * 
	 * @return true if validation passes
	 * @throws IllegalArgumentException if validation fails
	 */
	public boolean performValidation() {
		if (text == null || text.trim().isEmpty()) {
			throw new IllegalArgumentException("Answer text cannot be empty");
		}
		if (questionId <= 0) {
			throw new IllegalArgumentException("Valid question ID is required");
		}
		return true;
	}

	/**
	 * Converts this DAO to a transferable {@link AnswerDTO}.
	 * 
	 * @return populated AnswerDTO with this DAO's data
	 */
	public AnswerDTO forTransport() {
		AnswerDTO dto = new AnswerDTO();
		dto.setId(getId());
		dto.setAnswerText(text);
		dto.setCorrect(correct);
		dto.setQuestionId(questionId);
		return dto;
	}

	/**
	 * Creates a new AnswerDAO_Serializable from a transport DTO.
	 * 
	 * @param dto the AnswerDTO containing answer data
	 * @return new AnswerDAO_Serializable instance
	 */
	public static AnswerDAO_Serializable fromTransport(AnswerDTO dto) {
		if (dto == null) {
			return new AnswerDAO_Serializable();
		}

		AnswerDAO_Serializable dao = new AnswerDAO_Serializable();
		dao.setId(dto.getId());
		dao.setText(dto.getAnswerText());
		dao.setCorrect(dto.isCorrect());
		dao.setQuestionId(dto.getQuestionId());
		return dao;
	}

	/**
	 * Creates a new AnswerDAO_Serializable from a DTO with explicit question ID.
	 * 
	 * @param dto        the AnswerDTO containing answer data
	 * @param questionId the ID of the associated question
	 * @return new AnswerDAO_Serializable instance
	 */
	public static AnswerDAO_Serializable fromTransport(AnswerDTO dto, int questionId) {
		AnswerDAO_Serializable dao = fromTransport(dto);
		dao.setQuestionId(questionId);
		return dao;
	}

	/**
	 * Returns validation errors as a formatted string.
	 * 
	 * @return validation errors as a String, or null if no errors
	 */
	public String getValidationErrors() {
		StringBuilder errors = new StringBuilder();

		if (text == null || text.trim().isEmpty()) {
			errors.append("Answer text cannot be empty.\n");
		}
		if (questionId <= 0) {
			errors.append("Valid question ID is required.\n");
		}

		return errors.length() > 0 ? errors.toString() : null;
	}

	/**
	 * Checks if this DAO represents a new answer (ID <= 0).
	 * 
	 * @return true if the answer is new, false otherwise
	 */
	public boolean isNew() {
		return getId() <= 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		AnswerDAO_Serializable that = (AnswerDAO_Serializable) obj;

		// If both have IDs, compare by ID
		if (this.getId() > 0 && that.getId() > 0) {
			return this.getId() == that.getId();
		}

		// Otherwise compare by content
		return Objects.equals(this.text, that.text) && this.correct == that.correct
				&& this.questionId == that.questionId;
	}

	@Override
	public int hashCode() {
		if (getId() > 0) {
			return Objects.hash(getId());
		}
		return Objects.hash(text, correct, questionId);
	}

	@Override
	public String toString() {
		return String.format("AnswerDAO_Serializable{id=%d, text='%s', correct=%s, questionId=%d}", getId(), text,
				correct, questionId);
	}
}
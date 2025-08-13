package quizlogic.dto;

import java.util.Objects;

import quizlogic.QObject;

/**
 * Data Transfer Object representing an answer option for a quiz question. Each
 * answer belongs to a specific question and indicates whether it is correct or
 * not.
 * 
 * @author Your Name
 * @version 1.0
 */
public class AnswerDTO extends QObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The ID of the question this answer belongs to */
	private int questionId;

	/** The text content of the answer */
	private String answerText;

	/** Flag indicating whether this answer is correct */
	private boolean isCorrect;

	/**
	 * Default constructor creating an empty answer.
	 */
	public AnswerDTO() {
		super(-1); // Default ID set to -1 indicating no ID assigned yet
		this.questionId = -1;
		this.answerText = "";
		this.isCorrect = false;
	}

	/**
	 * Constructor creating an answer with all properties including ID.
	 * 
	 * @param id         the unique identifier for this answer
	 * @param questionId the ID of the question this answer belongs to
	 * @param answerText the text content of the answer
	 * @param isCorrect  whether this answer is correct
	 */
	public AnswerDTO(int id, int questionId, String answerText, boolean isCorrect) {
		super(id);
		this.questionId = questionId;
		this.answerText = answerText;
		this.isCorrect = isCorrect;
	}

	/**
	 * Constructor creating an answer without ID (for new answers).
	 * 
	 * @param questionId the ID of the question this answer belongs to
	 * @param answerText the text content of the answer
	 * @param isCorrect  whether this answer is correct
	 */
	public AnswerDTO(int questionId, String answerText, boolean isCorrect) {
		super(-1);
		this.questionId = questionId;
		this.answerText = answerText;
		this.isCorrect = isCorrect;
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
	 * @param questionId the question ID to set
	 */
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	/**
	 * Gets the text content of the answer.
	 * 
	 * @return the answer text
	 */
	public String getAnswerText() {
		return answerText;
	}

	/**
	 * Sets the text content of the answer.
	 * 
	 * @param answerText the answer text to set
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	/**
	 * Checks if this answer is correct.
	 * 
	 * @return true if this answer is correct, false otherwise
	 */
	public boolean isCorrect() {
		return isCorrect;
	}

	/**
	 * Sets whether this answer is correct.
	 * 
	 * @param correct true if this answer is correct, false otherwise
	 */
	public void setCorrect(boolean correct) {
		isCorrect = correct;
	}

	/**
	 * Returns a string representation of this answer.
	 * 
	 * @return a string containing the answer's details
	 */
	@Override
	public String toString() {
		return "AnswerDTO{" + "id=" + getId() + ", questionId=" + questionId + ", answerText='" + answerText + '\''
				+ ", isCorrect=" + isCorrect + '}';
	}

	/**
	 * Compares this answer with another object for equality. Two answers are equal
	 * if they have the same ID, question ID, text, and correctness flag.
	 * 
	 * @param o the object to compare with
	 * @return true if the objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AnswerDTO answerDTO = (AnswerDTO) o;
		return isCorrect == answerDTO.isCorrect && questionId == answerDTO.questionId
				&& Objects.equals(getId(), answerDTO.getId()) && Objects.equals(answerText, answerDTO.answerText);
	}

	/**
	 * Returns a hash code value for this answer.
	 * 
	 * @return a hash code value for this object
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getId(), questionId, answerText, isCorrect);
	}
}

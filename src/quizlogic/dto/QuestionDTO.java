package quizlogic.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import quizlogic.QObject;

/**
 * Data Transfer Object representing a quiz question. Each question belongs to a
 * theme and contains multiple answer options.
 * 
 * @author Your Name
 * @version 1.0
 */
public class QuestionDTO extends QObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The ID of the theme this question belongs to */
	private int themeId;

	/** The title of the question */
	private String questionTitle;

	/** The detailed text of the question */
	private String questionText;

	/** List of answer options for this question */
	private List<AnswerDTO> answers;

	/**
	 * Default constructor creating an empty question with an initialized answer
	 * list.
	 */
	public QuestionDTO() {
		super(-1);
		this.themeId = -1; 
		this.questionTitle = "";
		this.questionText = "";
		this.answers = new ArrayList<>(); // Initialize answers list
	}

	/**
	 * Constructor creating a question with all properties including ID.
	 * 
	 * @param id            the unique identifier for this question
	 * @param themeId       the ID of the theme this question belongs to
	 * @param questionTitle the title of the question
	 * @param questionText  the detailed text of the question
	 */
	public QuestionDTO(int id, int themeId, String questionTitle, String questionText) {
		super(id);
		this.themeId = themeId;
		this.questionTitle = questionTitle;
		this.questionText = questionText;
		this.answers = new ArrayList<>();
	}

	/**
	 * Constructor creating a question without ID (for new questions).
	 * 
	 * @param themeId       the ID of the theme this question belongs to
	 * @param questionTitle the title of the question
	 * @param questionText  the detailed text of the question
	 */
	public QuestionDTO(int themeId, String questionTitle, String questionText) {
		super(-1); 
		this.themeId = themeId;
		this.questionTitle = questionTitle;
		this.questionText = questionText;
		this.answers = new ArrayList<>();
	}

	/**
	 * Gets the ID of the theme this question belongs to.
	 * 
	 * @return the theme ID
	 */
	public int getThemeId() {
		return themeId;
	}

	/**
	 * Sets the ID of the theme this question belongs to.
	 * 
	 * @param themeId the theme ID to set
	 */
	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

	/**
	 * Gets the title of the question.
	 * 
	 * @return the question title
	 */
	public String getQuestionTitle() {
		return questionTitle;
	}

	/**
	 * Sets the title of the question.
	 * 
	 * @param questionTitle the question title to set
	 */
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	/**
	 * Gets the detailed text of the question.
	 * 
	 * @return the question text
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * Sets the detailed text of the question.
	 * 
	 * @param questionText the question text to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/**
	 * Gets the list of answer options for this question.
	 * 
	 * @return the list of answers
	 */
	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	/**
	 * Sets the list of answer options for this question. If null is provided, an
	 * empty list is created.
	 * 
	 * @param answers the list of answers to set
	 */
	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers != null ? answers : new ArrayList<>();
	}

	/**
	 * Adds an answer option to this question. If the answers list is null, it will
	 * be initialized.
	 * 
	 * @param answer the answer to add
	 */
	public void addAnswer(AnswerDTO answer) {
		if (answers == null) {
			answers = new ArrayList<>();
		}
		answers.add(answer);
	}

	/**
	 * Removes an answer option from this question.
	 * 
	 * @param answer the answer to remove
	 */
	public void removeAnswer(AnswerDTO answer) {
		if (answers != null) {
			answers.remove(answer);
		}
	}

	/**
	 * Finds and returns the correct answer for this question.
	 * 
	 * @return the correct answer, or null if no correct answer exists
	 */
	public AnswerDTO getCorrectAnswer() {
		if (answers != null) {
			return answers.stream().filter(AnswerDTO::isCorrect).findFirst().orElse(null);
		}
		return null;
	}

	/**
	 * Gets the number of answer options for this question.
	 * 
	 * @return the count of answers
	 */
	public int getAnswerCount() {
		return answers != null ? answers.size() : 0;
	}

	/**
	 * Returns a string representation of this question.
	 * 
	 * @return a string containing the question's details
	 */
	@Override
	public String toString() {
		return "QuestionDTO{" + "id=" + getId() + ", themeId=" + themeId + ", questionTitle='" + questionTitle + '\''
				+ ", questionText='" + questionText + '\'' + ", answersCount=" + getAnswerCount() + '}';
	}

	/**
	 * Compares this question with another object for equality. Two questions are
	 * equal if they have the same ID, theme ID, title, and text.
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
		QuestionDTO that = (QuestionDTO) o;
		return themeId == that.themeId && Objects.equals(getId(), that.getId())
				&& Objects.equals(questionTitle, that.questionTitle) && Objects.equals(questionText, that.questionText);
	}

	/**
	 * Returns a hash code value for this question.
	 * 
	 * @return a hash code value for this object
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getId(), themeId, questionTitle, questionText);
	}

	public String getText() {
		return questionText;
	}
}

package quizlogic.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object for quiz questions. Extends DataTransportObject for
 * consistent DTO behavior.
 */
public class QuestionDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	private String questionText;
	private String questionTitle;
	private List<AnswerDTO> answers;

	/**
	 * Default constructor
	 */
	public QuestionDTO() {
		super();
		this.answers = new ArrayList<>();
	}

	/**
	 * Constructor with ID
	 */
	public QuestionDTO(int id) {
		super(id);
		this.answers = new ArrayList<>();
	}

	/**
	 * Full constructor
	 */
	public QuestionDTO(String questionText, String questionTitle) {
		super();
		this.questionText = questionText;
		this.questionTitle = questionTitle;
		this.answers = new ArrayList<>();
	}


	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers != null ? answers : new ArrayList<>();
	}


	/**
	 * Legacy method - delegates to getQuestionText()
	 */
	public String getText() {
		return getQuestionText();
	}

	/**
	 * Legacy method - delegates to setQuestionText()
	 */
	public void setText(String text) {
		setQuestionText(text);
	}

	/**
	 * Legacy method - delegates to getQuestionTitle()
	 */
	public String getTitle() {
		return getQuestionTitle();
	}

	/**
	 * Legacy method - delegates to setQuestionTitle()
	 */
	public void setTitle(String title) {
		setQuestionTitle(title);
	}

	/**
	 * Checks if this question has at least one correct answer
	 * 
	 * @return true if question has correct answer(s)
	 */
	public boolean hasCorrectAnswer() {
		return answers.stream().anyMatch(AnswerDTO::isCorrect);
	}

	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof QuestionDTO))
			return false;

		QuestionDTO that = (QuestionDTO) other;
		return Objects.equals(this.questionText, that.questionText)
				&& Objects.equals(this.questionTitle, that.questionTitle);
	}

	@Override
	protected int contentHashCode() {
		return Objects.hash(questionText, questionTitle);
	}

	@Override
	protected String getContentString() {
		return "title='" + questionTitle + "', answers=" + answers.size();
	}

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
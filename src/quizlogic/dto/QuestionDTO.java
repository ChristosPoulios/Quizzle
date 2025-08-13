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

	// ==================== Getters/Setters ====================

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

	// ==================== Legacy-Kompatibilität ====================

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

	// ==================== Convenience Methods ====================

	public void addAnswer(AnswerDTO answer) {
		if (answer != null) {
			this.answers.add(answer);
		}
	}

	public boolean removeAnswer(AnswerDTO answer) {
		return this.answers.remove(answer);
	}

	public int getAnswerCount() {
		return answers.size();
	}

	public boolean hasAnswers() {
		return !answers.isEmpty();
	}

	/**
	 * Gets the correct answer(s) for this question
	 * 
	 * @return list of correct answers
	 */
	public List<AnswerDTO> getCorrectAnswers() {
		return answers.stream().filter(AnswerDTO::isCorrect).collect(java.util.stream.Collectors.toList());
	}

	/**
	 * Checks if this question has at least one correct answer
	 * 
	 * @return true if question has correct answer(s)
	 */
	public boolean hasCorrectAnswer() {
		return answers.stream().anyMatch(AnswerDTO::isCorrect);
	}

	/**
	 * Gets difficulty level (based on number of answers)
	 * 
	 * @return difficulty as int (1-5)
	 */
	public int getDifficulty() {
		int answerCount = answers.size();
		if (answerCount <= 2)
			return 1;
		if (answerCount <= 3)
			return 2;
		if (answerCount <= 4)
			return 3;
		if (answerCount <= 6)
			return 4;
		return 5;
	}

	/**
	 * Gets explanation (could be question title or derived)
	 * 
	 * @return explanation text
	 */
	public String getExplanation() {
		return questionTitle != null ? questionTitle : questionText;
	}

	// ==================== Template Method Implementations ====================

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

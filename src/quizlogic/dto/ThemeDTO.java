package quizlogic.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import quizlogic.QObject;

/**
 * Data Transfer Object representing a quiz theme. Each theme contains multiple
 * questions and serves as a category for quiz organization.
 * 
 * @author Your Name
 * @version 1.0
 */
public class ThemeDTO extends QObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The title of the theme */
	private String themeTitle;

	/** The description of the theme */
	private String themeDescription;

	/** List of questions belonging to this theme */
	private List<QuestionDTO> questions;

	/**
	 * Default constructor creating an empty theme with an initialized question
	 * list.
	 */
	public ThemeDTO() {
		super(-1);
		this.questions = new ArrayList<>();
	}

	/**
	 * Constructor creating a theme with all properties including ID.
	 * 
	 * @param id               the unique identifier for this theme
	 * @param themeTitle       the title of the theme
	 * @param themeDescription the description of the theme
	 */
	public ThemeDTO(int id, String themeTitle, String themeDescription) {
		super(id);
		this.themeTitle = themeTitle;
		this.themeDescription = themeDescription;
		this.questions = new ArrayList<>();
	}

	/**
	 * Constructor creating a theme without ID (for new themes).
	 * 
	 * @param themeTitle       the title of the theme
	 * @param themeDescription the description of the theme
	 */
	public ThemeDTO(String themeTitle, String themeDescription) {
		super(-1); 
		this.themeTitle = themeTitle;
		this.themeDescription = themeDescription;
		this.questions = new ArrayList<>();
	}

	/**
	 * Gets the title of the theme.
	 * 
	 * @return the theme title
	 */
	public String getThemeTitle() {
		return themeTitle;
	}

	/**
	 * Sets the title of the theme.
	 * 
	 * @param themeTitle the theme title to set
	 */
	public void setThemeTitle(String themeTitle) {
		this.themeTitle = themeTitle;
	}

	/**
	 * Gets the description of the theme.
	 * 
	 * @return the theme description
	 */
	public String getThemeDescription() {
		return themeDescription;
	}

	/**
	 * Sets the description of the theme.
	 * 
	 * @param themeDescription the theme description to set
	 */
	public void setThemeDescription(String themeDescription) {
		this.themeDescription = themeDescription;
	}

	/**
	 * Gets the list of questions belonging to this theme.
	 * 
	 * @return the list of questions
	 */
	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	/**
	 * Sets the list of questions belonging to this theme. If null is provided, an
	 * empty list is created.
	 * 
	 * @param questions the list of questions to set
	 */
	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions != null ? questions : new ArrayList<>();
	}

	/**
	 * Adds a question to this theme. If the questions list is null, it will be
	 * initialized.
	 * 
	 * @param question the question to add
	 */
	public void addQuestion(QuestionDTO question) {
		if (questions == null) {
			questions = new ArrayList<>();
		}
		questions.add(question);
	}

	/**
	 * Removes a question from this theme.
	 * 
	 * @param question the question to remove
	 */
	public void removeQuestion(QuestionDTO question) {
		if (questions != null) {
			questions.remove(question);
		}
	}

	/**
	 * Gets the number of questions in this theme.
	 * 
	 * @return the count of questions
	 */
	public int getQuestionCount() {
		return questions != null ? questions.size() : 0;
	}

	/**
	 * Calculates the total number of answers across all questions in this theme.
	 * 
	 * @return the total count of answers in all questions
	 */
	public int getTotalAnswerCount() {
		if (questions == null)
			return 0;
		return questions.stream().mapToInt(QuestionDTO::getAnswerCount).sum();
	}

	/**
	 * Returns a string representation of this theme.
	 * 
	 * @return a string containing the theme's details
	 */
	@Override
	public String toString() {
		return "ThemeDTO{" + "id=" + getId() + ", themeTitle='" + themeTitle + '\'' + ", themeDescription='"
				+ themeDescription + '\'' + ", questionCount=" + getQuestionCount() + '}';
	}

	/**
	 * Compares this theme with another object for equality. Two themes are equal if
	 * they have the same ID, title, and description.
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
		ThemeDTO themeDTO = (ThemeDTO) o;
		return Objects.equals(getId(), themeDTO.getId()) && Objects.equals(themeTitle, themeDTO.themeTitle)
				&& Objects.equals(themeDescription, themeDTO.themeDescription);
	}

	/**
	 * Returns a hash code value for this theme.
	 * 
	 * @return a hash code value for this object
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getId(), themeTitle, themeDescription);
	}

	public String getText() {
		return themeTitle + " - " + themeDescription;
	}
}

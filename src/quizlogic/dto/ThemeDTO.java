package quizlogic.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object for quiz themes. Extends DataTransportObject for
 * consistent DTO behavior.
 */
public class ThemeDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	private String themeTitle;
	private String themeDescription;
	private List<QuestionDTO> questions;

	/**
	 * Default constructor
	 */
	public ThemeDTO() {
		super();
		this.questions = new ArrayList<>();
	}

	/**
	 * Constructor with ID
	 */
	public ThemeDTO(int id) {
		super(id);
		this.questions = new ArrayList<>();
	}

	/**
	 * Full constructor
	 */
	public ThemeDTO(String themeTitle, String themeDescription) {
		super();
		this.themeTitle = themeTitle;
		this.themeDescription = themeDescription;
		this.questions = new ArrayList<>();
	}

	public String getThemeTitle() {
		return themeTitle;
	}

	public void setThemeTitle(String themeTitle) {
		this.themeTitle = themeTitle;
	}

	public String getThemeDescription() {
		return themeDescription;
	}

	public void setThemeDescription(String themeDescription) {
		this.themeDescription = themeDescription;
	}

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions != null ? questions : new ArrayList<>();
	}

	/**
	 * Legacy method - delegates to getThemeTitle() Für Kompatibilität mit
	 * bestehender GUI
	 */
	public String getTitle() {
		return getThemeTitle();
	}

	/**
	 * Legacy method - delegates to setThemeTitle()
	 */
	public void setTitle(String title) {
		setThemeTitle(title);
	}

	/**
	 * Legacy method - delegates to getThemeDescription()
	 */
	public String getText() {
		return getThemeDescription();
	}

	/**
	 * Gets a random question from this theme
	 * 
	 * @return random question or null if no questions available
	 */
	public QuestionDTO getRandomQuestion() {
		if (questions.isEmpty()) {
			return null;
		}
		int randomIndex = (int) (Math.random() * questions.size());
		return questions.get(randomIndex);
	}

	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof ThemeDTO))
			return false;

		ThemeDTO that = (ThemeDTO) other;
		return Objects.equals(this.themeTitle, that.themeTitle)
				&& Objects.equals(this.themeDescription, that.themeDescription);
	}

	@Override
	protected int contentHashCode() {
		return Objects.hash(themeTitle, themeDescription);
	}

	@Override
	protected String getContentString() {
		return "title='" + themeTitle + "', questions=" + questions.size();
	}

	@Override
	protected void validate() {
		if (themeTitle == null || themeTitle.trim().isEmpty()) {
			throw new IllegalArgumentException("Theme title cannot be null or empty");
		}
		if (themeTitle.length() > 100) {
			throw new IllegalArgumentException("Theme title cannot exceed 100 characters");
		}
		if (themeDescription != null && themeDescription.length() > 500) {
			throw new IllegalArgumentException("Theme description cannot exceed 500 characters");
		}
	}
}
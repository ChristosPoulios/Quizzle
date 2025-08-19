package quizlogic.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import constants.ValidationConstants;
import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a quiz theme (category).
 * <p>
 * Each theme contains:
 * <ul>
 * <li>A title</li>
 * <li>An optional description</li>
 * <li>A list of associated quiz questions</li>
 * </ul>
 * This class extends {@link DataTransportObject} for consistent ID and
 * validation handling.
 * <p>
 * Themes can be persisted, edited, and displayed in the quiz application.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class ThemeDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	/** The title of the theme */
	private String themeTitle;

	/** An optional descriptive text for the theme */
	private String themeDescription;

	/** The list of quiz questions associated with this theme */
	private List<QuestionDTO> questions;

	/**
	 * Default constructor for creating a new ThemeDTO. Initializes an empty
	 * question list.
	 */
	public ThemeDTO() {
		super();
		this.questions = new ArrayList<>();
	}

	/**
	 * Constructor for creating an existing persisted ThemeDTO.
	 *
	 * @param id unique ID of the theme
	 */
	public ThemeDTO(int id) {
		super(id);
		this.questions = new ArrayList<>();
	}

	/**
	 * Constructor for creating a theme with title and description.
	 *
	 * @param themeTitle       the title of the theme
	 * @param themeDescription the descriptive text of the theme
	 */
	public ThemeDTO(String themeTitle, String themeDescription) {
		super();
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
	 * @param themeTitle the new theme title
	 */
	public void setThemeTitle(String themeTitle) {
		this.themeTitle = themeTitle;
	}

	/**
	 * Gets the descriptive text of the theme.
	 *
	 * @return the theme description
	 */
	public String getThemeDescription() {
		return themeDescription;
	}

	/**
	 * Sets the descriptive text of the theme.
	 *
	 * @param themeDescription the new theme description
	 */
	public void setThemeDescription(String themeDescription) {
		this.themeDescription = themeDescription;
	}

	/**
	 * Gets the list of associated questions.
	 *
	 * @return list of questions
	 */
	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	/**
	 * Sets the list of associated questions.
	 *
	 * @param questions a list of questions, if null an empty list will be assigned
	 */
	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions != null ? questions : new ArrayList<>();
	}

	/**
	 * Legacy method - alias for {@link #getThemeTitle()}.
	 * <p>
	 * Provided for GUI compatibility.
	 *
	 * @return theme title
	 */
	public String getTitle() {
		return getThemeTitle();
	}

	/**
	 * Legacy method - alias for {@link #setThemeTitle(String)}.
	 *
	 * @param title the theme title
	 */
	public void setTitle(String title) {
		setThemeTitle(title);
	}

	/**
	 * Legacy method - alias for {@link #getThemeDescription()}.
	 *
	 * @return theme description
	 */
	public String getText() {
		return getThemeDescription();
	}

	/**
	 * Gets a random question from the theme.
	 *
	 * @return a random {@link QuestionDTO} or null if no questions are available
	 */
	public QuestionDTO getRandomQuestion() {
		if (questions.isEmpty()) {
			return null;
		}
		int randomIndex = (int) (Math.random() * questions.size());
		return questions.get(randomIndex);
	}

	/**
	 * Content equality comparison for new (unsaved) themes.
	 *
	 * @param other another {@link DataTransportObject} to compare
	 * @return true if titles and descriptions match
	 */
	@Override
	protected boolean contentEquals(DataTransportObject other) {
		if (!(other instanceof ThemeDTO))
			return false;
		ThemeDTO that = (ThemeDTO) other;
		return Objects.equals(this.themeTitle, that.themeTitle)
				&& Objects.equals(this.themeDescription, that.themeDescription);
	}

	/**
	 * Computes a hash code based on the theme title and description.
	 *
	 * @return hash code value
	 */
	@Override
	protected int contentHashCode() {
		return Objects.hash(themeTitle, themeDescription);
	}

	/**
	 * Creates a concise string summary of the theme.
	 *
	 * @return formatted string showing title and number of questions
	 */
	@Override
	protected String getContentString() {
		return "title='" + themeTitle + "', questions=" + questions.size();
	}

	/**
	 * Validates theme data.
	 * <ul>
	 * <li>Theme title must not be null or empty</li>
	 * <li>Theme title length must not exceed {@value ValidationConstants#THEME_TITLE_MAX_LENGTH} characters</li>
	 * <li>Theme description, if present, must not exceed {@value ValidationConstants#THEME_DESCRIPTION_MAX_LENGTH} characters</li>
	 * </ul>
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	@Override
	protected void validate() {
		if (themeTitle == null || themeTitle.trim().isEmpty()) {
			throw new IllegalArgumentException("Theme title cannot be null or empty");
		}
		if (themeTitle.length() > ValidationConstants.THEME_TITLE_MAX_LENGTH) {
			throw new IllegalArgumentException("Theme title cannot exceed " + ValidationConstants.THEME_TITLE_MAX_LENGTH + " characters");
		}
		if (themeDescription != null && themeDescription.length() > ValidationConstants.THEME_DESCRIPTION_MAX_LENGTH) {
			throw new IllegalArgumentException("Theme description cannot exceed " + ValidationConstants.THEME_DESCRIPTION_MAX_LENGTH + " characters");
		}
	}
}
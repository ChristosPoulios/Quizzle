package quizlogic.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import constants.UserStringConstants;
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
public class ThemeDTO extends DataTransportObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Unique identifier for the theme */
	private int id;
	
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
	 * Gets the unique identifier for this theme.
	 * 
	 * @return the theme ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for this theme.
	 * 
	 * @param id the theme ID
	 */
	public void setId(int id) {
		this.id = id;
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
	 * Returns a random question from this theme, avoiding recently asked questions.
	 * Uses improved algorithm to ensure variety in question selection.
	 * 
	 * @return A randomly selected question from this theme, or null if no questions
	 *         exist
	 */
	public QuestionDTO getRandomQuestion() {
		if (questions.isEmpty()) {
			return null;
		}

		java.util.List<QuestionDTO> availableQuestions = new java.util.ArrayList<>(questions);

		if (questions.size() > 3) {

			int skipIndex = (int) (Math.random() * Math.min(3, questions.size()));
			if (skipIndex < availableQuestions.size()) {
				availableQuestions.remove(skipIndex);
			}
		}

		if (availableQuestions.isEmpty()) {

			int randomIndex = (int) (Math.random() * questions.size());
			return questions.get(randomIndex);
		}

		int randomIndex = (int) (Math.random() * availableQuestions.size());
		return availableQuestions.get(randomIndex);
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
	 * <li>Theme title length must not exceed
	 * {@value ValidationConstants#THEME_TITLE_MAX_LENGTH} characters</li>
	 * <li>Theme description, if present, must not exceed
	 * {@value ValidationConstants#THEME_DESCRIPTION_MAX_LENGTH} characters</li>
	 * </ul>
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	@Override
	protected void validate() {
		if (themeTitle == null || themeTitle.trim().isEmpty()) {
			throw new IllegalArgumentException(UserStringConstants.ERROR_THEME_TITLE_NULL_OR_EMPTY);
		}
		if (themeTitle.length() > ValidationConstants.THEME_TITLE_MAX_LENGTH) {
			throw new IllegalArgumentException(String.format(UserStringConstants.ERROR_THEME_TITLE_TOO_LONG,
					ValidationConstants.THEME_TITLE_MAX_LENGTH));
		}
		if (themeDescription != null && themeDescription.length() > ValidationConstants.THEME_DESCRIPTION_MAX_LENGTH) {
			throw new IllegalArgumentException(String.format(UserStringConstants.ERROR_THEME_DESCRIPTION_TOO_LONG,
					ValidationConstants.THEME_DESCRIPTION_MAX_LENGTH));
		}
	}
}
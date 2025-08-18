package quizlogic.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a quiz theme or category.
 * <p>
 * A Theme groups multiple quiz questions under a common title and optional
 * description. Themes can be persisted, edited, and displayed within the quiz
 * application.
 * <p>
 * This class extends {@link DataTransportObject} and inherits common DTO
 * functionality such as validation, equality checks, and serialization
 * compatibility.
 * <p>
 * Each theme maintains a list of quiz questions associated with it,
 * facilitating themed quizzes.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class ThemeDTO extends DataTransportObject {

	private static final long serialVersionUID = 1L;

	/**
	 * The title or name of the theme (e.g., "Science", "History").
	 */
	private String themeTitle;

	/**
	 * An optional descriptive text providing additional details about the theme.
	 */
	private String themeDescription;

	/**
	 * The list of quiz questions associated with this theme. Typically contains
	 * objects of type {@link QuestionDTO}.
	 */
	private List<QuestionDTO> questions;

	/**
	 * Default constructor initializing an empty list of questions.
	 */
	public ThemeDTO() {
		super();
		this.questions = new ArrayList<>();
	}

	/**
	 * Constructs a ThemeDTO with a specified ID for representing existing persisted
	 * themes. Initializes the questions list to empty.
	 *
	 * @param id the unique identifier of the persisted theme
	 */
	public ThemeDTO(int id) {
		super(id);
		this.questions = new ArrayList<>();
	}

	/**
	 * Constructs a new ThemeDTO with the specified title and description.
	 * Initializes the questions list to empty.
	 *
	 * @param themeTitle       the title of the theme
	 * @param themeDescription an optional description of the theme
	 */
	public ThemeDTO(String themeTitle, String themeDescription) {
		super();
		this.themeTitle = themeTitle;
		this.themeDescription = themeDescription;
		this.questions = new ArrayList<>();
	}

	/**
	 * Retrieves the title of this theme.
	 *
	 * @return the theme title as a String
	 */
	public String getThemeTitle() {
		return themeTitle;
	}

	/**
	 * Sets the title of this theme.
	 *
	 * @param themeTitle the title to set for this theme
	 */
	public void setThemeTitle(String themeTitle) {
		this.themeTitle = themeTitle;
	}

	/**
	 * Retrieves the description text for this theme.
	 *
	 * @return the theme description or null if not set
	 */
	public String getThemeDescription() {
		return themeDescription;
	}

	/**
	 * Sets the description text of this theme.
	 *
	 * @param themeDescription the descriptive text to assign
	 */
	public void setThemeDescription(String themeDescription) {
		this.themeDescription = themeDescription;
	}

	/**
	 * Returns the list of quiz questions associated with this theme.
	 *
	 * @return a list of {@link QuestionDTO} objects under this theme
	 */
	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	/**
	 * Assigns the list of questions to this theme.
	 *
	 * @param questions a list containing quiz questions belonging to this theme
	 */
	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions != null ? questions : new ArrayList<>();
	}

	/**
	 * Legacy method - alias for {@link #getThemeTitle()}.
	 *
	 * @return theme title
	 */
	public String getTitle() {
		return getThemeTitle();
	}

	/**
	 * Legacy method - alias for {@link #setThemeTitle(String)}.
	 *
	 * @param title the new title to set for this theme
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
	 * Returns a random question from the theme's list of questions.
	 *
	 * @return a randomly selected {@link QuestionDTO} or null if no questions are
	 *         present
	 */
	public QuestionDTO getRandomQuestion() {
		if (questions.isEmpty()) {
			return null;
		}
		int randomIndex = (int) (Math.random() * questions.size());
		return questions.get(randomIndex);
	}

	/**
	 * Compares the content of this ThemeDTO with another DTO for equality when new
	 * (unsaved).
	 * <p>
	 * Equality is determined by matching theme title and description content.
	 *
	 * @param other another {@link DataTransportObject} to compare against
	 * @return true if both themes have matching titles and descriptions, false
	 *         otherwise
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
	 * Computes a hash code based on the main content of this theme.
	 * <p>
	 * Used when entity ID is not yet assigned (new entity).
	 *
	 * @return hash code derived from theme title and description
	 */
	@Override
	protected int contentHashCode() {
		return Objects.hash(themeTitle, themeDescription);
	}

	/**
	 * Generates a concise string summary describing the theme for debugging.
	 * <p>
	 * Includes the theme title and the count of associated questions.
	 *
	 * @return string representation summarizing the main theme content
	 */
	@Override
	protected String getContentString() {
		return "title='" + themeTitle + "', questions=" + questions.size();
	}

	/**
	 * Validates this theme's data to ensure it meets constraints.
	 * <p>
	 * Theme title must not be null, empty, or exceed 100 characters. Theme
	 * description, if present, cannot exceed 500 characters.
	 * <p>
	 * Throws {@link IllegalArgumentException} if validation fails.
	 */
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

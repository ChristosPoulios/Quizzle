package persistence.serialization.dao;

import persistence.DataAccessObject;

/**
 * Serializable DAO (Data Access Object) representing a quiz question for use
 * with file-based serialization persistence.
 * <p>
 * This class is a simple container for question data and inherits the unique ID
 * handling from {@link persistence.DataAccessObject}. It is intended for
 * scenarios where quiz data is stored and retrieved from serialized files
 * rather than a relational database.
 * 
 * <p>
 * Stores:
 * </p>
 * <ul>
 * <li>Unique question ID</li>
 * <li>Question title</li>
 * </ul>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuestionDAO_Serializable extends DataAccessObject {

	private static final long serialVersionUID = 1L;

	/** The title of the quiz question */
	private String title;

	/**
	 * Gets the question title.
	 *
	 * @return question title as a String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the question title.
	 *
	 * @param title the new question title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}

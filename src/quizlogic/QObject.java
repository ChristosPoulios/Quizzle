package quizlogic;

import java.io.Serializable;

/**
 * Base class for all quiz objects providing a common ID field.
 *
 * This abstract base class provides identification functionality for all
 * quiz-related objects like themes, questions, and answers.
 *
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Unique identifier for this object */
	private int id = -1;


	public QObject(int id2) {
		super();
		this.id = id2;
	}

	/**
	 * Gets the unique identifier of this object.
	 * 
	 * @return The object ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identifier of this object.
	 * 
	 * @param id The object ID to set
	 */
	public void setId(int id) {
		this.id = id;
	}
}

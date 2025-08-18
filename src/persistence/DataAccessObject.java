package persistence;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface defining the contract for quiz data persistence operations.
 * <p>
 * Implementations of this interface can use various storage backends, such as
 * relational databases, file systems, or in-memory stores.
 * <p>
 * All methods operate using Data Transfer Objects (DTOs) from the
 * {@code quizlogic.dto} package to separate persistence logic from business
 * logic.
 * 
 * <p>
 * This interface covers operations to:
 * <ul>
 * <li>Retrieve random or themed quiz questions</li>
 * <li>Retrieve collections of quiz themes, questions, and answers</li>
 * <li>Save or update themes, questions, and answers</li>
 * <li>Delete themes, questions, and answers</li>
 * </ul>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class DataAccessObject implements Serializable {

	/** Serial version UID for serialization compatibility */
	private static final long serialVersionUID = 1L;

	/** Unique entity ID, corresponds to the primary key in the database */
	private int id = -1;

	/**
	 * Gets the unique ID for this DAO entity.
	 *
	 * @return the entity ID, or -1 if it is not yet set
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique ID for this DAO entity.
	 *
	 * @param id the new entity ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Populates this DAO's fields from a {@link ResultSet}.
	 * <p>
	 * Reads the "id" column and assigns it to this object's ID.
	 *
	 * @param rs the {@link ResultSet} containing the row data
	 * @throws SQLException if the ResultSet is null or reading fails
	 */
	public void fromResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			this.id = rs.getInt("id");
		} else {
			throw new SQLException("ResultSet is null");
		}
	}
}

package persistence;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base Data Access Object (DAO) class providing common fields and methods for
 * all persistence layer entities.
 * <p>
 * Each DAO maps a database row to a Java object and contains:
 * <ul>
 * <li>A unique identifier (ID)</li>
 * <li>Basic setter/getter for the ID</li>
 * <li>Utility to populate the DAO from a {@link java.sql.ResultSet}</li>
 * </ul>
 * This base class is extended by DAO implementations for specific entities.
 * <p>
 * Implements {@link java.io.Serializable} for persistence via serialization if
 * needed.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class DataAccessObject implements Serializable {

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

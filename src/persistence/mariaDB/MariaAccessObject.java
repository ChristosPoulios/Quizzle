package persistence.mariaDB;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import persistence.DataAccessObject;

/**
 * Abstract base class for all MariaDB Data Access Objects (DAOs).
 * <p>
 * Extends {@link persistence.DataAccessObject} with database-specific
 * functionality and common metadata required for MariaDB persistence.
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Defines the common entity ID handling for MariaDB entities</li>
 * <li>Declares abstract SQL statement methods for SELECT, INSERT, UPDATE</li>
 * <li>Provides an abstract contract for setting {@link PreparedStatement}
 * parameters</li>
 * <li>Declares validation logic for subclasses</li>
 * </ul>
 * <p>
 * Subclasses must implement all abstract methods to perform database operations
 * for specific entities like Themes, Questions, or Answers.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public abstract class MariaAccessObject extends DataAccessObject {

	private static final long serialVersionUID = 1L;

	/** Unique identifier of the entity in the database */
	private int id;

	/**
	 * Default constructor for a new entity (unsaved).
	 * <p>
	 * Initializes the ID to 0, indicating this is a new entity that has not been
	 * persisted yet.
	 */
	public MariaAccessObject() {
		this.id = 0;
	}

	/**
	 * Constructor for an existing entity with a specific ID.
	 * <p>
	 * Initializes the ID to the provided value, indicating this entity has been
	 * persisted in the database.
	 *
	 * @param id the unique identifier of the existing entity
	 */
	public MariaAccessObject(int id) {
		this.id = id;
	}

	/**
	 * Gets the entity ID.
	 *
	 * @return the ID of the entity
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the entity ID.
	 *
	 * @param id the ID to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the SQL SELECT statement for this entity.
	 *
	 * @return SQL SELECT statement string
	 */
	public abstract String getSelectStatement();

	/**
	 * Returns the SQL INSERT statement for this entity.
	 *
	 * @return SQL INSERT statement string
	 */
	public abstract String getInsertStatement();

	/**
	 * Returns the SQL UPDATE statement for this entity.
	 *
	 * @return SQL UPDATE statement string
	 */
	public abstract String getUpdateStatement();

	/**
	 * Checks if this entity is new (unsaved).
	 *
	 * @return true if ID is less than or equal to 0, false otherwise
	 */
	public abstract boolean isNew();

	/**
	 * Sets the parameters on the given {@link PreparedStatement} for executing SQL
	 * INSERT or UPDATE operations.
	 *
	 * @param ps the prepared statement to configure
	 * @throws SQLException if setting parameters fails
	 */
	public abstract void setPreparedStatementParameters(PreparedStatement ps) throws SQLException;

	/**
	 * Template method for entity-specific validation logic. Subclasses should
	 * override this to check constraints and throw {@link IllegalArgumentException}
	 * if data is invalid.
	 *
	 * @return true if validation passed without exception
	 */
	protected abstract boolean performValidation();
}

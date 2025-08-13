package persistence.mariaDB;

import java.sql.PreparedStatement;
import persistence.DataAccessObject;

/**
 * Abstract base class for all MariaDB entities. Extends DataAccessObject with
 * database-specific functionalities.
 */
public abstract class MariaAccessObject extends DataAccessObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;

	/**
	 * Default constructor
	 */
	public MariaAccessObject() {
		this.id = 0;
	}

	/**
	 * Constructor with ID
	 * 
	 * @param id the entity ID
	 */
	public MariaAccessObject(int id) {
		this.id = id;
	}

	/**
	 * Gets the entity ID
	 * 
	 * @return the ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the entity ID
	 * 
	 * @param id the ID to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the SQL SELECT statement for this entity
	 * 
	 * @return SQL SELECT statement
	 */
	public abstract String getSelectStatement();

	/**
	 * Returns the SQL INSERT statement for this entity
	 * 
	 * @return SQL INSERT statement
	 */
	public abstract String getInsertStatement();

	/**
	 * Returns the SQL UPDATE statement for this entity
	 * 
	 * @return SQL UPDATE statement
	 */
	public abstract String getUpdateStatement();

	/**
	 * Sets parameters for prepared statement
	 * 
	 * @param preparedStatement the prepared statement to configure
	 * @return configured statement as string representation
	 */
	public abstract String getPreparedStatement(PreparedStatement preparedStatement);

	/**
	 * Template method for entity validation logic
	 */
	protected abstract void performValidation();

	/**
	 * Template method for entity-specific information
	 * 
	 * @return entity-specific info string
	 */
	protected abstract String getEntityInfo();
}

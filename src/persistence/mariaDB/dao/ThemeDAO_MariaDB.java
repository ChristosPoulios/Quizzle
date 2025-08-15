package persistence.mariaDB.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.mariaDB.MariaAccessObject;
import quizlogic.dto.ThemeDTO;

/**
 * MariaDB Data Access Object (DAO) for quiz themes.
 * <p>
 * Maps the "Theme" table rows to {@link ThemeDTO} and vice versa. Provides CRUD
 * operations and validation logic for theme entities.
 * </p>
 * <p>
 * Database columns include:
 * <ul>
 * <li>id (INT): primary key</li>
 * <li>title (VARCHAR): theme title</li>
 * <li>description (TEXT): theme description</li>
 * </ul>
 * </p>
 *
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class ThemeDAO_MariaDB extends MariaAccessObject {

	private static final long serialVersionUID = 1L;

	/** SQL INSERT statement for themes */
	private final String SQL_INSERT = "INSERT INTO Theme (title, description) VALUES (?, ?)";

	/** SQL UPDATE statement for themes */
	private final String SQL_UPDATE = "UPDATE Theme SET title = ?, description = ? WHERE id = ?";

	/** SQL SELECT statement for themes */
	private final String SQL_SELECT = "SELECT id, title, description FROM Theme";

	/** SQL DELETE statement for themes */
	private final String SQL_DELETE = "DELETE FROM Theme WHERE id = ?";

	/** Title of the theme */
	private String themeTitle;

	/** Description of the theme */
	private String themeDescription;

	/**
	 * Default constructor for creating a new (unsaved) Theme DAO instance.
	 */
	public ThemeDAO_MariaDB() {
		super();
	}

	/**
	 * Constructs a Theme DAO for an existing theme identified by its ID.
	 *
	 * @param id the database ID of the theme
	 */
	public ThemeDAO_MariaDB(int id) {
		super(id);
	}

	/**
	 * Constructs a Theme DAO with all fields explicitly set.
	 *
	 * @param id               the theme ID
	 * @param themeTitle       the title of the theme
	 * @param themeDescription the description of the theme
	 */
	public ThemeDAO_MariaDB(int id, String themeTitle, String themeDescription) {
		super(id);
		this.themeTitle = themeTitle;
		this.themeDescription = themeDescription;
	}

	/**
	 * Constructs a Theme DAO from a {@link ThemeDTO} instance.
	 *
	 * @param dto the DTO containing theme data
	 */
	public ThemeDAO_MariaDB(ThemeDTO dto) {
		super();
		this.themeTitle = dto.getThemeTitle();
		this.themeDescription = dto.getThemeDescription();
	}

	/**
	 * Returns the SQL SELECT statement for retrieving themes.
	 *
	 * @return SQL SELECT statement string
	 */
	@Override
	public String getSelectStatement() {
		return SQL_SELECT;
	}

	/**
	 * Returns the SQL INSERT statement for theme creation.
	 *
	 * @return SQL INSERT statement string
	 */
	@Override
	public String getInsertStatement() {
		return SQL_INSERT;
	}

	/**
	 * Returns the SQL UPDATE statement for theme updates.
	 *
	 * @return SQL UPDATE statement string
	 */
	@Override
	public String getUpdateStatement() {
		return SQL_UPDATE;
	}

	/**
	 * Returns the SQL DELETE statement for theme deletion.
	 *
	 * @return SQL DELETE statement string
	 */
	public String getDeleteStatement() {
		return SQL_DELETE;
	}

	/**
	 * Checks if this DAO instance represents a new (unsaved) theme.
	 *
	 * @return true if the theme is new '(ID <= 0)', false otherwise
	 */
	@Override
	public boolean isNew() {
		return getId() <= 0;
	}

	/**
	 * Sets the parameters on a {@link PreparedStatement} before executing an INSERT
	 * or UPDATE in the database.
	 *
	 * @param ps the prepared statement to set parameters on
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	public void setPreparedStatementParameters(PreparedStatement ps) throws SQLException {
		if (isNew()) {
			ps.setString(1, themeTitle);
			ps.setString(2, themeDescription);
		} else {
			ps.setString(1, themeTitle);
			ps.setString(2, themeDescription);
			ps.setInt(3, getId());
		}
	}

	/**
	 * Validates the content of this theme. Throws an
	 * {@link IllegalArgumentException} if validation fails.
	 *
	 * @return true if validation succeeds
	 */
	@Override
	public boolean performValidation() {
		if (themeTitle == null || themeTitle.trim().isEmpty()) {
			throw new IllegalArgumentException("Theme title cannot be empty");
		}
		return true;
	}

	/**
	 * Populates this DAO's fields from the current row of a {@link ResultSet}.
	 *
	 * @param rs the result set positioned at the desired record
	 * @throws SQLException if an error occurs reading the result set
	 */
	public void fromResultSet(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		this.themeTitle = rs.getString("title");
		this.themeDescription = rs.getString("description");
	}

	/**
	 * Converts this DAO to a {@link ThemeDTO} for data transfer.
	 *
	 * @return a populated ThemeDTO with this DAO's data
	 */
	public ThemeDTO forTransport() {
		ThemeDTO dto = new ThemeDTO();
		dto.setId(getId());
		dto.setThemeTitle(themeTitle);
		dto.setThemeDescription(themeDescription);
		return dto;
	}

	/**
	 * Creates a Theme DAO instance from a {@link ThemeDTO}.
	 *
	 * @param dto the DTO instance containing theme data
	 * @return a new ThemeDAO_MariaDB initialized with the DTO data
	 */
	public static ThemeDAO_MariaDB fromTransport(ThemeDTO dto) {
		ThemeDAO_MariaDB dao = new ThemeDAO_MariaDB();
		dao.setId(dto.getId());
		dao.setThemeTitle(dto.getThemeTitle());
		dao.setThemeDescription(dto.getThemeDescription());
		return dao;
	}

	/**
	 * Returns the title of the theme.
	 *
	 * @return the theme title
	 */
	public String getThemeTitle() {
		return themeTitle;
	}

	/**
	 * Sets the title of the theme.
	 *
	 * @param themeTitle the new title for the theme
	 */
	public void setThemeTitle(String themeTitle) {
		this.themeTitle = themeTitle;
	}

	/**
	 * Returns the description of the theme.
	 *
	 * @return the theme description
	 */
	public String getThemeDescription() {
		return themeDescription;
	}

	/**
	 * Sets the description of the theme.
	 *
	 * @param themeDescription the new description for the theme
	 */
	public void setThemeDescription(String themeDescription) {
		this.themeDescription = themeDescription;
	}
}

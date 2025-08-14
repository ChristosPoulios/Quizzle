package persistence.mariaDB.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.mariaDB.MariaAccessObject;
import quizlogic.dto.ThemeDTO;

/**
 * Data Access Object for Theme entities in MariaDB Updated to match quizzle.sql
 * schema
 */
public class ThemeDAO_MariaDB extends MariaAccessObject {

	private static final long serialVersionUID = 1L;

	private final String SQL_INSERT = "INSERT INTO Theme (title, description) VALUES (?, ?)";
	private final String SQL_UPDATE = "UPDATE Theme SET title = ?, description = ? WHERE id = ?";
	private final String SQL_SELECT = "SELECT id, title, description FROM Theme";
	private final String SQL_DELETE = "DELETE FROM Theme WHERE id = ?";

	private String title;
	private String description;

	/**
	 * Default constructor
	 */
	public ThemeDAO_MariaDB() {
		super();
	}

	/**
	 * Constructor with ID
	 */
	public ThemeDAO_MariaDB(int id) {
		super(id);
	}

	/**
	 * Full constructor
	 */
	public ThemeDAO_MariaDB(int id, String title, String description) {
		super(id);
		this.title = title;
		this.description = description;
	}

	public String getThemeTitle() {
		return title;
	}

	public void setThemeTitle(String title) {
		this.title = title;
	}

	public String getThemeDescription() {
		return description;
	}

	public void setThemeDescription(String description) {
		this.description = description;
	}

	@Override
	public String getSelectStatement() {
		return SQL_SELECT;
	}

	@Override
	public String getInsertStatement() {
		return SQL_INSERT;
	}

	@Override
	public String getUpdateStatement() {
		return SQL_UPDATE;
	}

	public String getDeleteStatement() {
		return SQL_DELETE;
	}

	@Override
	public boolean isNew() {
		return getId() <= 0;
	}

	@Override
	public void setPreparedStatementParameters(PreparedStatement ps) throws SQLException {
		if (isNew()) {
			// INSERT: title, description
			ps.setString(1, title);
			ps.setString(2, description);
		} else {
			// UPDATE: title, description, id
			ps.setString(1, title);
			ps.setString(2, description);
			ps.setInt(3, getId());
		}
	}

	@Override
	public boolean performValidation() {
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Theme title cannot be empty");
		}
		return true;
	}

	/**
	 * Populates this DAO from a ResultSet
	 */
	public void fromResultSet(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		this.title = rs.getString("title");
		this.description = rs.getString("description");
	}

	/**
	 * Creates a ThemeDTO for transport
	 */
	public ThemeDTO forTransport() {
		ThemeDTO dto = new ThemeDTO();
		dto.setId(getId());
		dto.setThemeTitle(title);
		dto.setThemeDescription(description);
		return dto;
	}

	/**
	 * Creates a ThemeDAO from a ThemeDTO
	 */
	public static ThemeDAO_MariaDB fromTransport(ThemeDTO dto) {
		ThemeDAO_MariaDB dao = new ThemeDAO_MariaDB();
		dao.setId(dto.getId());
		dao.setThemeTitle(dto.getThemeTitle());
		dao.setThemeDescription(dto.getThemeDescription());
		return dao;
	}
}
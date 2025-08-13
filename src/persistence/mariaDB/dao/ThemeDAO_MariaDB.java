package persistence.mariaDB.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.mariaDB.MariaAccessObject;
import quizlogic.dto.ThemeDTO;

/**
 * Data Access Object for Theme entities in MariaDB
 */
public class ThemeDAO_MariaDB extends MariaAccessObject {

	private static final long serialVersionUID = 1L;

	private final String SQL_INSERT = "";
	private final String SQL_UPDATE = "";
	private final String SQL_SELECT = "";
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

	@Override
	public void setPreparedStatementParameters(PreparedStatement ps) throws SQLException {
		if (isNew()) {
			ps.setString(1, this.title);
			ps.setString(2, this.description);
		} else {
			ps.setString(1, this.title);
			ps.setString(2, this.description);
			ps.setInt(3, getId());
		}
	}

	public boolean isNew() {
		return getId() <= 0;
	}

	@Override
	public void fromResultSet(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		this.title = rs.getString("title");
		this.description = rs.getString("description");
	}

	@Override
	protected void performValidation() {
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException("Title cannot be null or empty");
		}
		if (description == null) {
			throw new IllegalArgumentException("Description cannot be null");
		}
		if (description.length() > 500) {
			throw new IllegalArgumentException("Description cannot exceed 500 characters");
		}
	}

	@Override
	protected String getEntityInfo() {
		return "title='" + title + "'";
	}

	/**
	 * Converts this DAO to a DTO object
	 */
	public ThemeDTO forTransport() {
		ThemeDTO dto = new ThemeDTO();
		dto.setThemeTitle(this.title);
		dto.setThemeDescription(this.description);
		return dto;
	}

	/**
	 * Creates a DAO from a DTO object
	 */
	public static ThemeDAO_MariaDB fromTransport(ThemeDTO dto) {
		return new ThemeDAO_MariaDB(0, dto.getThemeTitle(), dto.getThemeDescription());
	}
}

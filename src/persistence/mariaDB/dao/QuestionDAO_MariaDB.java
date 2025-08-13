package persistence.mariaDB.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.mariaDB.MariaAccessObject;
import quizlogic.dto.QuestionDTO;

/**
 * Data Access Object for Question entities in MariaDB
 */
public class QuestionDAO_MariaDB extends MariaAccessObject {

	private static final long serialVersionUID = 1L;

	private final String SQL_INSERT = "INSERT INTO Question (title, text, theme_id) VALUES (?, ?, ?)";
	private final String SQL_UPDATE = "UPDATE Question SET title = ?, text = ?, theme_id = ? WHERE id = ?";
	private final String SQL_SELECT = "SELECT id, title, text, theme_id FROM Question";
	private final String SQL_DELETE = "DELETE FROM Question WHERE id = ?";

	private String title;
	private String text;
	private int themeId;

	/**
	 * Default constructor
	 */
	public QuestionDAO_MariaDB() {
		super();
	}

	/**
	 * Constructor with QuestionDTO from class diagram
	 */
	public QuestionDAO_MariaDB(QuestionDTO dto) {
		super();
		this.title = dto.getQuestionTitle();
		this.text = dto.getQuestionText();

	}

	/**
	 * Constructor with Object array from class diagram
	 */
	public QuestionDAO_MariaDB(Object[] row) {
		super();
		if (row.length >= 4) {
			setId((Integer) row[0]);
			this.title = (String) row[1];
			this.text = (String) row[2];
			this.themeId = (Integer) row[3];
		}
	}

	// ==================== Getters/Setters ====================

	/**
	 * Gets the question title
	 */
	public String getQuestionTitle() {
		return title;
	}

	/**
	 * Sets the question title
	 */
	public void setQuestionTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the question text
	 */
	public String getQuestionText() {
		return text;
	}

	public void setQuestionText(String text) {
		this.text = text;
	}

	public int getThemeId() {
		return themeId;
	}

	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

	// ==================== SQL Methods ====================

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
	public void setPreparedStatementParameters(PreparedStatement ps) throws SQLException {
		if (isNew()) {
			ps.setString(1, this.title);
			ps.setString(2, this.text);
			ps.setInt(3, this.themeId);
		} else {
			ps.setString(1, this.title);
			ps.setString(2, this.text);
			ps.setInt(3, this.themeId);
			ps.setInt(4, getId());
		}
	}

	public boolean isNew() {
		return getId() <= 0;
	}

	@Override
	public void fromResultSet(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		this.themeId = rs.getInt("theme_id");
		this.text = rs.getString("question_text");
	}

	@Override
	public boolean performValidation() {
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("Question text cannot be null or empty");
		}
		if (text.length() > 1000) {
			throw new IllegalArgumentException("Question text cannot exceed 1000 characters");
		}
		if (themeId <= 0) {
			throw new IllegalArgumentException("Theme ID must be positive");
		}
		return false;
	}

	@Override
	protected String getEntityInfo() {
		return "themeId=" + themeId + ", text='" + text + "'";
	}

	/**
	 * Converts this DAO to a DTO object
	 */
	public QuestionDTO forTransport() {
		QuestionDTO dto = new QuestionDTO();
		dto.setQuestionText(this.text);
		return dto;
	}

	/**
	 * Creates a DAO from a DTO object
	 */
	public static QuestionDAO_MariaDB fromTransport(QuestionDTO dto, int themeId) {
		QuestionDAO_MariaDB dao = new QuestionDAO_MariaDB(dto);
		dao.setThemeId(themeId);
		return dao;
	}
}
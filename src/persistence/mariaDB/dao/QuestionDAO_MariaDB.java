package persistence.mariaDB.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.mariaDB.MariaAccessObject;
import quizlogic.dto.QuestionDTO;

/**
 * Data Access Object for Question entities in MariaDB Updated to match
 * quizzle.sql schema
 */
public class QuestionDAO_MariaDB extends MariaAccessObject {

	private static final long serialVersionUID = 1L;

	// Updated SQL statements to match schema: Questions table with columns: id,
	// title, text, theme_id
	private final String SQL_INSERT = "INSERT INTO Questions (title, text, theme_id) VALUES (?, ?, ?)";
	private final String SQL_UPDATE = "UPDATE Questions SET title = ?, text = ?, theme_id = ? WHERE id = ?";
	private final String SQL_SELECT = "SELECT id, title, text, theme_id FROM Questions";
	private final String SQL_DELETE = "DELETE FROM Questions WHERE id = ?";

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
	 * Constructor with ID
	 */
	public QuestionDAO_MariaDB(int id) {
		super(id);
	}

	/**
	 * Full constructor
	 */
	public QuestionDAO_MariaDB(int id, String title, String text, int themeId) {
		super(id);
		this.title = title;
		this.text = text;
		this.themeId = themeId;
	}

	/**
	 * Constructor with QuestionDTO
	 */
	public QuestionDAO_MariaDB(QuestionDTO dto) {
		super();
		this.title = dto.getQuestionTitle();
		this.text = dto.getQuestionText();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

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
			// INSERT: title, text, theme_id
			ps.setString(1, title);
			ps.setString(2, text);
			ps.setInt(3, themeId);
		} else {
			// UPDATE: title, text, theme_id, id
			ps.setString(1, title);
			ps.setString(2, text);
			ps.setInt(3, themeId);
			ps.setInt(4, getId());
		}
	}

	@Override
	public boolean performValidation() {
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Question title cannot be empty");
		}
		if (text == null || text.trim().isEmpty()) {
			throw new IllegalArgumentException("Question text cannot be empty");
		}
		if (themeId <= 0) {
			throw new IllegalArgumentException("Valid theme ID is required");
		}
		return true;
	}

	/**
	 * Populates this DAO from a ResultSet
	 */
	public void fromResultSet(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		this.title = rs.getString("title");
		this.text = rs.getString("text");
		this.themeId = rs.getInt("theme_id");
	}

	/**
	 * Creates a QuestionDTO for transport
	 */
	public QuestionDTO forTransport() {
		QuestionDTO dto = new QuestionDTO();
		dto.setId(getId());
		dto.setQuestionTitle(title);
		dto.setQuestionText(text);
		return dto;
	}

	/**
	 * Creates a QuestionDAO from a QuestionDTO with theme ID
	 */
	public static QuestionDAO_MariaDB fromTransport(QuestionDTO dto, int themeId) {
		QuestionDAO_MariaDB dao = new QuestionDAO_MariaDB();
		dao.setId(dto.getId());
		dao.setTitle(dto.getQuestionTitle());
		dao.setText(dto.getQuestionText());
		dao.setThemeId(themeId);
		return dao;
	}
}
package persistence.mariaDB.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.mariaDB.MariaAccessObject;
import quizlogic.dto.QuestionDTO;

/**
 * MariaDB DAO (Data Access Object) for quiz questions.
 * <p>
 * Maps between the database table {@code Questions} and {@link QuestionDTO}
 * objects used for data transfer in the application.
 * <p>
 * Table structure:
 * <ul>
 * <li>id (INT, primary key)</li>
 * <li>title (VARCHAR) – optional short title of the question</li>
 * <li>text (TEXT) – the main question content</li>
 * <li>theme_id (INT, foreign key referencing Theme table)</li>
 * </ul>
 * 
 * Responsibilities:
 * <ul>
 * <li>Provide SQL statements for CRUD operations</li>
 * <li>Populate DAO from database ResultSets</li>
 * <li>Convert DAO to/from transport DTOs</li>
 * <li>Validate question data</li>
 * </ul>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuestionDAO_MariaDB extends MariaAccessObject {

	private static final long serialVersionUID = 1L;

	/** SQL INSERT statement for questions */
	private final String SQL_INSERT = "INSERT INTO Questions (title, text, theme_id) VALUES (?, ?, ?)";

	/** SQL UPDATE statement for questions */
	private final String SQL_UPDATE = "UPDATE Questions SET title = ?, text = ?, theme_id = ? WHERE id = ?";

	/** SQL SELECT statement for questions */
	private final String SQL_SELECT = "SELECT id, title, text, theme_id FROM Questions";

	/** SQL DELETE statement for questions */
	private final String SQL_DELETE = "DELETE FROM Questions WHERE id = ?";

	/** Question title */
	private String title;

	/** Question text */
	private String text;

	/** Associated theme ID */
	private int themeId;

	/**
	 * Default constructor for a new question DAO.
	 */
	public QuestionDAO_MariaDB() {
		super();
	}

	/**
	 * Constructor for existing question with an assigned ID.
	 *
	 * @param id the ID of the question
	 */
	public QuestionDAO_MariaDB(int id) {
		super(id);
	}

	/**
	 * Full constructor setting all fields.
	 *
	 * @param id      the question ID
	 * @param title   the question title
	 * @param text    the question text
	 * @param themeId the associated theme ID
	 */
	public QuestionDAO_MariaDB(int id, String title, String text, int themeId) {
		super(id);
		this.title = title;
		this.text = text;
		this.themeId = themeId;
	}

	/**
	 * Constructs from a QuestionDTO instance.
	 *
	 * @param dto the QuestionDTO containing data
	 */
	public QuestionDAO_MariaDB(QuestionDTO dto) {
		super();
		this.title = dto.getQuestionTitle();
		this.text = dto.getQuestionText();
	}

	/**
	 * Sets the parameters for a PreparedStatement based on the current DAO state.
	 * <p>
	 * Used for both INSERT and UPDATE operations.
	 *
	 * @param ps PreparedStatement to set parameters on
	 * @throws SQLException if setting parameters fails
	 */
	@Override
	public void setPreparedStatementParameters(PreparedStatement ps) throws SQLException {
		if (isNew()) {
			// INSERT
			ps.setString(1, title);
			ps.setString(2, text);
			ps.setInt(3, themeId);
		} else {
			// UPDATE
			ps.setString(1, title);
			ps.setString(2, text);
			ps.setInt(3, themeId);
			ps.setInt(4, getId());
		}
	}

	/**
	 * Validates the question data.
	 * <p>
	 * Ensures that title, text, and themeId are set correctly.
	 *
	 * @return true if validation passes
	 * @throws IllegalArgumentException if validation fails
	 */
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
	 * Populates this DAO from a ResultSet row.
	 *
	 * @param rs ResultSet containing question data
	 * @throws SQLException on database access error
	 */
	public void fromResultSet(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		this.title = rs.getString("title");
		this.text = rs.getString("text");
		this.themeId = rs.getInt("theme_id");
	}

	/**
	 * Converts this DAO into a transferable QuestionDTO.
	 *
	 * @return populated QuestionDTO
	 */
	public QuestionDTO forTransport() {
		QuestionDTO dto = new QuestionDTO();
		dto.setId(getId());
		dto.setQuestionTitle(title);
		dto.setQuestionText(text);
		return dto;
	}

	/**
	 * Creates a QuestionDAO_MariaDB from a DTO and a given theme ID.
	 *
	 * @param dto     question DTO
	 * @param themeId associated theme ID
	 * @return new QuestionDAO_MariaDB instance
	 */
	public static QuestionDAO_MariaDB fromTransport(QuestionDTO dto, int themeId) {
		QuestionDAO_MariaDB dao = new QuestionDAO_MariaDB();
		dao.setId(dto.getId());
		dao.setTitle(dto.getQuestionTitle());
		dao.setText(dto.getQuestionText());
		dao.setThemeId(themeId);
		return dao;
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
	 * Returns the SQL INSERT statement for adding new questions.
	 *
	 * @return SQL INSERT statement string
	 */
	@Override
	public String getInsertStatement() {
		return SQL_INSERT;
	}

	/**
	 * Returns the SQL UPDATE statement for modifying existing questions.
	 *
	 * @return SQL UPDATE statement string
	 */
	@Override
	public String getUpdateStatement() {
		return SQL_UPDATE;
	}

	/**
	 * Returns the SQL DELETE statement for removing questions.
	 *
	 * @return SQL DELETE statement string
	 */
	public String getDeleteStatement() {
		return SQL_DELETE;
	}

	/**
	 * Checks if this DAO represents a new question (ID <= 0).
	 *
	 * @return true if the question is new, false otherwise
	 */
	@Override
	public boolean isNew() {
		return getId() <= 0;
	}

	/**
	 * Returns the question title.
	 *
	 * @return the title of the question
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the question title.
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the question text.
	 *
	 * @return the text of the question
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the question text.
	 *
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the associated theme ID.
	 *
	 * @return the theme ID
	 */
	public String getQuestionText() {
		return text;
	}

	/**
	 * Sets the question text.
	 *
	 * @param text the text to set
	 */
	public void setQuestionText(String text) {
		this.text = text;
	}

	/**
	 * Returns the associated theme ID.
	 *
	 * @return the theme ID
	 */
	public int getThemeId() {
		return themeId;
	}

	/**
	 * Sets the associated theme ID.
	 *
	 * @param themeId the theme ID to set
	 */
	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

}

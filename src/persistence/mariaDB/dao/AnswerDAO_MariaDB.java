package persistence.mariaDB.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.mariaDB.MariaAccessObject;
import quizlogic.dto.AnswerDTO;

/**
 * MariaDB DAO (Data Access Object) for quiz answers.
 * <p>
 * Provides mapping between the database table {@code Answers} and the
 * {@link AnswerDTO} used for transport in the application.
 * <p>
 * Table structure:
 * <ul>
 * <li>id (INT, primary key)</li>
 * <li>text (VARCHAR) – the answer text</li>
 * <li>isCorrect (BOOLEAN) – whether this is the correct answer</li>
 * <li>question_id (INT, foreign key referencing Questions table)</li>
 * </ul>
 * 
 * Responsibilities:
 * <ul>
 * <li>Generate SQL statements for SELECT, INSERT, UPDATE, DELETE
 * operations</li>
 * <li>Map result sets from the database to DTOs</li>
 * <li>Map DTOs to SQL prepared statement parameters</li>
 * <li>Provide validation for answer data</li>
 * </ul>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class AnswerDAO_MariaDB extends MariaAccessObject {

	private static final long serialVersionUID = 1L;

	/** SQL INSERT statement for the Answers table */
	private final String SQL_INSERT = "INSERT INTO Answers (text, isCorrect, question_id) VALUES (?, ?, ?)";

	/** SQL UPDATE statement for the Answers table */
	private final String SQL_UPDATE = "UPDATE Answers SET text = ?, isCorrect = ?, question_id = ? WHERE id = ?";

	/** SQL SELECT statement for the Answers table */
	private final String SQL_SELECT = "SELECT id, text, isCorrect, question_id FROM Answers";

	/** SQL DELETE statement for the Answers table */
	private final String SQL_DELETE = "DELETE FROM Answers WHERE id = ?";

	/** Answer text */
	private String text;

	/** Whether the answer is correct */
	private boolean correct;

	/** ID of the associated question */
	private int questionId;

	/**
	 * Default constructor for creating new AnswerDAO_MariaDB instances.
	 */
	public AnswerDAO_MariaDB() {
		super();
	}

	/**
	 * Constructor with ID for existing entities.
	 *
	 * @param id answer ID
	 */
	public AnswerDAO_MariaDB(int id) {
		super(id);
	}

	/**
	 * Constructor with all fields for creating new answers.
	 *
	 * @param id         answer ID
	 * @param text       answer text
	 * @param correct    whether the answer is correct
	 * @param questionId associated question ID
	 */
	public AnswerDAO_MariaDB(int id, String text, boolean correct, int questionId) {
		super(id);
		this.text = text;
		this.correct = correct;
		this.questionId = questionId;
	}

	/**
	 * Constructor that initializes the DAO from an AnswerDTO.
	 *
	 * @param dto AnswerDTO containing answer data
	 */
	public AnswerDAO_MariaDB(AnswerDTO dto) {
		super();
		this.text = dto.getAnswerText();
		this.correct = dto.isCorrect();
	}

	/**
	 * Sets the parameters for a prepared statement based on this DAO's fields.
	 *
	 * @param ps PreparedStatement to set parameters for
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	public void setPreparedStatementParameters(PreparedStatement ps) throws SQLException {
		if (isNew()) {
			// INSERT
			ps.setString(1, text);
			ps.setBoolean(2, correct);
			ps.setInt(3, questionId);
		} else {
			// UPDATE
			ps.setString(1, text);
			ps.setBoolean(2, correct);
			ps.setInt(3, questionId);
			ps.setInt(4, getId());
		}
	}

	/**
	 * Validates the answer data.
	 * <p>
	 * Throws an exception if validation fails, otherwise returns true.
	 *
	 * @return true if validation passes
	 * @throws IllegalArgumentException if validation fails
	 */
	@Override
	public boolean performValidation() {
		if (text == null || text.trim().isEmpty()) {
			throw new IllegalArgumentException("Answer text cannot be empty");
		}
		if (questionId <= 0) {
			throw new IllegalArgumentException("Valid question ID is required");
		}
		return true;
	}

	/**
	 * Populates this DAO from a database {@link ResultSet}.
	 *
	 * @param rs ResultSet containing query results
	 * @throws SQLException if a database access error occurs
	 */
	public void fromResultSet(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		this.text = rs.getString("text");
		this.correct = rs.getBoolean("isCorrect");
		this.questionId = rs.getInt("question_id");
	}

	/**
	 * Converts this DAO to a transferable {@link AnswerDTO}.
	 *
	 * @return populated AnswerDTO
	 */
	public AnswerDTO forTransport() {
		AnswerDTO dto = new AnswerDTO();
		dto.setId(getId());
		dto.setAnswerText(text);
		dto.setCorrect(correct);
		return dto;
	}

	/**
	 * Creates a new AnswerDAO_MariaDB from a transport DTO and question ID.
	 *
	 * @param dto        AnswerDTO containing answer data
	 * @param questionId ID of the associated question
	 * @return new AnswerDAO_MariaDB instance
	 */
	public static AnswerDAO_MariaDB fromTransport(AnswerDTO dto, int questionId) {
		AnswerDAO_MariaDB dao = new AnswerDAO_MariaDB();
		dao.setId(dto.getId());
		dao.setText(dto.getAnswerText());
		dao.setCorrect(dto.isCorrect());
		dao.setQuestionId(questionId);
		return dao;
	}

	/**
	 * Returns validation errors as a CharSequence.
	 * <p>
	 * Checks if the answer text is empty and if the question ID is valid.
	 *
	 * @return validation errors as a String, or null if no errors
	 */
	public CharSequence getValidationErrors() {
		StringBuilder errors = new StringBuilder();
		if (text == null || text.trim().isEmpty()) {
			errors.append("Answer text cannot be empty.\n");
		}
		if (questionId <= 0) {
			errors.append("Valid question ID is required.\n");
		}
		return errors.length() > 0 ? errors.toString() : null;
	}

	/**
	 * Returns the answer text.
	 *
	 * @return the answer text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the answer text.
	 *
	 * @param text the answer text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Checks if this answer is marked as correct.
	 *
	 * @return true if the answer is correct, false otherwise
	 */
	public boolean isCorrect() {
		return correct;
	}

	/**
	 * Sets whether this answer is correct.
	 *
	 * @param correct true if the answer is correct, false otherwise
	 */
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	/**
	 * Returns the ID of the associated question.
	 *
	 * @return the question ID
	 */
	public int getQuestionId() {
		return questionId;
	}

	/**
	 * Sets the ID of the associated question.
	 *
	 * @param questionId the question ID to set
	 */
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	/**
	 * Returns the SQL SELECT statement for retrieving answers.
	 *
	 * @return SQL SELECT statement string
	 */
	@Override
	public String getSelectStatement() {
		return SQL_SELECT;
	}

	/**
	 * Returns the SQL INSERT statement for adding new answers.
	 *
	 * @return SQL INSERT statement string
	 */
	@Override
	public String getInsertStatement() {
		return SQL_INSERT;
	}

	/**
	 * Returns the SQL UPDATE statement for modifying existing answers.
	 *
	 * @return SQL UPDATE statement string
	 */
	@Override
	public String getUpdateStatement() {
		return SQL_UPDATE;
	}

	/**
	 * Returns the SQL DELETE statement for removing answers.
	 *
	 * @return SQL DELETE statement string
	 */
	public String getDeleteStatement() {
		return SQL_DELETE;
	}

	/**
	 * Checks if this DAO represents a new answer (ID <= 0).
	 *
	 * @return true if the answer is new, false otherwise
	 */
	@Override
	public boolean isNew() {
		return getId() <= 0;
	}
}

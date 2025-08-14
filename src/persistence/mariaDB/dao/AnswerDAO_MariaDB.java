package persistence.mariaDB.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.mariaDB.MariaAccessObject;
import quizlogic.dto.AnswerDTO;

/**
 * Data Access Object for Answer entities in MariaDB Updated to match
 * quizzle.sql schema
 */
public class AnswerDAO_MariaDB extends MariaAccessObject {

	private static final long serialVersionUID = 1L;

	// Updated SQL statements to match schema: Answers table with columns: id, text,
	// isCorrect, question_id
	private final String SQL_INSERT = "INSERT INTO Answers (text, isCorrect, question_id) VALUES (?, ?, ?)";
	private final String SQL_UPDATE = "UPDATE Answers SET text = ?, isCorrect = ?, question_id = ? WHERE id = ?";
	private final String SQL_SELECT = "SELECT id, text, isCorrect, question_id FROM Answers";
	private final String SQL_DELETE = "DELETE FROM Answers WHERE id = ?";

	private String text;
	private boolean correct;
	private int questionId;

	/**
	 * Default constructor
	 */
	public AnswerDAO_MariaDB() {
		super();
	}

	/**
	 * Constructor with ID
	 */
	public AnswerDAO_MariaDB(int id) {
		super(id);
	}

	/**
	 * Full constructor
	 */
	public AnswerDAO_MariaDB(int id, String text, boolean correct, int questionId) {
		super(id);
		this.text = text;
		this.correct = correct;
		this.questionId = questionId;
	}

	/**
	 * Constructor with AnswerDTO
	 */
	public AnswerDAO_MariaDB(AnswerDTO dto) {
		super();
		this.text = dto.getAnswerText();
		this.correct = dto.isCorrect();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
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
			// INSERT: text, isCorrect, question_id
			ps.setString(1, text);
			ps.setBoolean(2, correct);
			ps.setInt(3, questionId);
		} else {
			// UPDATE: text, isCorrect, question_id, id
			ps.setString(1, text);
			ps.setBoolean(2, correct);
			ps.setInt(3, questionId);
			ps.setInt(4, getId());
		}
	}

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
	 * Populates this DAO from a ResultSet
	 */
	public void fromResultSet(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		this.text = rs.getString("text");
		this.correct = rs.getBoolean("isCorrect");
		this.questionId = rs.getInt("question_id");
	}

	/**
	 * Creates an AnswerDTO for transport
	 */
	public AnswerDTO forTransport() {
		AnswerDTO dto = new AnswerDTO();
		dto.setId(getId());
		dto.setAnswerText(text);
		dto.setCorrect(correct);
		return dto;
	}

	/**
	 * Creates an AnswerDAO from an AnswerDTO with question ID
	 */
	public static AnswerDAO_MariaDB fromTransport(AnswerDTO dto, int questionId) {
		AnswerDAO_MariaDB dao = new AnswerDAO_MariaDB();
		dao.setId(dto.getId());
		dao.setText(dto.getAnswerText());
		dao.setCorrect(dto.isCorrect());
		dao.setQuestionId(questionId);
		return dao;
	}

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
}
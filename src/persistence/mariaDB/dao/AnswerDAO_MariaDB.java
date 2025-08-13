package persistence.mariaDB.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.mariaDB.MariaAccessObject;
import quizlogic.dto.AnswerDTO;

/**
 * Data Access Object for Answer entities in MariaDB
 */
public class AnswerDAO_MariaDB extends MariaAccessObject {
    
    private static final long serialVersionUID = 1L;
    
    // Final SQL constants from class diagram
    private final String SQL_INSERT = "";
    private final String SQL_UPDATE = "";
    private final String SQL_SELECT = "";
    
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
     * Constructor with AnswerDTO from class diagram
     */
    public AnswerDAO_MariaDB(AnswerDTO dto) {
        super();
        this.text = dto.getAnswerText();
        this.correct = dto.isCorrect();
    }
    
    /**
     * Constructor with Object array from class diagram
     */
    public AnswerDAO_MariaDB(Object[] row) {
        super();
        if (row.length >= 4) {
            setId((Integer) row[0]);
            this.questionId = (Integer) row[1];
            this.text = (String) row[2];
            this.correct = (Boolean) row[3];
        }
    }
    
    /**
     * setText method from class diagram (returns String - unusual but from diagram)
     */
    public String setText() {
        return text;
    }
    
    /**
     * Additional setter for practical use
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * isCorrect method from class diagram
     */
    public boolean isCorrect() {
        return correct;
    }
    
    /**
     * setCorrect method from class diagram
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
    
    /**
     * getQuestionId method from class diagram
     */
    public int getQuestionId() {
        return questionId;
    }
    
    /**
     * setQuestionId method from class diagram
     */
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
    
    @Override
    public void setPreparedStatementParameters(PreparedStatement ps) throws SQLException {
        if (isNew()) {
            ps.setInt(1, this.questionId);
            ps.setString(2, this.text);
            ps.setBoolean(3, this.correct);
        } else {
            ps.setInt(1, this.questionId);
            ps.setString(2, this.text);
            ps.setBoolean(3, this.correct);
            ps.setInt(4, getId());
        }
    }
    
    public boolean isNew() {
        return getId() <= 0;
    }
    
    @Override
    public void fromResultSet(ResultSet rs) throws SQLException {
        setId(rs.getInt("id"));
        this.questionId = rs.getInt("question_id");
        this.text = rs.getString("answer_text");
        this.correct = rs.getBoolean("is_correct");
    }
    
    @Override
    protected void performValidation() {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Answer text cannot be null or empty");
        }
        if (text.length() > 500) {
            throw new IllegalArgumentException("Answer text cannot exceed 500 characters");
        }
        if (questionId <= 0) {
            throw new IllegalArgumentException("Question ID must be positive");
        }
    }
    
    @Override
    protected String getEntityInfo() {
        return "questionId=" + questionId + ", text='" + text + "', correct=" + correct;
    }
    
    /**
     * Converts this DAO to a DTO object
     */
    public AnswerDTO forTransport() {
        AnswerDTO dto = new AnswerDTO();
        dto.setAnswerText(this.text);
        dto.setCorrect(this.correct);
        return dto;
    }
    
    /**
     * Creates a DAO from a DTO object
     */
    public static AnswerDAO_MariaDB fromTransport(AnswerDTO dto, int questionId) {
        AnswerDAO_MariaDB dao = new AnswerDAO_MariaDB(dto);
        dao.setQuestionId(questionId);
        return dao;
    }
}

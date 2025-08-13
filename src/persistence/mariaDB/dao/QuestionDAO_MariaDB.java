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
    
    // Final SQL constants from class diagram
    private final String SQL_INSERT = "";
    private final String SQL_UPDATE = "";
    private final String SQL_SELECT = "";
    
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
        this.text = dto.getQuestionText();
    }
    
    /**
     * Constructor with Object array from class diagram
     */
    public QuestionDAO_MariaDB(Object[] row) {
        super();
        if (row.length >= 3) {
            setId((Integer) row[0]);
            this.themeId = (Integer) row[1];
            this.text = (String) row[2];
        }
    }
    
    /**
     * getText method from class diagram
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
            ps.setInt(1, this.themeId);
            ps.setString(2, this.text);
        } else {
            ps.setInt(1, this.themeId);
            ps.setString(2, this.text);
            ps.setInt(3, getId());
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
    protected void performValidation() {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be null or empty");
        }
        if (text.length() > 1000) {
            throw new IllegalArgumentException("Question text cannot exceed 1000 characters");
        }
        if (themeId <= 0) {
            throw new IllegalArgumentException("Theme ID must be positive");
        }
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

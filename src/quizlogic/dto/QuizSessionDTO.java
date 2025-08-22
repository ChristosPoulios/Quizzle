package quizlogic.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import quizlogic.DataTransportObject;

/**
 * Data Transfer Object (DTO) representing a quiz session.
 * Tracks user progress through a quiz with timestamps and user answers.
 */
public class QuizSessionDTO extends DataTransportObject {
    private static final long serialVersionUID = 1L;
    
    private Date timestamp;
    private int userId;
    private List<UserAnswerDTO> userAnswers;
    
    public QuizSessionDTO() {
        super();
        this.timestamp = new Date();
        this.userId = -1; // Default user
        this.userAnswers = new ArrayList<>();
    }
    
    public QuizSessionDTO(int id) {
        super(id);
        this.timestamp = new Date();
        this.userId = -1;
        this.userAnswers = new ArrayList<>();
    }
    
    public QuizSessionDTO(Date timestamp, int userId) {
        super();
        this.timestamp = timestamp;
        this.userId = userId;
        this.userAnswers = new ArrayList<>();
    }
    
    // Getters and setters
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public List<UserAnswerDTO> getUserAnswers() {
        return userAnswers;
    }
    
    public void setUserAnswers(List<UserAnswerDTO> userAnswers) {
        this.userAnswers = userAnswers != null ? userAnswers : new ArrayList<>();
    }
    
    public void addUserAnswer(UserAnswerDTO userAnswer) {
        if (userAnswers == null) {
            userAnswers = new ArrayList<>();
        }
        userAnswers.add(userAnswer);
    }
    
    public void start() {
        this.timestamp = new Date();
    }
    
    public void end() {
        // Session end logic if needed
    }
    
    @Override
    protected boolean contentEquals(DataTransportObject other) {
        if (!(other instanceof QuizSessionDTO)) return false;
        QuizSessionDTO that = (QuizSessionDTO) other;
        return Objects.equals(this.timestamp, that.timestamp) &&
               this.userId == that.userId;
    }
    
    @Override
    protected int contentHashCode() {
        return Objects.hash(timestamp, userId);
    }
    
    @Override
    protected String getContentString() {
        return String.format("timestamp='%s', userId=%d, answers=%d", 
                timestamp, userId, userAnswers != null ? userAnswers.size() : 0);
    }
    
    @Override
    protected void validate() {
        if (timestamp == null) {
            throw new IllegalArgumentException("Quiz session timestamp cannot be null");
        }
        if (userId < 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
    }
}

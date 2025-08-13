package persistence;

import java.util.ArrayList;

import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Interface for quiz data operations.
 * Defines contract for data persistence operations.
 */
public interface QuizDataInterface {
    
    
    /**
     * Gets a random question from all available questions
     * @return random question or null if no questions available
     */
    QuestionDTO getRandomQuestion();
    
    /**
     * Gets a random question for a specific theme
     * @param theme the theme to get question from
     * @return random question from theme or null if no questions available
     */
    QuestionDTO getRandomQuestionFor(ThemeDTO theme);
    
    
    /**
     * Gets all available themes
     * @return list of all themes
     */
    ArrayList<ThemeDTO> getAllThemes();
    
    /**
     * Saves a theme to the data store
     * @param theme the theme to save
     * @return status message or result string
     */
    String saveTheme(ThemeDTO theme);
    
    /**
     * Deletes a theme from the data store
     * @param theme the theme to delete
     * @return status message or result string
     */
    String deleteTheme(ThemeDTO theme);
    
    
    /**
     * Gets all questions for a specific theme
     * @param theme the theme to get questions for
     * @return list of questions for the theme
     */
    ArrayList<QuestionDTO> getQuestions(ThemeDTO theme);
    
    /**
     * Saves a question to the data store
     * @param question the question to save
     * @return status message or result string
     */
    String saveQuestion(QuestionDTO question);
    
    /**
     * Deletes a question from the data store
     * @param question the question to delete
     * @return status message or result string
     */
    String deleteQuestion(QuestionDTO question);
    
    
    /**
     * Gets all answers for a specific question
     * @param question the question to get answers for
     * @return list of answers for the question
     */
    ArrayList<AnswerDTO> getAnswersFor(QuestionDTO question);
}

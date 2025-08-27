package persistence;

import java.util.ArrayList;

import constants.ConfigManager;
import persistence.mariaDB.DBManager;
import persistence.serialization.QuizDataManager;
import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Hybrid data manager that automatically falls back to file-based serialization 
 * when database connection is not available.
 * <p>
 * This manager tries to use the MariaDB database first, and automatically 
 * switches to the existing QuizDataManager from the serialization package 
 * if the database connection fails.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class DataManager implements QuizDataInterface {

    /** Singleton instance */
    private static DataManager instance;
    
    /** Database manager instance */
    private DBManager dbManager;
    
    /** Serialization manager instance (from existing package) */
    private QuizDataManager serializationManager;
    
    /** Flag indicating which storage method is currently active */
    private boolean usingDatabase = false;

    /**
     * Private constructor for singleton pattern.
     */
    private DataManager() {
        initializeDataInterface();
    }

    /**
     * Returns the singleton instance of DataManager.
     * 
     * @return DataManager instance
     */
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    /**
     * Initializes the appropriate data interface based on database availability.
     */
    private void initializeDataInterface() {
        // Try database connection first
        try {
            dbManager = DBManager.getInstance();
            dbManager.connect();
            if (dbManager.isConnected()) {
                usingDatabase = true;
                ConfigManager.debugPrint("DEBUG: Using MariaDB database for data storage");
                System.out.println("Database connection established successfully - using MariaDB storage");
                return;
            }
        } catch (Exception e) {
            ConfigManager.debugPrint("DEBUG: Database connection failed: " + e.getMessage());
            System.err.println("Database connection failed: " + e.getMessage());
        }

        // Fall back to serialization using existing QuizDataManager
        try {
            serializationManager = new QuizDataManager();
            usingDatabase = false;
            ConfigManager.debugPrint("DEBUG: Using file-based serialization storage");
            System.out.println("Using file-based storage (serialization)");
        } catch (Exception e) {
            ConfigManager.debugPrint("DEBUG: Serialization initialization failed: " + e.getMessage());
            System.err.println("ERROR: Both database and file storage initialization failed!");
            throw new RuntimeException("Cannot initialize any data storage method", e);
        }
    }

    /**
     * Returns whether the manager is currently using database storage.
     * 
     * @return true if using database, false if using serialization
     */
    public boolean isUsingDatabase() {
        return usingDatabase;
    }

    /**
     * Returns a description of the current storage method.
     * 
     * @return storage method description
     */
    public String getStorageMethodDescription() {
        return usingDatabase ? "MariaDB Database" : "File-based Serialization";
    }

    /**
     * Attempts to reconnect to the database if currently using serialization.
     * 
     * @return true if successfully switched to database, false if still using serialization
     */
    public boolean tryReconnectToDatabase() {
        if (usingDatabase) {
            return true; // Already using database
        }

        try {
            if (dbManager == null) {
                dbManager = DBManager.getInstance();
            }
            dbManager.connect();
            if (dbManager.isConnected()) {
                usingDatabase = true;
                ConfigManager.debugPrint("DEBUG: Successfully reconnected to database");
                System.out.println("Successfully reconnected to database");
                return true;
            }
        } catch (Exception e) {
            ConfigManager.debugPrint("DEBUG: Database reconnection failed: " + e.getMessage());
        }

        return false;
    }

    // QuizDataInterface implementation - delegates to appropriate manager

    @Override
    public QuestionDTO getRandomQuestion() {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.getRandomQuestion();
            } else {
                return serializationManager.getRandomQuestion();
            }
        });
    }

    @Override
    public QuestionDTO getRandomQuestionFor(ThemeDTO theme) {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.getRandomQuestionFor(theme);
            } else {
                return serializationManager.getRandomQuestionFor(theme);
            }
        });
    }

    @Override
    public ArrayList<ThemeDTO> getAllThemes() {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.getAllThemes();
            } else {
                return serializationManager.getAllThemes();
            }
        });
    }

    @Override
    public String saveTheme(ThemeDTO theme) {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.saveTheme(theme);
            } else {
                return serializationManager.saveTheme(theme);
            }
        });
    }

    @Override
    public String deleteTheme(ThemeDTO theme) {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.deleteTheme(theme);
            } else {
                return serializationManager.deleteTheme(theme);
            }
        });
    }

    @Override
    public ArrayList<QuestionDTO> getQuestionsFor(ThemeDTO theme) {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.getQuestionsFor(theme);
            } else {
                return serializationManager.getQuestionsFor(theme);
            }
        });
    }

    @Override
    public String saveQuestion(QuestionDTO question) {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.saveQuestion(question);
            } else {
                return serializationManager.saveQuestion(question);
            }
        });
    }

    @Override
    public String deleteQuestion(QuestionDTO question) {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.deleteQuestion(question);
            } else {
                return serializationManager.deleteQuestion(question);
            }
        });
    }

    @Override
    public ArrayList<AnswerDTO> getAnswersFor(QuestionDTO question) {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.getAnswersFor(question);
            } else {
                return serializationManager.getAnswersFor(question);
            }
        });
    }

    /**
     * Extended method: Saves a question and associates it with a theme.
     * 
     * @param question the question to save
     * @param theme the theme to associate with
     * @return result message
     */
    public String saveQuestion(QuestionDTO question, ThemeDTO theme) {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.saveQuestion(question, theme);
            } else {
                return serializationManager.saveQuestion(question, theme);
            }
        });
    }
    
    /**
     * Extended method: Saves an answer and associates it with a question.
     * 
     * @param answer the answer to save
     * @param question the question to associate with
     * @return result message
     */
    public String saveAnswer(AnswerDTO answer, QuestionDTO question) {
        return executeWithFallback(() -> {
            if (usingDatabase) {
                return dbManager.saveAnswer(answer, question);
            } else {
                return serializationManager.saveAnswer(answer, question);
            }
        });
    }

    /**
     * Executes a data operation with automatic fallback to serialization if database fails.
     */
    private <T> T executeWithFallback(DataOperation<T> operation) {
        try {
            return operation.execute();
        } catch (Exception e) {
            if (usingDatabase) {
                ConfigManager.debugPrint("DEBUG: Database operation failed, falling back to serialization: " + e.getMessage());
                System.err.println("Database operation failed, switching to file storage: " + e.getMessage());
                
                // Switch to serialization
                if (serializationManager == null) {
                    serializationManager = new QuizDataManager();
                }
                usingDatabase = false;
                
                // Retry the operation with serialization
                try {
                    return operation.execute();
                } catch (Exception serializationException) {
                    ConfigManager.debugPrint("DEBUG: Serialization operation also failed: " + serializationException.getMessage());
                    throw new RuntimeException("All storage methods failed", serializationException);
                }
            } else {
                ConfigManager.debugPrint("DEBUG: Serialization operation failed: " + e.getMessage());
                throw new RuntimeException("File storage operation failed", e);
            }
        }
    }

    @FunctionalInterface
    private interface DataOperation<T> {
        T execute() throws Exception;
    }

    /**
     * Closes the data manager and any active connections.
     */
    public void close() {
        if (dbManager != null && dbManager.isConnected()) {
            dbManager.disconnect();
            ConfigManager.debugPrint("DEBUG: Database connection closed");
        }
        ConfigManager.debugPrint("DEBUG: DataManager closed");
    }
}
package persistence.mariaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import constants.ConfigManager;
import constants.UserStringConstants;
import persistence.QuizDataInterface;
import persistence.mariaDB.dao.AnswerDAO_MariaDB;
import persistence.mariaDB.dao.QuestionDAO_MariaDB;
import persistence.mariaDB.dao.ThemeDAO_MariaDB;
import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * MariaDB-based implementation of the {@link persistence.QuizDataInterface}.
 * <p>
 * This class provides CRUD operations and queries for:
 * <ul>
 * <li>Quiz themes</li>
 * <li>Quiz questions</li>
 * <li>Quiz answers</li>
 * </ul>
 * It uses DAOs from the {@code persistence.mariaDB.dao} package to map between
 * database table rows and DTO objects from the {@code quizlogic.dto} package.
 * <p>
 * The DBManager is a <b>Singleton</b>, ensuring a single shared connection is
 * used throughout the application's lifecycle.
 * <p>
 * Connection configuration is loaded from the ConfigManager.
 * 
 * <b>Responsibilities:</b>
 * <ul>
 * <li>Manage lifecycle of the MariaDB connection</li>
 * <li>Implement persistence logic declared in {@link QuizDataInterface}</li>
 * <li>Maintain in-memory mappings between DTOs and their associated DAOs</li>
 * </ul>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class DBManager implements QuizDataInterface {

	/** Singleton instance */
	private static DBManager instance;

	/** Active database connection instance */
	private Connection connection;

	/** Configuration manager for database settings */
	private ConfigManager configManager;

	/** Map of ThemeDTO to ThemeDAO_MariaDB for caching */
	private Map<ThemeDTO, ThemeDAO_MariaDB> themeDaoMap = new HashMap<>();

	/** Map of QuestionDTO to QuestionDAO_MariaDB for caching */
	private Map<QuestionDTO, QuestionDAO_MariaDB> questionDaoMap = new HashMap<>();

	/** Map of AnswerDTO to AnswerDAO_MariaDB for caching */
	private Map<AnswerDTO, AnswerDAO_MariaDB> answerDaoMap = new HashMap<>();

	/**
	 * Private constructor (singleton). Use {@link #getInstance()} to access the
	 * shared instance.
	 */
	private DBManager() {
		configManager = ConfigManager.getInstance();
	}

	/**
	 * Returns the singleton instance of the DBManager.
	 *
	 * @return DBManager singleton
	 */
	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	/**
	 * Establishes a connection to the MariaDB database if not already connected.
	 *
	 * @throws RuntimeException if the connection fails
	 */
	public void connect() {
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName(configManager.getDatabaseDriver());
				connection = DriverManager.getConnection(
					configManager.getDatabaseUrl(),
					configManager.getDatabaseUser(),
					configManager.getDatabasePassword()
				);
				connection.setAutoCommit(false);
			}
		} catch (Exception e) {
			throw new RuntimeException(UserStringConstants.DB_ERROR_CONNECTION_FAILED, e);
		}
	}

	/**
	 * Closes the database connection if it is open.
	 *
	 * @throws RuntimeException if closing the connection fails
	 */
	public void disconnect() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(UserStringConstants.DB_ERROR_DISCONNECT_FAILED, e);
		}
	}

	/**
	 * Checks whether the DBManager currently has an active database connection.
	 *
	 * @return true if connected, false otherwise
	 */
	public boolean isConnected() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Retrieves a random question from the database.
	 *
	 * @return a random {@link QuestionDTO} or null if no questions exist
	 */
	@Override
	public QuestionDTO getRandomQuestion() {
		connect();
		String sql = "SELECT id, title, text, theme_id FROM Questions ORDER BY RAND() LIMIT 1";
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				QuestionDAO_MariaDB dao = new QuestionDAO_MariaDB();
				dao.fromResultSet(rs);
				QuestionDTO dto = dao.forTransport();
				questionDaoMap.put(dto, dao);
				return dto;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to get random question", e);
		}
		return null;
	}

	/**
	 * Retrieves a random question for a given theme from the database.
	 *
	 * @param theme the {@link ThemeDTO} representing the theme
	 * @return a random {@link QuestionDTO} or null if no questions exist for that
	 *         theme
	 */
	@Override
	public QuestionDTO getRandomQuestionFor(ThemeDTO theme) {
		connect();
		ThemeDAO_MariaDB themeDao = themeDaoMap.get(theme);
		if (themeDao == null) {
			return null;
		}
		String sql = "SELECT id, title, text, theme_id FROM Questions WHERE theme_id = ? ORDER BY RAND() LIMIT 1";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, themeDao.getId());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					QuestionDAO_MariaDB dao = new QuestionDAO_MariaDB();
					dao.fromResultSet(rs);
					QuestionDTO dto = dao.forTransport();
					questionDaoMap.put(dto, dao);
					return dto;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to get random question for theme", e);
		}
		return null;
	}

	/**
	 * Retrieves all available themes from the database.
	 *
	 * @return list of {@link ThemeDTO} objects
	 */
	@Override
	public ArrayList<ThemeDTO> getAllThemes() {
		connect();
		ArrayList<ThemeDTO> themes = new ArrayList<>();
		String sql = new ThemeDAO_MariaDB().getSelectStatement();
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ThemeDAO_MariaDB dao = new ThemeDAO_MariaDB();
				dao.fromResultSet(rs);
				ThemeDTO dto = dao.forTransport();
				themeDaoMap.put(dto, dao);
				themes.add(dto);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to get all themes", e);
		}
		return themes;
	}

	/**
	 * Saves a theme to the database. Inserts if the theme is new, updates
	 * otherwise.
	 *
	 * @param theme theme DTO to save
	 * @return result message
	 */
	@Override
	public String saveTheme(ThemeDTO theme) {
		connect();
		try {
			ThemeDAO_MariaDB dao = themeDaoMap.get(theme);
			if (dao == null) {
				dao = ThemeDAO_MariaDB.fromTransport(theme);
			} else {
				dao.setThemeTitle(theme.getThemeTitle());
				dao.setThemeDescription(theme.getThemeDescription());
			}

			try {
				dao.performValidation();
			} catch (IllegalArgumentException ex) {
				return String.format(UserStringConstants.DB_ERROR_VALIDATION_FAILED, ex.getMessage());
			}

			if (dao.isNew()) {
				try (PreparedStatement ps = connection.prepareStatement(dao.getInsertStatement(),
						Statement.RETURN_GENERATED_KEYS)) {
					dao.setPreparedStatementParameters(ps);
					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								int newId = generatedKeys.getInt(1);
								dao.setId(newId);
								theme.setId(newId);
								themeDaoMap.put(theme, dao);
								connection.commit();
								return UserStringConstants.DB_MSG_THEME_CREATED_SUCCESS;
							}
						}
					}
				}
			} else {
				try (PreparedStatement ps = connection.prepareStatement(dao.getUpdateStatement())) {
					dao.setPreparedStatementParameters(ps);
					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						connection.commit();
						return UserStringConstants.DB_MSG_THEME_UPDATED_SUCCESS;
					}
				}
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_DATABASE, e.getMessage());
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_GENERAL, e.getMessage());
		}
		return UserStringConstants.DB_ERROR_THEME_SAVE_FAILED;
	}

	/**
	 * Deletes a theme from the database.
	 *
	 * @param theme Theme DTO to delete
	 * @return result message
	 */
	@Override
	public String deleteTheme(ThemeDTO theme) {
		connect();
		ThemeDAO_MariaDB dao = themeDaoMap.get(theme);
		if (dao == null || dao.isNew()) {
			return UserStringConstants.DB_ERROR_THEME_NOT_FOUND;
		}
		try (PreparedStatement ps = connection.prepareStatement(dao.getDeleteStatement())) {
			ps.setInt(1, dao.getId());
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				themeDaoMap.remove(theme);
				connection.commit();
				return UserStringConstants.DB_MSG_THEME_DELETED_SUCCESS;
			} else {
				return UserStringConstants.DB_ERROR_THEME_NOT_FOUND_FOR_DELETE;
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_DATABASE, e.getMessage());
		}
	}

	/**
	 * Retrieves all questions for a given theme.
	 *
	 * @param theme theme DTO
	 * @return list of {@link QuestionDTO}s
	 */
	@Override
	public ArrayList<QuestionDTO> getQuestionsFor(ThemeDTO theme) {
		connect();
		ThemeDAO_MariaDB themeDao = themeDaoMap.get(theme);
		if (themeDao == null) {
			return new ArrayList<>();
		}
		ArrayList<QuestionDTO> questions = new ArrayList<>();
		String sql = new QuestionDAO_MariaDB().getSelectStatement() + " WHERE theme_id = ? ORDER BY id";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, themeDao.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					QuestionDAO_MariaDB dao = new QuestionDAO_MariaDB();
					dao.fromResultSet(rs);
					QuestionDTO dto = dao.forTransport();
					questionDaoMap.put(dto, dao);
					questions.add(dto);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to get questions for theme", e);
		}
		return questions;
	}

	/**
	 * Saves a question to the database (must already be linked to a theme in
	 * cache).
	 *
	 * @param question Question DTO
	 * @return result message
	 */
	@Override
	public String saveQuestion(QuestionDTO question) {
		connect();
		try {
			QuestionDAO_MariaDB dao = questionDaoMap.get(question);
			if (dao == null) {
				return UserStringConstants.DB_ERROR_QUESTION_NOT_ASSOCIATED;
			}
			dao.setQuestionText(question.getQuestionText());
			try {
				dao.performValidation();
			} catch (IllegalArgumentException ex) {
				return String.format(UserStringConstants.DB_ERROR_VALIDATION_FAILED, ex.getMessage());
			}
			if (dao.isNew()) {
				try (PreparedStatement ps = connection.prepareStatement(dao.getInsertStatement(),
						Statement.RETURN_GENERATED_KEYS)) {
					dao.setPreparedStatementParameters(ps);
					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								dao.setId(generatedKeys.getInt(1));
								connection.commit();
								return UserStringConstants.DB_MSG_QUESTION_CREATED_SUCCESS;
							}
						}
					}
				}
			} else {
				try (PreparedStatement ps = connection.prepareStatement(dao.getUpdateStatement())) {
					dao.setPreparedStatementParameters(ps);
					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						connection.commit();
						return UserStringConstants.DB_MSG_QUESTION_UPDATED_SUCCESS;
					}
				}
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_DATABASE, e.getMessage());
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_GENERAL, e.getMessage());
		}
		return UserStringConstants.DB_ERROR_QUESTION_SAVE_FAILED;
	}

	/**
	 * Saves a question and associates it with a specific theme.
	 *
	 * @param question question DTO
	 * @param theme    theme DTO
	 * @return result message
	 */
	public String saveQuestion(QuestionDTO question, ThemeDTO theme) {
		connect();
		ThemeDAO_MariaDB themeDao = themeDaoMap.get(theme);
		if (themeDao == null) {
			return UserStringConstants.DB_ERROR_THEME_NOT_FOUND;
		}
		try {
			QuestionDAO_MariaDB dao = questionDaoMap.get(question);
			if (dao == null) {
				dao = QuestionDAO_MariaDB.fromTransport(question, themeDao.getId());
				questionDaoMap.put(question, dao);
			} else {
				dao.setTitle(question.getQuestionTitle());
				dao.setQuestionText(question.getQuestionText());
				dao.setThemeId(themeDao.getId());
			}
			try {
				dao.performValidation();
			} catch (IllegalArgumentException ex) {
				return String.format(UserStringConstants.DB_ERROR_VALIDATION_FAILED, ex.getMessage());
			}

			String questionResult = null;
			if (dao.isNew()) {
				try (PreparedStatement ps = connection.prepareStatement(dao.getInsertStatement(),
						Statement.RETURN_GENERATED_KEYS)) {
					dao.setPreparedStatementParameters(ps);
					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								int newId = generatedKeys.getInt(1);
								dao.setId(newId);
								question.setId(newId);
								questionResult = UserStringConstants.DB_MSG_QUESTION_CREATED_SUCCESS;
							}
						}
					}
				}
			} else {
				try (PreparedStatement ps = connection.prepareStatement(dao.getUpdateStatement())) {
					dao.setPreparedStatementParameters(ps);
					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						questionResult = UserStringConstants.DB_MSG_QUESTION_UPDATED_SUCCESS;
					}
				}
			}

			if (questionResult != null && question.getAnswers() != null && !question.getAnswers().isEmpty()) {
				StringBuilder results = new StringBuilder(questionResult);

				if (!dao.isNew()) {
					deleteAnswersForQuestion(dao.getId());
				}

				for (AnswerDTO answer : question.getAnswers()) {
					answer.setQuestionId(dao.getId());
					String answerResult = saveAnswer(answer, question);
					if (!answerResult.contains("successfully")) {
						results.append("; Answer save failed: ").append(answerResult);
					}
				}

				connection.commit();
				return results.toString();
			}

			if (questionResult != null) {
				connection.commit();
			}

			return questionResult != null ? questionResult : UserStringConstants.DB_ERROR_QUESTION_SAVE_FAILED;

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_DATABASE, e.getMessage());
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_GENERAL, e.getMessage());
		}
	}

	/**
	 * Deletes a question from the database.
	 *
	 * @param question Question DTO to delete
	 * @return result message
	 */
	@Override
	public String deleteQuestion(QuestionDTO question) {
		connect();
		QuestionDAO_MariaDB dao = questionDaoMap.get(question);
		if (dao == null || dao.isNew()) {
			return UserStringConstants.DB_ERROR_QUESTION_NOT_FOUND;
		}
		try (PreparedStatement ps = connection.prepareStatement(dao.getDeleteStatement())) {
			ps.setInt(1, dao.getId());
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				questionDaoMap.remove(question);
				connection.commit();
				return UserStringConstants.DB_MSG_QUESTION_DELETED_SUCCESS;
			} else {
				return UserStringConstants.DB_ERROR_QUESTION_NOT_FOUND_FOR_DELETE;
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_DATABASE, e.getMessage());
		}
	}

	/**
	 * Retrieves all answers for a given question from the database.
	 *
	 * @param question Question DTO
	 * @return list of {@link AnswerDTO}s
	 */
	@Override
	public ArrayList<AnswerDTO> getAnswersFor(QuestionDTO question) {
		connect();
		QuestionDAO_MariaDB questionDao = questionDaoMap.get(question);
		if (questionDao == null) {
			return new ArrayList<>();
		}
		ArrayList<AnswerDTO> answers = new ArrayList<>();
		String sql = new AnswerDAO_MariaDB().getSelectStatement() + " WHERE question_id = ? ORDER BY id";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, questionDao.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					AnswerDAO_MariaDB dao = new AnswerDAO_MariaDB();
					dao.fromResultSet(rs);
					AnswerDTO dto = dao.forTransport();
					answerDaoMap.put(dto, dao);
					answers.add(dto);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to get answers for question", e);
		}
		return answers;
	}

	/**
	 * Saves an answer and associates it with a given question.
	 *
	 * @param answer   Answer DTO
	 * @param question Question DTO
	 * @return result message
	 */
	public String saveAnswer(AnswerDTO answer, QuestionDTO question) {
		connect();
		QuestionDAO_MariaDB questionDao = questionDaoMap.get(question);
		if (questionDao == null) {
			return UserStringConstants.DB_ERROR_QUESTION_NOT_FOUND;
		}
		try {
			AnswerDAO_MariaDB dao = answerDaoMap.get(answer);
			if (dao == null) {
				dao = AnswerDAO_MariaDB.fromTransport(answer, questionDao.getId());
				answerDaoMap.put(answer, dao);
			} else {
				dao.setText(answer.getAnswerText());
				dao.setCorrect(answer.isCorrect());
				dao.setQuestionId(questionDao.getId());
			}
			try {
				dao.performValidation();
			} catch (IllegalArgumentException ex) {
				return String.format(UserStringConstants.DB_ERROR_VALIDATION_FAILED, ex.getMessage());
			}
			if (dao.isNew()) {
				try (PreparedStatement ps = connection.prepareStatement(dao.getInsertStatement(),
						Statement.RETURN_GENERATED_KEYS)) {
					dao.setPreparedStatementParameters(ps);
					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								int newId = generatedKeys.getInt(1);
								dao.setId(newId);
								answer.setId(newId);
								connection.commit();
								return UserStringConstants.DB_MSG_ANSWER_CREATED_SUCCESS;
							}
						}
					}
				}
			} else {
				try (PreparedStatement ps = connection.prepareStatement(dao.getUpdateStatement())) {
					dao.setPreparedStatementParameters(ps);
					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						connection.commit();
						return UserStringConstants.DB_MSG_ANSWER_UPDATED_SUCCESS;
					}
				}
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_DATABASE, e.getMessage());
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {

			}
			return String.format(UserStringConstants.DB_ERROR_GENERAL, e.getMessage());
		}
		return UserStringConstants.DB_ERROR_ANSWER_SAVE_FAILED;
	}

	/**
	 * Deletes all answers for a specific question.
	 *
	 * @param questionId ID of the question whose answers should be deleted
	 * @throws SQLException if a database error occurs
	 */
	public void deleteAnswersForQuestion(int questionId) throws SQLException {
		String deleteSQL = "DELETE FROM answers WHERE question_id = ?";
		try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
			ps.setInt(1, questionId);
			ps.executeUpdate();
			answerDaoMap.entrySet().removeIf(entry -> {
				AnswerDAO_MariaDB dao = entry.getValue();
				return dao.getQuestionId() == questionId;
			});
		}
	}

	/**
	 * Tests database connectivity.
	 *
	 * @return status message
	 */
	public String testConnection() {
		try {
			connect();
			return isConnected() ? UserStringConstants.DB_MSG_CONNECTION_SUCCESS : UserStringConstants.DB_MSG_CONNECTION_FAILED;
		} catch (Exception e) {
			return String.format(UserStringConstants.DB_MSG_CONNECTION_FAILED_DETAILS, e.getMessage());
		}
	}

	/**
	 * Returns the active JDBC connection.
	 *
	 * @return active database connection
	 * @throws SQLException if not connected
	 */
	public Connection getConnection() throws SQLException {
		connect();
		return connection;
	}

	/**
	 * Clears all cached DTO-to-DAO mappings.
	 */
	public void clearMappings() {
		themeDaoMap.clear();
		questionDaoMap.clear();
		answerDaoMap.clear();
	}

	/**
	 * Returns a list of all theme titles from the database.
	 *
	 * @return list of theme titles
	 */
	public ArrayList<String> getThemeTitles() {
		connect();
		ArrayList<String> titles = new ArrayList<>();
		String sql = new ThemeDAO_MariaDB().getSelectStatement();
		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				titles.add(rs.getString("title"));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to get theme titles", e);
		}
		return titles;
	}

	/**
	 * Returns a list of question entries for a given theme title.
	 *
	 * @param selectedThemeTitle theme title, or "Alle Themen" for all
	 * @return list of question text entries
	 */
	public ArrayList<String> getQuestionListEntries(String selectedThemeTitle) {
		connect();
		ArrayList<String> entries = new ArrayList<>();
		if (selectedThemeTitle == null || selectedThemeTitle.equals(UserStringConstants.ALL_THEMES_OPTION)) {
			String sql = new QuestionDAO_MariaDB().getSelectStatement();
			try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					entries.add(rs.getString("text"));
				}
			} catch (SQLException e) {
				throw new RuntimeException("Failed to get all questions", e);
			}
		} else {
			String sql = new QuestionDAO_MariaDB().getSelectStatement()
					+ " WHERE theme_id = (SELECT id FROM Theme WHERE title = ?)";
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, selectedThemeTitle);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						entries.add(rs.getString("text"));
					}
				}
			} catch (SQLException e) {
				throw new RuntimeException("Failed to get questions for theme", e);
			}
		}
		return entries;
	}

	/**
	 * Gets a question by its global index (0-based) in the database.
	 *
	 * @param index the index of the question
	 * @return QuestionDTO or null if index is invalid
	 */
	public QuestionDTO getQuestionByGlobalIndex(int index) {
		connect();
		String sql = new QuestionDAO_MariaDB().getSelectStatement() + " LIMIT 1 OFFSET ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, index);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					QuestionDAO_MariaDB dao = new QuestionDAO_MariaDB();
					dao.fromResultSet(rs);
					QuestionDTO dto = dao.forTransport();
					questionDaoMap.put(dto, dao);
					return dto;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to get question by index", e);
		}
		return null;
	}
}
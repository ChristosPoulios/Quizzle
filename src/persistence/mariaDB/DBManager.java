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

import persistence.QuizDataInterface;
import persistence.mariaDB.dao.AnswerDAO_MariaDB;
import persistence.mariaDB.dao.QuestionDAO_MariaDB;
import persistence.mariaDB.dao.ThemeDAO_MariaDB;
import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Database Manager for MariaDB operations. Implements QuizDataInterface for
 * database persistence.
 */
public class DBManager implements QuizDataInterface {

	private static DBManager instance;
	private Connection connection;

	private static final String DB_URL = "jdbc:mariadb://localhost:3306/quizzle_db";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "vu8dzctc";

	private Map<ThemeDTO, ThemeDAO_MariaDB> themeDaoMap = new HashMap<>();
	private Map<QuestionDTO, QuestionDAO_MariaDB> questionDaoMap = new HashMap<>();
	private Map<AnswerDTO, AnswerDAO_MariaDB> answerDaoMap = new HashMap<>();

	/**
	 * Private constructor for singleton pattern
	 */
	private DBManager() {
	}

	/**
	 * Gets the singleton instance
	 * 
	 * @return DBManager instance
	 */
	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	/**
	 * Establishes database connection
	 */
	public void connect() {
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName("org.mariadb.jdbc.Driver");
				connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				connection.setAutoCommit(true);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to connect to database", e);
		}
	}

	/**
	 * Closes database connection
	 */
	public void disconnect() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to disconnect from database", e);
		}
	}

	/**
	 * Checks if connected to database
	 * 
	 * @return true if connected
	 */
	public boolean isConnected() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

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
				return "Validation failed: " + ex.getMessage();
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
								return "Theme successfully created";
							}
						}
					}
				}
			} else {

				try (PreparedStatement ps = connection.prepareStatement(dao.getUpdateStatement())) {
					dao.setPreparedStatementParameters(ps);

					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						return "Theme successfully updated";
					}
				}
			}

		} catch (SQLException e) {
			return "Database error: " + e.getMessage();
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}

		return "Failed to save theme";
	}

	@Override
	public String deleteTheme(ThemeDTO theme) {
		connect();
		ThemeDAO_MariaDB dao = themeDaoMap.get(theme);
		if (dao == null || dao.isNew()) {
			return "Theme not found in database";
		}
		try (PreparedStatement ps = connection.prepareStatement(dao.getDeleteStatement())) {
			ps.setInt(1, dao.getId());
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				themeDaoMap.remove(theme);
				return "Theme successfully deleted";
			} else {
				return "Theme not found";
			}
		} catch (SQLException e) {
			return "Database error: " + e.getMessage();
		}
	}

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

	@Override
	public String saveQuestion(QuestionDTO question) {
		connect();

		try {
			QuestionDAO_MariaDB dao = questionDaoMap.get(question);
			if (dao == null) {
				return "Question not associated with a theme. Use saveQuestion with theme parameter.";
			}

			dao.setQuestionText(question.getQuestionText());

			try {
				dao.performValidation();
			} catch (IllegalArgumentException ex) {
				return "Validation failed: " + ex.getMessage();
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
								return "Question successfully created";
							}
						}
					}
				}
			} else {

				try (PreparedStatement ps = connection.prepareStatement(dao.getUpdateStatement())) {
					dao.setPreparedStatementParameters(ps);

					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						return "Question successfully updated";
					}
				}
			}

		} catch (SQLException e) {
			return "Database error: " + e.getMessage();
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}

		return "Failed to save question";
	}

	/**
	 * Additional method to save question with theme
	 */
	public String saveQuestion(QuestionDTO question, ThemeDTO theme) {
		connect();

		ThemeDAO_MariaDB themeDao = themeDaoMap.get(theme);
		if (themeDao == null) {
			return "Theme not found in database";
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
				return "Validation failed: " + ex.getMessage();
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
								question.setId(newId); // IMPORTANT: Update the DTO with new ID
								return "Question successfully created";
							}
						}
					}
				}
			} else {

				try (PreparedStatement ps = connection.prepareStatement(dao.getUpdateStatement())) {
					dao.setPreparedStatementParameters(ps);

					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						return "Question successfully updated";
					}
				}
			}

		} catch (SQLException e) {
			return "Database error: " + e.getMessage();
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}

		return "Failed to save question";
	}

	@Override
	public String deleteQuestion(QuestionDTO question) {
		connect();
		QuestionDAO_MariaDB dao = questionDaoMap.get(question);
		if (dao == null || dao.isNew()) {
			return "Question not found in database";
		}
		try (PreparedStatement ps = connection.prepareStatement(dao.getDeleteStatement())) {
			ps.setInt(1, dao.getId());
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				questionDaoMap.remove(question);
				return "Question successfully deleted";
			} else {
				return "Question not found";
			}
		} catch (SQLException e) {
			return "Database error: " + e.getMessage();
		}
	}

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
	 * Tests the database connection
	 * 
	 * @return status message
	 */
	public String testConnection() {
		try {
			connect();
			return isConnected() ? "Database connection successful" : "Database connection failed";
		} catch (Exception e) {
			return "Database connection failed: " + e.getMessage();
		}
	}

	/**
	 * Gets the current connection
	 * 
	 * @return current connection
	 * @throws SQLException if not connected
	 */
	public Connection getConnection() throws SQLException {
		connect();
		return connection;
	}

	/**
	 * Clear all mappings
	 */
	public void clearMappings() {
		themeDaoMap.clear();
		questionDaoMap.clear();
		answerDaoMap.clear();
	}

	/**
	 * Returns a list of all theme titles.
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
	 * Returns a list of question entries for a given theme.
	 */
	public ArrayList<String> getQuestionListEntries(String selectedThemeTitle) {
		connect();
		ArrayList<String> entries = new ArrayList<>();
		if (selectedThemeTitle == null || selectedThemeTitle.equals("Alle Themen")) {
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
	 * Returns the QuestionDTO by its global index.
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

	public String saveAnswer(AnswerDTO answer, QuestionDTO question) {
		connect();

		QuestionDAO_MariaDB questionDao = questionDaoMap.get(question);
		if (questionDao == null) {
			return "Question not found in database";
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
				return "Validation failed: " + ex.getMessage();
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
								return "Answer successfully created";
							}
						}
					}
				}
			} else {

				try (PreparedStatement ps = connection.prepareStatement(dao.getUpdateStatement())) {
					dao.setPreparedStatementParameters(ps);

					int rowsAffected = ps.executeUpdate();
					if (rowsAffected > 0) {
						return "Answer successfully updated";
					}
				}
			}

		} catch (SQLException e) {
			return "Database error: " + e.getMessage();
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}

		return "Failed to save answer";
	}
}
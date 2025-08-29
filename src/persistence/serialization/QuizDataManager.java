package persistence.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import constants.ConfigManager;
import constants.LogicConstants;
import persistence.QuizDataInterface;
import quizlogic.QuestionSessionManager;
import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Serialization-based implementation of QuizDataInterface.
 * <p>
 * Stores themes as individual serialized files in the quizData directory. Each
 * theme file is named theme_{id}.dat where id is the theme's unique identifier.
 * </p>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuizDataManager implements QuizDataInterface {

	/** Directory where theme files are stored */
	private static final String DATA_DIRECTORY = "./quizData";

	/** File prefix for theme files */
	private static final String THEME_FILE_PREFIX = "theme_";

	/** File extension for serialized files */
	private static final String FILE_EXTENSION = ".dat";

	/** Success message template for save operations */
	private static final String SAVE_SUCCESS_MESSAGE = "%s '%s' saved successfully.";

	/** Error message template for save operations */
	private static final String SAVE_ERROR_MESSAGE = "Error saving %s: %s";

	/** Success message template for delete operations */
	private static final String DELETE_SUCCESS_MESSAGE = "%s '%s' deleted successfully.";

	/** Error message template for delete operations */
	private static final String DELETE_ERROR_MESSAGE = "Error deleting %s: %s";

	/** Question session manager to avoid repetitive questions */
	private QuestionSessionManager questionSessionManager = new QuestionSessionManager();

	/**
	 * Constructor - ensures data directory exists.
	 */
	public QuizDataManager() {
		File dataDir = new File(DATA_DIRECTORY);
		if (!dataDir.exists()) {
			dataDir.mkdirs();
		}
	}

	/**
	 * Returns the data directory path.
	 * 
	 * @return data directory path
	 */
	private String getDataDirectory() {
		return DATA_DIRECTORY;
	}

	@Override
	public String saveTheme(ThemeDTO theme) {
		try {
			if (theme.getId() == LogicConstants.INVALID_ID) {
				int newId = generateNextThemeId();
				theme.setId(newId);
				ConfigManager.debugPrint("DEBUG: New Theme ID assigned: " + newId);
			}
			String filename = THEME_FILE_PREFIX + theme.getId() + FILE_EXTENSION;
			ConfigManager.debugPrint("DEBUG: Saving Theme to file: " + filename);

			try (ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(getDataDirectory() + File.separator + filename))) {
				oos.writeObject(theme);
			}
			return String.format(SAVE_SUCCESS_MESSAGE, "Theme", theme.getThemeTitle());
		} catch (IOException e) {
			return String.format(SAVE_ERROR_MESSAGE, "Theme", e.getMessage());
		}
	}

	@Override
	public ArrayList<ThemeDTO> getAllThemes() {
		ArrayList<ThemeDTO> themes = new ArrayList<>();
		File dataDir = new File(getDataDirectory());

		if (!dataDir.exists()) {
			return themes;
		}

		@SuppressWarnings("unused")
		File[] files = dataDir
				.listFiles((dir, name) -> name.startsWith(THEME_FILE_PREFIX) && name.endsWith(FILE_EXTENSION));

		if (files != null) {
			ConfigManager.debugPrint("DEBUG: Found theme files: " + files.length);
			for (File file : files) {
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
					ThemeDTO theme = (ThemeDTO) ois.readObject();
					themes.add(theme);
				} catch (IOException | ClassNotFoundException e) {
					System.err.println("Error loading theme from file " + file.getName() + ": " + e.getMessage());
				}
			}
		}

		return themes;
	}

	/**
	 * Generates the next available theme ID by examining existing files.
	 * 
	 * @return next available theme ID
	 */
	private int generateNextThemeId() {
		File dataDir = new File(getDataDirectory());
		if (!dataDir.exists()) {
			dataDir.mkdirs();
		}

		@SuppressWarnings("unused")
		File[] files = dataDir
				.listFiles((dir, name) -> name.startsWith(THEME_FILE_PREFIX) && name.endsWith(FILE_EXTENSION));
		int maxId = LogicConstants.MIN_VALID_ID - 1;

		if (files != null && files.length > 0) {
			ConfigManager.debugPrint("DEBUG: Analyzing " + files.length + " existing files for ID assignment");
			for (File file : files) {
				String name = file.getName();
				try {
					int startIndex = THEME_FILE_PREFIX.length();
					int endIndex = name.lastIndexOf(FILE_EXTENSION);
					if (endIndex > startIndex) {
						int id = Integer.parseInt(name.substring(startIndex, endIndex));
						ConfigManager.debugPrint("DEBUG: Found ID: " + id + ", current max ID: " + maxId);
						maxId = Math.max(maxId, id);
					}
				} catch (NumberFormatException e) {
					ConfigManager.debugPrint("DEBUG: Could not parse ID from: " + name);
				}
			}
		}

		int newId = Math.max(LogicConstants.MIN_VALID_ID, maxId + 1);
		ConfigManager.debugPrint("DEBUG: New ID will be: " + newId);
		return newId;
	}

	@Override
	public String deleteTheme(ThemeDTO theme) {
		try {
			String filename = THEME_FILE_PREFIX + theme.getId() + FILE_EXTENSION;
			Path filePath = Paths.get(getDataDirectory(), filename);

			if (Files.exists(filePath)) {
				Files.delete(filePath);
				ConfigManager.debugPrint("DEBUG: Theme file deleted: " + filename);
				return String.format(DELETE_SUCCESS_MESSAGE, "Theme", theme.getThemeTitle());
			} else {
				return String.format(DELETE_ERROR_MESSAGE, "Theme", "File not found");
			}
		} catch (IOException e) {
			return String.format(DELETE_ERROR_MESSAGE, "Theme", e.getMessage());
		}
	}

	/**
	 * Returns a random question from all themes, avoiding recently asked questions.
	 * Uses improved algorithm to ensure variety in question selection.
	 * 
	 * @return A randomly selected unasked question, or null if no questions exist
	 */
	@Override
	public QuestionDTO getRandomQuestion() {
		ArrayList<ThemeDTO> themes = getAllThemes();
		if (themes.isEmpty())
			return null;

		ArrayList<QuestionDTO> allQuestions = new ArrayList<>();
		for (ThemeDTO theme : themes) {
			if (theme.getQuestions() != null) {
				allQuestions.addAll(theme.getQuestions());
			}
		}

		if (allQuestions.isEmpty())
			return null;

		// Use session manager for better question variety
		return questionSessionManager.getRandomQuestionWithVariety(allQuestions);
	}

	/**
	 * Returns a random question from the specified theme, avoiding recently asked questions.
	 * Uses improved algorithm to ensure variety in question selection within the theme.
	 * 
	 * @param theme The theme to select a question from
	 * @return A randomly selected unasked question from the theme, or null if no questions exist
	 */
	@Override
	public QuestionDTO getRandomQuestionFor(ThemeDTO theme) {
		if (theme.getQuestions() == null || theme.getQuestions().isEmpty()) {
			return null;
		}
		
		// Use session manager for better question variety within the theme
		return questionSessionManager.getRandomQuestionForThemeWithVariety(theme);
	}

	@Override
	public ArrayList<QuestionDTO> getQuestionsFor(ThemeDTO theme) {
		return (ArrayList<QuestionDTO>) theme.getQuestions();
	}

	@Override
	public ArrayList<AnswerDTO> getAnswersFor(QuestionDTO question) {
		return (ArrayList<AnswerDTO>) question.getAnswers();
	}

	@Override
	public String saveQuestion(QuestionDTO question) {

		try {
			for (ThemeDTO theme : getAllThemes()) {
				if (theme.getQuestions() != null) {
					for (QuestionDTO existingQ : theme.getQuestions()) {
						if (existingQ.getId() == question.getId()) {

							existingQ.setQuestionTitle(question.getQuestionTitle());
							existingQ.setQuestionText(question.getQuestionText());
							existingQ.setAnswers(question.getAnswers());

							String saveResult = saveTheme(theme);
							ConfigManager.debugPrint("DEBUG: Updated question in theme: " + saveResult);
							return "Question updated successfully";
						}
					}
				}
			}

			ConfigManager.debugPrint("DEBUG: Question with ID " + question.getId() + " not found for update");
			return "Error: Question not found. Use saveQuestion(question, theme) for new questions.";

		} catch (Exception e) {
			ConfigManager.debugPrint("DEBUG: Error saving question: " + e.getMessage());
			return "Error saving question: " + e.getMessage();
		}
	}

	@Override
	public String deleteQuestion(QuestionDTO question) {
		try {

			for (ThemeDTO theme : getAllThemes()) {
				if (theme.getQuestions() != null) {
					boolean questionFound = false;

					theme.getQuestions().removeIf(q -> {
						if (q.getId() == question.getId()) {
							ConfigManager.debugPrint("DEBUG: Found and removing question: " + q.getQuestionTitle());
							return true;
						}
						return false;
					});

					for (QuestionDTO q : theme.getQuestions()) {
						if (q.getId() == question.getId()) {
							questionFound = false;
							break;
						} else {
							questionFound = true;
						}
					}

					if (questionFound || theme.getQuestions().stream().noneMatch(q -> q.getId() == question.getId())) {

						String saveResult = saveTheme(theme);
						ConfigManager.debugPrint("DEBUG: Saved theme after question deletion: " + saveResult);
						return "Question and all associated answers deleted successfully";
					}
				}
			}

			ConfigManager.debugPrint("DEBUG: Question with ID " + question.getId() + " not found in any theme");
			return "Error: Question not found in any theme";

		} catch (Exception e) {
			ConfigManager.debugPrint("DEBUG: Error deleting question: " + e.getMessage());
			return "Error deleting question: " + e.getMessage();
		}
	}

	/**
	 * Deletes an answer from a question (extended method for compatibility).
	 * 
	 * @param answer   the answer to delete
	 * @param question the question containing the answer
	 * @return result message
	 */
	public String deleteAnswer(AnswerDTO answer, QuestionDTO question) {
		try {

			if (question.getAnswers() != null) {
				boolean removed = question.getAnswers().removeIf(a -> a.getId() == answer.getId());

				if (removed) {
					ConfigManager.debugPrint("DEBUG: Removed answer: " + answer.getAnswerText());

					for (ThemeDTO theme : getAllThemes()) {
						if (theme.getQuestions() != null) {
							for (QuestionDTO q : theme.getQuestions()) {
								if (q.getId() == question.getId()) {

									q.setAnswers(question.getAnswers());

									String saveResult = saveTheme(theme);
									ConfigManager.debugPrint("DEBUG: Saved theme after answer deletion: " + saveResult);
									return "Answer deleted successfully";
								}
							}
						}
					}

					return "Error: Question not found in any theme";
				} else {
					return "Error: Answer not found";
				}
			} else {
				return "Error: Question has no answers";
			}

		} catch (Exception e) {
			ConfigManager.debugPrint("DEBUG: Error deleting answer: " + e.getMessage());
			return "Error deleting answer: " + e.getMessage();
		}
	}

	/**
	 * Saves a question and associates it with a theme (extended method for
	 * compatibility).
	 * 
	 * @param question the question to save
	 * @param theme    the theme to associate with
	 * @return result message
	 */
	public String saveQuestion(QuestionDTO question, ThemeDTO theme) {
		try {

			if (theme.getQuestions() == null) {
				theme.setQuestions(new ArrayList<>());
			}

			if (question.getAnswers() != null) {
				for (AnswerDTO answer : question.getAnswers()) {
					if (answer.getId() == LogicConstants.INVALID_ID) {

						int maxAnswerId = 0;
						for (AnswerDTO existingAnswer : question.getAnswers()) {
							if (existingAnswer.getId() > maxAnswerId) {
								maxAnswerId = existingAnswer.getId();
							}
						}
						answer.setId(maxAnswerId + 1);
						ConfigManager.debugPrint("DEBUG: Assigned new answer ID: " + answer.getId() + " for answer: "
								+ answer.getAnswerText());
					}
				}
			}

			boolean exists = false;
			@SuppressWarnings("unused")
			QuestionDTO targetQuestion = null;
			for (QuestionDTO existingQ : theme.getQuestions()) {
				if (existingQ.getId() == question.getId()) {

					existingQ.setQuestionTitle(question.getQuestionTitle());
					existingQ.setQuestionText(question.getQuestionText());
					existingQ.setAnswers(question.getAnswers());
					targetQuestion = existingQ;
					exists = true;
					ConfigManager.debugPrint(
							"DEBUG: Updated existing question with answers: " + question.getQuestionTitle());
					break;
				}
			}

			if (!exists) {

				if (question.getId() == LogicConstants.INVALID_ID) {

					int maxId = 0;
					for (QuestionDTO q : theme.getQuestions()) {
						if (q.getId() > maxId) {
							maxId = q.getId();
						}
					}
					question.setId(maxId + 1);
					ConfigManager.debugPrint("DEBUG: Assigned new question ID: " + question.getId());
				}

				targetQuestion = question;
				theme.getQuestions().add(question);
				ConfigManager.debugPrint("DEBUG: Added new question with "
						+ (question.getAnswers() != null ? question.getAnswers().size() : 0) + " answers: "
						+ question.getQuestionTitle());
			}

			String result = saveTheme(theme);
			if (result != null && result.contains("successfully")) {
				ConfigManager.debugPrint("DEBUG: Theme saved successfully with question and answers");
				return "Question saved successfully";
			} else {
				return "Error saving question: " + result;
			}

		} catch (Exception e) {
			ConfigManager.debugPrint("DEBUG: Error saving question: " + e.getMessage());
			return "Error saving question: " + e.getMessage();
		}
	}

	/** File prefix for session files */
	private static final String SESSION_FILE_PREFIX = "session_";

	@Override
	public String saveQuizSession(quizlogic.dto.QuizSessionDTO session) {
		try {
			if (session == null || session.getUserAnswers() == null || session.getUserAnswers().isEmpty()) {
				return "Cannot save empty session";
			}

			if (session.getId() == LogicConstants.INVALID_ID) {
				int newId = generateNextSessionId();
				session.setId(newId);
				ConfigManager.debugPrint("DEBUG: New Session ID assigned: " + newId);
			}

			String timestamp = String.valueOf(System.currentTimeMillis());
			String filename = SESSION_FILE_PREFIX + session.getId() + "_" + timestamp + FILE_EXTENSION;
			String filepath = DATA_DIRECTORY + File.separator + filename;

			File dataDir = new File(DATA_DIRECTORY);
			if (!dataDir.exists()) {
				dataDir.mkdirs();
			}

			try (FileOutputStream fos = new FileOutputStream(filepath);
					ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				oos.writeObject(session);
				ConfigManager.debugPrint("DEBUG: Session saved to: " + filepath);
				return null;
			}

		} catch (Exception e) {
			ConfigManager.debugPrint("DEBUG: Error saving session: " + e.getMessage());
			return "Error saving session: " + e.getMessage();
		}
	}

	/**
	 * Generates the next available session ID by examining existing session files.
	 * 
	 * @return next available session ID
	 */
	private int generateNextSessionId() {
		int maxId = 0;
		File dataDir = new File(DATA_DIRECTORY);

		if (dataDir.exists()) {
			@SuppressWarnings("unused")
			File[] files = dataDir
					.listFiles((dir, name) -> name.startsWith(SESSION_FILE_PREFIX) && name.endsWith(FILE_EXTENSION));

			if (files != null) {
				for (File file : files) {
					try {
						String filename = file.getName();

						String withoutPrefix = filename.substring(SESSION_FILE_PREFIX.length());
						String idPart = withoutPrefix.substring(0, withoutPrefix.indexOf('_'));
						int id = Integer.parseInt(idPart);
						if (id > maxId) {
							maxId = id;
						}
					} catch (Exception e) {

						ConfigManager.debugPrint("DEBUG: Skipping invalid session file: " + file.getName());
					}
				}
			}
		}

		return maxId + 1;
	}

	@Override
	public ArrayList<quizlogic.dto.QuizSessionDTO> getAllQuizSessions() {
		ArrayList<quizlogic.dto.QuizSessionDTO> sessions = new ArrayList<>();

		try {
			File dataDir = new File(DATA_DIRECTORY);
			if (!dataDir.exists()) {
				return sessions;
			}

			@SuppressWarnings("unused")
			File[] files = dataDir
					.listFiles((dir, name) -> name.startsWith(SESSION_FILE_PREFIX) && name.endsWith(FILE_EXTENSION));

			if (files != null) {
				for (File file : files) {
					try (FileInputStream fis = new FileInputStream(file);
							ObjectInputStream ois = new ObjectInputStream(fis)) {

						quizlogic.dto.QuizSessionDTO session = (quizlogic.dto.QuizSessionDTO) ois.readObject();
						sessions.add(session);
						ConfigManager.debugPrint("DEBUG: Loaded session from: " + file.getName());

					} catch (Exception e) {
						ConfigManager.debugPrint(
								"DEBUG: Error loading session from " + file.getName() + ": " + e.getMessage());
					}
				}
			}

			sessions.sort((s1, s2) -> s2.getTimestamp().compareTo(s1.getTimestamp()));

		} catch (Exception e) {
			ConfigManager.debugPrint("DEBUG: Error retrieving sessions: " + e.getMessage());
		}

		return sessions;
	}

	@Override
	public ArrayList<quizlogic.dto.QuizSessionDTO> getRecentQuizSessions(int limit) {
		ArrayList<quizlogic.dto.QuizSessionDTO> allSessions = getAllQuizSessions();

		if (allSessions.size() <= limit) {
			return allSessions;
		} else {
			return new ArrayList<>(allSessions.subList(0, limit));
		}
	}
}
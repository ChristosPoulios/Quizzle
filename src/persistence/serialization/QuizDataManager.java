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
import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Serialization-based implementation of QuizDataInterface.
 * <p>
 * Stores themes as individual serialized files in the quizData directory.
 * Each theme file is named theme_{id}.dat where id is the theme's unique identifier.
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

		File[] files = dataDir.listFiles((dir, name) -> name.startsWith(THEME_FILE_PREFIX) && name.endsWith(FILE_EXTENSION));

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

		File[] files = dataDir.listFiles((dir, name) -> name.startsWith(THEME_FILE_PREFIX) && name.endsWith(FILE_EXTENSION));
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

	@Override
	public QuestionDTO getRandomQuestion() {
		ArrayList<ThemeDTO> themes = getAllThemes();
		if (themes.isEmpty()) return null;
		
		ArrayList<QuestionDTO> allQuestions = new ArrayList<>();
		for (ThemeDTO theme : themes) {
			if (theme.getQuestions() != null) {
				allQuestions.addAll(theme.getQuestions());
			}
		}
		
		if (allQuestions.isEmpty()) return null;
		
		int randomIndex = (int) (Math.random() * allQuestions.size());
		return allQuestions.get(randomIndex);
	}

	@Override
	public QuestionDTO getRandomQuestionFor(ThemeDTO theme) {
		if (theme.getQuestions() == null || theme.getQuestions().isEmpty()) {
			return null;
		}
		int randomIndex = (int) (Math.random() * theme.getQuestions().size());
		return theme.getQuestions().get(randomIndex);
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
		// Basic implementation - in a real scenario this would save to storage
		return "Question saved successfully";
	}

	@Override
	public String deleteQuestion(QuestionDTO question) {
		// Basic implementation - in a real scenario this would delete from storage
		return "Question deleted successfully";
	}
}
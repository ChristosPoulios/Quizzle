package persistence.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import persistence.QuizDataInterface;
import quizlogic.dto.AnswerDTO;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Serialization-based data manager for quiz persistence.
 * <p>
 * This implementation of {@link persistence.QuizDataInterface} stores each
 * theme as a separate serialized file located in
 * {@code ./quizData/theme_{id}.dat} where {@code id} is the unique theme
 * identifier.
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Serializing and deserializing quiz theme objects</li>
 * <li>Providing CRUD operations on themes, questions, and answers</li>
 * <li>Managing listeners for updates on data changes</li>
 * </ul>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class QuizDataManager implements QuizDataInterface {

	/** Directory where serialized theme files are stored */
	private static final String FOLDER = "./quizData";

	/** Prefix for theme filenames */
	private static final String THEME_PREFIX = "theme_";

	/** File extension for serialized theme files */
	private static final String EXTENSION = ".dat";

	/** List of update listeners that are notified when data changes */
	private List<Runnable> updateListeners = new ArrayList<>();

	/**
	 * Creates a new QuizDataManager instance. Ensures that the quiz data directory
	 * exists, creating it if necessary.
	 */
	public QuizDataManager() {
		File folder = new File(FOLDER);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	/**
	 * Returns a list of all theme titles from storage.
	 *
	 * @return an {@link ArrayList} containing theme titles
	 */
	public ArrayList<String> getThemeTitles() {
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<ThemeDTO> themes = getAllThemes();
		for (ThemeDTO theme : themes) {
			titles.add(theme.getThemeTitle());
		}
		return titles;
	}

	/**
	 * Retrieves a question by its global index across all themes.
	 *
	 * @param idx zero-based global index across all stored questions
	 * @return the {@link QuestionDTO} or null if index is invalid
	 */
	public QuestionDTO getQuestionByGlobalIndex(int idx) {
		ArrayList<ThemeDTO> themes = getAllThemes();
		ArrayList<QuestionDTO> allQuestions = new ArrayList<>();
		for (ThemeDTO theme : themes) {
			if (theme.getQuestions() != null) {
				allQuestions.addAll(theme.getQuestions());
			}
		}
		if (idx < 0 || idx >= allQuestions.size()) {
			return null;
		}
		QuestionDTO question = allQuestions.get(idx);
		if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
			createAnswersFor(question);
		}
		return question;
	}

	/**
	 * Returns a list of formatted question entries for a specified theme. If
	 * {@code themeTitle} is null or equals "Alle Themen", all questions from all
	 * themes are returned. Otherwise, only questions belonging to the provided
	 * theme are included.
	 *
	 * @param themeTitle Selected theme title or null/"Alle Themen" for all
	 * @return an {@link ArrayList} of formatted strings showing both title and a
	 *         truncated preview of text
	 */
	public ArrayList<String> getQuestionListEntries(String themeTitle) {
		ArrayList<String> entries = new ArrayList<>();
		ArrayList<ThemeDTO> themes = getAllThemes();

		if (themeTitle == null || themeTitle.equals("Alle Themen")) {
			int questionCounter = 1;
			for (ThemeDTO theme : themes) {
				if (theme.getQuestions() != null) {
					for (QuestionDTO q : theme.getQuestions()) {
						String displayText = formatQuestionDisplay(q, questionCounter);
						entries.add(displayText);
						questionCounter++;
					}
				}
			}
		} else {
			for (ThemeDTO theme : themes) {
				if (theme.getThemeTitle().equals(themeTitle) && theme.getQuestions() != null) {
					for (int i = 0; i < theme.getQuestions().size(); i++) {
						QuestionDTO q = theme.getQuestions().get(i);
						String displayText = formatQuestionDisplay(q, i + 1);
						entries.add(displayText);
					}
					break;
				}
			}
		}
		return entries;
	}

	/**
	 * Formats a question for list display combining title and a truncated preview
	 * of the question text.
	 *
	 * @param question the {@link QuestionDTO} to format
	 * @param number   the number of the question in the list
	 * @return formatted string for display purposes
	 */
	private String formatQuestionDisplay(QuestionDTO question, int number) {
		if (question == null) {
			return "Frage " + number + ": [Keine Daten]";
		}
		String title = question.getQuestionTitle();
		String text = question.getQuestionText();
		String displayTitle = (title != null && !title.trim().isEmpty()) ? title : "Frage " + number;
		if (text != null && !text.trim().isEmpty()) {
			String preview = text.length() > 50 ? text.substring(0, 47) + "..." : text;
			return displayTitle + ": " + preview;
		} else {
			return displayTitle + ": [Kein Fragetext]";
		}
	}

	/**
	 * Adds an observer/listener that will be notified whenever quiz data changes.
	 *
	 * @param listener Runnable callback to execute on updates
	 */
	public void addUpdateListener(Runnable listener) {
		updateListeners.add(listener);
	}

	/**
	 * Notifies all registered update listeners about a data change.
	 */
	private void notifyListeners() {
		for (Runnable listener : updateListeners) {
			listener.run();
		}
	}

	/**
	 * Creates default placeholder answers for a question if none exist. Useful for
	 * ensuring the UI has something to display.
	 *
	 * @param question the {@link QuestionDTO} to populate with answers
	 */
	private void createAnswersFor(QuestionDTO question) {
		ArrayList<AnswerDTO> answers = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			AnswerDTO answer = new AnswerDTO();
			answer.setId(i);
			answer.setAnswerText("Antwort " + (i + 1));
			answer.setCorrect(i == 0);
			answer.setQuestionId(question.getId());
			answers.add(answer);
		}
		question.setAnswers(answers);
	}

	/**
	 * Saves a theme to storage as a serialized file.
	 * <p>
	 * If the theme has no ID yet, a new ID is generated automatically.
	 *
	 * @param theme the {@link ThemeDTO} to save
	 * @return success or error message
	 */
	@Override
	public String saveTheme(ThemeDTO theme) {
		try {
			if (theme.getId() == -1) {
				int newId = createNewThemeId();
				theme.setId(newId);
				System.out.println("DEBUG: New Theme ID assigned: " + newId);
			}
			String filename = FOLDER + "/" + THEME_PREFIX + theme.getId() + EXTENSION;
			System.out.println("DEBUG: Saving Theme to file: " + filename);
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
				oos.writeObject(theme);
				notifyListeners();
				return "Theme successfully saved: " + theme.getThemeTitle() + " (ID: " + theme.getId() + ")";
			} catch (IOException e) {
				e.printStackTrace();
				return "Error while saving: " + e.getMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while saving: " + e.getMessage();
		}
	}

	/**
	 * Loads all themes from storage.
	 *
	 * @return list of all loaded {@link ThemeDTO}s
	 */
	@Override
	public ArrayList<ThemeDTO> getAllThemes() {
		ArrayList<ThemeDTO> themes = new ArrayList<>();
		File folder = new File(FOLDER);
		if (!folder.exists() || !folder.isDirectory()) {
			return themes;
		}
		@SuppressWarnings("unused")
		File[] files = folder.listFiles((dir, name) -> name.startsWith(THEME_PREFIX) && name.endsWith(EXTENSION));
		if (files != null) {
			System.out.println("DEBUG: Found theme files: " + files.length);
			for (File file : files) {
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
					ThemeDTO theme = (ThemeDTO) ois.readObject();
					themes.add(theme);
					System.out
							.println("DEBUG: Theme loaded: " + theme.getThemeTitle() + " (ID: " + theme.getId() + ")");
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return themes;
	}

	/**
	 * Creates a new unique theme ID by finding the highest existing ID and adding
	 * 1.
	 *
	 * @return the next available theme ID
	 */
	private int createNewThemeId() {
		int maxId = 0;
		File folder = new File(FOLDER);
		if (folder.exists() && folder.isDirectory()) {
			@SuppressWarnings("unused")
			File[] files = folder.listFiles((dir, name) -> name.startsWith(THEME_PREFIX) && name.endsWith(EXTENSION));
			if (files != null) {
				System.out.println("DEBUG: Analyzing " + files.length + " existing files for ID assignment");
				for (File file : files) {
					String name = file.getName();
					String idStr = name.substring(THEME_PREFIX.length(), name.length() - EXTENSION.length());
					try {
						int id = Integer.parseInt(idStr);
						maxId = Math.max(maxId, id);
						System.out.println("DEBUG: Found ID: " + id + ", current max ID: " + maxId);
					} catch (NumberFormatException e) {
						System.out.println("DEBUG: Could not parse ID from: " + name);
					}
				}
			}
		}
		int newId = maxId + 1;
		System.out.println("DEBUG: New ID will be: " + newId);
		return newId;
	}

	/**
	 * Gets a random question from all available questions in storage.
	 *
	 * @return a random {@link QuestionDTO}, or null if no questions exist
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
		int randomIndex = (int) (Math.random() * allQuestions.size());
		QuestionDTO question = allQuestions.get(randomIndex);
		if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
			createAnswersFor(question);
		}
		return question;
	}

	/**
	 * Returns the list of questions for a given theme.
	 *
	 * @param theme a {@link ThemeDTO}
	 * @return list of associated {@link QuestionDTO}s
	 */
	@Override
	public ArrayList<QuestionDTO> getQuestionsFor(ThemeDTO theme) {
		return (ArrayList<QuestionDTO>) theme.getQuestions();
	}

	/**
	 * Returns the list of answers for a given question.
	 *
	 * @param question a {@link QuestionDTO}
	 * @return list of answers
	 */
	@Override
	public ArrayList<AnswerDTO> getAnswersFor(QuestionDTO question) {
		return (ArrayList<AnswerDTO>) question.getAnswers();
	}

	/**
	 * Deletes a theme from storage by removing its serialized file.
	 *
	 * @param theme the {@link ThemeDTO} to delete
	 * @return null if successful, otherwise an error message
	 */
	@Override
	public String deleteTheme(ThemeDTO theme) {
		try {
			if (theme == null || theme.getId() <= 0) {
				return "Invalid Theme";
			}
			String filename = FOLDER + "/" + THEME_PREFIX + theme.getId() + EXTENSION;
			File file = new File(filename);
			if (file.exists()) {
				if (file.delete()) {
					System.out.println("DEBUG: Theme file deleted: " + filename);
					notifyListeners();
					return null;
				} else {
					return "File could not be deleted";
				}
			} else {
				return "Theme file not found: " + filename;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error deleting theme: " + e.getMessage();
		}
	}

	@Override
	public QuestionDTO getRandomQuestionFor(ThemeDTO theme) {
		// TODO: Implement method
		return null;
	}

	@Override
	public String saveQuestion(QuestionDTO question) {
		// TODO: Implement method
		return null;
	}

	@Override
	public String deleteQuestion(QuestionDTO question) {
		// TODO: Implement method
		return null;
	}
}

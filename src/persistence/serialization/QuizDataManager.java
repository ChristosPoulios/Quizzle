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
 * Central data manager for quiz persistence with proper ID management.
 * 
 * Ensures each theme gets a unique ID and is saved to a separate file. Provides
 * observer pattern for UI updates and replaces FakeDataFactory functionality.
 * 
 * @author System
 * @version 2.0
 * @since 2.0
 */
public class QuizDataManager implements QuizDataInterface {
	private static final String FOLDER = "./quizData";
	private static final String THEME_PREFIX = "theme_";
	private static final String EXTENSION = ".dat";

	/** List of observers that get notified when data changes */
	private List<Runnable> updateListeners = new ArrayList<>();

	/**
	 * Constructs a new QuizDataManager and ensures the data directory exists.
	 */
	public QuizDataManager() {
		File folder = new File(FOLDER);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	/**
	 * Returns all theme titles for UI components like ComboBoxes.
	 * 
	 * @return ArrayList containing all theme titles
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
	 * Returns a question by its global index across all themes.
	 * 
	 * @param idx zero-based global index
	 * @return requested Question or null if not found
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
	 * Returns display entries for questions based on the selected theme title.
	 * 
	 * @param themeTitle The title of the selected theme, or null/"Alle Themen" for
	 *                   all themes
	 * @return ArrayList of question display strings like "Frage 1", "Frage 2", ...
	 */
	public ArrayList<String> getQuestionListEntries(String themeTitle) {
		ArrayList<String> entries = new ArrayList<>();
		ArrayList<ThemeDTO> themes = getAllThemes();

		if (themeTitle == null || themeTitle.equals("Alle Themen")) {
			int questionCounter = 1;

			for (ThemeDTO theme : themes) {

				if (theme.getQuestions() != null) {

					for (@SuppressWarnings("unused") QuestionDTO q : theme.getQuestions()) {
						entries.add("Frage " + questionCounter);
						questionCounter++;
					}
				}
			}
		} else {
			for (ThemeDTO theme : themes) {
				if (theme.getThemeTitle().equals(themeTitle) && theme.getQuestions() != null) {
					for (int i = 0; i < theme.getQuestions().size(); i++) {
						entries.add("Frage " + (i + 1));
					}
					break;
				}
			}
		}
		return entries;
	}

	/**
	 * Adds an observer that gets notified when data changes.
	 * 
	 * @param listener The callback to execute when data updates occur
	 */
	public void addUpdateListener(Runnable listener) {
		updateListeners.add(listener);
	}

	/**
	 * Notifies all registered observers about data changes.
	 */
	private void notifyListeners() {
		for (Runnable listener : updateListeners) {
			listener.run();
		}
	}

	/**
	 * Creates sample answers for a question if none exist.
	 * 
	 * @param question The question to create answers for
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

	@Override
	public String saveTheme(ThemeDTO theme) {
		try {

			if (theme.getId() == -1) {
				int newId = createNewThemeId();
				theme.setId(newId);
				System.out.println("DEBUG: Neue Theme ID vergeben: " + newId);
			}

			String filename = FOLDER + "/" + THEME_PREFIX + theme.getId() + EXTENSION;
			System.out.println("DEBUG: Speichere Theme in Datei: " + filename);

			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
				oos.writeObject(theme);
				notifyListeners();
				return "Thema erfolgreich gespeichert: " + theme.getThemeTitle() + " (ID: " + theme.getId() + ")";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "Fehler beim Speichern: " + e.getMessage();
		}
	}

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

			System.out.println("DEBUG: Gefundene Theme-Dateien: " + files.length);

			for (File file : files) {

				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
					ThemeDTO theme = (ThemeDTO) ois.readObject();
					themes.add(theme);

					System.out.println("DEBUG: Theme geladen: " + theme.getThemeTitle() + " (ID: " + theme.getId() + ")");
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
	 * @return The next available theme ID
	 */
	private int createNewThemeId() {
		int maxId = 0;
		File folder = new File(FOLDER);
		if (folder.exists() && folder.isDirectory()) {

			@SuppressWarnings("unused")
			File[] files = folder.listFiles((dir, name) -> name.startsWith(THEME_PREFIX) && name.endsWith(EXTENSION));

			if (files != null) {

				System.out.println("DEBUG: Analysiere " + files.length + " existierende Dateien für ID-Vergabe");

				for (File file : files) {
					String name = file.getName();
					String idStr = name.substring(THEME_PREFIX.length(), name.length() - EXTENSION.length());

					try {
						int id = Integer.parseInt(idStr);
						maxId = Math.max(maxId, id);
						System.out.println("DEBUG: Gefundene ID: " + id + ", aktuelle Max-ID: " + maxId);
					} catch (NumberFormatException e) {
						System.out.println("DEBUG: Konnte ID nicht parsen aus: " + name);
					}
				}
			}
		}
		int newId = maxId + 1;
		System.out.println("DEBUG: Neue ID wird sein: " + newId);
		return newId;
	}

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

	@Override
	public ArrayList<QuestionDTO> getQuestionsFor(ThemeDTO theme) {
		return (ArrayList<QuestionDTO>) theme.getQuestions();
	}

	@Override
	public ArrayList<AnswerDTO> getAnswersFor(QuestionDTO question) {
		return (ArrayList<AnswerDTO>) question.getAnswers();
	}

	/**
	 * Deletes a theme from persistent storage.
	 * 
	 * @param theme The theme to delete
	 * @return null if successful, error message if failed
	 */
	@Override
	public String deleteTheme(ThemeDTO theme) {
		try {
			if (theme == null || theme.getId() <= 0) {
				return "Ungültiges Theme";
			}

			String filename = FOLDER + "/" + THEME_PREFIX + theme.getId() + EXTENSION;
			File file = new File(filename);

			if (file.exists()) {

				if (file.delete()) {
					System.out.println("DEBUG: Theme-Datei gelöscht: " + filename);
					notifyListeners();
					return null;
				} else {

					return "Datei konnte nicht gelöscht werden";
				}
			} else {

				return "Theme-Datei nicht gefunden: " + filename;
			}
		} catch (Exception e) {

			e.printStackTrace();
			return "Fehler beim Löschen: " + e.getMessage();
		}
	}

	@Override
	public String saveQuestion(QuestionDTO q) {
		// TODO: Implementation needed
		return null;
	}

	@Override
	public String deleteQuestion(QuestionDTO q) {
		// TODO: Implementation needed
		return null;
	}
}

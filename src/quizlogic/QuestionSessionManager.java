package quizlogic;

import java.util.*;
import quizlogic.dto.QuestionDTO;
import quizlogic.dto.ThemeDTO;

/**
 * Manages question sessions to avoid repeating questions too frequently. Tracks
 * which questions have been asked and ensures variety in question selection.
 */
public class QuestionSessionManager {

	/** Set containing IDs of already asked questions globally */
	private Set<Integer> globalAskedQuestions = new HashSet<>();

	/** Map containing already asked questions per theme */
	private Map<Integer, Set<Integer>> themeAskedQuestions = new HashMap<>();

	/** List of available question IDs for current global session */
	private List<Integer> globalAvailableQuestions = new ArrayList<>();

	/** Map of available question IDs per theme for current session */
	private Map<Integer, List<Integer>> themeAvailableQuestions = new HashMap<>();

	/** Maximum number of questions to remember as "recently asked" */
	private static final int MAX_RECENT_QUESTIONS = 500;

	/** Queue to track recently asked questions for global session */
	private Queue<Integer> recentGlobalQuestions = new LinkedList<>();

	/** Map of recently asked questions per theme */
	private Map<Integer, Queue<Integer>> recentThemeQuestions = new HashMap<>();

	/**
	 * Gets a random question from all available themes, avoiding recently asked
	 * questions.
	 * 
	 * @param allQuestions List of all available questions
	 * @return A randomly selected question that hasn't been asked recently, or null
	 *         if no questions available
	 */
	public QuestionDTO getRandomQuestionWithVariety(List<QuestionDTO> allQuestions) {
		if (allQuestions == null || allQuestions.isEmpty()) {
			return null;
		}

		if (globalAvailableQuestions.isEmpty()) {
			initializeGlobalSession(allQuestions);
		}

		if (globalAvailableQuestions.isEmpty()) {
			resetGlobalSession(allQuestions);
		}

		Random random = new Random();
		int randomIndex = random.nextInt(globalAvailableQuestions.size());
		Integer selectedQuestionId = globalAvailableQuestions.remove(randomIndex);

		addToRecentQuestions(selectedQuestionId, recentGlobalQuestions);
		globalAskedQuestions.add(selectedQuestionId);

		return findQuestionById(allQuestions, selectedQuestionId);
	}

	/**
	 * Gets a random question from a specific theme, avoiding recently asked
	 * questions.
	 * 
	 * @param theme The theme to select a question from
	 * @return A randomly selected question from the theme that hasn't been asked
	 *         recently
	 */
	public QuestionDTO getRandomQuestionForThemeWithVariety(ThemeDTO theme) {
		if (theme == null || theme.getQuestions() == null || theme.getQuestions().isEmpty()) {
			return null;
		}

		int themeId = theme.getId();

		if (!themeAvailableQuestions.containsKey(themeId) || themeAvailableQuestions.get(themeId).isEmpty()) {
			initializeThemeSession(theme);
		}

		if (themeAvailableQuestions.get(themeId).isEmpty()) {
			resetThemeSession(theme);
		}

		List<Integer> availableForTheme = themeAvailableQuestions.get(themeId);
		if (availableForTheme.isEmpty()) {

			Random random = new Random();
			int randomIndex = random.nextInt(theme.getQuestions().size());
			return theme.getQuestions().get(randomIndex);
		}

		Random random = new Random();
		int randomIndex = random.nextInt(availableForTheme.size());
		Integer selectedQuestionId = availableForTheme.remove(randomIndex);

		Queue<Integer> recentForTheme = recentThemeQuestions.computeIfAbsent(themeId, _ -> new LinkedList<>());
		addToRecentQuestions(selectedQuestionId, recentForTheme);

		Set<Integer> askedForTheme = themeAskedQuestions.computeIfAbsent(themeId, _ -> new HashSet<>());
		askedForTheme.add(selectedQuestionId);

		// Find and return the question
		return findQuestionById(theme.getQuestions(), selectedQuestionId);
	}

	/**
	 * Initializes the global question session with all available questions.
	 */
	private void initializeGlobalSession(List<QuestionDTO> allQuestions) {
		globalAvailableQuestions.clear();
		for (QuestionDTO question : allQuestions) {
			if (!recentGlobalQuestions.contains(question.getId())) {
				globalAvailableQuestions.add(question.getId());
			}
		}
	}

	/**
	 * Initializes the question session for a specific theme.
	 */
	private void initializeThemeSession(ThemeDTO theme) {
		int themeId = theme.getId();
		List<Integer> availableForTheme = new ArrayList<>();

		Queue<Integer> recentForTheme = recentThemeQuestions.get(themeId);

		for (QuestionDTO question : theme.getQuestions()) {
			if (recentForTheme == null || !recentForTheme.contains(question.getId())) {
				availableForTheme.add(question.getId());
			}
		}

		themeAvailableQuestions.put(themeId, availableForTheme);
	}

	/**
	 * Resets the global session, allowing all questions except recent ones to be
	 * asked again.
	 */
	private void resetGlobalSession(List<QuestionDTO> allQuestions) {
		globalAskedQuestions.clear();
		initializeGlobalSession(allQuestions);
	}

	/**
	 * Resets the session for a specific theme, allowing all questions except recent
	 * ones to be asked again.
	 */
	private void resetThemeSession(ThemeDTO theme) {
		int themeId = theme.getId();
		if (themeAskedQuestions.containsKey(themeId)) {
			themeAskedQuestions.get(themeId).clear();
		}
		initializeThemeSession(theme);
	}

	/**
	 * Adds a question to the recent questions queue, maintaining the maximum size.
	 */
	private void addToRecentQuestions(Integer questionId, Queue<Integer> recentQueue) {
		if (recentQueue.size() >= MAX_RECENT_QUESTIONS) {
			recentQueue.poll();
		}
		recentQueue.offer(questionId);
	}

	/**
	 * Finds a question by its ID in a list of questions.
	 */
	private QuestionDTO findQuestionById(List<QuestionDTO> questions, Integer questionId) {
		for (QuestionDTO question : questions) {
			if (question.getId() == questionId) {
				return question;
			}
		}
		return null;
	}

	/**
	 * Resets all question sessions, allowing all questions to be asked again.
	 */
	public void resetAllSessions() {
		globalAskedQuestions.clear();
		globalAvailableQuestions.clear();
		themeAskedQuestions.clear();
		themeAvailableQuestions.clear();
		recentGlobalQuestions.clear();
		recentThemeQuestions.clear();
	}

	/**
	 * Returns the number of remaining unasked questions globally.
	 */
	public int getRemainingQuestionsCount() {
		return globalAvailableQuestions.size();
	}

	/**
	 * Returns the number of remaining unasked questions for a specific theme.
	 */
	public int getRemainingQuestionsCountForTheme(int themeId) {
		List<Integer> available = themeAvailableQuestions.get(themeId);
		return available != null ? available.size() : 0;
	}
}
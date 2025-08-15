package persistence.serialization.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import persistence.DataAccessObject;

/**
 * Serializable Data Access Object (DAO) representing a quiz theme, used in the
 * file-based serialization persistence implementation.
 * <p>
 * This DAO persists its data directly to disk using Java serialization. Stored
 * data includes:
 * <ul>
 * <li>Unique theme ID</li>
 * <li>Theme title</li>
 * <li>List of associated questions</li>
 * </ul>
 * <p>
 * Files are stored in the {@code ./quizData} folder with a naming pattern:
 * 
 * <pre>
 * Theme.{id}
 * </pre>
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public class ThemeDAO_Serializable extends DataAccessObject {

	private static final long serialVersionUID = 1L;

	/** Directory where serialized theme files are stored */
	private static final String FOLDER = ".\\quizData";

	/** File name base pattern for serialized theme files */
	private static final String FILE = FOLDER + "\\Theme.";

	/** Theme title */
	private String Title;

	/** List of questions associated with this theme */
	private ArrayList<?> questions;

	/**
	 * Saves this ThemeDAO_Serializable instance to disk using Java serialization.
	 * <p>
	 * If the theme has no ID assigned, a new one is generated automatically.
	 */
	public void save() {
		try {
			if (getId() == -1)
				setId(createNewId());
			FileOutputStream fileOutputStream = new FileOutputStream(FILE + getId());
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(this);
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a ThemeDAO_Serializable object from disk by its ID.
	 *
	 * @param id the unique theme ID
	 * @return the loaded {@link ThemeDAO_Serializable} instance, or null if read
	 *         fails
	 */
	public static ThemeDAO_Serializable readById(int id) {
		ThemeDAO_Serializable theme = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(FILE + id);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			theme = (ThemeDAO_Serializable) objectInputStream.readObject();
			objectInputStream.close();
			return theme;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return theme;
	}

	/**
	 * Gets the theme title.
	 *
	 * @return theme title
	 */
	public String getThemeTitle() {
		return Title;
	}

	/**
	 * Sets the theme title.
	 *
	 * @param title the new theme title
	 */
	public void setThemeTitle(String title) {
		Title = title;
	}

	/**
	 * Gets the list of questions belonging to this theme.
	 *
	 * @return list of questions
	 */
	public ArrayList<?> getQuestions() {
		return questions;
	}

	/**
	 * Sets the list of questions belonging to this theme.
	 *
	 * @param questions the list to assign
	 */
	public void setQuestions(ArrayList<?> questions) {
		this.questions = questions;
	}

	/**
	 * Creates a new unique theme ID by counting the number of existing theme files
	 * in the storage folder.
	 *
	 * @return new theme ID
	 */
	public int createNewId() {
		File folder = new File(FOLDER);
		int count = 0;
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory())
				count++;
		}
		return ++count;
	}
}

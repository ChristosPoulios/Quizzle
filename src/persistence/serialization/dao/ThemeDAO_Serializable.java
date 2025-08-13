package persistence.serialization.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import persistence.DataAccessObject;

public class ThemeDAO_Serializable extends DataAccessObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String FOLDER = ".\\quizData";
	private static final String FILE = FOLDER + "\\Theme.";

	private String Title;
	private ArrayList<QuestionDAO_Serializable> questions;
	
	public void save() {

		FileOutputStream fileOutputStream;
		try {
			if (getId() == -1)
				setId(createNewId());
				
			fileOutputStream = new FileOutputStream(FILE + getId());

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(this);
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Liest ein Thema anhand seiner Id 
	 * @param id
	 * @return
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


	public String getThemeTitle() {
		return Title;
	}

	public void setThemeTitle(String title) {
		Title = title;
	}

	public ArrayList<QuestionDAO_Serializable> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<QuestionDAO_Serializable> questions) {
		this.questions = questions;
	}

	/**
	 * id = Anzahl der Dateien im Folder + 1
	 * @return
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

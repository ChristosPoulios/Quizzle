package quizlogic;

import java.io.Serializable;

/**
 * The class genralizes all objects of the logic layer.
 */
public class DataTransportObject implements Serializable {

	protected static final long serialVersionUID = 1L;

	private int id = -1;


	public DataTransportObject(int id) {
		super();

		this.id = id;
	}

	public DataTransportObject() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

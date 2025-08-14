package quizlogic;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class generalizes all objects of the logic layer. Provides common
 * functionality for all Data Transfer Objects.
 */
public abstract class DataTransportObject implements Serializable {

	protected static final long serialVersionUID = 1L;

	private int id = -1;

	/**
	 * Default constructor for new entities
	 */
	public DataTransportObject() {
		super();
	}

	/**
	 * Constructor with ID for existing entities
	 * 
	 * @param id the entity ID
	 */
	public DataTransportObject(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Checks if this is a new (unsaved) entity
	 * 
	 * @return true if entity is new
	 */
	public boolean isNew() {
		return id <= 0;
	}

	/**
	 * Checks if this entity has been persisted
	 * 
	 * @return true if entity has been saved
	 */
	public boolean isPersisted() {
		return id > 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		DataTransportObject that = (DataTransportObject) obj;

		if (this.isNew() && that.isNew()) {
			return this.contentEquals(that);
		}

		return this.id == that.id;
	}

	@Override
	public int hashCode() {
		return isNew() ? contentHashCode() : Objects.hash(id);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("{id=").append(id);

		String content = getContentString();
		if (content != null && !content.trim().isEmpty()) {
			sb.append(", ").append(content);
		}

		sb.append("}");
		return sb.toString();
	}

	/**
	 * Template method for content-based equality comparison. Used when both objects
	 * are new entities.
	 * 
	 * @param other the other DataTransportObject to compare with
	 * @return true if content is equal
	 */
	protected abstract boolean contentEquals(DataTransportObject other);

	/**
	 * Template method for content-based hash code. Used when object is a new
	 * entity.
	 * 
	 * @return hash code based on content
	 */
	protected abstract int contentHashCode();

	/**
	 * Template method for content description in toString.
	 * 
	 * @return string representation of main content
	 */
	protected abstract String getContentString();

	/**
	 * Validates the DTO content
	 * 
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		try {
			validate();
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * Template method for validation logic. Should throw IllegalArgumentException
	 * for validation errors.
	 */
	protected abstract void validate();
}

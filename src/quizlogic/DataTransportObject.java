package quizlogic;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class for all Data Transfer Objects (DTOs) in the logic layer.
 * <p>
 * This class provides common functionality for all DTOs, including:
 * <ul>
 * <li>ID management for persisted and non-persisted entities</li>
 * <li>Equality and hash code handling based on either ID or content</li>
 * <li>String representation with content description</li>
 * <li>Validation framework for DTO data integrity</li>
 * </ul>
 * <p>
 * Subclasses must implement content comparison, content hash code, content
 * string, and validation logic.
 *
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public abstract class DataTransportObject implements Serializable {

	/** Serialization version UID for consistent serialization */
	protected static final long serialVersionUID = 1L;

	/**
	 * Unique identifier for the entity. Default is -1 indicating unsaved/new
	 * entity.
	 */
	private int id = -1;

	/**
	 * Default constructor for creating a new (unsaved) entity DTO. Assigns a
	 * default ID of -1.
	 */
	public DataTransportObject() {
		super();
	}

	/**
	 * Constructor for creating a DTO representing an existing persisted entity.
	 *
	 * @param id the unique ID of the entity
	 */
	public DataTransportObject(int id) {
		super();
		this.id = id;
	}

	/**
	 * Gets the unique identifier of this DTO.
	 *
	 * @return the entity ID, or -1 if not yet persisted
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identifier of this DTO.
	 *
	 * @param id the entity ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Checks if this DTO represents a new (unsaved) entity.
	 *
	 * @return {@code true} if entity ID is less than or equal to 0, otherwise false
	 */
	public boolean isNew() {
		return id <= 0;
	}

	/**
	 * Checks if this DTO has been persisted.
	 *
	 * @return {@code true} if entity ID is greater than 0, otherwise false
	 */
	public boolean isPersisted() {
		return id > 0;
	}

	/**
	 * Compares this DTO to another object for equality.
	 * <p>
	 * Equality rules:
	 * <ul>
	 * <li>If both objects are new (unsaved), equality is based on
	 * {@link #contentEquals(DataTransportObject)}</li>
	 * <li>Otherwise, equality is based on their IDs</li>
	 * </ul>
	 *
	 * @param obj the object to compare
	 * @return true if equal according to the above rules, false otherwise
	 */
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

	/**
	 * Generates a hash code for this DTO.
	 * <p>
	 * If the entity is new (unsaved), the hash code is based on its content.
	 * Otherwise, the hash code is based solely on the ID.
	 *
	 * @return the computed hash code value
	 */
	@Override
	public int hashCode() {
		return isNew() ? contentHashCode() : Objects.hash(id);
	}

	/**
	 * Creates a string representation of this DTO, including ID and main content
	 * description.
	 *
	 * @return human-readable string representation of the DTO
	 */
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
	 * Template method for content-based equality comparison. This is called when
	 * both objects are new entities and have no IDs yet.
	 *
	 * @param other the other DataTransportObject to compare with
	 * @return true if content is equal, false otherwise
	 */
	protected abstract boolean contentEquals(DataTransportObject other);

	/**
	 * Template method for generating a hash code based on the main content of this
	 * DTO. This is used when the DTO is a new entity.
	 *
	 * @return hash code computed from the main content fields
	 */
	protected abstract int contentHashCode();

	/**
	 * Template method for generating a string description of the main content. This
	 * is used in {@link #toString()} for representation purposes.
	 *
	 * @return a string summary of the DTO's main content
	 */
	protected abstract String getContentString();

	/**
	 * Checks if the DTO content is valid.
	 *
	 * @return true if the DTO passes {@link #validate()} without throwing an
	 *         exception, false otherwise
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
	 * Template method for implementing validation logic in subclasses.
	 * <p>
	 * Subclasses should override this method to:
	 * <ul>
	 * <li>Ensure all required fields are populated</li>
	 * <li>Ensure values meet length or format constraints</li>
	 * <li>Throw {@link IllegalArgumentException} if validation fails</li>
	 * </ul>
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	protected abstract void validate();
}

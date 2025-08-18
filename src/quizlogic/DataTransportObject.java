package quizlogic;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class for all Data Transfer Objects (DTOs) within the
 * application logic layer.
 * <p>
 * This class provides common functionality shared by all DTOs, including:
 * <ul>
 * <li>Unique identifier management</li>
 * <li>Content-based equality and hashing logic</li>
 * <li>String representation for debugging</li>
 * <li>Validation support</li>
 * </ul>
 * <p>
 * Subclasses must implement abstract methods to define content-based
 * comparison, hashing, string content description, and validation rules
 * specific to their fields.
 * <p>
 * This class also implements {@link Serializable} to allow instances of DTOs to
 * be serialized for storage or transmission.
 * 
 * @author Christos Poulios
 * @version 1.0
 * @since 1.0
 */
public abstract class DataTransportObject implements Serializable {

	/**
	 * Serialization version UID for ensuring compatibility during serialization.
	 */
	protected static final long serialVersionUID = 1L;

	/**
	 * The unique identifier for the entity this DTO represents.
	 * <p>
	 * A default of -1 indicates the entity is new and has not yet been persisted.
	 */
	private int id = -1;

	/**
	 * Default constructor creating a new (unsaved) DTO entity.
	 * <p>
	 * The ID is initialized to -1 to indicate the entity is not yet persisted.
	 */
	public DataTransportObject() {
		super();
	}

	/**
	 * Constructs a DTO representing an existing persisted entity with a known ID.
	 * 
	 * @param id the unique identifier of a persisted entity
	 */
	public DataTransportObject(int id) {
		super();
		this.id = id;
	}

	/**
	 * Returns the unique identifier of this DTO.
	 * <p>
	 * An ID of -1 indicates the entity has not yet been persisted.
	 *
	 * @return the entity ID or -1 if not persisted
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identifier of this DTO.
	 *
	 * @param id the unique entity identifier to assign
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Indicates whether this DTO represents a new (unsaved) entity.
	 *
	 * @return true if the entity ID is less than or equal to 0, false otherwise
	 */
	public boolean isNew() {
		return id <= 0;
	}

	/**
	 * Indicates whether this DTO has been persisted.
	 *
	 * @return true if the entity ID is greater than 0, false otherwise
	 */
	public boolean isPersisted() {
		return id > 0;
	}

	/**
	 * Compares this DTO with another object for equality.
	 * <p>
	 * If both objects are new (unsaved), it compares their content using
	 * {@link #contentEquals(DataTransportObject)}. If both have IDs, it compares
	 * the IDs directly.
	 *
	 * @param obj the object to compare with this DTO
	 * @return true if both objects are equal based on their IDs or content, false
	 *         otherwise
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
	 * Computes a hash code for this DTO.
	 * <p>
	 * If the DTO is new (unsaved), it uses {@link #contentHashCode()} to compute
	 * the hash based on its content. If it has an ID, it uses the ID for hashing.
	 *
	 * @return hash code based on ID or content
	 */
	@Override
	public int hashCode() {
		return isNew() ? contentHashCode() : Objects.hash(id);
	}

	/**
	 * Returns a string representation of this DTO.
	 * <p>
	 * The format includes the class name, ID, and a summary of the main content
	 * fields as defined by {@link #getContentString()}.
	 *
	 * @return a string summary of this DTO's state
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

/**
 * Relationship
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.output;

/**
 * The Class Relationship.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class Relationship {

	/** The directed. */
	private final static boolean directed = true;

	/** The owner. */
	private final int owner;

	/** The target. */
	private final int target;

	/** The value. */
	private int value = 1;

	/**
	 * Instantiates a new relationship.
	 *
	 * @param owner
	 *            the owner
	 * @param target
	 *            the target
	 */
	public Relationship(final int owner, final int target) {
		this.owner = owner;
		this.target = target;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Relationship other = (Relationship) obj;
		if (this.owner != other.owner) {
			return false;
		}
		if (this.target != other.target) {
			return false;
		}
		if (this.value != other.value) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public int getOwner() {
		return this.owner;
	}

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public int getTarget() {
		return this.target;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.owner;
		result = (prime * result) + this.target;
		result = (prime * result) + this.value;
		return result;
	}

	/**
	 * Increment value.
	 *
	 * @return the int
	 */
	int incrementValue() {
		return ++this.value;
	}

	/**
	 * Checks if is directed.
	 *
	 * @return true, if is directed
	 */
	public boolean isDirected() {
		return Relationship.directed;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.owner + " " + this.target + " " + this.value;
	}
}

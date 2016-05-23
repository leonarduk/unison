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
	private boolean directed = true;

	/** The owner. */
	private final int owner;

	/** The target. */
	private final int target;

	/** The value. */
	public int value = 1;

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

	/**
	 * Need to over-ride hashcode and equals to be able to make a compund key.
	 *
	 * @param obj
	 *            the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Relationship)) {
			return false;
		}
		final Relationship other = (Relationship) obj;

		// Not interested in value - if we have this link
		// already we will just add to this one
		return ((other.getOwner() == this.getOwner()) && (other.getTarget() == this.getTarget())
		        && (other.isDirected() == this.isDirected()));
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

	/**
	 * Need to over-ride hashcode and equals to be able to make a compund key.
	 *
	 * @return the int
	 * @Override
	 */
	@Override
	public int hashCode() {
		int hashcode = this.owner + this.target;

		if (this.isDirected()) {
			hashcode++;
		}
		return hashcode;
	}

	/**
	 * Increment value.
	 *
	 * @return the int
	 */
	public int incrementValue() {
		return ++this.value;
	}

	/**
	 * Checks if is directed.
	 *
	 * @return true, if is directed
	 */
	public boolean isDirected() {
		return this.directed;
	}

	/**
	 * Make undirected.
	 */
	public void makeUndirected() {
		this.directed = false;
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

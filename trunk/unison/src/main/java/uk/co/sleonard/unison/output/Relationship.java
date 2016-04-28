package uk.co.sleonard.unison.output;

public class Relationship {
	private boolean directed = true;

	private final int owner;

	private final int target;

	public int value = 1;

	public Relationship(final int owner, final int target) {
		this.owner = owner;
		this.target = target;
	}

	/**
	 * Need to over-ride hashcode and equals to be able to make a compund key
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Relationship)) {
			return false;
		}
		final Relationship other = (Relationship) obj;

		// Not interested in value - if we have this link
		// already we will just add to this one
		if ((other.getOwner() == this.getOwner())
				&& (other.getTarget() == this.getTarget())
				&& (other.isDirected() == this.isDirected())) {
			return true;
		}

		return false;
	}

	public int getOwner() {
		return this.owner;
	}

	public int getTarget() {
		return this.target;
	}

	public int getValue() {
		return this.value;
	}

	/**
	 * Need to over-ride hashcode and equals to be able to make a compund key
	 * 
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

	public int incrementValue() {
		return ++this.value;
	}

	public boolean isDirected() {
		return this.directed;
	}

	public void makeUndirected() {
		this.directed = false;
	}

	@Override
	public String toString() {
		return this.owner + " " + this.target + " " + this.value;
	}
}
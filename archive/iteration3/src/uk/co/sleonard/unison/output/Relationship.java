package uk.co.sleonard.unison.output;

public class Relationship {
	private boolean directed = true;

	private int owner;

	private int target;

	public int value = 1;

	public Relationship(int owner, int target) {
		this.owner = owner;
		this.target = target;
	}

	/**
	 * Need to over-ride hashcode and equals to be able to make a compund
	 * key
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Relationship)) {
			return false;
		}
		Relationship other = (Relationship) obj;

		// Not interested in value - if we have this link
		// already we will just add to this one
		if (other.getOwner() == this.getOwner()
				&& other.getTarget() == this.getTarget()
				&& (other.isDirected() == isDirected())) {
			return true;
		}

		return false;
	}

	public int getOwner() {
		return owner;
	}

	public int getTarget() {
		return target;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Need to over-ride hashcode and equals to be able to make a compund
	 * key
	 * 
	 * @Override
	 */
	@Override
	public int hashCode() {
		int hashcode = owner + target;

		if (isDirected()) {
			hashcode++;
		}
		return hashcode;
	}

	public int incrementValue() {
		return ++value;
	}

	public boolean isDirected() {
		return directed;
	}

	public void makeUndirected() {
		directed = false;
	}

	@Override
	public String toString() {
		return owner + " " + target + " " + value;
	}
}
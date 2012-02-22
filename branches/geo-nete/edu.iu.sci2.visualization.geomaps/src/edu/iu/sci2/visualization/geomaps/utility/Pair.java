package edu.iu.sci2.visualization.geomaps.utility;

import com.google.common.base.Objects;

class Pair<T> {
	private T left;
	private T right;

	private Pair(T left, T right) {
		/* TODO Assert not null? */
		this.left = left;
		this.right = right;
	}
	public static <T> Pair<T> of(T left, T right) {
		return new Pair<T>(left, right);
	}
	

	public T getLeft() {
		return left;
	}

	public T getRight() {
		return right;
	}
	
	public boolean arePartsEqual() {
		return Objects.equal(getLeft(), getRight());
	}
	
	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Pair<?>)) {
			return false;
		}		
		Pair<?> that = (Pair<?>) thatObject;
		
		return Objects.equal(this.left, that.left)
			&& Objects.equal(this.right, that.right);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(this.left, this.right);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("left", this.left)
				.add("right", this.right)
				.toString();
	}
}

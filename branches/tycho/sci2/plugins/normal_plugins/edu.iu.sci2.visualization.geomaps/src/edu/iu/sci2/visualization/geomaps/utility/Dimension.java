package edu.iu.sci2.visualization.geomaps.utility;

import com.google.common.base.Objects;

/**
 * A simple two-dimensional, generic-valued dimension value class.  
 */
public class Dimension<T> {
	private final T width;
	private final T height;

	private Dimension(T width, T height) {
		this.width = width;
		this.height = height;
	}
	public static <T> Dimension<T> ofSize(T width, T height) {
		return new Dimension<T>(width, height);
	}
	public static <T> Dimension<T> copyOf(Dimension<T> dimension) {
		return ofSize(dimension.width, dimension.height);
	}	
	public static Dimension<Integer> fromAWTDimension(java.awt.Dimension awtDimension) {
		return ofSize(awtDimension.width, awtDimension.height);
	}
	
	public static java.awt.Dimension copyAsAWTDimension(Dimension<? extends Number> dimension) {
		return new java.awt.Dimension(dimension.width.intValue(), dimension.height.intValue());
	}
	
	
	public T getWidth() {
		return width;
	}
	
	public T getHeight() {
		return height;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(width, height);
	}
	
	@Override
	public boolean equals(Object thatObject) {
		if (this == thatObject) { return true; }
		if (thatObject == null) { return false; }
		if (!(thatObject instanceof Dimension<?>)) { return false; }
		Dimension<?> that = (Dimension<?>) thatObject;

		return Objects.equal(this.width,
							 that.width) &&
			   Objects.equal(this.height,
					   		 that.height);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("width", width)
				.add("height", height)
				.toString();
	}
}

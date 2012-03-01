package edu.iu.sci2.visualization.geomaps.viz.strategy;


public class CircleAreaStrategy implements Strategy {
	private final double area;
	
	/**
	 * @param area
	 */
	private CircleAreaStrategy(double area) {
		this.area = area;
	}	
	public static CircleAreaStrategy forArea(double area) {
		return new CircleAreaStrategy(area);
	}

	public double getArea() {
		return area;
	}

	@Override
	public String toPostScript() {
		return String.valueOf(area); // TODO :\
	}

	public static double calculateAreaFromRadius(double radius) {
		return (Math.PI * radius * radius);
	}
	
	public static double calculateRadiusFromArea(double area) {
		return Math.sqrt(area / Math.PI);
	}
}

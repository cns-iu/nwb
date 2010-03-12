package edu.iu.scipolicy.visualization.scimap.references;

public class Edge {
	private int source;
	private int target;
	private float weight;
	private String color;
	private float red;
	private float blue;
	private float green;
	
	public Edge(String source, String target, String weight, String color) {
		setSource(source);
		setTarget(target);
		setWeight(weight);
		setColor(color);
	}
	
	public void setSource(String source) {
		this.source = Integer.parseInt(source);
	}
	public int getSource() {
		return source;
	}
	public void setTarget(String target) {
		this.target = Integer.parseInt(target);
	}
	public int getTarget() {
		return target;
	}
	public void setWeight(String weight) {
		this.weight = Float.parseFloat(weight);
	}
	public float getWeight() {
		return weight;
	}
	public void setColor(String color) {
		String[] components = color.split(":");
		red = Float.parseFloat(components[0]) / 255;
		green = Float.parseFloat(components[1]) / 255;
		blue = Float.parseFloat(components[2]) / 255;
	}
	
	public float getRed() {
		return red;
	}
	
	public float getGreen() {
		return green;
	}

	public float getBlue() {
		return blue;
	}
}

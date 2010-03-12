package edu.iu.scipolicy.visualization.scimap.references;


public class Node {	
	private String pajek;
	private int id;
	private float x;
	private float y;
	private float size;
	private float red;
	private float green;
	private float blue;
	private String category;
	
	
	public Node(String pajek, String id, String x, String y, String size, String color) {
		setPajek(pajek);
		setId(id);
		setX(x);
		setY(y);
		setSize(size);
		setColor(color);
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setColor(String color) {
		category = MapOfScience.categories.get(color);
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
	
	public void setPajek(String pajek) {
		this.pajek = pajek;
	}
	public String getPajek() {
		return pajek;
	}
	public void setId(String id) {
		this.id = Integer.parseInt(id);
	}
	public int getId() {
		return id;
	}
	public void setX(String x) {
		this.x = Float.parseFloat(x) * 72;
	}
	public float getX() {
		return x;
	}
	public void setY(String y) {
		this.y = Float.parseFloat(y) * 72;
	}
	public float getY() {
		return y;
	}
	public void setSize(String size) {
		this.size = Float.parseFloat(size);
	}
	public float getSize() {
		return size;
	}

	
}

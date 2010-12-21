package edu.iu.scipolicy.visualization.scimap.references;

public class JournalLocation {
	private String name;
	private int id;
	private float fraction;

	public JournalLocation(String name, String id, String fraction) {
		setName(name);
		setId(id);
		setFraction(fraction);
	}

	private void setFraction(String fraction) {
		this.fraction = Float.parseFloat(fraction);
	}

	private void setId(String id) {
		this.id = Integer.parseInt(id);
	}

	private void setName(String name) {
		this.name = name;
	}
	
	public float getFraction() {
		return fraction;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return MapOfScience.nodes.get(id).getCategory();
	}
}

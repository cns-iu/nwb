package edu.iu.sci2.database.star.extract.network;

public class LeafTableDescriptor {
	private String name;
	private String type;

	public LeafTableDescriptor(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}
}
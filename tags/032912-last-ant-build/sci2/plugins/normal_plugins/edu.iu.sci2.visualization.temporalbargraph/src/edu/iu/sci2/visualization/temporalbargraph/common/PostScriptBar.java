package edu.iu.sci2.visualization.temporalbargraph.common;

import java.awt.Color;

public class PostScriptBar {
	private String name;
	private double daysSinceEarliest;
	private double lengthInDays;
	private double amountPerDay;
	private Color color;

	/**
	 * A postscript bar keeps all of the knowledge that postscript needs to know
	 * to create a bar and label
	 * 
	 * @param daysSinceEarliest
	 * @param lengthInDays
	 * @param amountPerDay
	 */
	public PostScriptBar(double daysSinceEarliest, double lengthInDays,
			double amountPerDay, Record record, Color color) {
		super();
		this.name = record.getLabel();
		this.daysSinceEarliest = daysSinceEarliest;
		this.lengthInDays = lengthInDays;
		this.amountPerDay = amountPerDay;
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	public String getName() {
		return this.name;
	}

	public double daysSinceEarliest() {
		return this.daysSinceEarliest;
	}

	public double lengthInDays() {
		return this.lengthInDays;
	}

	public double amountPerDay() {
		return this.amountPerDay;
	}

	public double getArea() {
		return this.lengthInDays * this.amountPerDay;
	}

	@Override
	public String toString() {
		return "PostScriptBar [name=" + this.name + ", daysSinceEarliest="
				+ this.daysSinceEarliest + ", lengthInDays=" + this.lengthInDays
				+ ", amountPerDay=" + this.amountPerDay + ", color=" + this.color + "]";
	}


}

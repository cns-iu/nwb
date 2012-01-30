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
		return color;
	}

	public String getName() {
		return name;
	}

	public double daysSinceEarliest() {
		return daysSinceEarliest;
	}

	public double lengthInDays() {
		return lengthInDays;
	}

	public double amountPerDay() {
		return amountPerDay;
	}

	public double getArea() {
		return lengthInDays * amountPerDay;
	}

	@Override
	public String toString() {
		return "PostScriptBar [name=" + name + ", daysSinceEarliest="
				+ daysSinceEarliest + ", lengthInDays=" + lengthInDays
				+ ", amountPerDay=" + amountPerDay + ", color=" + color + "]";
	}


}

package edu.iu.sci2.visualization.horizontalbargraph.bar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple model of color label that includes the attributes of 
 * the label of the color and the color box. 
 * @author kongch
 *
 */
public class ColorLegend {
	private String title;
	private double x;
	private double y;
	private List<ColorLegendLabel> labelList;
	
	public ColorLegend(String title,
						double x,
						double y){
		this.title = "Categorized by " + title;
		this.x = x;
		this.y = y;
		this.labelList = new ArrayList<ColorLegendLabel>();
	}

	public String getTitle() {
		return title;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public List<ColorLegendLabel> getColorLegendLabelList() {
		return labelList;
	}
	
	public boolean isEmpty() {
		return labelList.isEmpty();
	}
	
	public void add(ColorLegendLabel colorLegendLabel) {
		if(colorLegendLabel!=null){
			labelList.add(colorLegendLabel);
		}
	}

}

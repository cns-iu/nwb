package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.google.common.collect.ImmutableList;


public class HowToRead implements PostScriptable {
	public static final double TITLE_FONT_GRAY = 0.0;
	public static final double TEXT_FONT_GRAY = 0.15;
	
	// TODO This text is specific to the world map and the circles mode..
	public static final ImmutableList<String> TEXT_LINES =
			ImmutableList.of(
					"This proportional symbol map visualizes all records per",
					"unique geolocation by a circle. All circles are placed",
					"on a map and are area coded by a numerical value.",
					"Interior and exterior circle colors can represent",
					"additional attribute values. Minimum and maximum data",
					"values are given in the legend.");
	
	private final Point2D.Double lowerLeft;

	public HowToRead(Double lowerLeft) {
		this.lowerLeft = lowerLeft;
	}

	
	@Override
	public String toPostScript() {
		String howToRead = "";
		
		howToRead += "% How to Read" + "\n";
		howToRead += "gsave" + "\n";
		
		howToRead += String.format("%f %f moveto" + "\n", lowerLeft.x, lowerLeft.y);
		
		howToRead += PSUtility.findscalesetfont(GeoMapViewPS.TITLE_FONT) + "\n";
		howToRead += PSUtility.setgray(TITLE_FONT_GRAY) + "\n";
		howToRead += "(How to Read This Map) show" + "\n";
		
		howToRead += String.format("%f %f moveto", lowerLeft.x, lowerLeft.y - GeoMapViewPS.TITLE_FONT.getSize()) + "\n";
		
		howToRead += PSUtility.findscalesetfont(GeoMapViewPS.CONTENT_FONT) + "\n";
		howToRead += PSUtility.setgray(TEXT_FONT_GRAY) + "\n";
		
		for (String textLine : TEXT_LINES) {
			howToRead += PSUtility.showAndNewLine(textLine, GeoMapViewPS.CONTENT_FONT.getSize());
		}
		
		howToRead += "grestore" + "\n";
		
		return howToRead;
	}
	
	public static void main(String[] args) {
		System.out.println(new HowToRead(new Point2D.Double(100.0, 200.0)).toPostScript());
	}
}

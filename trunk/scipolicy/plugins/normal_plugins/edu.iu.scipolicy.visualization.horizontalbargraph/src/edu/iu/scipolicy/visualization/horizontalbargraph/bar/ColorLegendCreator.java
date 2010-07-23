package edu.iu.scipolicy.visualization.horizontalbargraph.bar;

import edu.iu.scipolicy.visualization.horizontalbargraph.bar.ColorizedByRegistry;

/**
 * Create the ColorLegend that used for the bars. It used the data
 * provided by the ColorizedByRegistry and generate a model that
 * represents the display of the color legend.
 * 
 * It is implemented as a independent class which is differ from 
 * BasicLayout.createBars() for better responsibility distribution.
 * 
 * @author kongch
 *
 */
public class ColorLegendCreator {
	public final static double COLOR_LEGEND_LEFT = 36.0;
	public final static double COLOR_LEGEND_TOP = -480.0;
	public final static double TITLE_HEIGHT = -10.0;
	public final static double BOX_WIDTH = 15.0;
	public final static double BOX_HEIGHT = -5.0;
	public final static double BOX_LEFT_MARGIN = 4.0;
	public final static double LABEL_LEFT_MARGIN = 20.0;
	public final static double ROW_GAP = -5.0;
	private String title;
	private double currentX;
	private double currentY;
	private ColorizedByRegistry colorizedByRegistry;
	
	public ColorLegendCreator(String title, ColorizedByRegistry colorizedByRegistry){
		this.title = title;
		this.colorizedByRegistry = colorizedByRegistry;
	}
	
	public ColorLegend create() {
		ColorLegend colorLegend= new ColorLegend(this.title, COLOR_LEGEND_LEFT, COLOR_LEGEND_TOP);
	
		currentX = COLOR_LEGEND_LEFT + BOX_LEFT_MARGIN;
		currentY = COLOR_LEGEND_TOP + TITLE_HEIGHT;
		for (String key : colorizedByRegistry.getKeySet()) {
			colorLegend.add(createColorLegendLabel(key, colorizedByRegistry.getColorOf(key)));
		}

		return colorLegend;
	}
	
	private ColorLegendLabel createColorLegendLabel(String label, Color color){
		
		ColorLegendLabel colorLegendLabel = new ColorLegendLabel(label,
																currentX + LABEL_LEFT_MARGIN, 
																currentY + ROW_GAP, 
																currentX, 
																currentY, 
																BOX_WIDTH, 
																BOX_HEIGHT, 
																color);
		currentY = currentY + BOX_HEIGHT + ROW_GAP;
		return colorLegendLabel;
	}
}

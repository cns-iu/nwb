package edu.iu.sci2.visualization.horizontalbargraph.bar;

import java.awt.Color;

import edu.iu.sci2.visualization.horizontalbargraph.HeaderAndFooterPositioningData;

/**
 * Create the ColorLegend that used for the bars. It used the data
 * provided by the ColorizedByRegistry and generate a model that
 * represents the display of the color legend.
 * 
 * It is implemented as a independent class which is differ from 
 * BasicLayout.createBars() for better responsibility distribution.
 * 
 * Important: The x is in pixel while y is in inches. This is due
 * to dynamic scale of y based on PageOrientationType
 * 
 * @author kongch
 *
 */
public class ColorLegendCreator {
	public final static double COLOR_LEGEND_LEFT = 36.0;
	public final static double BOX_WIDTH = 15.0;
	public final static double BOX_HEIGHT = -6.0;
	public final static double BOX_LEFT_MARGIN = 4.0;
	public final static double LABEL_LEFT_MARGIN = 20.0;
	private String title;
	private double currentX;
	private double currentY;
	private ColorizedByRegistry colorizedByRegistry;
	
	public ColorLegendCreator(String title, ColorizedByRegistry colorizedByRegistry){
		this.title = title;
		this.colorizedByRegistry = colorizedByRegistry;
	}
	
	public ColorLegend create() {
		ColorLegend colorLegend= new ColorLegend(this.title, HeaderAndFooterPositioningData.X_POSITION, 
												HeaderAndFooterPositioningData.LEGEND_START_Y);
	
		currentX = COLOR_LEGEND_LEFT + BOX_LEFT_MARGIN;
		currentY = HeaderAndFooterPositioningData.LEGEND_START_Y - 
					HeaderAndFooterPositioningData.LEGEND_SUBITEM_GAP;
		for (String key : colorizedByRegistry.getKeySet()) {
			colorLegend.add(createColorLegendLabel(key, colorizedByRegistry.getColorOf(key)));
		}

		return colorLegend;
	}
	
	private ColorLegendLabel createColorLegendLabel(String label, Color color){
		
		ColorLegendLabel colorLegendLabel = new ColorLegendLabel(label,
																currentX + LABEL_LEFT_MARGIN, 
																currentY - HeaderAndFooterPositioningData.LEGEND_SUBLABEL_GAP, 
																currentX,
																currentY, 
																BOX_WIDTH, 
																BOX_HEIGHT, 
																color);
		currentY = currentY - HeaderAndFooterPositioningData.LEGEND_SUBITEM_GAP;
		return colorLegendLabel;
	}
}

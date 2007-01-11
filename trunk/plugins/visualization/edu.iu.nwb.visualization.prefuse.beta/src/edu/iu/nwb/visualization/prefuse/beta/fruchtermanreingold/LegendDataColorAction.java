package edu.iu.nwb.visualization.prefuse.beta.fruchtermanreingold;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import prefuse.action.assignment.DataColorAction;
import prefuse.data.tuple.TupleSet;
import prefuse.util.ColorLib;
import prefuse.util.ColorMap;
import prefuse.util.DataLib;
import prefuse.util.FontLib;

public class LegendDataColorAction extends DataColorAction {

	private String dataField;
	private int[] palette;

	public LegendDataColorAction(String group, String dataField, int dataType, String colorField, int[] palette) {
		super(group, dataField, dataType, colorField, palette);
		this.dataField = dataField;
		this.palette = palette;
		
		
	}
	
	public JComponent getLegend(String context, String column) {
		LayoutManager layout = new FlowLayout();
		JComponent legend;
		
		final TupleSet tuples = this.getVisualization().getGroup(this.m_group);
		
		if(this.getDataType() == prefuse.Constants.NUMERICAL) {
			legend = new JPanel(layout);
			
			double min = DataLib.min(tuples, dataField).getDouble(dataField);
			double max = DataLib.max(tuples, dataField).getDouble(dataField);
			Canvas canvas;
			if(this.getScale() != prefuse.Constants.QUANTILE_SCALE) {
				final ColorMap colorMap = new ColorMap(palette, 0, 1);
				
				canvas = new Canvas() {
					public void paint(Graphics g) {
						Graphics2D graphics = (Graphics2D) g;
						
						Object[] valueArray = DataLib.ordinalArray(tuples, dataField);
						int width = this.getWidth() / valueArray.length - 1;
						for(int valueIndex = 0; valueIndex < valueArray.length; valueIndex++) {
							int x = width * valueIndex;
							Color color = ColorLib.getColor(colorMap.getColor(((Double) valueArray[valueIndex]).doubleValue()));
							graphics.setColor(color);
							graphics.fillRect(x, 0, width, this.getHeight()); 
						}
					}
				};
			} else {
				
				canvas = new Canvas() {
					public void paint(Graphics g) {
						Graphics2D graphics = (Graphics2D) g;
						Color startColor = ColorLib.getColor(palette[0]);
						Color endColor = ColorLib.getColor(palette[palette.length - 1]);
						GradientPaint gradient = new GradientPaint(0, 0, startColor, this.getWidth(), 0, endColor);
						graphics.setPaint(gradient);
						graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
					}
				};
			}
			canvas.setBounds(0, 0, 50, 10);
			
			legend.add(new JLabel(context + " (" + column + "): " + min));
			
			legend.add(canvas);
			
			legend.add(new JLabel("" + max));
		} else {
			legend = new Box(BoxLayout.PAGE_AXIS);
			JLabel label = new JLabel(context + " (" + column + "):");
			label.setFont(FontLib.getFont("Tahoma", Font.BOLD, 11));
			legend.add(label);
			legend.add(Box.createVerticalStrut(3));
			
			final ColorMap colorMap = new ColorMap(null, 0, 1);
			colorMap.setColorPalette(palette);
			double[] distribution = this.getDistribution();
			colorMap.setMinValue(distribution[0]);
			colorMap.setMaxValue(distribution[1]);
			
			final Object[] values = DataLib.ordinalArray(tuples, dataField);
			
			for(int valueIndex = 0; valueIndex < values.length; valueIndex++) {
				final int tempIndex = valueIndex;
				Canvas canvas = new Canvas() {
					public void paint(Graphics g) {
						Graphics2D graphics = (Graphics2D) g;
						Color color = ColorLib.getColor(colorMap.getColor(tempIndex));
						graphics.setColor(color);
						graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
					}
				};
				canvas.setBounds(0, 0, 10, 10);
				JPanel keyValue = new JPanel(new FlowLayout(FlowLayout.RIGHT));
				String value = values[valueIndex].toString();
				JLabel itemLabel;
				if("".equals(value)) {
					itemLabel = new JLabel("(empty): ");
					itemLabel.setFont(FontLib.getFont("Tahoma", Font.ITALIC, 8));
				} else {
					itemLabel = new JLabel(value + ": ");
					itemLabel.setFont(FontLib.getFont("Tahoma", 8));
				}
				
				keyValue.add(itemLabel);
				keyValue.add(canvas);
				legend.add(keyValue);
				legend.add(Box.createVerticalStrut(3));
			}	
		}
		
		return legend;
	}

}

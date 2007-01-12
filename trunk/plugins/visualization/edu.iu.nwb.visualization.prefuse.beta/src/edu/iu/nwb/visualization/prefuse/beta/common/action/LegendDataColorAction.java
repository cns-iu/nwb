package edu.iu.nwb.visualization.prefuse.beta.common.action;

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

import edu.iu.nwb.visualization.prefuse.beta.common.Constants;

import prefuse.action.assignment.DataColorAction;
import prefuse.data.DataTypeException;
import prefuse.data.tuple.TupleSet;
import prefuse.util.ColorLib;
import prefuse.util.ColorMap;
import prefuse.util.DataLib;
import prefuse.util.FontLib;

public class LegendDataColorAction extends DataColorAction implements LegendAction {

	private String dataField;
	private int[] palette;
	private String column;
	private String context;
	private int size = 0;

	public LegendDataColorAction(String group, String dataField, int dataType, String colorField, int[] palette, String column, String context) {
		super(group, dataField, dataType, colorField, palette);
		this.dataField = dataField;
		this.palette = palette;
		this.column = column;
		this.context = context;
		
	}
	
	public JComponent getLegend() {
		//LayoutManager layout = new FlowLayout(FlowLayout.LEFT);
		JComponent legend;
		
		final TupleSet tuples = this.getVisualization().getGroup(this.m_group);
		
		if(this.getDataType() == prefuse.Constants.NUMERICAL) {
			size = 2;
			
			legend = new Box(BoxLayout.PAGE_AXIS);
			
			double min = 0;
			double max = 1;
			
			boolean bad = false;
			try {
				min = DataLib.min(tuples, dataField).getDouble(dataField);
				max = DataLib.max(tuples, dataField).getDouble(dataField);
			} catch(DataTypeException exception) {
				bad = true;
			} catch(NullPointerException exception) {
				bad = true;
			}
			Canvas canvas;
			if(this.getScale() == prefuse.Constants.QUANTILE_SCALE) {
				
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
			canvas.setBounds(0, 0, 50, Constants.LEGEND_CANVAS_HEIGHT);
			
			JLabel fieldLabel = new JLabel(context + " (" + column + ")");
			fieldLabel.setFont(Constants.FIELD_SPECIFYING_FONT);
			JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			titlePanel.add(fieldLabel);
			legend.add(titlePanel);
			legend.add(Box.createVerticalStrut(Constants.VERTICAL_STRUT_DISTANCE));
			JPanel continuumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel minLabel;
			if(bad) {
				minLabel = new JLabel("error");
				minLabel.setFont(Constants.EMPTY_FIELD_FONT);
			} else {
				minLabel = new JLabel("" + min);
				minLabel.setFont(Constants.FIELD_VALUE_FONT);
			}
			
			continuumPanel.add(minLabel);
			
			continuumPanel.add(canvas);
			
			JLabel maxLabel;
			if(bad) {
				maxLabel = new JLabel("error");
				maxLabel.setFont(Constants.EMPTY_FIELD_FONT);
			} else {
				maxLabel = new JLabel("" + max);
				maxLabel.setFont(Constants.FIELD_VALUE_FONT);
			}
			continuumPanel.add(maxLabel);
			legend.add(continuumPanel);
		} else {
			size += 1;
			legend = new Box(BoxLayout.PAGE_AXIS);
			JLabel label = new JLabel(context + " (" + column + ")");
			label.setFont(Constants.FIELD_SPECIFYING_FONT);
			JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			titlePanel.add(label);
			legend.add(titlePanel);
			legend.add(Box.createVerticalStrut(Constants.VERTICAL_STRUT_DISTANCE));
			
			final ColorMap colorMap = new ColorMap(null, 0, 1);
			colorMap.setColorPalette(palette);
			double[] distribution = this.getDistribution();
			colorMap.setMinValue(distribution[0]);
			colorMap.setMaxValue(distribution[1]);
			
			final Object[] values = DataLib.ordinalArray(tuples, dataField);
			
			for(int valueIndex = 0; valueIndex < values.length; valueIndex++) {
				size += 1;
				final int tempIndex = valueIndex;
				Canvas canvas = new Canvas() {
					public void paint(Graphics g) {
						Graphics2D graphics = (Graphics2D) g;
						Color color = ColorLib.getColor(colorMap.getColor(tempIndex));
						graphics.setColor(color);
						graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
					}
				};
				canvas.setBounds(0, 0, Constants.LEGEND_CANVAS_HEIGHT, Constants.LEGEND_CANVAS_HEIGHT);
				JPanel keyValue = new JPanel(new FlowLayout(FlowLayout.LEFT));
				Object objectValue = values[valueIndex];
				String value;
				if(objectValue == null) {
					value = "";
				} else {
					value = objectValue.toString();
				}
				JLabel itemLabel;
				if("".equals(value)) {
					itemLabel = new JLabel(" (empty)");
					itemLabel.setFont(Constants.EMPTY_FIELD_FONT);
				} else {
					itemLabel = new JLabel(" " + value);
					itemLabel.setFont(Constants.FIELD_VALUE_FONT);
				}
				keyValue.add(canvas);
				keyValue.add(itemLabel);
				
				legend.add(keyValue);
				legend.add(Box.createVerticalStrut(Constants.VERTICAL_STRUT_DISTANCE));
			}	
		}
		
		return legend;
	}

	public int getLegendSize() {
		getLegend(); //minor hack, make sure size is computed
		return size;
	}

}

package edu.iu.nwb.visualization.prefuse.beta.common.action;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import prefuse.action.assignment.DataColorAction;
import prefuse.data.DataTypeException;
import prefuse.data.tuple.TupleSet;
import prefuse.util.ColorLib;
import prefuse.util.ColorMap;
import prefuse.util.DataLib;
import edu.iu.nwb.visualization.prefuse.beta.common.Constants;

public class LegendDataColorAction extends DataColorAction implements LegendAction {

	private String dataField;
	private int[] palette;
	private String column;
	private String context;
	private int size = 0;

	public LegendDataColorAction(String group, String dataField, int dataType, String colorField, int[] palette, String column, String context) {
		super(group, dataField, dataType, colorField, palette);
		this.dataField = dataField; //the likely-indirected field used for the data
		this.palette = palette;
		this.column = column; //the name of the field the data's originally from
		this.context = context;

	}

	public JComponent getLegend() {
		JComponent legend = Box.createVerticalBox();
		legend.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) (Constants.LEGEND_CANVAS_HEIGHT * 2.5)));
		//legend.setMaximumSize(new Dimension(75, Constants.LEGEND_CANVAS_HEIGHT * 3));

		final TupleSet tuples = this.getVisualization().getGroup(this.m_group);

		if(this.getDataType() == prefuse.Constants.NUMERICAL) {
			size = 2;

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
			JPanel canvas;
			if(this.getScale() == prefuse.Constants.QUANTILE_SCALE) {

				final ColorMap colorMap = new ColorMap(palette, 0, 1);

				canvas = new JPanel() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

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
				canvas = new JPanel() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

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
			canvas.setMinimumSize(new Dimension(Constants.LEGEND_CANVAS_HEIGHT * 3, 0));

			JLabel fieldLabel = new JLabel(context + " (" + column + ")");
			fieldLabel.setFont(Constants.FIELD_SPECIFYING_FONT);
			JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			titlePanel.add(fieldLabel);
			legend.add(titlePanel, BorderLayout.NORTH);
			//legend.add(Box.createVerticalStrut(Constants.VERTICAL_STRUT_DISTANCE));
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
			legend.add(continuumPanel, BorderLayout.SOUTH);
		} else {
			size += 1;
			//legend = Box.createVerticalBox();
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
				JPanel canvas = new JPanel() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

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
		//legend.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) (Constants.LEGEND_CANVAS_HEIGHT * size)));
		return size;
	}

}

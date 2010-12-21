package edu.iu.nwb.visualization.prefuse.beta.common.action;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import prefuse.action.assignment.DataShapeAction;
import prefuse.render.ShapeRenderer;
import prefuse.util.DataLib;
import edu.iu.nwb.visualization.prefuse.beta.common.Constants;

public class LegendDataShapeAction extends DataShapeAction implements LegendAction {
	
	private int size = 0;
	private String column;
	private String context;

	public LegendDataShapeAction(String group, String field, int[] shapes, String indirectedColumn, String context) {
		super(group, field, shapes);
		this.column = indirectedColumn;
		this.context = context;
	}

	public JComponent getLegend() {
		JComponent legend = new Box(BoxLayout.PAGE_AXIS);
		size += 1;
		
		JLabel label = new JLabel(context + " (" + column + ")");
		label.setFont(Constants.FIELD_SPECIFYING_FONT);
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		titlePanel.add(label);
		legend.add(titlePanel);
		legend.add(Box.createVerticalStrut(Constants.VERTICAL_STRUT_DISTANCE));
		
		final Object[] values = DataLib.ordinalArray(m_vis.getGroup(m_group), m_dataField);
		
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
					Shape shape = getSpecifiedShape(m_palette[tempIndex % m_palette.length], 1, 1, this.getWidth() - 3);
					graphics.setPaint(Color.BLACK);
					graphics.draw(shape);
				}
			};
			canvas.setBackground(Color.WHITE);
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
		
		return legend;
	}
	
	private Shape getSpecifiedShape(int shape, double x, double y, double width) {
		ShapeRenderer shaper = new ShapeRenderer();
		switch ( shape ) {
        case prefuse.Constants.SHAPE_NONE:
            return null;
        case prefuse.Constants.SHAPE_RECTANGLE:
            return shaper.rectangle(x, y, width, width);
        case prefuse.Constants.SHAPE_ELLIPSE:
            return shaper.ellipse(x, y, width, width);
        case prefuse.Constants.SHAPE_TRIANGLE_UP:
            return shaper.triangle_up((float)x, (float)y, (float)width);
        case prefuse.Constants.SHAPE_TRIANGLE_DOWN:
            return shaper.triangle_down((float)x, (float)y, (float)width);
        case prefuse.Constants.SHAPE_TRIANGLE_LEFT:
            return shaper.triangle_left((float)x, (float)y, (float)width);
        case prefuse.Constants.SHAPE_TRIANGLE_RIGHT:
            return shaper.triangle_right((float)x, (float)y, (float)width);
        case prefuse.Constants.SHAPE_CROSS:
            return shaper.cross((float)x, (float)y, (float)width);
        case prefuse.Constants.SHAPE_STAR:
            return shaper.star((float)x, (float)y, (float)width);
        case prefuse.Constants.SHAPE_HEXAGON:
            return shaper.hexagon((float)x, (float)y, (float)width);
        case prefuse.Constants.SHAPE_DIAMOND:
            return shaper.diamond((float)x, (float)y, (float)width);
        default:
            throw new IllegalStateException("Unknown shape type: " + shape);
        }
	}

	public int getLegendSize() {
		getLegend();
		//legend.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) (Constants.LEGEND_CANVAS_HEIGHT * size)));
		return size;
	}
	
}

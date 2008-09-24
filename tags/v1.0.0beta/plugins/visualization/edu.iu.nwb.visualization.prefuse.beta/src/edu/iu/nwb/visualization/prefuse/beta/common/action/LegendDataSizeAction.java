package edu.iu.nwb.visualization.prefuse.beta.common.action;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import prefuse.action.assignment.DataSizeAction;
import prefuse.data.DataTypeException;
import prefuse.data.tuple.TupleSet;
import prefuse.util.DataLib;
import edu.iu.nwb.visualization.prefuse.beta.common.Constants;

public class LegendDataSizeAction extends DataSizeAction implements LegendAction {

	//private String realColumn;
	private String context;
	private String fakeColumn;
	private String column;
	private int size = 2;

	public LegendDataSizeAction(String group, String fakeColumn, String column, String context) {
		super(group, fakeColumn);
		this.fakeColumn = fakeColumn;
		this.column = column;
		this.context = context;
		
	}

	public JComponent getLegend() {
		final TupleSet tuples = this.getVisualization().getGroup(this.m_group);
		
		JComponent legend = new Box(BoxLayout.PAGE_AXIS);
		
		
		
		double min = 0;
		double max = 1;
		boolean bad = false;
		try {
			min = DataLib.min(tuples, fakeColumn).getDouble(fakeColumn);
			max = DataLib.max(tuples, fakeColumn).getDouble(fakeColumn);
		} catch(DataTypeException exception) {
			bad = true;
		} catch(NullPointerException exception) {
			bad = true;
		}
		final double tempMin = min;
		final double tempMax = max;
		final boolean badNumbers = bad;
		
		JPanel canvas = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g) {
				Graphics2D graphics = (Graphics2D) g;
				int startSize;
				if(badNumbers) {
					startSize = this.getHeight();
				} else {
					startSize = (int) (this.getHeight() * tempMin / tempMax);
				}
				
				Polygon polygon = new Polygon();
				polygon.addPoint(this.getWidth(), this.getHeight());
				polygon.addPoint(this.getWidth(), 0);
				int pinch = (this.getHeight() - startSize)/2;
				polygon.addPoint(0, pinch);
				polygon.addPoint(0, this.getHeight() - pinch);
				
				graphics.setColor(Color.DARK_GRAY);
				graphics.fillPolygon(polygon);
			}
		};
		
		canvas.setBounds(0, 0, 50, Constants.LEGEND_CANVAS_HEIGHT);
		canvas.setMinimumSize(new Dimension(Constants.LEGEND_CANVAS_HEIGHT * 3, 0));
		
		JLabel fieldLabel = new JLabel(context + " (" + column + ")", JLabel.LEFT);
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
		return legend;
	}

	public int getLegendSize() {
		//JComponent legend = getLegend();
		
		return size;
	}

}

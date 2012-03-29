package edu.iu.nwb.visualization.prefuse.alpha.smallworld.action;

//Copyright (C) 2005 Andreas Noack
//
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
//
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with this library; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Dialog with a simple graph visualization, displaying graph nodes
 * as circles of specified sizes at specified positions.  
 * 
 * @author Andreas Noack
 * @version 28.09.2005
 */
public class GraphFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the dialog.
	 * 
	 * @param nodeToPosition map from each graph node to its position.
	 * 		Each position array must have at least two elements, 
	 * 		one for the horizontal position and one for the vertical position.
	 * @param nodeToDiameter map from each graph node to the diameter
	 * 		of its representing circle.
	 */
	public GraphFrame(Map<String,float[]> nodeToPosition, Map<String,Float> nodeToDiameter) {
		setTitle("LinLogLayout");
        setSize(getToolkit().getScreenSize().width/2, getToolkit().getScreenSize().height/2);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("North", new JLabel("Move the mouse cursor over a node to display its name."));
		getContentPane().add("Center", new GraphCanvas(nodeToPosition, nodeToDiameter));
	}
	
};

/**
 * Canvas for a simple graph visualization.
 * 
 * @author Andreas Noack
 */
class GraphCanvas extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Minimum and maximum positions of the nodes. */
	private float minX, maxX, minY, maxY;
	/** Node whose name is displayed. */
	private String namedNode = null;
	/** Map from each node to its position. */
	private Map<String,float[]> nodeToPosition;
	/** Map from each node to the diameter of its representating circle. */
	private Map<String,Float> nodeToDiameter;
	
	/**
	 * Constructs the canvas. 
	 */
	public GraphCanvas(Map<String,float[]> nodeToPosition, Map<String,Float> nodeToDiameter) {
		this.nodeToPosition = nodeToPosition;
		this.nodeToDiameter = nodeToDiameter;

		// determine minimum and maximum positions of the nodes
		minX = Float.MAX_VALUE; maxX = -Float.MAX_VALUE;
		minY = Float.MAX_VALUE; maxY = -Float.MAX_VALUE;
		for (String node : nodeToPosition.keySet()) {
			float[] position = nodeToPosition.get(node);
			float diameter = nodeToDiameter.get(node);
			minX = Math.min(minX, position[0] - diameter/2);
			maxX = Math.max(maxX, position[0] + diameter/2);
			minY = Math.min(minY, position[1] - diameter/2);
			maxY = Math.max(maxY, position[1] + diameter/2);
		}

		// for showing the name of a node when the mouse cursor is over it
		addMouseMotionListener(
			new MouseMotionAdapter() {
				public void mouseMoved(MouseEvent event) {
					paintNodeName(event.getX(), event.getY());
				}
			}
		);
	}
	
	public void paint(Graphics g) {
		float moveX = -minX;
		float moveY = -minY;
		float scale = Math.min(getWidth() / (maxX-minX), getHeight() / (maxY-minY));
		
		// draw nodes as circles
		for (String node : nodeToPosition.keySet()) {
			g.setColor(new Color(0.5f, 0.5f, 1.0f));
			int positionX = Math.round((nodeToPosition.get(node)[0] + moveX) * scale);
			int positionY = Math.round((nodeToPosition.get(node)[1] + moveY) * scale);
			int diameter = Math.round(nodeToDiameter.get(node) * scale);
			g.fillOval(positionX-diameter/2, positionY-diameter/2, diameter, diameter);
		}
		// draw name for namedNode
		if (namedNode != null) {
			final int FONT_SIZE = 20;
	        g.setFont(g.getFont().deriveFont((float)FONT_SIZE));
			g.setColor(Color.BLACK);
			int positionX = Math.round((nodeToPosition.get(namedNode)[0] + moveX) * scale);
			int positionY = Math.round((nodeToPosition.get(namedNode)[1] + moveY) * scale);
			g.drawString(namedNode, 
				Math.min(positionX, getWidth() - g.getFontMetrics().stringWidth(namedNode)),  
				Math.max(positionY, FONT_SIZE));
		}
	}
	
	/**
	 * Sets the attribute <code>namedNode</code> to the name of the node
	 * at the given screen coordinates, or to <code>null</code>
	 * if there is no node at these screen coordinates.
	 * Initiates a repaint if <code>namedNode</code> changes.
	 *    
	 * @param x horizontal coordinate of the mouse cursor.
	 * @param y vertical coordinate of the mouse cursor.
	 */
	private void paintNodeName(int x, int y) {
		float moveX = -minX;
		float moveY = -minY;
		float scale = Math.min(getWidth() / (maxX-minX), getHeight() / (maxY-minY));
		
		for (String node : nodeToPosition.keySet()) {
			int positionX = Math.round((nodeToPosition.get(node)[0] + moveX) * scale);
			int positionY = Math.round((nodeToPosition.get(node)[1] + moveY) * scale);
			int diameter = Math.round(nodeToDiameter.get(node) * scale);
			
			if (	x >= positionX-diameter/2 && x <= positionX+diameter/2 
				 && y >= positionY-diameter/2 && y <= positionY+diameter/2) {
				if (!node.equals(namedNode)) {
					namedNode = node;
					repaint();
				}
				return;
			}
		}
		if (namedNode != null) {
			namedNode = null;
			repaint();
		}	
	}
	
}

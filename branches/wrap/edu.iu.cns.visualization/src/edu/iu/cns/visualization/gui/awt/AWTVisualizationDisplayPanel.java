package edu.iu.cns.visualization.gui.awt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;

public class AWTVisualizationDisplayPanel extends JPanel {
	private static final long serialVersionUID = 3120777501972493968L;

	public AWTVisualizationDisplayPanel(Dimension initialSize) {
		setPreferredSize(initialSize);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		if (graphics == null) {
			return;
		}

		VectorGraphics vectorGraphics = VectorGraphics.create(graphics);
		Dimension currentSize = getSize();
		vectorGraphics.setColor(Color.white);
		vectorGraphics.fillRect(0, 0, currentSize.width, currentSize.height);
		vectorGraphics.setColor(Color.black);
		
//		this.visualization.renderBody(vectorGraphics, graphicsState, this.messages, currentSize);
	}
}
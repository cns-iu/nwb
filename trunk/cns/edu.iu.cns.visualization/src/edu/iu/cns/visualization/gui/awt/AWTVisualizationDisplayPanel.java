package edu.iu.cns.visualization.gui.awt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;

import edu.iu.cns.visualization.utility.GraphicsState;
import edu.iu.cns.visualization.utility.VisualizationMessages;

public class AWTVisualizationDisplayPanel extends JPanel {
	private static final long serialVersionUID = 3120777501972493968L;

	private Dimension size;
	private VisualizationMessages messages;

	public AWTVisualizationDisplayPanel(Dimension initialSize, VisualizationMessages messages) {
		this.size = initialSize;
		this.messages = messages;
		setPreferredSize(initialSize);
	}

	public void paintComponent(Graphics graphics) {
		if (graphics == null) {
			return;
		}

		VectorGraphics vectorGraphics = VectorGraphics.create(graphics);
		Dimension currentSize = getSize();
		vectorGraphics.setColor(Color.white);
		vectorGraphics.fillRect(0, 0, currentSize.width, currentSize.height);
		vectorGraphics.setColor(Color.black);

		GraphicsState graphicsState = new GraphicsState(vectorGraphics);
//		this.visualization.renderBody(vectorGraphics, graphicsState, this.messages, currentSize);
	}
}
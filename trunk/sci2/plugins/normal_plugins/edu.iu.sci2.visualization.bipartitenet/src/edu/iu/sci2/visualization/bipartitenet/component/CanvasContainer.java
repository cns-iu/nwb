package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class CanvasContainer extends Canvas {
	/**
	 * Serializable Sucks
	 */
	private static final long serialVersionUID = 437819803332326758L;
	private PaintableContainer container = new PaintableContainer();
	
	public CanvasContainer() {
		setBackground(Color.white);
	}
	
	public void add(Paintable child) {
		container.add(child);
	}

	public boolean remove(Paintable child) {
		return container.remove(child);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		container.paint(g2);
	}

}

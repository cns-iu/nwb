package edu.iu.cns.visualization.utility;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Stack;

public class GraphicsState {
	private Stack<AffineTransform> transforms = new Stack<AffineTransform>();
	private Graphics2D graphics;

	public GraphicsState(Graphics2D graphics) {
		this.graphics = graphics;
	}

	public AffineTransform saveState() {
		AffineTransform transform = this.graphics.getTransform();
		this.transforms.push(transform);

		return transform;
	}

	public AffineTransform restoreState() {
		AffineTransform transform = this.transforms.pop();
		this.graphics.setTransform(transform);

		return transform;
	}
}
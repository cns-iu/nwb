package edu.iu.sci2.visualization.bipartitenet.tests;

import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import org.apache.commons.lang.WordUtils;

import math.geom2d.Point2D;
import edu.iu.sci2.visualization.bipartitenet.component.CanvasContainer;
import edu.iu.sci2.visualization.bipartitenet.component.Paintable;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;

public class SimpleLabelPainterTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame("Application Review");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CanvasContainer cc = new CanvasContainer();
		cc.setPreferredSize(new Dimension(400, 800));
//		PageDirector r = new PageDirector(layout, model, "Who", "Who title", "What", "What title");
		addPaintableThings(cc);
		f.getContentPane().add(cc);
		f.pack();
		f.setVisible(true);
	}

	private static void addPaintableThings(CanvasContainer cc) {
		int x = 200;
		int y = 50;
		for (XAlignment xAlign : XAlignment.values()) {
			for (YAlignment yAlign : YAlignment.values()) {
				final Point2D position = new Point2D(x, y);
				cc.add(new SimpleLabelPainter(
						position,
						xAlign,
						yAlign,
						WordUtils.capitalize(String.format("X: %s, Y: %s", xAlign, yAlign).toLowerCase()),
						null, null));
				
				cc.add(new Paintable() {
					@Override
					public void paint(Graphics2D g) {
						position.draw(g, 3);
					}
				});
				
				y = y + 50;
			}
		}
	}


}

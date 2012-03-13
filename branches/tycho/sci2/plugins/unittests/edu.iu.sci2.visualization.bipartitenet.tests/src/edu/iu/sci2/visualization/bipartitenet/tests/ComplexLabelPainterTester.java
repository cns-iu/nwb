package edu.iu.sci2.visualization.bipartitenet.tests;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;

import math.geom2d.Point2D;
import edu.iu.sci2.visualization.bipartitenet.component.CanvasContainer;
import edu.iu.sci2.visualization.bipartitenet.component.ComplexLabelPainter;

public class ComplexLabelPainterTester {

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
		Font font = new Font(Font.DIALOG, Font.PLAIN, 14);
		Font bigFont = font.deriveFont(16f);
		cc.add(new ComplexLabelPainter.Builder(new Point2D(x, y), font, Color.black)
			.addLine("Generated from my ass", bigFont)
			.addLine("With magic")
			.addLine("At midnight")
			.build());
			
	}


}

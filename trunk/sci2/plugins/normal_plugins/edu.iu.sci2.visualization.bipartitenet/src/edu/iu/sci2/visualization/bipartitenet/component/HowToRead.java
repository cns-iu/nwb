package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import math.geom2d.Point2D;
import edu.iu.sci2.visualization.bipartitenet.PageDirector;

public class HowToRead implements Paintable {
	private static final int BLURB_WIDTH = 360;

	private static final String TEXT = 
			"This bipartite network shows two record types and their interconnections. Each "+
			"record is represented by a labeled circle that is size coded by a numerical "+
			"attribute value. Records of each type are vertically aligned and sorted, e.g., "+
			"by node size or alphabetically. Links between records of different type may be "+
			"weighted as represented by line thickness.";
	
	private static final String TITLE = "How To Read This Map"; // Map? wtf
	
	private final Font titleFont;
	private final Font textFont;
	private final Point2D position;
	
	public HowToRead(Font titleFont, Font textFont, Point2D position) {
		this.titleFont = titleFont;
		this.textFont = textFont;
		this.position = position;
	}

	@Override
	public void paint(Graphics2D g) {
		ComplexLabelPainter title = new ComplexLabelPainter.Builder(position, textFont, Color.black)
			.withLineSpacing(PageDirector.LINE_SPACING)
			.addLine(TITLE, titleFont).build();
		title.paint(g);
		Point2D textStart = position.translate(0, title.estimateHeight());
		
		AttributedString aString = new AttributedString(TEXT);
		aString.addAttribute(TextAttribute.SIZE, 10);
		aString.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, 5, 22); // start, end of "bipartite network"
		MultiLineLabelPainter text = new MultiLineLabelPainter(textStart, aString.getIterator(), 
				BLURB_WIDTH);
		text.paint(g);
		
	}

}

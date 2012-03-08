package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class CopyrightInfo {
	String copyrightText;
			
	public CopyrightInfo(){
		this.copyrightText = "© 2008  The Regents of the University of California and SciTech Strategies.\nMap updated  by SciTech Strategies, OST, and CNS in 2011.";
	}
	
	public void render(GraphicsState state, float topBound, float pageWidth){
		state.save();
		state.current.translate(0, topBound);
		
		Color copyrightColor = Color.black;
		double copyrightFontSize = 10;
		state.current.setColor(copyrightColor);
		state.setFontSize(copyrightFontSize);
		String[] copyrightLines = this.copyrightText.split("\n");
		for (String line : copyrightLines) {
			
			state.save();
			Rectangle2D lineTextBox = state.current.getFontMetrics().getStringBounds(line, state.current);
			float leftBoundTextCentered = (float) ((pageWidth / 2) - lineTextBox.getCenterX());
			state.current.translate(leftBoundTextCentered, 0);
			state.current.drawString(line, 0, 0);
			state.restore();

			state.current.translate(0, copyrightFontSize);
		}
		state.restore();
	}
}
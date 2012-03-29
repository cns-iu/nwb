package edu.iu.cns.visualization.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class VisualizationUtilities {
	public static void postScript_FindFont(Graphics2D graphics, String fontName) {
		graphics.setFont(Font.getFont(fontName));
	}

	public static void postScript_FindFont(
			Graphics2D graphics, String fontName, int fontScale) {
		graphics.setFont(new Font(fontName, Font.PLAIN, fontScale));
	}

	public static void postScript_SetGray(Graphics2D graphics, float gray) {
		graphics.setColor(new Color(gray, gray, gray));
	}
}
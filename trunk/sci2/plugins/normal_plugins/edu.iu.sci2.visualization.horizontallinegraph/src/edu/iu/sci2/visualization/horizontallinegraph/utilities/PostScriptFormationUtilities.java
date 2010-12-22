package edu.iu.sci2.visualization.horizontallinegraph.utilities;

import java.io.StringWriter;
import java.util.Date;

public class PostScriptFormationUtilities {
	public static final int DOTS_PER_INCH = 72;
	
	/*
	 * TODO: Give this a more accurate name?  I'm not sure if this is actually
	 *  changing the background.
	 */
	public static String background() {
		return line("0 0 1 setrgbcolor");
	}
	
	public static String centeringTranslate(double xTranslate) {
		/* double yTranslate = 0.0;
		
		return line(xTranslate + " " + yTranslate + " translate"); */
		return line("");
	}
	
	public static String comments(
			double boundingBoxHeight, double pageWidth, double scale) {
		long pageBoundingBoxWidth = CalculationUtilities.pageBoundingBoxWidth(
			pageWidth);
		long pageBoundingBoxHeight =
			CalculationUtilities.pageBoundingBoxHeight(
				boundingBoxHeight, scale);
		
		return
			/*
			 * TODO: The bounding box is a big fat hack. (It needs to take into
			 *  account text size and stuff.)
			 * TODO: Stringtemplate!
			 */
			line("%!PS-Adobe-2.0 EPSF-2.0") +
			line("%%BoundingBox:" +
					0 +
					" " +
					0 +
					" " +
				 	pageBoundingBoxWidth +
				 	" " +
				 	pageBoundingBoxHeight) +
			// line("%%Pages: 1") +
			line("%%Title: Horizontal Line Graph") +
			line("%%Creator: Sci2") +
			line("%%EndComments") +
			line("");
	}
	
	public static String header() {
		String TICK_COLOR_STRING = "0.75 0.75 0.75";
		String RECORD_LABEL_COLOR_STRING = "0.4 0.4 0.4";
		String RECORD_BAR_COLOR_STRING = "0.0 0.0 0.0";
		
		return
			line("") +
			line("/tick {") +
				line(tabbed("newpath")) +
				line(tabbed("moveto")) +
				line(tabbed("0 -25 rlineto")) +
				line(tabbed("stroke")) +
			line("} def") +
			line("") +
			
			line("/verticaltick {") +
				line(tabbed("newpath")) +
				line(tabbed("moveto")) +
				line(tabbed("10 0 rlineto")) +
				line(tabbed("stroke")) +
			line("} def") +
			line("") +
			
			line("/fontheightadjust {") +
				line(tabbed("0 -3 rmoveto")) +
			line("} def") +
			line("") +
			
			line("/ticklabel {") +
				line(tabbed("gsave")) +
				line(tabbed("moveto")) +
				line(tabbed("dup stringwidth pop 2 div neg 0 rmoveto")) +
				line(tabbed("fontheightadjust")) +
				line(tabbed("show")) +
				line(tabbed("grestore")) +
			line("} def") +
			line("") +
			
			line("/personlabel {") +
				line(tabbed("moveto")) +
				line(tabbed("dup stringwidth pop neg 15 sub 0 rmoveto")) +
				line(tabbed("fontheightadjust")) +
				line(tabbed("show")) +
			line("} def") +
			line("") +
			
			line("/keyitem {") +
				line(tabbed("1 index 1 index")) +
				line(tabbed("newpath")) +
				line(tabbed("300 0 rlineto")) +
				line(tabbed("stroke")) +
				line(tabbed("moveto")) +
				line(tabbed("350 0 rmoveto")) +
				line(tabbed("fontheightadjust")) +
				line(tabbed("show")) +
			line("} def") +
			line("") +
			
			line("/period {") +
				line(tabbed("newpath")) +
				line(tabbed("4 2 roll moveto")) +
				line(tabbed("exch dup 0 rlineto 0 3 -1 roll")) +
				line(tabbed("rlineto neg 0 rlineto")) +
				line(tabbed("fill")) +
			line("} def") +
			line("") +
			
			line("/record {") +
				line(tabbed("5 -1 roll")) +
				line(tabbed("4 index 4 index 3 index 2 div add")) +
				line(tabbed(setrgbcolor(RECORD_LABEL_COLOR_STRING))) +
				line(tabbed("personlabel")) +
				line(tabbed(setrgbcolor(RECORD_BAR_COLOR_STRING))) +
				line(tabbed("period")) +
			line("} def") +
			line("") +
			
			line("/vertical {") +
				line(tabbed("gsave")) +
				line(tabbed("[15] 0 setdash")) +
				line(tabbed("1 setlinewidth")) +
				line(tabbed(setrgbcolor(TICK_COLOR_STRING))) +
				line(tabbed("2 index")) +
				line(tabbed("newpath")) +
				line(tabbed("exch")) +
				line(tabbed("moveto")) +
				line(tabbed("lineto")) +
				line(tabbed("stroke")) +
				line(tabbed("grestore")) +
			line("} def") +
			line("") +
			
			line("0.0039 0.4509 0.5843 setrgbcolor") +
			line("1.5 setlinewidth") +
			line("/Garamond findfont 25 scalefont setfont");
	}
	
	public static String rotate(double scale) {
		return "";
	}
	
	public static String scale(double scale) {
		return line(scale + " " + scale + " scale");
	}
	
	public static String translateForMargins(
			double calculatedBoundingBoxHeight,
			double marginHeightFactor,
			double scale) {
		double xTranslate = 0.0;
		double marginHeight = CalculationUtilities.marginHeight(
			calculatedBoundingBoxHeight, marginHeightFactor, scale);
		double yTranslate = (marginHeight / 2.0);
		
		return line(xTranslate + " " + yTranslate + " translate");
	}
	
	public static String yearLabels(
			Date[] newYearsDates,
			Date graphStartDate,
			Date graphEndDate,
			double defaultBoundingBoxWidth,
			int startYPosition,
			double margin,
			double scale,
			double calculatedBoundingBoxHeight,
			double marginHeightFactor) {
		final double marginHeight = CalculationUtilities.marginHeight(
			calculatedBoundingBoxHeight, marginHeightFactor, 1.0);
		final double pageBoundingBoxHeight =
			CalculationUtilities.pageBoundingBoxHeight(
				calculatedBoundingBoxHeight, 1.0);
		final double verticalTickHeight =
			(pageBoundingBoxHeight - (marginHeight / 2.0));
		StringWriter yearLabelPostScript = new StringWriter();
		
		for (Date currentNewYearsDate : newYearsDates) {
			double xCoordinate = CalculationUtilities.xCoordinate(
				currentNewYearsDate,
				graphStartDate,
				graphEndDate,
				defaultBoundingBoxWidth,
				margin);
			
			yearLabelPostScript.append(
				line("0 setgray") +
				line("(" + currentNewYearsDate.getYear() + ") " + 
						xCoordinate + " " + startYPosition + " ticklabel") +
				line("" + xCoordinate + " " + startYPosition + " " +
						verticalTickHeight + " vertical"));
		}
		
		return yearLabelPostScript.toString();
	}
	
	public static String line(String str) {
		return str + "\r\n";
	}
	
	public static String tabbed(String str) {
		return "\t" + str;
	}
	
	public static String setrgbcolor(String str) {
		return str + " setrgbcolor";
	}
}
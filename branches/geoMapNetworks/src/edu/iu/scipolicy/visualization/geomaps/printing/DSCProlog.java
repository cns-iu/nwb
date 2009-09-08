package edu.iu.scipolicy.visualization.geomaps.printing;

import java.io.IOException;

import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

/* DSC = Document Structuring Conventions, the standards that a piece of
 * PostScript code must obey to be valid Encapsulated PostScript.
 * 
 * The DSC Prolog is a set of PostScript comments at the start of the file
 * that describe some aspects of the content.
 */
public class DSCProlog {
	public static final String CREATOR_DSC_COMMENT_VALUE =
		"Geo Maps plug-in for the Sci² Tool, a product of the Cyberinfrastructure " +
		"for Network Science Center (http://cns.slis.indiana.edu) at Indiana University";
	public static final int NUMBER_OF_PAGES_DSC_COMMENT_VALUE = 1;
	private static final String PAGE_ORDER_DSC_COMMENT_VALUE = "Ascend";
	
	private String outputPSFileName;
	private double pageHeightInPoints;
	
	
	public DSCProlog(String outputPSFileName, double pageHeightInPoints) {
		this.outputPSFileName = outputPSFileName;
		this.pageHeightInPoints = pageHeightInPoints;
	}
	
	
	public String toPostScript() throws IOException {
		String s = "";
		
		s += ("%%Creator: " + CREATOR_DSC_COMMENT_VALUE + "\n");
		s += ("%%Title: " + outputPSFileName + "\n");
		s += ("%%Pages: " + NUMBER_OF_PAGES_DSC_COMMENT_VALUE + "\n");
		s += ("%%: " + PAGE_ORDER_DSC_COMMENT_VALUE + "\n");
		s += ("%%BoundingBox: " + createBoundingBoxCommentValue() + "\n");
		s += ("%%Pages: 1" + "\n");
		s += ("%%EndComments" + "\n");
		s += "\n";
		
		return s;
	}
	
	/* Heuristic only.
	 * This calculation will likely be incorrect if the map and legend are
	 * repositioned.
	 * Should work when the legend is below the map.
	 */
	private String createBoundingBoxCommentValue() throws IOException {
//		double lowerLeftX = Math.min(mapBoundingBox.getLowerLeftX(), legend.getLowerLeftX());
//		double lowerLeftY = Math.min(mapBoundingBox.getLowerLeftY(), legend.getLowerLeftY());
//		double upperRightX = mapBoundingBox.getUpperRightX();
//		double upperRightY = mapBoundingBox.getUpperRightY();		
//		
//		return "%%BoundingBox: " + lowerLeftX + " " + lowerLeftY + " " + upperRightX + " " + upperRightY + "\n";
		
		/* Now that we've added the page header and footer, we may in fact be
		 * writing on the page outside of the previous map-and-legend-only
		 * bounds.  To keep things simple, we'll claim that we'd like to
		 * reserve the entire (landscape US Letter size) page for our
		 * BoundingBox.  Yes, it could be a tiny bit tighter, but it is still
		 * correct.
		 */		
		return "0 0 " + Constants.PAGE_WIDTH_IN_POINTS + " " + pageHeightInPoints + "\n";
	}
}

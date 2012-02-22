package edu.iu.sci2.visualization.geomaps.viz.ps;

import edu.iu.sci2.visualization.geomaps.viz.Constants;

/* DSC = Document Structuring Conventions, the standards that a piece of
 * PostScript code must obey to be valid Encapsulated PostScript.
 * 
 * The DSC Prolog is a set of PostScript comments at the start of the file
 * that describe some aspects of the content.
 */
public class DSCProlog {
	public static final String CREATOR_DSC_COMMENT_VALUE =
		"Geo Maps plug-in for the Sci� Tool, a product of the Cyberinfrastructure " +
		"for Network Science Center (http://cns.iu.edu) at Indiana University";
	public static final int NUMBER_OF_PAGES_DSC_COMMENT_VALUE = 1;
	private static final String PAGE_ORDER_DSC_COMMENT_VALUE = "Ascend";
	
	private final String outputPSFileName;
	private final double pageHeightInPoints;
	
	
	public DSCProlog(String outputPSFileName, double pageHeightInPoints) {
		this.outputPSFileName = outputPSFileName;
		this.pageHeightInPoints = pageHeightInPoints;
	}
	
	
	public String toPostScript() {
		String s = "";
		
		s += ("%!PS-Adobe-3.0 EPSF-3.0" + "\n");
		s += ("%%Creator: " + CREATOR_DSC_COMMENT_VALUE + "\n");
		s += ("%%Title: " + outputPSFileName + "\n");
		s += ("%%Pages: 1" + "\n");
		s += ("%%PageOrder: " + PAGE_ORDER_DSC_COMMENT_VALUE + "\n");
		s += ("%%BoundingBox: " + createBoundingBoxCommentValue() + "\n");
		/* TODO GhostScript obeys, Distiller ignores.  Non-standard.
		 * Note that GhostScript seems to be sensitive to where this comment is in the prolog,
		 * so don't move it.
		 */
		s += ("%%Orientation: Landscape" + "\n");
		s += ("%%Page: 1" + "\n");
		s += ("%%EndComments" + "\n");
		s += "\n";
		
		/* TODO Note that this currently rotates and translates assuming that the PostScript
		 * renderer obeys the %%Orientation setting and the swapped BoundingBox.  This is to
		 * optimize prettiness in GhostScript.
		 * This will likely break according to the natural aspect ratio of the
		 * shapefile and the projection used.
		 */
		s += ("90 rotate" + "\n");
		s += ("0" + " " + (-pageHeightInPoints) + " " + "translate" + "\n");
		
		return s;
	}

	private String createBoundingBoxCommentValue() {
//		/* Heuristic only.
//		 * This calculation will likely be incorrect if the map and legend are
//		 * repositioned.
//		 * Should work when the legend is below the map.
//		 */
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
		/* TODO We give in height,width order rather than width,height order because we assume
		 * that the PostScript renderer will obey the %%Orientation comment in the DSC prolog.
		 * In particular, this makes GhostScript work, and that's what we need now.
		 */
		String value = "0 0 " +
						((int) pageHeightInPoints) +
						" " +
						((int) Constants.PAGE_WIDTH_IN_POINTS);
		
		return value;
	}
}

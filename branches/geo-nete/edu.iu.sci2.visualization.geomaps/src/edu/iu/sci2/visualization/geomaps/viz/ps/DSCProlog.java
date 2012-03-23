package edu.iu.sci2.visualization.geomaps.viz.ps;

import edu.iu.sci2.visualization.geomaps.utility.Dimension;

/* DSC = Document Structuring Conventions, the standards that a piece of
 * PostScript code must obey to be valid Encapsulated PostScript.
 * 
 * The DSC Prolog is a set of PostScript comments at the start of the file
 * that describe some aspects of the content.
 */
public class DSCProlog implements PostScriptable {
	public static final String CREATOR_DSC_COMMENT_VALUE =
		"Geo Maps plug-in for the Sci² Tool, a product of the Cyberinfrastructure " +
		"for Network Science Center (http://cns.iu.edu) at Indiana University";
	public static final int NUMBER_OF_PAGES_DSC_COMMENT_VALUE = 1;
	private static final String PAGE_ORDER_DSC_COMMENT_VALUE = "Ascend";
	
	private final String outputPSFileName;
	private final Dimension<Double> pageDimensions;
	
	
	public DSCProlog(String outputPSFileName, Dimension<Double> pageDimensions) {
		this.outputPSFileName = outputPSFileName;
		this.pageDimensions = pageDimensions;
	}
	
	
	@Override
	public String toPostScript() {
		String s = "";
		
		s += ("%!PS-Adobe-3.0 EPSF-3.0" + "\n");
		s += ("%%Creator: " + CREATOR_DSC_COMMENT_VALUE + "\n");
		s += ("%%Title: " + outputPSFileName + "\n");
		s += ("%%Pages: 1" + "\n");
		s += ("%%PageOrder: " + PAGE_ORDER_DSC_COMMENT_VALUE + "\n");
		s += ("%%BoundingBox: " + createBoundingBoxCommentValue() + "\n");
		/* GhostScript obeys, Distiller ignores.  Non-standard.
		 * GhostScript seems to be sensitive to where this comment occurs in the prolog.
		 */
		s += ("%%Orientation: Landscape" + "\n");
		s += ("%%Page: 1" + "\n");
		s += ("%%EndComments" + "\n");
		s += "\n";
		
		/* Rotates and translates assuming that the PostScript renderer obeys the %%Orientation
		 * setting and the swapped BoundingBox.
		 */
		s += ("90 rotate" + "\n");
		s += ("0" + " " + (-pageDimensions.getHeight()) + " " + "translate" + "\n");
		
		return s;
	}

	private String createBoundingBoxCommentValue() {
		/* The width-then-height order is intentionally reversed here because we assume that the
		 * PostScript renderer will obey the %%Orientation comment in the DSC prolog.
		 */
		return String.format("0 0 %d %d",
				pageDimensions.getHeight().intValue(),
				pageDimensions.getWidth().intValue());
	}
}

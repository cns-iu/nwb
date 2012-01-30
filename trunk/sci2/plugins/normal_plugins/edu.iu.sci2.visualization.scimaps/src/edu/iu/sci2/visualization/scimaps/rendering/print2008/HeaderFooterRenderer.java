package edu.iu.sci2.visualization.scimaps.rendering.print2008;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.text.DateFormat;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * This class renders the header and footer
 *
 */
public class HeaderFooterRenderer {
	public static void render(GraphicsState state, HeaderFooter headerfooter) {
		state.setFontSize(8);
		state.setGray(0.3);

		DateFormat longDateFormat = DateFormat.getDateTimeInstance(
				DateFormat.LONG, DateFormat.LONG);
		String dateFormatted = longDateFormat.format(headerfooter.getDate());

		// Header
		state.current.drawString("Date and time: " + dateFormatted, inch(.5f),
				inch(.3f));
		state.current.drawString(
				"Input data: " + headerfooter.getInputDataName(), inch(.5f),
				inch(.45f));

		// Footer
		state.current.drawString(
				"Cyberinfrastructure for Network Science Center (2010) Science Map. "
						+ "Indiana University, http://sci.slis.indiana.edu",
				inch(.5f), inch(10.65f));
	}
}

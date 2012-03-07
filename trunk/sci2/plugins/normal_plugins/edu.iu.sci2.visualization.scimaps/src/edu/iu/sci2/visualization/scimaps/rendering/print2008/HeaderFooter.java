package edu.iu.sci2.visualization.scimaps.rendering.print2008;

import java.util.Date;

/**
 * This class represents a header and a footer.
 */
@Deprecated
public class HeaderFooter {
	private String inputDataName;
	private Date date;

	@Deprecated
	public HeaderFooter(String inputDataName, Date date) {
		this.inputDataName = inputDataName;
		this.date = date;
	}
	
	@Deprecated
	public String getInputDataName() {
		return inputDataName;
	}
	
	@Deprecated
	public Date getDate() {
		return date;
	}
	
}
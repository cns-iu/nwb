package edu.iu.sci2.visualization.scimaps.rendering.print2008;

import java.util.Date;

/**
 * This class represents a header and a footer.
 */
public class HeaderFooter {
	private String inputDataName;
	private Date date;


	public HeaderFooter(String inputDataName, Date date) {
		this.inputDataName = inputDataName;
		this.date = date;
	}

	public String getInputDataName() {
		return inputDataName;
	}
	
	public Date getDate() {
		return date;
	}
	
}
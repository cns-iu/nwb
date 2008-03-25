package edu.iu.iv.modeling.tarl.util.impl;

import edu.iu.iv.modeling.tarl.main.TarlHelper;
import edu.iu.iv.modeling.tarl.publication.PublicationManager;
import edu.iu.iv.modeling.tarl.util.YearInformation;


/**
 * This class defines a default implementation of the <code>YearInformationInterface</code> interface.  It defines model parameters such as start year and end year which are used mainly by the <code>TarlHelper</code> and <code>PublicationManager</code>.
 *
 * @author Jeegar T Maru
 * @see PublicationManager
 * @see TarlHelper
 */
public class DefaultYearInformation implements YearInformation {
	/**
	 * Stores the start year for the Model
	 */
	protected int startYear;

	/**
	 * Stores the end year for the Model
	 */
	protected int endYear;

	/**
	 * Creates a new instance for a <code>YearInformation</code>.  It initializes the parameters using default values.
	 */
	public DefaultYearInformation() {
		startYear = 1995;
		endYear = 2001;
	}

	/**
	 * Initializes the <code>YearInformation</code> of the model with default values
	 */
	public void initializeDefault() {
		this.startYear = 1995;
		this.endYear = 2001;
	}

	/**
	 * Returns the start year of the model
	 *
	 * @return the start year of the model
	 */
	public int getStartYear() {
		return startYear;
	}

	/**
	 * Returns the end year of the model
	 *
	 * @return the end year of the model
	 */
	public int getEndYear() {
		return endYear;
	}

	/**
	 * Stores the start year of the model
	 *
	 * @param startYear the start year of the model
	 */
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	/**
	 * Stores the end year of the model
	 *
	 * @param endYear the end year of the model
	 */
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
}

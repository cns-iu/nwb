package edu.iu.iv.modeling.tarl.util;


/**
* This interface is used to store the <code>YearInformation</code> for the model.  This information includes the start year and the end year of the model.
*
* @author Jeegar T Maru
*/
public interface YearInformation {
	/**
	 * Initializes the Year Information using default values
	 */
	public void initializeDefault();

	/**
	 * Returns the start year for the Model
	 *
	 * @return the start year for the Model
	 */
	public int getStartYear();

	/**
	 * Returns the end year for the Model
	 *
	 * @return the end year for the Model
	 */
	public int getEndYear();

	/**
	 * Stores the start year for the Model
	 *
	 * @param startYear the start year for the Model
	 */
	public void setStartYear(int startYear);

	/**
	 * Stores the end year for the Model
	 *
	 * @param endYear the end year for the Model
	 */
	public void setEndYear(int endYear);
}

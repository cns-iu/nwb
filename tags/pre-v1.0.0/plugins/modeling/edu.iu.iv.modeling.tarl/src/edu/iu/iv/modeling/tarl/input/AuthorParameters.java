package edu.iu.iv.modeling.tarl.input;

import edu.iu.iv.modeling.tarl.TarlException;


/**
 * This interface defines the Parameters for the <code>AuthorManager</code> module of Tarl.  These parameters are used only by the <code>AuthorManager</code> module.
 *
 * @author Jeegar T Maru
 * @see ExecuterParameters
 */
public interface AuthorParameters {
	/**
	 * Initializes the <code>AuthorParameters</code> with default values
	 */
	public void initializeDefault();

	/**
	 * Returns the maximum age of the <code>Authors</code>
	 *
	 * @return the maximum age of the authors
	 */
	public int getMaximumAge();

	/**
	 * Returns the number of <code>Authors</code> to be deactivated in numDeactivationYears years
	 *
	 * @return the number of authors to be deactivate in numDeactivationYears years
	 */
	public int getNumDeactivationAuthors();

	/**
	 * Returns the number of years in which deactivation is supposed to take place
	 *
	 * @return the number of years in which deactivation is supposed to take place
	 */
	public int getNumDeactivationYears();

	/**
	 * Returns the number of co-authors for each <code>Author</code>
	 *
	 * @return the number of co-authors for each author
	 */
	public int getNumCoAuthors();

	/**
	 * Stores the maximum age of the <code>Authors</code>
	 *
	 * @param maximumAge the maximum age of the authors
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setMaximumAge(int maximumAge) throws TarlException;

	/**
	 * Stores the number of <code>Authors</code> to be deactivated in numDeactivationYears years
	 *
	 * @param numDeactivationAuthors the number of authors to be deactivate in numDeactivationYears years
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumDeactivationAuthors(int numDeactivationAuthors)
		throws TarlException;

	/**
	 * Stores the number of years in which deactivation is supposed to take place
	 *
	 * @param numDeactivationYears the number of years in which deactivation is supposed to take place
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumDeactivationYears(int numDeactivationYears)
		throws TarlException;

	/**
	 * Stores the number of co-authors for each <code>Author</code>
	 *
	 * @param numCoAuthors the number of co-authors for each author
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumCoAuthors(int numCoAuthors) throws TarlException;
}

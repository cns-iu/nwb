package edu.iu.iv.modeling.tarl.input.impl;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.impl.DefaultAuthorManager;
import edu.iu.iv.modeling.tarl.input.AuthorParameters;


/**
 * This class defines a default implementation of the <code>AuthorParametersInterface</code> interface.  It defines model parameters which are related to <code>Authors</code> and methods to access them.  This class is directly used by the <code>DefaultAuthorManager</code> class.
 *
 * @author Jeegar T Maru
 * @see DefaultAuthorManager
 * @see DefaultExecuterParameters
 */
public class DefaultAuthorParameters implements AuthorParameters {
	/**
	 * Stores the maximum age of authors
	 */
	protected int maximumAge;

	/**
	 * Stores the number of authors to be deactivated in numDeactivationYears years
	 */
	protected int numDeactivationAuthors;

	/**
	 * Stores the number of years in which deactivation is supposed to take place
	 */
	protected int numDeactivationYears;

	/**
	 * Stores the number of co-authors for each author
	 */
	protected int numCoAuthors;

	/**
	 * Creates a new default instance for <code>PublicationParameters</code>.  The default values are the values mentioned in the PNAS paper.
	 */
	public DefaultAuthorParameters() {
		maximumAge = -10;
		numDeactivationAuthors = 0;
		numDeactivationYears = 10;
		numCoAuthors = 2;
	}

	/**
	 * Initializes the <code>AuthorParameters</code> with default values
	 */
	public void initializeDefault() {
		this.maximumAge = -10;
		this.numDeactivationAuthors = 0;
		this.numDeactivationYears = 10;
		this.numCoAuthors = 2;
	}

	/**
	 * Returns the maximum age of the authors
	 *
	 * @return the maximum age of the authors
	 */
	public int getMaximumAge() {
		return maximumAge;
	}

	/**
	 * Returns the number of authors to be deactivated in numDeactivationYears years
	 *
	 * @return the number of authors to be deactivate in numDeactivationYears years
	 */
	public int getNumDeactivationAuthors() {
		return numDeactivationAuthors;
	}

	/**
	 * Returns the number of years in which deactivation is supposed to take place
	 *
	 * @return the number of years in which deactivation is supposed to take place
	 */
	public int getNumDeactivationYears() {
		return numDeactivationYears;
	}

	/**
	 * Returns the number of Co Authors for each <code>Author</code>
	 *
	 * @return the number of co-authors
	 */
	public int getNumCoAuthors() {
		return numCoAuthors;
	}

	/**
	 * Stores the maximum age of the authors
	 *
	 * @param maximumAge the maximum age of the authors
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setMaximumAge(int maximumAge) throws TarlException {
		if (true)
			this.maximumAge = maximumAge;
		else
			throw (new TarlException(new String(
			        "Maximum Age cannot be a non-integer\n")));
	}

	/**
	 * Stores the number of authors to be deactivated in numDeactivationYears years
	 *
	 * @param numDeactivationAuthors the number of authors to be deactivate in numDeactivationYears years
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumDeactivationAuthors(int numDeactivationAuthors)
		throws TarlException {
		if (numDeactivationAuthors >= 0)
			this.numDeactivationAuthors = numDeactivationAuthors;
		else
			throw (new TarlException(new String(
			        "Number of Deactivation authors cannot be negative\n")));
	}

	/**
	 * Stores the number of years in which deactivation is supposed to take place
	 *
	 * @param numDeactivationYears the number of years in which deactivation is supposed to take place
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumDeactivationYears(int numDeactivationYears)
		throws TarlException {
		if (numDeactivationYears > 0)
			this.numDeactivationYears = numDeactivationYears;
		else
			throw (new TarlException(new String(
			        "Number of Deactivation years cannot be zero or negative\n")));
	}

	/**
	 * Stores the number of co-authors for each author
	 *
	 * @param numCoAuthors the number of co-authors for each author
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumCoAuthors(int numCoAuthors) throws TarlException {
		if (numCoAuthors >= 0)
			this.numCoAuthors = numCoAuthors;
		else
			throw (new TarlException(new String(
			        "Number of Co-authors cannot be negative\n")));
	}
}

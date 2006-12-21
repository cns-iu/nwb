package edu.iu.iv.modeling.tarl.input.impl;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.input.PublicationParameters;
import edu.iu.iv.modeling.tarl.publication.impl.DefaultPublicationManager;
import edu.iu.iv.modeling.tarl.util.YearInformation;
import edu.iu.iv.modeling.tarl.util.impl.DefaultYearInformation;


/**
 * This class defines a default implementation of the <code>PublicationParametersInterface</code> interface.  It defines model parameters which are related to <code>Publications</code> and methods to access them.  This class is directly used by the <code>DefaultPublicationManager</code> class.
 *
 * @author Jeegar T Maru
 * @see DefaultPublicationManager
 * @see DefaultExecuterParameters
 */
public class DefaultPublicationParameters
	implements PublicationParameters {
	/**
	 * Stores whether aging is enabled or not
	 */
	protected boolean agingEnabled;

	/**
	 * Stores the number of publications to be read
	 */
	protected int numPublicationsRead;

	/**
	 * Stores the number of publications to be cited
	 */
	protected int numPublicationsCited;

	/**
	 * Stores the number of publications to be written by each author each year
	 */
	protected int numPublicationsWritten;

	/**
	 * Stores the number of levels of references considered for reading publications
	 */
	protected int numLevelsReferences;

	/**
	 * Stores the Year Information for the model
	 */
	protected YearInformation yearInformation;

	/**
	 * Creates a new default instance for <code>PublicationParameters</code>.  The default values are the values mentioned in the PNAS paper.
	 */
	public DefaultPublicationParameters() {
		agingEnabled = true;
		numPublicationsRead = 5;
		numPublicationsCited = 3;
		numPublicationsWritten = 1;
		numLevelsReferences = 1;

		yearInformation = new DefaultYearInformation();
	}

	/**
	 * Initializes the <code>PublicationParameters</code> with default values
	 */
	public void initializeDefault() {
		this.agingEnabled = true;
		this.numPublicationsRead = 5;
		this.numPublicationsCited = 3;
		this.numPublicationsWritten = 1;
		this.numLevelsReferences = 1;
	}

	/**
	 * Returns whether aging has been enabled for the model or not
	 *
	 * @return true, if aging has been enabled
	 */
	public boolean getAgingEnabled() {
		return agingEnabled;
	}

	/**
	 * Returns the number of publications to be read
	 *
	 * @return the number of publications to be read
	 */
	public int getNumPublicationsRead() {
		return numPublicationsRead;
	}

	/**
	 * Returns the number of publications to be cited
	 *
	 * @return the number of publications to be cited
	 */
	public int getNumPublicationsCited() {
		return numPublicationsCited;
	}

	/**
	 * Returns the number of publications to be written by each author each year
	 *
	 * @return the number of publications to be written by each author each year
	 */
	public int getNumPublicationsWritten() {
		return numPublicationsWritten;
	}

	/**
	 * Returns the number of levels of references considered for reading publications
	 *
	 * @return the number of levels of references considered for reading publications
	 */
	public int getNumLevelsReferences() {
		return numLevelsReferences;
	}

	/**
	 * Returns the <code>YearInformation</code> for the model
	 *
	 * @return the year information for the model
	 */
	public YearInformation getYearInformation() {
		return yearInformation;
	}

	/**
	 * Stores whether aging has been enabled for the model or not
	 *
	 * @param agingEnabled Specifies whether aging has been enabled
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setAgingEnabled(boolean agingEnabled) throws TarlException {
		if (true)
			this.agingEnabled = agingEnabled;
		else
			throw (new TarlException(new String(
			        "aging is neither enabled nor disabled\n")));
	}

	/**
	 * Stores the number of publications to be read
	 *
	 * @param numPublicationsRead the number of publications to be read
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumPublicationsRead(int numPublicationsRead)
		throws TarlException {
		if (numPublicationsRead >= 0)
			this.numPublicationsRead = numPublicationsRead;
		else
			throw (new TarlException(new String(
			        "Number of Publications Read cannot be negative\n")));
	}

	/**
	 * Stores the number of publications to be cited
	 *
	 * @param numPublicationsCited the number of publications to be cited
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumPublicationsCited(int numPublicationsCited)
		throws TarlException {
		if ((numPublicationsCited >= 0)
			    && (numPublicationsCited <= numPublicationsRead))
			this.numPublicationsCited = numPublicationsCited;
		else
			throw (new TarlException(new String(
			        "Number of Publications Cited cannot be negative or greater than number of publications read\n")));
	}

	/**
	 * Stores the number of publications to be written by each author each year
	 *
	 * @param numPublicationsWritten the number of publications to be written by each author each year
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumPublicationsWritten(int numPublicationsWritten)
		throws TarlException {
		if (numPublicationsWritten >= 0)
			this.numPublicationsWritten = numPublicationsWritten;
		else
			throw (new TarlException(new String(
			        "Number of Publications Written cannot be negative\n")));
	}

	/**
	 * Stores the number of levels of references considered for reading publications
	 *
	 * @param numLevelsReferences the number of levels of references considered for reading publications
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumLevelsReferences(int numLevelsReferences)
		throws TarlException {
		if (numLevelsReferences >= 0)
			this.numLevelsReferences = numLevelsReferences;
		else
			throw (new TarlException(new String(
			        "Number of levels of references cannot be negative\n")));
	}

	/**
	 * Stores the <code>YearInformation</code> for the model
	 *
	 * @param yearInformation Specifies the year information for the model
	 */
	public void setYearInformation(YearInformation yearInformation) {
		this.yearInformation = yearInformation;
	}
}

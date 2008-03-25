package edu.iu.iv.modeling.tarl.input;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.util.YearInformation;


/**
 * This interface defines the Parameters for the <code>PublicationManager</code> module of Tarl.  These parameters are used only by the <code>PublicationManager</code> module.
 *
 * @author Jeegar T Maru
 * @see ExecuterParameters
 */
public interface PublicationParameters {
	/**
	 * Initializes the <code>PublicationParameters</code> with default values
	 */
	public void initializeDefault();

	/**
	 * Returns whether aging has been enabled for the model or not
	 *
	 * @return true, if aging has been enabled
	 */
	public boolean getAgingEnabled();

	/**
	 * Returns the number of <code>Publications</code> to be read
	 *
	 * @return the number of publications to be read
	 */
	public int getNumPublicationsRead();

	/**
	 * Returns the number of <code>Publications</code> to be cited
	 *
	 * @return the number of publications to be cited
	 */
	public int getNumPublicationsCited();

	/**
	 * Returns the number of <code>Publications</code> to be written by each <code>Author</code> each year
	 *
	 * @return the number of publications to be written by each author each year
	 */
	public int getNumPublicationsWritten();

	/**
	 * Returns the number of levels of references considered for reading <code>publications</code>
	 *
	 * @return the number of levels of references considered for reading publications
	 */
	public int getNumLevelsReferences();

	/**
	 * Stores whether aging has been enabled for the model or not
	 *
	 * @param agingEnabled Specifies whether aging has been enabled
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setAgingEnabled(boolean agingEnabled) throws TarlException;

	/**
	 * Stores the number of <code>Publications</code> to be read
	 *
	 * @param numPublicationsRead the number of publications to be read
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumPublicationsRead(int numPublicationsRead)
		throws TarlException;

	/**
	 * Stores the number of <code>Publications</code> to be cited
	 *
	 * @param numPublicationsCited the number of publications to be cited
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumPublicationsCited(int numPublicationsCited)
		throws TarlException;

	/**
	 * Stores the number of <code>Publications</code> to be written by each <code>Author</code> each year
	 *
	 * @param numPublicationsWritten the number of publications to be written by each author each year
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumPublicationsWritten(int numPublicationsWritten)
		throws TarlException;

	/**
	 * Stores the number of levels of references considered for reading <code>Publications</code>
	 *
	 * @param numLevelsReferences the number of levels of references considered for reading publications
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumLevelsReferences(int numLevelsReferences)
		throws TarlException;

	/**
	 * Returns the <code>YearInformation</code> for the model
	 *
	 * @return the year information for the model
	 */
	public YearInformation getYearInformation();

	/**
	 * Stores the <code>YearInformation</code> for the model
	 *
	 * @param yearInformation Specifies the year information for the model
	 */
	public void setYearInformation(YearInformation yearInformation);
}

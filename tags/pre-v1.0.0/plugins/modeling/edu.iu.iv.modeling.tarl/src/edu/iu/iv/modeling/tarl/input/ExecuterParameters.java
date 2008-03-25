package edu.iu.iv.modeling.tarl.input;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.util.YearInformation;


/**
 * This interface defines the Parameters for the Executer module of Tarl.  These parameters are used only by the Executer module.
 *
 * @author Jeegar T Maru
 * @see HelperParameters
 */
public interface ExecuterParameters {
	/**
	 * Initializes the <code>ExecuterParameters</code> with default values
	 */
	public void initializeDefault();

	/**
	 * Returns the number of <code>Authors</code> at the start of the Model
	 *
	 * @return the number of authors at the start of the Model
	 */
	public int getNumAuthorsAtStart();

	/**
	 * Returns the number of <code>Publications</code> at the start of the Model
	 *
	 * @return the number of publications at the start of the Model
	 */
	public int getNumPublicationsAtStart();

	/**
	 * Returns the number of <code>Authors</code> to be created in numCreationYears years
	 *
	 * @return the number of authors to be created in numCreationYears years
	 */
	public int getNumCreationAuthors();

	/**
	 * Returns the number of years in which creation is supposed to take place
	 *
	 * @return the number of years in which creation is supposed to take place
	 */
	public int getNumCreationYears();

	/**
	 * Stores the number of authors at the start of the Model
	 *
	 * @param numAuthorsAtStart the number of authors at the start of the Model
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumAuthorsAtStart(int numAuthorsAtStart)
		throws TarlException;

	/**
	 * Stores the number of publications at the start of the Model
	 *
	 * @param numPublicationsAtStart the number of publications at the start of the Model
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumPublicationsAtStart(int numPublicationsAtStart)
		throws TarlException;

	/**
	 * Stores the number of <code>Authors</code> to be created in numCreationYears years
	 *
	 * @param numCreationAuthors the number of authors to be created in numCreationYears years
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumCreationAuthors(int numCreationAuthors)
		throws TarlException;

	/**
	 * Stores the number of years in which creation is supposed to take place
	 *
	 * @param numCreationYears the number of years in which creation is supposed to take place
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumCreationYears(int numCreationYears)
		throws TarlException;

	/**
	 * Returns the ParametersInterface for the <code>Publication</code> module of Tarl
	 *
	 * @return the parameters interface for the publication module of tarl
	 */
	public PublicationParameters getPublicationParameters();

	/**
	 * Returns the ParametersInterface for the <code>Author</code> module of Tarl
	 *
	 * @return the parameters interface for the author module of tarl
	 */
	public AuthorParameters getAuthorParameters();

	/**
	 * Returns the ParametersInterface for the <code>Topic</code> module of Tarl
	 *
	 * @return the parameters interface for the topic module of tarl
	 */
	public TopicParameters getTopicParameters();

	/**
	  * Returns the <code>YearInformation</code> for the model
	  *
	  * @return the year information for the model
	  */
	public YearInformation getYearInformation();
}

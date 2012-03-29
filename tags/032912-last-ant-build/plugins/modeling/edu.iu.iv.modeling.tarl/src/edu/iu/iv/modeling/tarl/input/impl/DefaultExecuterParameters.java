package edu.iu.iv.modeling.tarl.input.impl;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.input.AuthorParameters;
import edu.iu.iv.modeling.tarl.input.ExecuterParameters;
import edu.iu.iv.modeling.tarl.input.PublicationParameters;
import edu.iu.iv.modeling.tarl.input.TopicParameters;
import edu.iu.iv.modeling.tarl.main.impl.DefaultTarlExecuter;
import edu.iu.iv.modeling.tarl.util.YearInformation;
import edu.iu.iv.modeling.tarl.util.impl.DefaultYearInformation;


/**
 * This class defines a default implementation of the <code>ExecuterParametersInterface</code> interface.  It defines model parameters which are needed by the <code>TarlExecuter</code> and methods to access them.
 *
 * @author Jeegar T Maru
 * @see DefaultPublicationParameters
 * @see DefaultAuthorParameters
 * @see DefaultTopicParameters
 * @see DefaultTarlExecuter
 */
public class DefaultExecuterParameters implements ExecuterParameters {
	/**
	 * Stores the number of authors at the start of the Model
	 */
	protected int numAuthorsAtStart;

	/**
	 * Stores the number of publications at the start of the Model
	 */
	protected int numPublicationsAtStart;

	/*
	 * Stores the number of authors to be created in num_creation_years years
	 */
	protected int numCreationAuthors;

	/**
	 * Stores the number of years in which creation is supposed to take place
	 */
	protected int numCreationYears;

	/**
	 * Stores the <code>YearInformation</code> for the model
	 */
	protected YearInformation yearInformation;

	/**
	 * Stores the <code>PublicationParameters</code> for the model
	 */
	protected PublicationParameters publicationParameters;

	/**
	 * Stores the <code>AuthorParameters</code> for the model
	 */
	protected AuthorParameters authorParameters;

	/**
	 * Stores the <code>TopicParameters</code> for the model
	 */
	protected TopicParameters topicParameters;

	/**
	 * Creates a new default instance of an <code>ExecuterParameters</code>.  It initializes the parameters using default values as described in the PNAS paper.
	 */
	public DefaultExecuterParameters() {
		numAuthorsAtStart = 5;
		numPublicationsAtStart = 20;
		numCreationAuthors = 0;
		numCreationYears = 10;

		yearInformation = new DefaultYearInformation();
		publicationParameters = new DefaultPublicationParameters();
		publicationParameters.setYearInformation(yearInformation);
		authorParameters = new DefaultAuthorParameters();
		topicParameters = new DefaultTopicParameters();
	}

	/**
	 * Initializes the <code>ExecuterParameters</code> with default values
	 */
	public void initializeDefault() {
		this.numAuthorsAtStart = 5;
		this.numPublicationsAtStart = 20;
		this.numCreationAuthors = 0;
		this.numCreationYears = 10;

		yearInformation = new DefaultYearInformation();
		yearInformation.initializeDefault();
		publicationParameters = new DefaultPublicationParameters();
		publicationParameters.initializeDefault();
		publicationParameters.setYearInformation(yearInformation);
		authorParameters = new DefaultAuthorParameters();
		authorParameters.initializeDefault();
		topicParameters = new DefaultTopicParameters();
		topicParameters.initializeDefault();
	}

	/**
	 * Returns the number of authors at the start of the Model
	 *
	 * @return the number of authors at the start of the Model
	 */
	public int getNumAuthorsAtStart() {
		return numAuthorsAtStart;
	}

	/**
	 * Returns the number of publications at the start of the Model
	 *
	 * @return the number of publications at the start of the Model
	 */
	public int getNumPublicationsAtStart() {
		return numPublicationsAtStart;
	}

	/**
	 * Returns the number of authors to be created in numCreationYears years
	 *
	 * @return the number of authors to be created in numCreationYears years
	 */
	public int getNumCreationAuthors() {
		return numCreationAuthors;
	}

	/**
	 * Returns the number of years in which creation is supposed to take place
	 *
	 * @return the number of years in which creation is supposed to take place
	 */
	public int getNumCreationYears() {
		return numCreationYears;
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
	 * Returns the <code>PublicationParameters</code> for the model
	 *
	 * @return the publication parameters for the model
	 */
	public PublicationParameters getPublicationParameters() {
		return publicationParameters;
	}

	/**
	 * Returns the <code>AuthorParameters</code> for the model
	 *
	 * @return the author parameters for the model
	 */
	public AuthorParameters getAuthorParameters() {
		return authorParameters;
	}

	/**
	 * Returns the <code>TopicParameters</code> for the model
	 *
	 * @return the topic parameters for the model
	 */
	public TopicParameters getTopicParameters() {
		return topicParameters;
	}

	/**
	 * Stores the number of authors at the start of the Model
	 *
	 * @param numAuthorsAtStart the number of authors at the start of the Model
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumAuthorsAtStart(int numAuthorsAtStart)
		throws TarlException {
		if (numAuthorsAtStart > 0)
			this.numAuthorsAtStart = numAuthorsAtStart;
		else
			throw (new TarlException(new String(
			        "Number of authors at the start cannot be zero or negative\n")));
	}

	/**
	 * Stores the number of publications at the start of the Model
	 *
	 * @param numPublicationsAtStart the number of publications at the start of the Model
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumPublicationsAtStart(int numPublicationsAtStart)
		throws TarlException {
		if (numPublicationsAtStart >= 0)
			this.numPublicationsAtStart = numPublicationsAtStart;
		else
			throw (new TarlException(new String(
			        "Number of publications at the start cannot be negative\n")));
	}

	/**
	 * Stores the number of authors to be created in numCreationYears years
	 *
	 * @param numCreationAuthors the number of authors to be created in numCreationYears years
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumCreationAuthors(int numCreationAuthors)
		throws TarlException {
		if (numCreationAuthors >= 0)
			this.numCreationAuthors = numCreationAuthors;
		else
			throw (new TarlException(new String(
			        "Number of Creation authors cannot be negative\n")));
	}

	/**
	 * Stores the number of years in which creation is supposed to take place
	 *
	 * @param numCreationYears the number of years in which creation is supposed to take place
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumCreationYears(int numCreationYears)
		throws TarlException {
		if (numCreationYears > 0)
			this.numCreationYears = numCreationYears;
		else
			throw (new TarlException(new String(
			        "Number of Creation years cannot be zero or negative\n")));
	}

	/**
	 * Stores the <code>YearInformation</code> for the model
	 *
	 * @param yearInformation Specifies the year information for the model
	 */
	public void setYearInformation(YearInformation yearInformation) {
		this.yearInformation = yearInformation;
	}

	/**
	 * Stores the <code>PublicationParameters</code> for the model
	 *
	 * @param publicationParameters Specifies the publicationParameters for the model
	 */
	public void setPublicationParameters(
	    PublicationParameters publicationParameters) {
		this.publicationParameters = publicationParameters;
	}

	/**
	 * Stores the <code>AuthorParameters</code> for the model
	 *
	 * @param authorParameters Specifies the authorParameters for the model
	 */
	public void setAuthorParameters(AuthorParameters authorParameters) {
		this.authorParameters = authorParameters;
	}

	/**
	 * Stores the <code>TopicParameters</code> for the model
	 *
	 * @param topicParameters Specifies the topicParameters for the model
	 */
	public void setTopicParameters(TopicParameters topicParameters) {
		this.topicParameters = topicParameters;
	}
}

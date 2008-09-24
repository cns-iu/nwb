package edu.iu.iv.modeling.tarl.publication.impl;

import java.io.File;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.input.PublicationParameters;
import edu.iu.iv.modeling.tarl.publication.AgingHelperInterface;
import edu.iu.iv.modeling.tarl.publication.Publication;
import edu.iu.iv.modeling.tarl.publication.PublicationDatabase;
import edu.iu.iv.modeling.tarl.publication.PublicationGroup;
import edu.iu.iv.modeling.tarl.publication.PublicationManager;
import edu.iu.iv.modeling.tarl.util.AuthorsTopicBucket;
import edu.iu.iv.modeling.tarl.util.YearInformation;


/**
 * This class defines a default implementation of the <code>PublicationManagerInterface</code> which manages the group of publications.  It uses an instance of the <code>PublicationDatabase</code> to perform its tasks.
 *
 * @author Jeegar T Maru
 * @see DefaultPublicationDatabase
 * @see PublicationManager
 */
public class DefaultPublicationManager implements PublicationManager {
	/**
	 * Stores the list of Publications as a <code>PublicationDatabase</code>
	 */
	protected PublicationDatabase publication_database;

	/**
	 * Stores the aging helper for the publications
	 */
	protected AgingHelperInterface agingHelper;

	/**
	 * Stores the current year of the system.  This makes it easy to add a new <code>Publication</code> without specifying the year of publication.
	 */
	protected int current_year;

	/**
	 * Stores the model parameters related to <code>Publications</code>
	 */
	protected PublicationParameters publicationParameters;

	/**
	 * Creates a new instance of a PublicationManager with the default current year 0
	 */
	public DefaultPublicationManager() {
		publication_database = null;
		agingHelper = null;
	}

	/**
	 * Creates a new instance of a PublicationManager with the given current year
	 *
	 * @param publicationParameters Specifies the model parameters related to publication environment
	 */
	public DefaultPublicationManager(
	    PublicationParameters publicationParameters) {
		YearInformation yearInformation;

		publication_database = new DefaultPublicationDatabase();
		yearInformation = publicationParameters.getYearInformation();
		this.current_year = yearInformation.getStartYear();
	}

	/**
	 * Initializes the <code>Publications</code> environment with the specified model parameters
	 *
	 * @param publicationParameters Specifies the publication parameters for the model
	 */
	public void initializePublications(
	    PublicationParameters publicationParameters, File agingFunctionFile) {
		YearInformation yearInformation;

		publication_database = new DefaultPublicationDatabase();
		agingHelper = new DefaultAgingHelper();
		agingHelper.initialize(agingFunctionFile);

		this.publicationParameters = publicationParameters;
		yearInformation = publicationParameters.getYearInformation();
		current_year = yearInformation.getStartYear();
	}

	/**
	 * Produces the <code>Publications</code> for the specified authors and topic at the very start of the model
	 *
	 * @param authorsTopicBucket Specifies the authors and topic for the new publications
	 *
	 * @exception TarlException if the new publications cannot be initialized
	 */
	public void producePublicationsAtStart(
	    AuthorsTopicBucket authorsTopicBucket)
		throws TarlException {
		publication_database.addPublication(current_year,
		    authorsTopicBucket.getTopic(), authorsTopicBucket.getAuthors());
	}

	/**
	 * Produces the list of citations for the newly created <code>Publication</code>.  The mechanism used depends on whether aging is enabled or not.
	 *
	 * @param authorsTopicBucket Specifies the author-topic bucket for the new publication
	 */
	private PublicationGroup computeCitations(
	    AuthorsTopicBucket authorsTopicBucket) {
		int numPublicationsCited;
		Publication tempPublication;
		PublicationGroup allPublications;
		PublicationGroup publicationsCited;

		numPublicationsCited = publicationParameters.getNumPublicationsCited();
		allPublications = publication_database.getPublications();
		allPublications =
			DefaultPublicationUtility.filterOnYearLessThan(allPublications,
			    current_year);
		allPublications =
			DefaultPublicationUtility.filterOnTopicEquality(allPublications,
			    authorsTopicBucket.getTopic());

		if (publicationParameters.getAgingEnabled()) {
			int timeSlice;
			int publicationCitedSize;
			PublicationGroup tempPublicationGroup;

			if (allPublications.size() <= numPublicationsCited)
				return (allPublications);

			publicationsCited = new DefaultPublicationGroup();
			publicationCitedSize = publicationsCited.size();

			while (publicationCitedSize < numPublicationsCited) {
				timeSlice = agingHelper.getRandomYearDifference();
				tempPublicationGroup =
					DefaultPublicationUtility.filterOnYearEquality(allPublications,
					    current_year - timeSlice);

				if (publicationsCited.isSubset(tempPublicationGroup))
					continue;

				do {
					tempPublication =
						tempPublicationGroup.getRandomPublication();
					publicationsCited.addPublication(tempPublication);
				} while (publicationsCited.size() == publicationCitedSize);

				publicationCitedSize = publicationsCited.size();
			}
		} else {
			int numPublicationsRead;
			int numLevelsReferences;
			PublicationGroup publicationsRead;

			numPublicationsRead =
				publicationParameters.getNumPublicationsRead();
			numLevelsReferences =
				publicationParameters.getNumLevelsReferences();

			publicationsRead =
				allPublications.getRandomPublications(numPublicationsRead);
			publicationsRead.union(DefaultPublicationUtility
			    .getAllCitedPublications(publicationsRead, numLevelsReferences));
			publicationsCited =
				publicationsRead.getRandomPublications(numPublicationsCited);
		}

		return publicationsCited;
	}

	/**
	 * Produces the <code>Publications</code> for the specified authors and topic
	 *
	 * @param authorsTopicBucket Specifies the authors and topic for the new publications
	 *
	 * @exception TarlException if the new publications cannot be initialized
	 */
	public void producePublications(
	    AuthorsTopicBucket authorsTopicBucket)
		throws TarlException {
		int i;
		int numPublicationsWritten;
		PublicationGroup citations;

		numPublicationsWritten =
			publicationParameters.getNumPublicationsWritten();

		for (i = 0; i < numPublicationsWritten; i++) {
			citations = this.computeCitations(authorsTopicBucket);
			publication_database.addPublication(current_year,
			    authorsTopicBucket.getTopic(), authorsTopicBucket.getAuthors(),
			    citations);
		}
	}

	/**
	 * Terminates the current year.  This method is called so that the <code>PublicationManager</code> can update its internal copy of current_year.
	 */
	public void terminateCurrentYear() {
		current_year++;
	}

	/**
	 * Returns all the <code>Publications</code> in the environment
	 *
	 *@return the group of publications in the environment
	 */
	public PublicationGroup getPublications() {
		return (publication_database.getPublications());
	}

	/**
	 * Cleans up the publication environment by removing all the instances of the <code>Publications</code> from the database
	 */
	public void cleanUpPublication() {
		publication_database.removeAll();
	}

	/**
	 * Returns the details of the <code>PublicationDatabase</code> as a <code>String</code>
	 *
	 * @return a string describing the publication database
	 */
	public String toString() {
		return (publication_database.toString());
	}
}

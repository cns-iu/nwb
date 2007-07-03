package edu.iu.iv.modeling.tarl.main.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.AuthorManager;
import edu.iu.iv.modeling.tarl.author.impl.DefaultAuthorManager;
import edu.iu.iv.modeling.tarl.input.ExecuterParameters;
import edu.iu.iv.modeling.tarl.main.TarlExecuter;
import edu.iu.iv.modeling.tarl.output.OutputWriter;
import edu.iu.iv.modeling.tarl.output.impl.DefaultOutputWriter;
import edu.iu.iv.modeling.tarl.publication.PublicationManager;
import edu.iu.iv.modeling.tarl.publication.impl.DefaultPublicationManager;
import edu.iu.iv.modeling.tarl.topic.TopicManager;
import edu.iu.iv.modeling.tarl.topic.impl.DefaultTopicManager;
import edu.iu.iv.modeling.tarl.util.AuthorsTopicBucket;
import edu.iu.iv.modeling.tarl.util.impl.DefaultAuthorsTopicBucket;
import edu.iu.iv.modeling.tarl.util.impl.ExtendedHashSet;


/**
 * This class defines a default implementation of the <code>TarlExecuterInterface</code>.  It utilizes the default implementations of the standard interfaces to achieve its cause.
 *
 * @author Jeegar T Maru
 * @see DefaultAuthorManager
 * @see DefaultPublicationManager
 * @see DefaultTopicManager
 * @see TarlExecuter
 */
public class DefaultTarlExecuter implements TarlExecuter {
	/**
	 * Stores the <code>AuthorManager</code> for the system
	 */
	protected AuthorManager authorManager;

	/**
	 * Stores the <code>PublicationManager</code> for the system
	 */
	protected PublicationManager publicationManager;

	/**
	 * Stores the <code>TopicManager</code> for the system
	 */
	protected TopicManager topicManager;

	/**
	 * Stores the model parameters related to the <code>TarlExecuter</code>
	 */
	protected ExecuterParameters executerParameters;

	/**
	 * Stores the offset number of years from the start year
	 */
	protected int offsetYear;

	/**
	 * Creates a new instance of the <code>TarlExecuter</code>
	 */
	public DefaultTarlExecuter() {
		authorManager = null;
		publicationManager = null;
		topicManager = null;
	}

	/**
	 * Initializes itself with the corresponding model parameters
	 *
	 * @param executerParameters Specifies the executer parameters of the model
	 *
	 * @exception TarlException if the new publications cannot be initialized
	 */
	public void initializeModel(ExecuterParameters executerParameters, File agingFunctionFile)
		throws TarlException {
		int i;
		int numAuthorsAtStart;
		int numPublicationsAtStart;
		ExtendedHashSet extHashSet;
		AuthorsTopicBucket authorsTopicBucket;

		this.executerParameters = executerParameters;

		topicManager = new DefaultTopicManager();
		authorManager = new DefaultAuthorManager();
		publicationManager = new DefaultPublicationManager();

		offsetYear = 0;

		topicManager.initializeTopics(executerParameters.getTopicParameters());
		authorManager.initializeAuthors(executerParameters.getAuthorParameters());
		publicationManager.initializePublications(executerParameters
		    .getPublicationParameters(), agingFunctionFile);

		numAuthorsAtStart = executerParameters.getNumAuthorsAtStart();

		for (i = 0; i < numAuthorsAtStart; i++)
			authorManager.addAuthor(topicManager.getRandomTopic());

		numPublicationsAtStart = executerParameters.getNumPublicationsAtStart();

		for (i = 0; i < numPublicationsAtStart; i++) {
			extHashSet =
				(ExtendedHashSet)authorManager.partitionActiveAuthors();
			authorsTopicBucket =
				(DefaultAuthorsTopicBucket)extHashSet.getRandomElement();

			if (authorsTopicBucket == null)
				break;

			publicationManager.producePublicationsAtStart(authorsTopicBucket);
		}
	}

	/**
	 * Produces <code>Publications</code> for the current year.
	 *
	 * @exception TarlException if the new publications cannot be initialized
	 */
	public void producePublications() throws TarlException {
		Collection collection;
		Iterator iterator;

		collection = authorManager.partitionActiveAuthors();
		iterator = collection.iterator();

		while (iterator.hasNext())
			publicationManager.producePublications((AuthorsTopicBucket)iterator
			    .next());
	}

	/**
	 * Terminates the current year for the system.
	 *
	 * @exception TarlException if new authors cannot be instantiated
	 */
	public void terminateCurrentYear() throws TarlException {
		int i;
		int numCreationAuthors;

		authorManager.terminateCurrentYear();
		publicationManager.terminateCurrentYear();

		offsetYear++;

		if ((offsetYear % (executerParameters.getNumCreationYears())) == 0) {
			numCreationAuthors = executerParameters.getNumCreationAuthors();

			for (i = 0; i < numCreationAuthors; i++)
				authorManager.addAuthor(topicManager.getRandomTopic());
		}
	}

	/**
	 * Writes the output Files to the file system
	 *
	 * @param outputFolder Specifies the name of the output folder
	 */
	public void writeOutputFiles(String outputFolder) {
		OutputWriter outputWriter;

		outputWriter = new DefaultOutputWriter();
		outputWriter.initialize(publicationManager.getPublications(),
		    authorManager.getAuthors(), topicManager.getTopics());

		try {
			outputWriter.writePajekFiles(outputFolder);
			outputWriter.writeDataFiles(outputFolder);
			System.out.println("Output Files written to the requested folder");
		} catch (FileNotFoundException fnfe) {
			System.err.println("Exception occurred : " + fnfe);
			fnfe.printStackTrace();
		}
	}

	/**
	 * Cleans up the system.
	 */
	public void cleanUpSystem() {
		publicationManager.cleanUpPublication();
		authorManager.cleanUpAuthor();
		topicManager.cleanUpTopic();
	}
    public AuthorManager getAuthorManager() {
        return authorManager;
    }
    public PublicationManager getPublicationManager() {
        return publicationManager;
    }
}

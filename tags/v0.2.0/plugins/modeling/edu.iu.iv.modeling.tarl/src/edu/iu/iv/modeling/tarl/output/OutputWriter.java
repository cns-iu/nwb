package edu.iu.iv.modeling.tarl.output;

import java.io.FileNotFoundException;

import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.publication.PublicationGroup;
import edu.iu.iv.modeling.tarl.topic.TopicGroup;


/**
 * This interface defines methods to write the output of the model to the file system.  The interface supports output in the form of Pajek files and Data Files.
 *
 * @author Jeegar T Maru
 * @see PublicationGroup
 * @see AuthorGroup
 * @see TopicGroup
 */
public interface OutputWriter {
	/**
	 * Initializes the <code>OutputWriter</code> with the given publications, authors and topics
	 *
	 * @param publicationGroup Specifies the group of all the publications for the model
	 * @param authorGroup Specifies the group of all authors for the model
	 * @param topicGroup Specifies the group of all topics for the model
	 */
	public void initialize(PublicationGroup publicationGroup,
	    AuthorGroup authorGroup, TopicGroup topicGroup);

	/**
	 * Writes out Pajek Files to the file system in the given folder
	 *
	 * @param folderPath Specifies the location of the folder for the output files
	 *
	 * @exception FileNotFoundException if the files cannot be written to the file system
	 */
	public void writePajekFiles(String folderPath) throws FileNotFoundException;

	/**
	 * Writes out the Data Files to the file system in the given folder
	 *
	 * @param folderPath Specifies the location of the folder for the output files
	 *
	 * @exception FileNotFoundException if the files cannot be written to the file system
	 */
	public void writeDataFiles(String folderPath) throws FileNotFoundException;
}

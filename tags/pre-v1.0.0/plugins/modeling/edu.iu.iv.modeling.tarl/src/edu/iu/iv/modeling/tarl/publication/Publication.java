package edu.iu.iv.modeling.tarl.publication;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This interface defines a <code>Publication</code> by defining the necessary functions it should support.  The <code>Publication</code> is used to represent the produce by an <code>Author</code>.  The minimal attributes of a <code>Publication</code> are : <br>
 * <ul>
 * <li> a non-null year of <code>Publication</code> which has an equivalent integer representation </li>
 * <li> a non-null topic that the <code>Publication</code> belongs to which has an equivalent <code>TopicInterface</code> representation </li>
 * <li>a non-null group of authors who have collaborated for producing the <code>Publication</code> which has an equivalent <code>AuthorGroupInterface</code> representation </li>
 * <li>a group of citations for the <code>Publication</code> which has an equivalent <code>PublicationGroupInterface</code>.  The group of citations could be null which is usually used to represent <code>Publications</code> produced at the start of the model. </li>
 * </ul>
 * <br>
* All <code>Publications</code> should also support an identification tag that the <code>PublicationDatabase</code> provides to enable other classes to distinguish between different <code>Publications</code>.
 *
 * @author Jeegar T Maru
 * @see PublicationDatabase
 * @see Topic
 * @see AuthorGroup
 * @see PublicationGroup
 */
public interface Publication {
	/**
	 * Initializes the <code>Publications</code> with the specified parameters
	 *
	 * @param id Specifies the unique publication identification
	 * @param year Specifies the year of publication
	 * @param topic Specifies the topic of the publication
	 * @param authors Specifies the authors who have collaborated for the publication
	 *
	 * @exception TarlException if the new publication cannot be initialized
	 */
	public void initialize(int id, int year, Topic topic,
	    AuthorGroup authors) throws TarlException;

	/**
	 * Initializes the <code>Publications</code> with the specified parameters
	 *
	 * @param id Specifies the unique publication identification
	 * @param year Specifies the year of publication
	 * @param topic Specifies the topic of the publication
	 * @param authors Specifies the authors who have collaborated for the publication
	 * @param citations Specifies the citations for the publication
	 *
	 * @exception TarlException if the new publication cannot be initialized
	 */
	public void initialize(int id, int year, Topic topic,
	    AuthorGroup authors, PublicationGroup citations)
		throws TarlException;

	/**
	 * Returns the unique identifier assigned by the <code>PublicationDatabaseInterface</code> of the <code>Publication</code> as an integer
	 *
	 * @return the unique identifier of the publication
	 */
	public int getId();

	/**
	 * Returns the year of publication
	 *
	 * @return the year of publication
	 */
	public int getYear();

	/**
	 * Returns the <code>Topic</code> of the <code>Publication</code>
	 *
	 * @return the topic of the publication
	 */
	public Topic getTopic();

	/**
	 * Returns the <code>Authors</code> who have collaborated for the <code>Publication</code>
	 *
	 * @return the authors who have collaborated for the publication
	 */
	public AuthorGroup getAuthors();

	/**
	 * Returns the citations of the <code>Publication</code>
	 *
	 * @return the citations of the publication
	 */
	public PublicationGroup getCitations();

	/**
	 * Tests whether the current <code>Publication</code> is the same as the specified one.  This test should be based on the identification tag that the <code>PublicationDatabase</code> has assigned to both the <code>Publications</code>.
	 *
	 * @param publicationInterface Specifies the publication to be equated against
	 *
	 * @return true, if the current publication is equal to the specified one
	 */
	public boolean equals(Publication publicationInterface);
}

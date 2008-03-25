package edu.iu.iv.modeling.tarl.publication.impl;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.author.impl.DefaultAuthorGroup;
import edu.iu.iv.modeling.tarl.publication.Publication;
import edu.iu.iv.modeling.tarl.publication.PublicationGroup;
import edu.iu.iv.modeling.tarl.topic.Topic;
import edu.iu.iv.modeling.tarl.topic.impl.DefaultTopic;


/**
 * This class defines a default implementation of a <code>PublicationInterface</code>.  <code>Publications</code> are represented by the following attributes : <br>
 * <ul>
 * <li> the unique identifier of the <code>Publication</code> as an integer </li>
 * <li> the year of publication as an integer </li>
 * <li> the <code>Topic</code> of the <code>Publication</code> </li>
 * <li> the group of <code>Authors</code> who have collaborated for the <code>Publication</code> as an <code>AuthorGroup</code> </li>
 * <li> the group of cited <code>Publications</code> as a <code>PublicationGroup</code> </li>
 * </ul>
 *
 * @author Jeegar T Maru
 * @see DefaultTopic
 * @see DefaultAuthorGroup
 * @see DefaultPublicationGroup
 * @see Publication
 */
public class DefaultPublication implements Publication {
	/**
	 * Stores the unique id of the Publication
	 */
	protected int id;

	/**
	 * Stores the year of Publication
	 */
	protected int year;

	/**
	 * Stores the <code>Topic</code> that this Publication belongs to
	 */
	protected Topic topic;

	/**
	 * Stores the group of <code>Authors</code> that have contributed to the Publication, as a <code>AuthorGroup</code>
	 */
	protected AuthorGroup authors;

	/**
	 * Stores the group of Citations for the Publication, as a <code>PublicationGroup</code>
	 */
	protected PublicationGroup citations;

	/**
	 * Creates a new instance of a default <code>Publication</code>
	 */
	public DefaultPublication() {
		topic = null;
		authors = null;
		citations = null;
	}

	/**
	 * Creates a new instance for an <code>Publication</code> with the specified id and <code>Topic</code>.  A Publication should have an id, a year of publication, a <code>Topic</code> and a set of authors at the very least.
	 *
	 * @param id Specifies the unique id for the newly instantiated Publication
	 * @param topic Specifies the topic for the newly instantiated Publication
	 * @param authors Specifies the authors who have contributed for the newly instantiated Publication
	 *
	 * @exception TarlException if topic or authors are not specified
	 */
	public DefaultPublication(int id, int year, Topic topic,
	    AuthorGroup authors) throws TarlException {
		if ((topic == null) || (authors == null))
			throw (new TarlException(new String(
			        "Publications need to have a valid topic and an author group\n")));

		this.id = id;
		this.year = year;
		this.topic = topic;

		this.authors = new DefaultAuthorGroup();
		this.authors.initialize(authors.getAuthors());

		if (this.authors.size() <= 0)
			throw (new TarlException(new String(
			        "Publications need to have at least one author\n")));

		citations = null;
	}

	/**
	 * Creates a new instance for a <code>Publication</code> with the specified id, <code>Topic</code>, authors and citations
	 *
	 * @param id Specifies the unique id for the newly instantiated Publication
	 * @param topic Specifies the topic for the newly instantiated Publication
	 * @param authors Specifies the authors who have contributed for the newly instantiated Publication
	 * @param citations Specifies the citations for the the newly instantiated Publication
	 *
	 * @exception TarlException if there are no topics or authors specified
	 */
	public DefaultPublication(int id, int year, Topic topic,
	    AuthorGroup authors, PublicationGroup citations)
		throws TarlException {
		if ((topic == null) || (authors == null))
			throw (new NullPointerException(new String(
			        "Publications need to have a valid topic and an author group\n")));

		this.id = id;
		this.year = year;
		this.topic = topic;

		this.authors = new DefaultAuthorGroup();
		this.authors.initialize(authors.getAuthors());

		if (this.authors.size() <= 0)
			throw (new TarlException(new String(
			        "Publications need to have at least one author\n")));

		try {
			this.citations = new DefaultPublicationGroup();
			this.citations.initialize(citations.getPublications());
		} catch (NullPointerException npe) {
			this.citations = null;
		}
	}

	/**
	 * Initializes an instance of a <code>Publication</code> with the specified id, <code>Topic</code> and the group of <code>Authors</code>
	 *
	 * @param id Specifies the unique identifier for the publication
	 * @param topic Specifies the topic for the publication
	 * @param authors Specifies the authors who have contributed for the publication
	 *
	 * @exception TarlException if topic or authors are not specified
	 */
	public void initialize(int id, int year, Topic topic,
	    AuthorGroup authors) throws TarlException {
		if ((topic == null) || (authors == null))
			throw (new TarlException(new String(
			        "Publications need to have a valid topic and an author group\n")));

		this.id = id;
		this.year = year;
		this.topic = topic;

		this.authors = new DefaultAuthorGroup();
		this.authors.initialize(authors.getAuthors());

		if (this.authors.size() <= 0)
			throw (new TarlException(new String(
			        "Publications need to have at least one author\n")));

		citations = null;
	}

	/**
	 * Initializes an instance of a <code>Publication</code> with the specified id, <code>Topic</code>, the group of <code>Authors</code> and the group of <code>Publications</code>
	 *
	 * @param id Specifies the unique id for the newly instantiated Publication
	 * @param topic Specifies the topic for the newly instantiated Publication
	 * @param authors Specifies the authors who have contributed for the newly instantiated Publication
	 * @param citations Specifies the citations for the the newly instantiated Publication
	 *
	 * @exception TarlException if there are no topics or authors specified
	 */
	public void initialize(int id, int year, Topic topic,
	    AuthorGroup authors, PublicationGroup citations)
		throws TarlException {
		if ((topic == null) || (authors == null))
			throw (new NullPointerException(new String(
			        "Publications need to have a valid topic and an author group\n")));

		this.id = id;
		this.year = year;
		this.topic = topic;

		this.authors = new DefaultAuthorGroup();
		this.authors.initialize(authors.getAuthors());

		if (this.authors.size() <= 0)
			throw (new TarlException(new String(
			        "Publications need to have at least one author\n")));

		try {
			this.citations = new DefaultPublicationGroup();
			this.citations.initialize(citations.getPublications());
		} catch (NullPointerException npe) {
			this.citations = null;
		}
	}

	/**
	 * Returns the id of the <code>Publication</code>
	 *
	 * @return the id of the publication as an integer
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the year of <code>Publication</code>
	 *
	 * @return the year of publication as an integer
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Returns the <code>Topic</code> of the <code>Publication</code>
	 *
	 * @return the topic of the publication
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 * Returns the <code>Authors</code> of the <code>Publication</code>
	 *
	 * @return the authors who have collaborated for the publication, as an authorset
	 */
	public AuthorGroup getAuthors() {
		return authors;
	}

	/**
	 * Returns the Citations of the <code>Publication</code>
	 *
	 * @return the citations of the publication, as a publicationset
	 */
	public PublicationGroup getCitations() {
		return citations;
	}

	/**
	 * Adds a Citation to the group of citations of the <code>Publication</code>
	 *
	 * @param citation Specifies the citation to be added to the group
	 */
	public void addCitation(Publication citation) {
		if (citation == null)
			return;

		if (citations == null)
			citations = new DefaultPublicationGroup();

		citations.addPublication(citation);
	}

	/**
	 * Tests whether the current <code>Publication</code> is the same as the <code>Publication</code> given by the parameter
	 *
	 * @param publication Specifies the publication to be equated against
	 *
	 * @return true if the current publication and the specified publication are equal
	 */
	public boolean equals(Publication publication) {
		if (publication == null)
			return false;

		return (id == publication.getId());
	}

	/**
	 * Returns the details of the <code>Publication</code> as a String
	 *
	 * @return a String describing the publication
	 */
	public String toString() {
		StringBuffer sb;

		sb = new StringBuffer();
		sb.append(new String("Id : "));
		sb.append(id);
		sb.append(new String("\t"));
		sb.append(new String("Year : "));
		sb.append(year);
		sb.append(new String("\nTopic : "));
		sb.append(topic);
		sb.append(new String("Authors : "));
		sb.append(authors.toString());

		if (citations != null) {
			sb.append(new String("Citations : "));
			sb.append(citations.toString());
		}

		sb.append(new String("\n"));

		return (sb.toString());
	}
}

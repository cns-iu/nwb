package edu.iu.iv.modeling.tarl.publication.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.publication.Publication;
import edu.iu.iv.modeling.tarl.publication.PublicationDatabase;
import edu.iu.iv.modeling.tarl.publication.PublicationGroup;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This class defines a default implementation of <code>PublicationDatabaseInterface</code>.  It uses a <code>PublicationGroup</code> to store the <code>Publications</code>.
 *
 * @author Jeegar T Maru
 * @see DefaultPublication
 * @see DefaultPublicationGroup
 * @see PublicationDatabase
 */
public class DefaultPublicationDatabase implements PublicationDatabase {
	/**
	 * Stores the list of Publications as a <code>PublicationGroup</code>
	 */
	protected PublicationGroup publications;

	/**
	 * Stores the iterator to be used for the list of Publications
	 */
	protected Iterator iterator;

	/**
	 * Creates a new instance for a <code>PublicationDatabase</code>
	 */
	public DefaultPublicationDatabase() {
		publications = new DefaultPublicationGroup();
		iterator = publications.getIterator();
	}

	/**
	 * Adds a <code>Publication</code> to the group
	 *
	 * @param year Specifies the year of publication
	 * @param topic Specifies the topic of the publication
	 * @param authors Specifies the authors who have collaborated for the publication
	 *
	 * @exception TarlException if the new publication cannot be initialized
	 */
	public void addPublication(int year, Topic topic,
	    AuthorGroup authors) throws TarlException {
		Publication publication;

		publication = new DefaultPublication();
		publication.initialize(publications.size(), year, topic, authors);
		publications.addPublication(publication);
	}

	/**
	 * Adds a <code>Publication</code> to the group
	 *
	 * @param year Specifies the year of publication
	 * @param topic Specifies the topic of the publication
	 * @param authors Specifies the authors who have collaborated for the publication
	 * @param citations Specifies the publications that this publication cites
	 *
	 * @exception TarlException if the new publication cannot be initialized
	 */
	public void addPublication(int year, Topic topic,
	    AuthorGroup authors, PublicationGroup citations)
		throws TarlException {
		Publication publication;

		publication = new DefaultPublication();
		publication.initialize(publications.size(), year, topic, authors,
		    citations);
		publications.addPublication(publication);
	}

	/**
	 * Removes all the <code>Publications</code> from the database
	 */
	public void removeAll() {
		publications.removeAllPublications();
	}

	/**
	 * Returns the group of <code>Publications</code> in the Database
	 *
	 *@return the group of Publications in the Database
	 */
	public PublicationGroup getPublications() {
		return publications;
	}

	/**
	 * Returns the number of <code>Publications</code> in the Database
	 *
	 * @return the number of publications in the Database
	 */
	public int size() {
		return (publications.size());
	}

	/**
	 * Resets the Search Index so the Database can be searched iteratively
	 */
	public void resetSearchIndex() {
		iterator = publications.getIterator();
	}

	/**
	 * Tests whether the Database has more publications for iteration
	 *
	 * @return true if the Database has more publications to iterate over
	 */
	public boolean hasMorePublications() {
		return (iterator.hasNext());
	}

	/**
	 * Returns the next <code>Publication</code> in the database
	 *
	 * @return the next publication of the iteration
	 *
	 * @exception NoSuchElementException if the iteration has no more publications
	 */
	public Publication getNextPublication()
		throws NoSuchElementException {
		return ((DefaultPublication)iterator.next());
	}

	/**
	 * Returns the details of the <code>PublicationDatabase</code> as a String
	 *
	 * @return a String describing the PublicationDatabase
	 */
	public String toString() {
		return (publications.toString());
	}
}

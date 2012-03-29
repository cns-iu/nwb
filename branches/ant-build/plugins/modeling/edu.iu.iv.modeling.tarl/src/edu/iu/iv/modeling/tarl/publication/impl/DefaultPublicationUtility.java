package edu.iu.iv.modeling.tarl.publication.impl;

import java.util.Iterator;

import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.publication.AbstractPublicationUtility;
import edu.iu.iv.modeling.tarl.publication.Publication;
import edu.iu.iv.modeling.tarl.publication.PublicationGroup;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This class defines a default extension of <code>AbstractPublicationUtility</code> by defining all the abstract methods.  The class uses the default implementation of the standard interfaces to implement the methods.
 *
 * @author Jeegar T Maru
 * @see DefaultPublication
 * @see DefaultPublicationGroup
 * @see AbstractPublicationUtility
 */
public class DefaultPublicationUtility extends AbstractPublicationUtility {
	/**
	 * Filters out all the <code>Publications</code> which do not have the same year of publications as specified.  It returns a group of all the <code>Publications</code> which have the same year of publication as the specified one.
	 *
	 * @param publicationGroup Specifies the group of publications on which to apply the filter
	 * @param year Specifies the year of publication to be equated against
	 *
	 * @return the group of publications which are published in the specified year
	 */
	public static PublicationGroup filterOnYearEquality(
	    PublicationGroup publicationGroup, int year) {
		Iterator iterator;
		Publication publication;
		PublicationGroup result;

		result = new DefaultPublicationGroup();
		iterator = publicationGroup.getIterator();

		while (iterator.hasNext()) {
			publication = (DefaultPublication)iterator.next();

			if (publication.getYear() == year)
				result.addPublication(publication);
		}

		return result;
	}

	/**
	 * Filters out all the <code>Publications</code> which have a year greater than or equal to the specified year
	 *
	 * @param publicationGroup Specifies the group of publications on which to apply the filter
	 * @param year Specifies the year of publication
	 *
	 * @return the group of publications which are published before the specified year
	 */
	public static PublicationGroup filterOnYearLessThan(
	    PublicationGroup publicationGroup, int year) {
		Iterator iterator;
		Publication publication;
		PublicationGroup result;

		result = new DefaultPublicationGroup();
		iterator = publicationGroup.getIterator();

		while (iterator.hasNext()) {
			publication = (DefaultPublication)iterator.next();

			if (publication.getYear() < year)
				result.addPublication(publication);
		}

		return result;
	}

	/**
	 * Filters based on the <code>Topic</code> of <code>Publication</code> on the conditions of equality with the specified <code>Topic</code>.  It returns a group of all the <code>Publications</code> which have the specified <code>Topic</code>.
	 *
	 * @param publicationGroup Specifies the group of publications on which to apply the filter
	 * @param topic Specifies the topic of publication to be equated against
	 *
	 * @return the group of publications which belong to the specified topic
	 */
	public static PublicationGroup filterOnTopicEquality(
	    PublicationGroup publicationGroup, Topic topic) {
		Iterator iterator;
		Publication publication;
		PublicationGroup result;

		result = new DefaultPublicationGroup();
		iterator = publicationGroup.getIterator();

		while (iterator.hasNext()) {
			publication = (DefaultPublication)iterator.next();

			if (topic.equals(publication.getTopic()))
				result.addPublication(publication);
		}

		return result;
	}

	/**
	 * Filters based on the <code>Author</code> of <code>Publication</code> on the conditions of equality with the specified <code>Author</code>.  It returns a group of all the <code>Publications</code> which have the specified <code>Author</code>.
	 *
	 * @param publicationGroup Specifies the group of publications on which to apply the filter
	 * @param author Specifies the author of publication to be equated against
	 *
	 * @return the group of publications on which the specified author has collaborated on
	 */
	public static PublicationGroup filterOnAuthorEquality(
	    PublicationGroup publicationGroup, Author author) {
		Iterator iterator;
		Publication publication;
		PublicationGroup result;

		result = new DefaultPublicationGroup();
		iterator = publicationGroup.getIterator();

		while (iterator.hasNext()) {
			publication = (DefaultPublication)iterator.next();

			if ((publication.getAuthors()).containsAuthor(author))
				result.addPublication(publication);
		}

		return result;
	}

	/**
	 * Filters based on the <code>Citation</code> of <code>Publication</code> on the conditions of equality with the specified <code>Topic</code>.  It returns a group of all the <code>Publications</code> which cite the specified <code>Publication</code>.
	 *
	 * @param publicationGroup Specifies the group of publications on which to apply the filter
	 * @param citation Specifies the citation to be equated against
	 *
	 * @return the group of publications which cited the specified publication
	 */
	public static PublicationGroup filterOnCitationEquality(
	    PublicationGroup publicationGroup,
	    Publication citation) {
		Iterator iterator;
		Publication publication;
		PublicationGroup result;
		PublicationGroup tempCitations;

		result = new DefaultPublicationGroup();
		iterator = publicationGroup.getIterator();

		while (iterator.hasNext()) {
			publication = (DefaultPublication)iterator.next();
			tempCitations = publication.getCitations();

			if (tempCitations != null)
				if (tempCitations.containsPublication(citation))
					result.addPublication(publication);
		}

		return result;
	}

	/**
	 * Returns the Group of <code>Publications</code> cited by the specified Group
	 *
	 * @param publicationGroup Specifies the group of publications whose citations are to be collected
	 *
	 * @return the group of citations of the specified group of publications
	 */
	public static PublicationGroup getAllCitedPublications(
	    PublicationGroup publicationGroup) {
		Iterator iterator;
		Publication publication;
		PublicationGroup citationGroup;
		PublicationGroup tempCitations;

		citationGroup = new DefaultPublicationGroup();
		iterator = publicationGroup.getIterator();

		while (iterator.hasNext()) {
			publication = (DefaultPublication)iterator.next();
			tempCitations = publication.getCitations();

			if (tempCitations != null)
				citationGroup.union(tempCitations);
		}

		return citationGroup;
	}

	/**
	 * Returns the Group of <code>Publications</code> cited by the specified Group uptil the specified level of references
	 *
	 * @param publicationGroup Specifies the group of publications whose citations are to be collected
	 * @param numLevels Specifies the number of levels of references to consider
	 *
	 * @return the group of citations of the specified group of publications
	 */
	public static PublicationGroup getAllCitedPublications(
	    PublicationGroup publicationGroup, int numLevels) {
		int i;
		PublicationGroup result;
		PublicationGroup tempPublicationGroup;

		tempPublicationGroup =
			DefaultPublicationUtility.getAllCitedPublications(publicationGroup);
		result = new DefaultPublicationGroup();
		result.initialize(tempPublicationGroup.getPublications());

		for (i = 1; i < numLevels; i++) {
			tempPublicationGroup =
				DefaultPublicationUtility.getAllCitedPublications(tempPublicationGroup);
			result.union(tempPublicationGroup);
		}

		return result;
	}
}

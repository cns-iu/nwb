package edu.iu.iv.modeling.tarl.publication;

import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This abstract class defines the functions in order to filter <code>Publications</code> based on their attributes.  Some of the functions that it fulfils are filtering based on the year of publication, <code>Topic</code>, <code>Authors</code> and citations.
 *
 * @author Jeegar T Maru
 * @see Publication
 * @see PublicationGroup
 */
public abstract class AbstractPublicationUtility {
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
		return null;
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
		return null;
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
		return null;
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
		return null;
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
		return null;
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
		return null;
	}
}

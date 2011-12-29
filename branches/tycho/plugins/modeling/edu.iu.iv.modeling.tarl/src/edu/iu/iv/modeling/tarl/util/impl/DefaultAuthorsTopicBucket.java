package edu.iu.iv.modeling.tarl.util.impl;

import java.util.Collection;
import java.util.Iterator;

import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.author.impl.DefaultAuthor;
import edu.iu.iv.modeling.tarl.author.impl.DefaultAuthorGroup;
import edu.iu.iv.modeling.tarl.topic.Topic;
import edu.iu.iv.modeling.tarl.util.AuthorsTopicBucket;

/**
 * This class is a default implemention of the
 * <code>AuthorsTopicBucketInterface</code>.<br>
 * It represents the group of <code>Authors</code> as an instance of
 * <code>DefaultAuthorGroup</code>. It represents the <code>Topic</code> as
 * an instance of <code>DefaultTopic</code>.
 * 
 * @author Jeegar T Maru
 * @see AuthorGroup
 * @see Topic
 * @see AuthorsTopicBucket
 */
public class DefaultAuthorsTopicBucket implements AuthorsTopicBucket {
    /**
     * Stores the group of <code>Authors</code> as an instance of the
     * <code>DefaultAuthorGroup</code>
     */
    protected AuthorGroup authorGroup;

    /**
     * Stores the common <code>Topic</code> of the bucket as an instance of
     * <code>DefaultTopic</code>
     */
    protected Topic topic;

    /**
     * Creates a new empty instance of <code>AuthorsTopicBucket</code>. The
     * common <code>Topic</code> is set to null.
     */
    public DefaultAuthorsTopicBucket() {
        authorGroup = new DefaultAuthorGroup();
        topic = null;
    }

    /**
     * Initializes the Bucket with <code>Authors</code> from the collection
     * 
     * @param collection
     *        Specifies the collection of authors
     */
    public void initialize(Collection collection) {
        Iterator iterator;

        iterator = collection.iterator();

        while (iterator.hasNext())
            this.addAuthor((DefaultAuthor) iterator.next());
    }

    /**
     * Adds an <code>Author</code> only if his/her <code>Topic</code> is the
     * same as that of the Bucket.
     * 
     * @param author
     *        the author to be added to the bucket
     */
    public void addAuthor(Author author) {
        if (topic == null) {
            topic = author.getTopic();
            authorGroup.addAuthor(author);
        }
        else if (topic.equals(author.getTopic())) {
            authorGroup.addAuthor(author);
        }
    }

    /**
     * Returns the common <code>Topic</code> of the <code>AuthorGroup</code>
     * 
     * @return the common topic of the authors
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * Returns the <code>AuthorGroup</code> that the
     * <code>AuthorsTopicBucket</code> stores
     * 
     * @return the group of authors
     */
    public AuthorGroup getAuthors() {
        return authorGroup;
    }

    /**
     * Partitions the bucket to fixed sized groups of <code>Authors</code> and
     * returns the <code>Collection</code> of these groups. <br>
     * The method uses the static method
     * {@link ExtendedHashSet#partitionSet(Collection collection, int numElements)}
     * available with the <code>ExtendedHashSet</code>.
     * 
     * @param numElements
     *        the number of elements in most of the individual buckets
     * @return a collection of <code>AuthorsTopicBuckets</code>
     */
    public Collection partitionBucket(int numElements) {
        Iterator iterator;
        Collection authorGroupCollection;
        Collection result;
        AuthorsTopicBucket tempAuthorsTopicBucket;

        authorGroupCollection = ExtendedHashSet.partitionSet(authorGroup
                .getAuthors(), numElements);
        result = new ExtendedHashSet();
        iterator = authorGroupCollection.iterator();

        while (iterator.hasNext()) {
            tempAuthorsTopicBucket = new DefaultAuthorsTopicBucket();
            tempAuthorsTopicBucket
                    .initialize((ExtendedHashSet) iterator.next());
            result.add(tempAuthorsTopicBucket);
        }

        return result;
    }
}
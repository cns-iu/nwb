package edu.iu.nwb.util.nwbfile.model;

import java.util.Map;

import com.google.common.base.Predicate;

/**
 * A {@link Predicate} that is called on the set of attributes, which is
 * a {@code Map<String, Object>}.
 * <p>
 * Due to the structure of NWB files, it is safe to assume that a given attribute will always
 * have the same Java type (e.g. Double or String).  However, any attribute is NOT guaranteed
 * to be present in every node or edge.  {@code AttributePredicate} implementations SHOULD
 * check for the existence of any keys they use, before using them, and probably return false
 * if the keys are not present.
 * <p>
 * The {@code Map<String, Object>} passed to the {@link Predicate#apply(Object)} method may
 * or may not be modifiable, but {@code apply()} MUST NOT modify it.
 * <p>
 * The {@code apply()} method SHOULD NOT throw any exceptions, except if there is an
 * unrecoverable error.
 * <p>
 * It is also nice for implementers to override {@code toString()}, with a short description
 * of what the Predicate does, e.g. "AttributePredicate(weight > 3.5)".
 * <p>
 * The map of attributes does not include the id or label of a node, or the source or target of
 * an edge.
 * 
 * @see AttributePredicates
 * @see Predicate#apply(Object)
 * @author thgsmith
 */
public interface AttributePredicate extends Predicate<Map<String, Object>> {
	// no added methods
}

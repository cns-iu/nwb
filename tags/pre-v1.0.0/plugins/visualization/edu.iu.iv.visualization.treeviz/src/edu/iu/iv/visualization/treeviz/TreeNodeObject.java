/*
 * Created on Jan 27, 2005
 */
package edu.iu.iv.visualization.treeviz;

import java.util.Map;

/**
 * @author Shashikant
 */
public interface TreeNodeObject {

    /**
     * @return The attribute map of this TreeNodeObject
     */
    public Map getAttributeMap();

    /**
     * @return The object used as the key into the attribute
     * map for obtaining the string representation.
     */
    public Object getStrRepKey();

    /**
     * @return A string representation of this TreeNodeObject
     */
    public String toString();
}
/*
 * Created on Jan 27, 2005
 */
package edu.iu.iv.visualization.treeviz;

import java.util.Map;

/**
 * @author Shashikant
 */
public class DefaultTreeNodeObject implements TreeNodeObject {

    Map attributeMap;

    Object strRepKey;

    public DefaultTreeNodeObject(Map attributeMap,
            Object stringRepresentationKey) {
        this.attributeMap = attributeMap;
        this.strRepKey = stringRepresentationKey;
    }

    /**
     *  @see TreeNodeObject#getAttributeMap()
     */
    public Map getAttributeMap() {
        return attributeMap;
    }

    /**
     * @see TreeNodeObject#getStrRepKey()
     */
    public Object getStrRepKey() {
        return strRepKey;
    }

    /**
     * @see TreeNodeObject#toString()
     */
    public String toString() {
        return (String) this.attributeMap.get(this.strRepKey);
    }
}
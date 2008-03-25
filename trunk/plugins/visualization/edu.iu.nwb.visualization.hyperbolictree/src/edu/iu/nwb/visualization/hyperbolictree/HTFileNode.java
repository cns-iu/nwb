/*
 * HTFileNode.java
 *
 * www.bouthier.net
 * 2001
 */

package edu.iu.nwb.visualization.hyperbolictree;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;


/**
 * The HTFileNode implements an example of HTNode encapsulating a File.
 */
public class HTFileNode
    extends AbstractHTNode {

    private File      file     = null; // the File encapsulated
    private HashMap children = null; // the children of this node


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param file    the File encapsulated in this node
     */
    public HTFileNode(File file) {
        this.file = file;
        children = new HashMap();

        if (! isLeaf()) {
            String[] tabFichiers = file.list();
            for (int i = 0; i < tabFichiers.length; i++) {
                File fichier = new File(file.getPath() +
                                        File.separator + tabFichiers[i]);
                HTFileNode child = new HTFileNode(fichier);
                addChild(child);
            }

        }
    }


  /* --- Tree management --- */

    /**
     * Add child to the node.
     * 
     * @param child    the HTFileNode to add as a child
     */
    protected void addChild(HTFileNode child) {
        children.put(child.getName(), child);
    }


  /* --- HTNode --- */

    /**
     * Returns the children of this node in an Enumeration.
     * If this node is a file, return a empty Enumeration.
     * Else, return an Enumeration full with HTFileNode.
     *
     * @return    an Iterator containing child values of this node
     */
    public Iterator children() {
        return this.children.values().iterator();
    }

    /**
     * Returns true if this node is not a directory.
     *
     * @return    <CODE>false</CODE> if this node is a directory;
     *            <CODE>true</CODE> otherwise
     */
    public boolean isLeaf() {
        return (! file.isDirectory());
    }
    
    /**
     * Returns the name of the file.
     *
     * @return    the name of the file
     */
    public String getName() {
        return file.getName();
    }


    public Color getColor() {
	return new Color(file.hashCode());
    }
}


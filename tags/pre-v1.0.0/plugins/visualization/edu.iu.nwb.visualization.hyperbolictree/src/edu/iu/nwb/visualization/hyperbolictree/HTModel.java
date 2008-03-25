/*
 * HTModel.java
 *
 * www.bouthier.net
 * 2001
 */

package edu.iu.nwb.visualization.hyperbolictree;


/**
 * The HTModel class implements the model for the HyperTree.
 * It's a tree of HTModelNode and HTModelNodeComposite, each keeping the
 * initial layout of the tree in the Poincarre's Model.
 */
public class HTModel {

    private HTModelNode root   = null; // the root of the tree's model 

    private double      length = 0.3;  // distance between node and children
    private int         nodes  = 0;    // number of nodes


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param root    the root of the real tree 
     */
    HTModel(HTNode root) {
        if (root.isLeaf()) {
            this.root = new HTModelNode(root, this);
        } else {
            this.root = new HTModelNodeComposite(root, this);
        }
        this.root.layoutHyperbolicTree();
    }


  /* --- Accessor --- */

    /**
     * Returns the root of the tree model.
     *
     * @return    the root of the tree model
     */
    HTModelNode getRoot() {
        return root;
    }

    /**
     * Returns the distance between a node and its children
     * in the hyperbolic space.
     *
     * @return    the distance
     */
    double getLength() {
        return length;
    }


  /* --- Number of nodes --- */
  
    /**
     * Increments the number of nodes.
     */
    void incrementNumberOfNodes() {
        nodes++;
    }
    
    /**
     * Returns the number of nodes.
     *
     * @return    the number of nodes
     */
    int getNumberOfNodes() {
        return nodes;
    }

}


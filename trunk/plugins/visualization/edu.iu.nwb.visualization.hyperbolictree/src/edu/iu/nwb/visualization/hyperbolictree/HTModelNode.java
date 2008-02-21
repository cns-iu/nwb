/*
 * HTModelNode.java
 *
 * www.bouthier.net
 * 2001
 */

package edu.iu.nwb.visualization.hyperbolictree;


/**
 * The HTModelNode class implements encapsulation of a HTNode
 * for the model. 
 * It keeps the original euclidian coordinates of the node.
 * It implements the Composite design pattern.
 */
public class HTModelNode {

    private   HTNode               node   = null; // encapsulated HTNode

    protected HTModel              model  = null; // tree model
    protected HTModelNodeComposite parent = null; // parent node

    protected HTCoordE             z      = null; // Euclidian coordinates
    protected double               weight = 1.0;  // part of space taken by this node


  /* --- Constructor --- */

    /**
     * Constructor for root node.
     *
     * @param node     the encapsulated HTNode
     * @param model    the tree model using this HTModelNode
     */
    HTModelNode(HTNode node, HTModel model) {
        this(node, null, model);
    }

    /**
     * Constructor.
     *
     * @param node      the encapsulated HTNode
     * @param parent    the parent node
     * @param model     the tree model using this HTModelNode
     */
    HTModelNode(HTNode node, HTModelNodeComposite parent, HTModel model) {
        this.node = node;
        this.parent = parent;
        this.model = model;
        model.incrementNumberOfNodes();
         
        z = new HTCoordE();
    }


    /**
     * Returns the encapsulated node.
     *
     * @return    the encapsulated node
     */
    HTNode getNode() {
        return node;
    }

  /* --- Name --- */

    /**
     * Returns the name of this node.
     *
     * @return    the name of this node
     */
    String getName() {
        return node.getName();
    }


  /* --- Weight Managment --- */

    /**
     * Returns the weight of this node.
     *
     * @return    the weight of this node
     */
    double getWeight() {
        return weight;
    }


  /* --- Tree management --- */

    /**
     * Returns the parent of this node.
     *
     * @return    the parent of this node
     */
    HTModelNodeComposite getParent() {
        return parent;
    }

    /**
     * Returns <CODE>true</CODE> if this node
     * is not an instance of HTModelNodeComposite.
     *
     * @return    <CODE>true</CODE>
     */
    boolean isLeaf() {
        return true;
    }


  /* --- Coordinates --- */

    /**
     * Returns the coordinates of this node.
     * Thoses are the original hyperbolic coordinates, 
     * without any translations.
     * WARNING : this is NOT a copy but the true object
     * (for performance).
     *
     * @return    the original hyperbolic coordinates
     */
    HTCoordE getCoordinates() {
        return z;
    }


  /* --- Hyperbolic layout --- */

    /**
     * Layouts the nodes in the hyperbolic space.
     */
    void layoutHyperbolicTree() {
        this.layout(0.0, Math.PI, model.getLength());
    }

    /**
     * Layout this node in the hyperbolic space.
     * First set the point at the right distance,
     * then translate by father's coordinates.
     * Then, compute the right angle and the right width.
     *
     * @param angle     the angle from the x axis (bold as love)
     * @param width     the angular width to divide, / 2
     * @param length    the parent-child length
     */
    void layout(double angle, double width, double length) {
        // Nothing to do for the root node
        if (parent == null) {
            return;
        }
        
        HTCoordE zp = parent.getCoordinates();

        // We first start as if the parent was the origin.
        // We still are in the hyperbolic space.
        z.x = length * Math.cos(angle);
        z.y = length * Math.sin(angle);

        // Then translate by parent's coordinates
        z.translate(zp);
    } 


  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = getName() +
                        "\n\t" + z +
                        "\n\tWeight = " + weight; 
        return result;
    }

}


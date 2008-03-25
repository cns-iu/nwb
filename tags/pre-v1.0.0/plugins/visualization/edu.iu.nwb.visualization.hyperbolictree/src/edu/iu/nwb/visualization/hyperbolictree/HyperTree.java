/*
 * HyperTree.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2001 Christophe Bouthier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package edu.iu.nwb.visualization.hyperbolictree;


/**
 * The HyperTree class implements an hyperbolic tree representation for data.
 * <P>
 * An HyperTree is build from hierarchical data, given as a tree
 * of HTNode. So, the first parameter to give to build an HyperTree
 * is the HTNode which is the root of the to-be-represented tree.
 * The tree to be displayed by the HyperTree should be build before the call
 * to HyperTree, that is the root node should return children
 * when the children() method is called.
 * <P>
 * You can get a HTView (herited from JView) containing the HyperTree by calling
 * getView().
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
public class HyperTree {

    private HTModel model = null; // the model of the tree for the HyperTree


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param root    the root of the tree to be represented;
     *                could not be <CODE>null</CODE>
     */
    public HyperTree(HTNode root) {
        model = new HTModel(root);
    }


  /* --- View --- */

    /**
     * Returns a view of the hypertree.
     *
     * @return              the desired view of the hypertree
     */
    public SwingHTView getView() {
        return new SwingHTView(model);
    }


    /** Returns an AWT compatible view of the hypertree.
     *
     * @return a java.awt.Panel view of the hypertree.
     */
     public AWTHTView getAWTView() {
 	return new AWTHTView(model);
     }

}


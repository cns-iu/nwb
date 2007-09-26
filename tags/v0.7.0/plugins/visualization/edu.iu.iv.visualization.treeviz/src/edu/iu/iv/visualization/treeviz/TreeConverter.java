/*
 * Created on Jan 27, 2005
 */
package edu.iu.iv.visualization.treeviz;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Converts a Prefuse (API) Tree to a Java Swing API TreeModel recursively.
 * TODO: Its obvious that such a class would be immensely useful in many
 * situtations so create a set of interfaces for Converters.
 * 
 * @author Shashikant
 */
public class TreeConverter {

    /**
     * Converts a {@link Tree} to a {@link TreeModel}. Attributes are copied
     * unmodified. The object that set for each node of the Java Tree is
     * {@link DefaultTreeNodeObject}. The {@link DefaultTreeNodeObject#toString()} method
     * of the TreeNodeObject is set to return the <code>label</code> attribute
     * of the map passed in.
     * 
     * @param tree The input Prefuse API tree.
     * @return The Java Swing TreeModel.
     */
    public TreeModel convert(Tree tree) {
        javax.swing.tree.TreeNode rootNode = convertSubTree(null, tree
                .getRoot());
        TreeModel treeModel = new DefaultTreeModel(rootNode);
        return treeModel ;
    }

    /**
     * Recursive method that converts a Prefuse Tree to a Java Swing Tree. 
     * 
     * @param newCurrentSubTreeRootNode The converted root node of the sub-tree currently being processed.
     * @param oldCurrentSubTreeRootNode The unconverted root node of the sub-tree currently being processed.  
     * @return The converted root node of the sub-tree currently being processed, with the
     * sub-tree of its children also converted.
     */
    private MutableTreeNode convertSubTree(
            MutableTreeNode newCurrentSubTreeRootNode,
            TreeNode oldCurrentSubTreeRootNode) {
        // if this thing has no children
        if (oldCurrentSubTreeRootNode.getChildCount() == 0) {
            return convertTreeNode(oldCurrentSubTreeRootNode);
        }
        // if there are children, then collect all the children and convert
        // each subtree
        else {
            // if the node passed in is null, it means
            // that we are starting off at the root.
            // In that case, create a new convertedTreeNode
            if (newCurrentSubTreeRootNode == null) {
                newCurrentSubTreeRootNode = convertTreeNode(oldCurrentSubTreeRootNode);
            }
            // if the node passed in is not null, it means that it is
            // a converted version of the root of the subtree (the converted
            // root node of the current subtree.
            // what we need to do now is:
            // get the children of the current subtree root
            // convert each subtree of which the children are roots
            // return the current converted rootnode.
            
            // get the number of children
            int numChildren = oldCurrentSubTreeRootNode.getChildCount();
            for (int i = 0; i < numChildren; ++i) {
                // take each child
                TreeNode oldChildTreeNode = oldCurrentSubTreeRootNode.getChild(i) ;
                // convert the child to the new treenode
                MutableTreeNode newChildTreeNode = convertTreeNode(oldChildTreeNode) ;
                // convert the subtree of the child and add it to the current root
                newCurrentSubTreeRootNode.insert(convertSubTree(newChildTreeNode, oldChildTreeNode), i) ;
            }
            // this will be the root after everything is done.
            return newCurrentSubTreeRootNode ;
        }
    }

    private MutableTreeNode convertTreeNode(TreeNode treeNode) {
        return new DefaultMutableTreeNode(new DefaultTreeNodeObject(treeNode
                .getAttributes(), "label"));
    }
}
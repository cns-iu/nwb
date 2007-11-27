package edu.iu.nwb.converter.prefuseTreeBetaAlpha;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import java.util.Iterator;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tree;
import prefuse.data.Tuple;


/**
 * Takes in a prefuse.data.Tree, and returns a edu.berkeley.guir.prefuse.graph.Tree.
 * To build a prefuse Alpha Tree based on an existing prefuse Beta Tree 
 * @author Heng Zhang
 */

public class PrefuseTreeBetaAlphaConverter
{
	private edu.berkeley.guir.prefuse.graph.Tree AlphaTree;
	
    public PrefuseTreeBetaAlphaConverter()
    {
        AlphaTree = new DefaultTree();
    }

    /**
     *  This method will read a beta tree and set the root node of alpha tree
     */
    public edu.berkeley.guir.prefuse.graph.Tree getPrefuseBetaTree(Tree BetaTree)
    {
        TreeNode rootNodeAlhpha = new DefaultTreeNode();
        Node rootNodeBeta = BetaTree.getRoot();
        merge(rootNodeBeta, rootNodeAlhpha);
        AlphaTree.setRoot(rootNodeAlhpha);
        buildTreeAlpha(rootNodeAlhpha, rootNodeBeta);
        return AlphaTree;
    }

    /**
     *  This method will copy each node from beta to alpha
     */
    private void merge(Tuple prefuseTuple, Entity prefuseEntity)
    {
        Schema schema = prefuseTuple.getSchema();
        int columns = schema.getColumnCount();
        for(int ii = 0; ii < columns; ii++)
            if(prefuseTuple.get(ii) != null)
                prefuseEntity.setAttribute(schema.getColumnName(ii), prefuseTuple.get(ii).toString());

    }

    /**
     *  This method will traverse all child nodes in beta tree and build alpha tree
     */
    private void buildTreeAlpha(TreeNode nodeAlpha, Node nodeBeta)
    {
        Iterator iterBeta = nodeBeta.children();
        Iterator iter = nodeBeta.children();
        if(iter == null)
            return;
        edu.berkeley.guir.prefuse.graph.Edge edge;
        for(; iter.hasNext(); nodeAlpha.addChild(edge))
        {
            TreeNode nodeAlphaChild = new DefaultTreeNode();
            Node nodeBetaChild = (Node)iter.next();
            merge(nodeBetaChild, nodeAlphaChild);
            edge = new DefaultEdge(nodeAlpha, nodeAlphaChild);
        }

        Iterator iterAlpha = nodeAlpha.getChildren();
        for(; iterBeta.hasNext(); buildTreeAlpha((TreeNode)iterAlpha.next(), (Node)iterBeta.next()));
    }

    
}
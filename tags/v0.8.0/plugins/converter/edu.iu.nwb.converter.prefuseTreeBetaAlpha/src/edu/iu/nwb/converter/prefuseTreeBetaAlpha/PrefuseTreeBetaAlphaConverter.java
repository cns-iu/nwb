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
import java.util.*;




class NodePair
{
	private TreeNode nodeAlhpha;
	private Node     nodeBeta;
	
	public NodePair(TreeNode tn, Node nd)
	{
		nodeAlhpha = tn;
		nodeBeta   = nd;
	}
	
	TreeNode getNodeAlpha()
	{
		return nodeAlhpha;
	}
	
	Node getNodeBeta()
	{
		return nodeBeta;
	}
	
}


/**
 * Takes in a prefuse.data.Tree, and returns a edu.berkeley.guir.prefuse.graph.Tree.
 * To build a prefuse Alpha Tree based on an existing prefuse Beta Tree 
 * @author Heng Zhang
 */
public class PrefuseTreeBetaAlphaConverter
{
	private edu.berkeley.guir.prefuse.graph.Tree alphaTree;
	private ArrayList alChildQue;
	
    public PrefuseTreeBetaAlphaConverter()
    {
        alphaTree = new DefaultTree();
        alChildQue = new ArrayList(); 
    }
    

    /**
     *  This method will read a beta tree and set the root node of alpha tree
     */
    public edu.berkeley.guir.prefuse.graph.Tree getPrefuseBetaTree(Tree betaTree)
    {
    	
    	TreeNode rootNodeAlhpha = new DefaultTreeNode();
        Node rootNodeBeta = betaTree.getRoot();
        merge(rootNodeBeta, rootNodeAlhpha);
        alphaTree.setRoot(rootNodeAlhpha);
        alChildQue.add(new NodePair(rootNodeAlhpha,rootNodeBeta));
        buildTreeAlpha(rootNodeAlhpha, rootNodeBeta);
        return alphaTree;
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
    private void buildTreeAlpha(TreeNode rootAlpha, Node rootBeta)
    {
        TreeNode currentAlphaNode = rootAlpha;
        Node     currentBetaNode  = rootBeta;
        
       while(!alChildQue.isEmpty()){ 
           Iterator iter = currentBetaNode.children();
           if(iter == null){
        	   alChildQue.remove(0);
        	   continue;
           }      
           edu.berkeley.guir.prefuse.graph.Edge edge;
           for(; iter.hasNext();)
           {
        	   TreeNode nodeAlphaChild = new DefaultTreeNode();
               Node nodeBetaChild = (Node)iter.next();
               merge(nodeBetaChild, nodeAlphaChild);
               edge = new DefaultEdge(currentAlphaNode, nodeAlphaChild);
               currentAlphaNode.addChild(edge);
               alChildQue.add(new NodePair(nodeAlphaChild,nodeBetaChild));
           }
           if (!alChildQue.isEmpty())
           {
        	   alChildQue.remove(0);
           }
           if (!alChildQue.isEmpty()){
               currentBetaNode = ((NodePair)alChildQue.get(0)).getNodeBeta();
               currentAlphaNode = ((NodePair)alChildQue.get(0)).getNodeAlpha();
           }
           
        }
    }
}
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

/**
 * Takes in a prefuse.data.Tree, and returns a
 * edu.berkeley.guir.prefuse.graph.Tree. To build a prefuse Alpha Tree based on
 * an existing prefuse Beta Tree
 * 
 * @author Heng Zhang
 */
public class PrefuseTreeBetaAlphaConverter {
	private edu.berkeley.guir.prefuse.graph.Tree alphaTree;
	private ArrayList childQueue;

	public PrefuseTreeBetaAlphaConverter() {
		this.alphaTree = new DefaultTree();
		this.childQueue = new ArrayList();
	}

	/**
	 * This method will read a beta tree and set the root node of alpha tree
	 */
	public edu.berkeley.guir.prefuse.graph.Tree getPrefuseBetaTree(Tree betaTree) {
		TreeNode rootNodeAlhpha = new DefaultTreeNode();
		Node rootNodeBeta = betaTree.getRoot();
		merge(rootNodeBeta, rootNodeAlhpha);
		alphaTree.setRoot(rootNodeAlhpha);
		childQueue.add(new NodePair(rootNodeAlhpha, rootNodeBeta));
		buildTreeAlpha(rootNodeAlhpha, rootNodeBeta);
		return alphaTree;
	}

	/**
	 * This method will copy each node from beta to alpha
	 */
	private void merge(Tuple prefuseTuple, Entity prefuseEntity) {
		Schema schema = prefuseTuple.getSchema();
		int columns = schema.getColumnCount();
		for (int ii = 0; ii < columns; ii++)
			if (prefuseTuple.get(ii) != null)
				prefuseEntity.setAttribute(schema.getColumnName(ii),
						prefuseTuple.get(ii).toString());

	}

	/**
	 * This method will traverse all child nodes in beta tree and build alpha
	 * tree
	 */
	private void buildTreeAlpha(TreeNode rootAlpha, Node rootBeta) {
		TreeNode currentAlphaNode = rootAlpha;
		Node currentBetaNode = rootBeta;

		while (!childQueue.isEmpty()) {
			Iterator iter = currentBetaNode.children();
			if (iter == null) {
				childQueue.remove(0);
				continue;
			}
			edu.berkeley.guir.prefuse.graph.Edge edge;
			for (; iter.hasNext();) {
				TreeNode nodeAlphaChild = new DefaultTreeNode();
				Node nodeBetaChild = (Node) iter.next();
				merge(nodeBetaChild, nodeAlphaChild);
				edge = new DefaultEdge(currentAlphaNode, nodeAlphaChild);
				currentAlphaNode.addChild(edge);
				childQueue.add(new NodePair(nodeAlphaChild, nodeBetaChild));
			}
			if (!childQueue.isEmpty()) {
				childQueue.remove(0);
			}
			if (!childQueue.isEmpty()) {
				currentBetaNode = ((NodePair) childQueue.get(0)).getNodeBeta();
				currentAlphaNode = ((NodePair) childQueue.get(0))
						.getNodeAlpha();
			}

		}
	}
	
	
	private class NodePair {
		private TreeNode nodeAlpha;
		private Node nodeBeta;
	
		public NodePair(TreeNode tn, Node nd) {
			this.nodeAlpha = tn;
			this.nodeBeta = nd;
		}
	
		TreeNode getNodeAlpha() {
			return nodeAlpha;
		}
	
		Node getNodeBeta() {
			return nodeBeta;
		}	
	}
}
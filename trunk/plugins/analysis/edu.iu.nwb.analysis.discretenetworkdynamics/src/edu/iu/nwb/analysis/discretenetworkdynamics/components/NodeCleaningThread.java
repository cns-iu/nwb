package edu.iu.nwb.analysis.discretenetworkdynamics.components;

import java.util.Iterator;
import java.util.Stack;

import prefuse.data.CascadedTable;
import prefuse.data.Graph;
import prefuse.data.Node;

public class NodeCleaningThread extends Thread{
	private static final String preOrderColumn = "preOrder";
	private static final String strongComponentColumn = "strongComponentID";
	private Graph stateGraph;
	private int strongComponents = 0;
	private Stack componentMembers = new Stack();

	public NodeCleaningThread(Graph stateGraph){

		this.stateGraph = stateGraph;

	}

	public void run(){
		int nodeCount = this.stateGraph.getNodeCount();
		Stack firstStack = new Stack();
		firstStack.setSize(nodeCount);
		Stack secondStack = new Stack();
		secondStack.setSize(nodeCount);
		Node n;

		CascadedTable ct = new CascadedTable(this.stateGraph.getNodeTable());
		ct.addColumn(strongComponentColumn, int.class, new Integer(-1));
		ct.addColumn(preOrderColumn, int.class, new Integer(-1));



		Integer testCount = new Integer(0);

		int[] id = new int[nodeCount];
		int[] pre = new int[nodeCount];

		java.util.Arrays.fill(id, -1);
		java.util.Arrays.fill(pre, -1);

		for (int v = nodeCount-1; v >= 0; v--){
			n = this.stateGraph.getNode(v);
			if(n.getString("label") == null){
				this.stateGraph.getNodeTable().removeRow(n.getRow());			
			}
			else if(ct.getInt(v, NodeCleaningThread.preOrderColumn) == -1){
				recursiveStrongComponentCalculation(this.stateGraph,this.stateGraph.getNode(v),ct,firstStack,secondStack,testCount);
			}
		}
	}

	private void recursiveStrongComponentCalculation(final Graph g, Node n, CascadedTable ct, Stack firstStack, Stack secondStack, Integer count){
		int v;
		count = new Integer(count.intValue()+1);
		ct.setInt(n.getRow(), preOrderColumn, count.intValue());
		//preOrder[vertex] = count++;
		firstStack.push(new Integer(n.getRow()));
		secondStack.push(new Integer(n.getRow()));

		for(Iterator it = n.outNeighbors(); it.hasNext();){
			int outNode = ((Node)it.next()).getRow();
			if(ct.getInt(outNode, preOrderColumn) == -1) 
				recursiveStrongComponentCalculation(g, g.getNode(outNode),ct,firstStack,secondStack, count);
			else if (ct.getInt(outNode, strongComponentColumn)  == -1){
				while (ct.getInt(((Integer)secondStack.peek()).intValue(),preOrderColumn) > ct.getInt(outNode, preOrderColumn)){ 
					secondStack.pop();

				}

			}
		}

		if(((Integer)secondStack.peek()).intValue() == n.getRow()){ 
			secondStack.pop();
		}
		else{
			return;
		}


		do {
			v = ((Integer)firstStack.pop()).intValue();
			
			componentMembers.push(new Integer(v));
			ct.setInt(v, strongComponentColumn, this.strongComponents);
		} while (n.getRow() != v);

		
		if(componentMembers.size() > 2){
			while(!componentMembers.isEmpty()){
				Node n1 = this.stateGraph.getNode(((Integer)componentMembers.pop()).intValue());
				n1.set("attractor", new Integer(11));
			}
		}
		if(componentMembers.size() == 1){
			Node n1 = this.stateGraph.getNode(((Integer)componentMembers.pop()).intValue());
			if(n1.getInt("attractor") == 1){
				n1.set("attractor", new Integer(2));
			}
		}
		if(componentMembers.size() == 2){
			while(!componentMembers.isEmpty()){
				Node n1 = this.stateGraph.getNode(((Integer)componentMembers.pop()).intValue());
				n1.set("attractor", new Integer(13));
			}
		}

		componentMembers = new Stack();

		
		this.strongComponents++;

	}
}

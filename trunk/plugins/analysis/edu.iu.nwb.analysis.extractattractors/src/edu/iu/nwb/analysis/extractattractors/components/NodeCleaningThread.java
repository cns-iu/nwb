package edu.iu.nwb.analysis.extractattractors.components;

import java.util.Iterator;
import java.util.Stack;

import prefuse.data.CascadedTable;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;

public class NodeCleaningThread extends Thread{
	private static final String preOrderColumn = "preOrder";
	private static final String strongComponentColumn = "strongComponentID";
	private Graph stateGraph;
	private final Table originalTable;
	private Table attractorTable;
	private final String columnName;
	private int strongComponents = 0;
	private Stack componentMembers = new Stack();

	public NodeCleaningThread(Graph stateGraph, final Table originalTable, final String columnName){
		super();
		this.stateGraph = stateGraph;
		this.originalTable =originalTable;
		this.columnName = columnName;
		constructAttractorTable(originalTable,columnName,true);

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


		if(componentMembers.size() > 1){
			int componentSize = componentMembers.size();
			while(!componentMembers.isEmpty()){
				Node n1 = this.stateGraph.getNode(((Integer)componentMembers.pop()).intValue());
				n1.setInt("attractor", componentSize);
				annotateAttractorTable(n1.getString("label"),true);
			}
		}
		if(componentMembers.size() == 1){
			Node n1 = this.stateGraph.getNode(((Integer)componentMembers.pop()).intValue());
			for(Iterator edges = n1.outEdges(); edges.hasNext();){
				Edge e = (Edge)edges.next();
				if(e.getSourceNode() == e.getTargetNode()){
					n1.setInt("attractor", 1);
					annotateAttractorTable(n1.getString("label"),true);
				}
			}
		}
		componentMembers = new Stack();
		this.strongComponents++;
	}

	private void constructAttractorTable(Table orgTable, String labelColumn, boolean isHorizontal){
		this.attractorTable = new Table();
		if(!isHorizontal){
			attractorTable.addColumn("Label", String.class);
		}
		for(int i = 0; i < orgTable.getRowCount(); i++){
			if(isHorizontal){
				if(labelColumn == null || labelColumn.equals("")){
					attractorTable.addColumn("x" + (i+1), int.class);
				}else{
					attractorTable.addColumn(orgTable.getString(i, labelColumn), int.class);
				}
			}else{
				int rowNumber = attractorTable.addRow();
				if(labelColumn == null || labelColumn.equals("")){
					attractorTable.setString(rowNumber, "Label", "x"+(i+1));
				}
				else{
					attractorTable.setString(rowNumber, "Label", orgTable.getString(i, labelColumn));
				}
			}
		}

	}
	
	private void annotateAttractorTable(String value, boolean isHorizontal){
		String[] discreteValues = value.split("\\s+");
		if(isHorizontal){
			int rowNumber = this.attractorTable.addRow();
			for(int i = 0; i < discreteValues.length; i++){
				this.attractorTable.setInt(rowNumber, i, new Integer(discreteValues[i]).intValue());
			}
		}else{
			this.attractorTable.addColumn(new Integer(this.attractorTable.getColumnCount()).toString(), int.class);
			for(int i = 0; i < discreteValues.length; i++){
				this.attractorTable.setInt(0, this.attractorTable.getColumnCount()-1, new Integer(discreteValues[i]).intValue());
			}
		}
	}
	
	public Table getAttractorTable(){
		return this.attractorTable;
	}
	
}

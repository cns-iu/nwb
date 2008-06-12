package edu.iu.nwb.analysis.extractattractors.components;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;

public class ExtractAttractorBasins {

	ArrayList attractorBasins = new ArrayList();
	ArrayList attractorSizes = new ArrayList();
	ArrayList attractorTables = new ArrayList();
	ArrayList attractorRobustnessFiles = new ArrayList();

	public ExtractAttractorBasins extractAttractorBasins(Graph stateSpaceGraph, int nodeStates, boolean isBoolean, final Table originalTable, final String labelColumn) throws InterruptedException{

		weakComponentCalculation(stateSpaceGraph,originalTable,labelColumn);

		return this;
	}
	
	public File[] getBasins(){
		File[] basins = new File[this.attractorBasins.size()];
		
		return (File[])this.attractorBasins.toArray(basins);
	}
	
	public int[] getBasinSizes(){
		int[] sizes = new int[this.attractorSizes.size()];
		for(int i = 0; i < this.attractorSizes.size(); i++){
			sizes[i] = ((Integer)this.attractorSizes.get(i)).intValue();
		}
		return sizes;
	}
	
	public Table[] getAttractors(){
		Table[] attractors = new Table[this.attractorTables.size()];
		return (Table[])this.attractorTables.toArray(attractors);
	}

	public void weakComponentCalculation(final Graph grph, Table table, String label) throws InterruptedException{

		HashSet seenNodes = new HashSet();
		for(Iterator it = grph.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			Integer i = new Integer(n.getRow());
			if(!seenNodes.contains(i) && n.getString("label") != null){
				LinkedHashSet tree = undirectedDepthFirstSearch(grph, i);
				seenNodes.addAll(tree);
				BasinConstructorThread bct = new BasinConstructorThread(grph,table,label,tree);
				bct.start();
				attractorBasins.add(bct);
			}			
		}
		
		for(int i = 0; i < attractorBasins.size(); i++){
			BasinConstructorThread bct = (BasinConstructorThread)attractorBasins.get(i);
			bct.join();
			this.attractorBasins.set(i, bct.getAttractorBasin());
			this.attractorSizes.add(new Integer(bct.getBasinSize()));
			this.attractorTables.add(bct.getAttractorTable());
		}

	}

	protected static LinkedHashSet undirectedDepthFirstSearch(final Graph g, Integer n){

		LinkedHashSet nodeSet = new LinkedHashSet();
		Integer nodeNumber;
		
			nodeNumber = new Integer(n.intValue());
			runUDFS(g,nodeNumber, nodeSet);


		return nodeSet;
	}

	private static void runUDFS(final Graph g, Integer n, LinkedHashSet pre){
			LinkedList q = new LinkedList();

		Node nd;
		Integer nodeRow;
		Integer nodeNumber;
		Edge edg;
		Node nd2;
		q.addLast(new Integer(n.intValue()));
		while(!q.isEmpty()){
			nodeRow = new Integer(((Integer)q.removeLast()).intValue());
			if(!pre.contains(nodeRow)){
				nd = g.getNode(nodeRow.intValue());
				pre.add(nodeRow);

				for(Iterator it = nd.edges(); it.hasNext();){
					edg = (Edge)it.next();
					nd2 = edg.getTargetNode();
					nodeNumber = new Integer(nd2.getRow());
					if(!pre.contains(nodeNumber))
						q.add(nodeNumber);
					nd2 = edg.getSourceNode();
					nodeNumber = new Integer(nd2.getRow());
					if(!pre.contains(nodeNumber))
						q.add(nodeNumber);

				}
			}
		}
		nodeRow = null;
		nodeNumber = null;
		q = null;
		edg = null;
		nd2 = null;

	}

}

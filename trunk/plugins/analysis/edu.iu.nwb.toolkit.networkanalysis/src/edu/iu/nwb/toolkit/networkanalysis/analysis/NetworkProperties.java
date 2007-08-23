package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;


public class NetworkProperties {
	private Graph graph;
	private boolean stronglyConnected = false;
	private boolean connected = false;
	private boolean unilaterallyConnected = false;
	private int numberOfCycles = 0;
	private int numberOfSelfLoops = 0;

	public NetworkProperties(prefuse.data.Graph graph) {
		this.graph = graph ;
		//System.out.println(this.graph.getNodeCount());
		long time = new Date().getTime();
		calculateConnectedness();
		System.out.println("There are: " + this.numberOfSelfLoops + " self-loops");
		time = new Date().getTime() - time;
		System.out.println("Ran in: " + time);

	}



	public Graph getGraph() {
		return this.graph;
	}

	public int getNumNodes() {
		return this.graph.getNodeCount();
	}

	public boolean isConnected() {
		return this.connected;
	}

	public boolean isDirected() {
		//return PredicateUtils.enforcesDirected(this.graph);
		return false;
	}

	public boolean hasSelfLoops() {
		//return GraphProperties.containsSelfLoops(this.graph);
		return false;
	}

	public boolean hasParallelEdges() {
		//return GraphProperties.containsParallelEdges(this.graph);
		return false;
	}

	public int getNumEdges() {
		return this.graph.getEdgeCount();
	}

	public int getNumLoops() {
		int numLoops = 0;
		for (Iterator iterator = this.graph.edges(); iterator
		.hasNext();) {
			Edge e = (Edge) iterator.next();
			Node source = e.getSourceNode();
			Node target = e.getTargetNode();
			if (source.equals(target))
				++numLoops;
		}
		return numLoops;
	}

	//private static Tree[] 

	private ArrayList uDFS(final Graph g, Integer n){

		//int order = g.getNodeCount();
		ArrayList preOrder = new ArrayList();
		//ArrayList postOrder = new ArrayList();
		//Map discoveryNumbers = new HashMap();
		runUDFS(g,n, preOrder);
		//int visited = 0;
		//runDFS(g,n,preOrder,postOrder, false);
		//System.out.println(visited);
		//System.out.println(preOrder.size());
		//return preOrder;
		return preOrder;
	}

	private static ArrayList dDFS(final Graph g, Integer n){
		ArrayList preOrder = new ArrayList();
		runDDFS(g,n,preOrder);
		return preOrder;
	}

	private void runUDFS(final Graph g, Integer n, ArrayList pre){
		Queue q = new LinkedList();
		q.add(g.getNode(n.intValue()));
		int selfLoops = 0;
		while(!q.isEmpty()){
			Node nd = (Node)q.poll();
			Integer i = new Integer(nd.getRow());
			if(!pre.contains(i)){
				//  System.out.println(nd);
				pre.add(i);


				for(Iterator it = nd.neighbors(); it.hasNext();){
					Node nd2 = (Node)it.next();
					//System.out.println(nd + " " + nd2);
					if(nd2.equals(nd))
						selfLoops++;
					q.add(nd2);

				}
			}
		}
		this.numberOfSelfLoops = selfLoops/2;
		//System.out.println(this.numberOfSelfLoops);
		//return new Integer(selfLoops);
	}

	private static void runDDFS(final Graph g, Integer n, ArrayList pre){
		Queue q = new LinkedList();
		q.add(g.getNode(n.intValue()));
		while(!q.isEmpty()){
			Node nd = (Node) q.poll();
			Integer i = new Integer(nd.getRow());
			if(!pre.contains(i)){
				pre.add(i);
				//System.out.println(nd);

				for(Iterator it = nd.outEdges(); it.hasNext();){
					//Node nd = ((Edge)it.next()).getTargetNode();
					Node nd2 = ((Edge)it.next()).getTargetNode();

					q.add(nd2);	    		
				}

			}
		}
	}

	private void calculateConnectedness(){
		componentForest weak = new componentForest();
		weak.weakComponentCalculation(this.graph);
		if(weak.getMaximumConnectedNodes() == this.graph.getNodeCount()){
			this.connected = true;
		}

		weak.strongComponentCalculation(this.graph);
		if(weak.maxStrongConnectedNodes == this.graph.getNodeCount())
			this.stronglyConnected = true;

	}

	private class componentForest{
		//Map forest;
		int weakComponentClusters = 0;
		int maxWeakConnectedNodes = 0;

		int strongComponentClusters = 0;
		int maxStrongConnectedNodes = 0;

		int weakNodes;
		int strongNodes;

		BigDecimal averageWeakConnectedness = new BigDecimal(0.0);
		BigDecimal averageStrongConnectedness = new BigDecimal(0.0);

		public componentForest(){

		}


		public int getMaximumConnectedNodes(){
			return this.maxWeakConnectedNodes;
		}

		public double getAverageConnectedness(){
			return averageWeakConnectedness.doubleValue();
		}

		public int getComponentClusters(){
			return this.weakComponentClusters;
		}

		public void weakComponentCalculation(final Graph grph){

			ArrayList seenNodes = new ArrayList();
			int totalNodes = 0;
			int maxNodes = 0;
			for(Iterator it = grph.nodes(); it.hasNext();){
				Node n = (Node)it.next();
				Integer i = new Integer(n.getRow());
				if(!seenNodes.contains(i)){

					ArrayList tree = uDFS(grph, i);

					if(seenNodes.containsAll(tree)){

					}
					else{

						tree.removeAll(seenNodes);

						seenNodes.addAll(tree);
						if(tree.size() > maxNodes)
							maxNodes = tree.size();
						totalNodes += tree.size();
						this.weakComponentClusters++;

					}
				}
			}
			this.weakNodes = totalNodes;
			System.out.println("Contains " + this.weakNodes + " nodes");
			this.averageWeakConnectedness = new BigDecimal(((double)this.weakNodes/this.weakComponentClusters));
			System.out.println("Average Connectedness is: " + this.averageWeakConnectedness);
			this.maxWeakConnectedNodes = maxNodes;
			System.out.println("Largest Connected Component is: " + this.maxWeakConnectedNodes);

			System.out.println("There are " + this.weakComponentClusters + " component clusters");


		}

		private void strongComponentAssignment(final ArrayList connectedNodes){
			for(int ii = 0; ii < connectedNodes.size(); ii++){
				//System.out.println("\t\t"+connectedNodes.get(ii));
			}
			if(connectedNodes.size() > this.maxStrongConnectedNodes)
				this.maxStrongConnectedNodes = connectedNodes.size();
			this.strongNodes += connectedNodes.size();	
		}
		
		private void testPrinter(final ArrayList first, final ArrayList second, final TreeSet seen, final ArrayList adding){
			String s1 = "";
			String s2 = "";
			String s3 = "";
			String s4 = "";
			String s5 = "";
			int x = 0;
			System.out.println("First Pass\tSecond Pass\tSeen?\tSeen Nodes\tAdding");
			if(second != null){
				x = Math.max(first.size(), Math.max(second.size(), seen.size()));
			}
			else if(first != null){
				x = Math.max(first.size(), seen.size());
			}
			else
				x = seen.size();
			
			if(adding != null){
				x = Math.max(x, adding.size());
			}
			int ii = 0;
			for(Iterator it3 = seen.iterator(); it3.hasNext();){
				if(first != null && ii < first.size()){
					s1 = first.get(ii).toString();
					s3 = new Boolean(seen.contains(first.get(ii))).toString();
				}
				else{
					s1 = s3 = "";
				}
				if(second != null && ii < second.size()){
					s2 = second.get(ii).toString();
				}
				else
					s2 = "";
				if(adding != null && ii < adding.size()){
					s5 = adding.get(ii).toString();
				}
				else
					s5 = "";
				s4 = it3.next().toString();

				System.out.println(s1 +"\t\t" + s2 + "\t\t" + s3 + "\t" + s4+"\t\t"+s5);

				ii++;
			}
			for(int iii = ii;iii < x; iii++){
				if(first != null && iii < first.size()){
					s1 = first.get(iii).toString();
					s3 = new Boolean(seen.contains(first.get(iii))).toString();
				}
				else{
					s1 = s3 = "";
				}
				if(second != null && iii < second.size()){
					s2 = second.get(iii).toString();
				}
				else
					s2 = "";
				if(adding != null && iii < adding.size()){
					s5 = adding.get(iii).toString();
				}
				else
					s5 = "";

				s4 = "";

				System.out.println(s1 +"\t\t" + s2 + "\t\t" + s3 + "\t" + s4 + "\t\t" + s5);

			}
		}

		private void strongComponentArrayCalc(TreeSet seenNodes, final Graph g1, final Graph g2){
			ArrayList strongTemp;		                    		 
			ArrayList secondPass = null;
			ArrayList firstPass = null;
			
			
			for(Iterator it = g1.nodes(); it.hasNext();){
				
				
				
				firstPass = null;
				secondPass = null;
				Node n = (Node)it.next();
				Integer i = new Integer(n.getRow());
				
				if(!seenNodes.contains(i)){
					firstPass = dDFS(g1, i);
					

					if(firstPass.size() == 1){
						strongComponentAssignment(firstPass);
						seenNodes.addAll(firstPass);
						this.strongComponentClusters++;
						//testPrinter(firstPass, secondPass, seenNodes, firstPass);
					}else{

					//for(int j = 0; j < firstPass.size(); j++){
					//	Integer j2 = (Integer)firstPass.get(j);

					//	if(!seenNodes.contains(j2)){
							//System.out.println("\t"+j2);
							secondPass = dDFS(g2,i);
							//System.out.println(j2 + " " + secondPass.size());
							if(secondPass.size() == 1){
								strongComponentAssignment(secondPass);
								seenNodes.addAll(secondPass);
								this.strongComponentClusters++;
								//testPrinter(firstPass, secondPass, seenNodes, secondPass);
							}
							else{
							//firstPass.removeAll(seenNodes);
							//secondPass.removeAll(seenNodes);


							if(secondPass.size() <= firstPass.size()){

								strongTemp = new ArrayList(firstPass);
								strongTemp.retainAll(secondPass);
								//firstPass.removeAll(seenNodes);
								//strongTemp = firstPass;		
							}
							else{
								strongTemp = new ArrayList(secondPass);
								strongTemp.retainAll(firstPass);
								//secondPass.removeAll(seenNodes);
								//strongTemp = secondPass;	  
							}

							strongTemp.removeAll(seenNodes);
							strongComponentAssignment(strongTemp);
							seenNodes.addAll(strongTemp);
							//	System.out.println();

							/*	for(Iterator iii = seenNodes.iterator(); iii.hasNext();){
							System.out.println("\t\t\t"+iii.next());
							}*/

							this.strongComponentClusters++;
							//testPrinter(firstPass, secondPass, seenNodes, strongTemp);
						}
						//}
					//}
					}
					//testPrinter(firstPass, secondPass, seenNodes);
				}
				
			}
		}

		public void strongComponentCalculation(final Graph grph){
			TreeSet strongSeenNodes = new TreeSet();
			Graph reverseGraph = new Graph(grph.getNodeTable(),true);
			for(Iterator it = grph.edges(); it.hasNext();){
				Edge e = (Edge)it.next();

				reverseGraph.addEdge(e.getInt("target"),e.getInt("source"));
			}

			strongComponentArrayCalc(strongSeenNodes, grph, reverseGraph);


			System.out.println("Contains " + this.strongNodes + " nodes");
			this.averageStrongConnectedness = new BigDecimal(((double)this.strongNodes/this.strongComponentClusters));
			System.out.println("Average Connectedness is: " + this.averageStrongConnectedness);
			System.out.println("Largest Connected Component is: " + this.maxStrongConnectedNodes);
			//this.componentClusters = scForest.size();
			System.out.println("There are " + this.strongComponentClusters + " component clusters");
			//return scForest;




		}

	}

}

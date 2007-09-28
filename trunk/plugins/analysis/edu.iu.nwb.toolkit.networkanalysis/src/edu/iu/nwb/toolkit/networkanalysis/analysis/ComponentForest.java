package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;

public class ComponentForest{
	//Map forest;
	int weakComponentClusters = 0;
	int maxWeakConnectedNodes = 0;

	int strongComponentClusters = 0;
	int maxStrongConnectedNodes = 0;

	int weakNodes;
	int strongNodes;

	BigDecimal averageWeakConnectedness = new BigDecimal(0.0);
	BigDecimal averageStrongConnectedness = new BigDecimal(0.0);

	public ComponentForest(){

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
	
	public boolean isWeaklyConnected(){
		if(this.weakComponentClusters == 1){
			return true;
		}
		else
			return false;
	}

	public HashMap weakComponentCalculation(final Graph grph, NetworkProperties np){
		HashMap clusters = new HashMap();
		HashSet seenNodes = new HashSet();
		int maxNodes = 0;
		int cluster = 0;
		for(Iterator it = grph.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			Integer i = new Integer(n.getRow());
			if(!seenNodes.contains(i)){

				ArrayList tree = np.uDFS(grph, i);
		
					seenNodes.addAll(tree);
					if(tree.size() > maxNodes)
						maxNodes = tree.size();
					
					clusters.put(new Integer(cluster), new HashSet(tree));
					cluster++;
				}
			
			
			}
		
		this.weakComponentClusters = clusters.keySet().size();
		this.maxWeakConnectedNodes = maxNodes;
		return clusters;
	}

	private void strongComponentAssignment(final Queue connectedNodes){
		for(int ii = 0; ii < connectedNodes.size(); ii++){
		}
		if(connectedNodes.size() > this.maxStrongConnectedNodes)
			this.maxStrongConnectedNodes = connectedNodes.size();
		this.strongNodes += connectedNodes.size();	
	}

	private void testPrinter(final Queue first, final Queue second, final TreeSet seen, final Queue adding){
		String s1 = "";
		String s2 = "";
		String s3 = "";
		String s4 = "";
		String s5 = "";
		ArrayList firstArray = null;  //= first.toArray();
		ArrayList secondArray = null;  //= second.toArray();
		ArrayList addingArray = null;  //= adding.toArray();
		int x = 0;
		System.out.println("First Pass\tSecond Pass\tSeen?\tSeen Nodes\tAdding");
		if(first != null && second != null){
			firstArray = new ArrayList(first);
			secondArray = new ArrayList(second);
			x = Math.max(first.size(), Math.max(second.size(), seen.size()));
		}
		else if(first != null && second == null){
			firstArray =  new ArrayList(first);
			x = Math.max(first.size(), seen.size());
		}
		else if(second != null && first == null){
			secondArray = new ArrayList(second);
			x = Math.max(second.size(), seen.size());
		}
		else
			x = seen.size();

		if(adding != null){
			addingArray = new ArrayList(adding);
			x = Math.max(x, adding.size());
		}
		int ii = 0;
		for(Iterator it3 = seen.iterator(); it3.hasNext();){
			if(firstArray != null && ii < firstArray.size()){
				s1 = firstArray.get(ii).toString();
				s3 = new Boolean(seen.contains(firstArray.get(ii))).toString();
			}
			else{
				s1 = s3 = "";
			}
			if(secondArray != null && ii < secondArray.size()){
				s2 = secondArray.get(ii).toString();
			}
			else
				s2 = "";
			if(addingArray != null && ii < addingArray.size()){
				s5 = addingArray.get(ii).toString();
			}
			else
				s5 = "";
			s4 = it3.next().toString();

			System.out.println(s1 +"\t\t" + s2 + "\t\t" + s3 + "\t" + s4+"\t\t"+s5);

			ii++;
		}
		for(int iii = ii;iii < x; iii++){
			if(firstArray != null && iii < firstArray.size()){
				s1 = firstArray.get(iii).toString();
				s3 = new Boolean(seen.contains(firstArray.get(iii))).toString();
			}
			else{
				s1 = s3 = "";
			}
			if(secondArray != null && iii < secondArray.size()){
				s2 = secondArray.get(iii).toString();
			}
			else
				s2 = "";
			if(addingArray != null && iii < addingArray.size()){
				s5 = addingArray.get(iii).toString();
			}
			else
				s5 = "";

			s4 = "";

			System.out.println(s1 +"\t\t" + s2 + "\t\t" + s3 + "\t" + s4 + "\t\t" + s5);

		}
	}



	private boolean strongComponentArrayCalc(TreeSet seenNodes,Graph g1,Graph g2){
		Queue strongTemp;		                    		 
		Queue secondPass = null;
		Queue firstPass = null;


		for(Iterator it = g1.nodes(); it.hasNext();){
			firstPass = null;
			secondPass = null;
			Node n = (Node)it.next();


			Integer i = new Integer(n.getRow());

			if(!seenNodes.contains(i)){
				firstPass = NetworkProperties.dDFS(g1, i);
				firstPass.removeAll(seenNodes);
				//if(firstPass.size() != g1.getNodeCount()){
				if(firstPass.size() == 1){
					//return false;
					strongComponentAssignment(firstPass);
					//testPrinter(firstPass,secondPass,seenNodes,firstPass);
					seenNodes.addAll(firstPass);
					this.strongComponentClusters++;

				}else{
					while(!firstPass.isEmpty()){
						Integer ii = (Integer)firstPass.peek();
						if(!seenNodes.contains(ii)){
							secondPass = NetworkProperties.dDFS(g2,ii);
							secondPass.removeAll(seenNodes);
							/*if(!ii.equals(i))
								secondPass.retainAll(dDFS(g1,ii));*/
							//if(secondPass.size() != firstPass.size()){
							if(secondPass.size() == 1){
								//return false;
								strongComponentAssignment(secondPass);
								seenNodes.addAll(secondPass);
								//testPrinter(firstPass,secondPass,seenNodes,secondPass);

								this.strongComponentClusters++;
							}
							else{

								if(secondPass.size() <= firstPass.size()){

									strongTemp = new LinkedList(firstPass);
									strongTemp.retainAll(secondPass);


								}
								else{
									strongTemp = new LinkedList(secondPass);
									strongTemp.retainAll(firstPass);

								}
								/*//testPrinter(firstPass,secondPass,seenNodes,strongTemp);
								strongComponentAssignment(strongTemp);
								firstPass.removeAll(strongTemp);
								secondPass.removeAll(strongTemp);
								secondPass.removeAll(seenNodes);
								seenNodes.addAll(strongTemp);
								while(!secondPass.isEmpty()){
									Integer g = (Integer)secondPass.poll();
									seenNodes.add(g);
									firstPass.remove(g);
									this.strongNodes++;
									this.strongComponentClusters++;
								}

								this.strongComponentClusters++;
*/
								if(strongTemp.size() == g1.getNodeCount())
									return true;
								else
									return false;
							}
						}
						firstPass.poll();
					}
				}
			}
		}
		return true;
	}

	public boolean strongComponentCalculation(Graph grph){
		TreeSet strongSeenNodes = new TreeSet();
		Graph reverseGraph = new Graph(grph.getNodeTable(),true);
		for(Iterator it = grph.edges(); it.hasNext();){
			Edge e = (Edge)it.next();

			reverseGraph.addEdge(e.getInt("target"),e.getInt("source"));
		}

		return(strongComponentArrayCalc(strongSeenNodes, grph, reverseGraph));



	}

}

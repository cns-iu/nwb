package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import prefuse.data.Edge;

public class SelfLoopsParallelEdges {
	HashSet selfLoops;
	HashSet parallelEdges;
	HashMap edges;
	//int numberOfParallelEdges;
	boolean directed = false;
	
	public SelfLoopsParallelEdges(boolean directed){
		selfLoops = new HashSet();
		parallelEdges = new HashSet();
		edges = new HashMap();
		//numberOfParallelEdges = 0;
	
	}
	
	public void addEdge(Edge e){
		SimpleEdge se = new SimpleEdge(e);
		
		if(directed){
		
		if(edges.get(se) == null){
			HashSet temp = new HashSet();
			temp.add(e);
			edges.put(se, temp);
		}
		else{
			if(((HashSet)edges.get(se)).add(e)){
			
				parallelEdges.add(se);
			}
			
		}	
		}
		else{
			SimpleEdge se2 = new SimpleEdge(e.getTargetNode().getRow(), e.getSourceNode().getRow());
			
			if((edges.get(se) == null) && (edges.get(se2) == null)){
				HashSet temp = new HashSet();
				temp.add(e);
				edges.put(se, temp);
			}
			else if((edges.get(se) != null)){
				if(((HashSet)edges.get(se)).add(e)){
					
					parallelEdges.add(se);
				}
			}
			else{
				if(((HashSet)edges.get(se2)).add(e)){
					
					parallelEdges.add(se2);
				}
			}
			
		}
		
		if(e.getSourceNode().getRow() == e.getTargetNode().getRow()){
			selfLoops.add(e);
		}
	}
	
	public int getNumSelfLoops(){
		return selfLoops.size();
	}
	
	public int getNumParallelEdges(){
		int numParallelEdges = 0;
	
		for(Iterator it1 = this.parallelEdges.iterator();it1.hasNext();){
			SimpleEdge se = (SimpleEdge)it1.next();
			numParallelEdges += ((HashSet)this.edges.get(se)).size();
		}
		
		return numParallelEdges;
	}
	
	public String printParallelEdges(){
		StringBuffer sb = new StringBuffer();
		for(Iterator it1 = this.parallelEdges.iterator();it1.hasNext();){
			SimpleEdge se = (SimpleEdge)it1.next();
			for(Iterator it2 = ((HashSet)this.edges.get(se)).iterator(); it2.hasNext();){
				Edge edg = (Edge)it2.next();
			
				int columns = edg.getColumnCount();
				for(int i = 0; i < columns; i++){
					sb.append(edg.get(i) + " ");
				}
				sb.append(System.getProperty("line.separator"));
				//columns = edg.getSourceNode().getColumnCount();
			
				//sb.append(System.getProperty("line.separator"));
			
			}
		}
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}
	
	public String printSelfLoops(){
		StringBuffer sb = new StringBuffer();
		for(Iterator it = this.selfLoops.iterator(); it.hasNext();){
			sb.append((Edge)it.next());
		}
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}

}

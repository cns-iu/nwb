package edu.iu.nwb.nwbpersisters;

import java.util.Iterator;

public interface NWBModel {
	public long getNumNodes();
	
	public long getNumDirectedEdges();
	
	public long getNumUndirectedEdges();
	
	public Iterator getNodes();
	
	public Node getNode(Object id);
	
	public void addNode(Object id, Node node);
	
	public void removeNode(Object id);
	
	public Iterator getDirectedEdges();
	
	public Edge getDirectedEdge(Node origin, Node dest);
	
	public void addDirectedEdge(Edge edge);
	
	public void removeDirectedEdge(Node source, Node target);
	
	public Iterator getUndirectedEdges();
	
	public Edge getUndirectedEdge(Node origin, Node dest);
	
	public void addUndirectedEdge(Edge edge);
	
	public void removeUndirectedEdge(Node source, Node target);
}

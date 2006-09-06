package edu.iu.nwb.nwbpersisters;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import edu.iu.nwb.nwbpersisters.Edge;
import edu.iu.nwb.nwbpersisters.NWBModel;
import edu.iu.nwb.nwbpersisters.Node;
/**
 * In memory representation of an NWB data model
 * 
 */
public class BasicNWBModel implements NWBModel {
	
    private Map nodes;
    private Map directedEdges;
    private Map undirectedEdges;
    
   
    /**
     * Initialize the datastructures for the node components
     *
     */
    public BasicNWBModel() {
    	nodes          = new Hashtable();
    	directedEdges  = new Hashtable();
    	undirectedEdges = new Hashtable();
    }
    
    /**
     * Return number of nodes
     * @return Number of nodes
     */
	public long getNumNodes() {
		return nodes.size();
	}

	/**
	 * Return number of directed edges
	 * @return Number of directed edges
	 */
	public long getNumDirectedEdges() {
		return directedEdges.size();
	}

	/**
	 * Return number of undirected edges
	 * @return Number of undirected edges
	 */
	public long getNumUndirectedEdges() {
		return undirectedEdges.size();
	}

	/**
	 * Get an iterator over all of the nodes
	 * @return Iterator over the nodes
	 */
	public Iterator getNodes() {
		return nodes.values().iterator();
	}

	/**
	 * Add node to the datastructure
	 * @param id Node ID
	 * @param node Node to add 
	 */
	public void addNode(Object id, Node node) {
		nodes.put(id, node);
	}
	
	/**
	 * Get a single node given an ID
	 * @param id Node ID
	 * @return Instance of Node
	 */
	public Node getNode(Object id) {
		return (Node)nodes.get(id);
	}

	/**
	 * Get an iterator over all of the directed edges
	 * @return Iterator over the Directed Edges
	 */
	public Iterator getDirectedEdges() {
		return directedEdges.values().iterator();
	}

	/**
	 * Add a directed edge to the datastructure
	 * @param edge The edge to add
	 */
	public void addDirectedEdge(Edge edge) {
		Object id = "" + ((Edge)edge).getPropertyValue(Edge.ORIGIN) +
					((Edge)edge).getPropertyValue(Edge.DEST);
		directedEdges.put(id, edge);
	}

	/**
	 * Get an iterator over all of the undirected edges
	 * @return An iterator over all the undirected edges
	 */
	public Iterator getUndirectedEdges() {
        return undirectedEdges.values().iterator();
	}


	/**
	 * Add an undirected edge to the Graph
	 * @param source The edge to add into the instance
	 */
	public void addUndirectedEdge(Edge source) {
		Object id = "" + ((Edge)source).getPropertyValue(Edge.ORIGIN) +
		            ((Edge)source).getPropertyValue(Edge.DEST);
		undirectedEdges.put(id, source);
	}

	/**
	 * Get a single Directed Edge
	 * @param origin The source of the edge
	 * @param dest The destination of the edge
	 * @return The edge object
	 */
	public Edge getDirectedEdge(Node origin, Node dest) {
		return (Edge)directedEdges.get(origin.toString() + dest.toString());
	}

	/**
	 * Get a single undirected edge
	 * @param origin The origin of the edge
	 * @param dest The destination of the edge
	 * @return An instance of the edge
	 */
	public Edge getUndirectedEdge(Node origin, Node dest) {
		return (Edge)undirectedEdges.get(origin.toString() + dest.toString());
	}

	/**
	 * Remove using the Node ID
	 * @param id The Node ID
	 */
	public void removeNode(Object id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Remove the directed edge
	 * @param source The source of the edge
	 * @param target The target of the edge
	 */
	public void removeDirectedEdge(Node source, Node target) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Remove the undirected edge
	 * @param source The source of the edge
	 * @param target The target of the edge
	 */
	public void removeUndirectedEdge(Node source, Node target) {
		throw new UnsupportedOperationException();
	}
}

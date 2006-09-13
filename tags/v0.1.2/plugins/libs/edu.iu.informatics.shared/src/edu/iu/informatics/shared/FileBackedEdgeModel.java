package edu.iu.informatics.shared;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import edu.iu.iv.core.persistence.FileResourceDescriptor;
import edu.iu.nwb.core.model.Edge;
import edu.iu.nwb.core.model.NWBModel;
import edu.iu.nwb.core.model.Node;

/**
 * Handles a network file which is just a list of edges wrapped by an
 * NWBModel.  Programs using this will view this as an NWBModel.
 * 
 * @author Team NWB
 *
 */
public class FileBackedEdgeModel implements NWBModel {
	
	private FileResourceDescriptor fileResourceDesc;
	private long                   numNodes;

	/**
	 * Create a new edge model
	 * 
	 * @param fileResourceDesc
	 * @param numNodes
	 */
	public FileBackedEdgeModel(FileResourceDescriptor fileResourceDesc, long numNodes) {
		this.numNodes         = numNodes;
		this.fileResourceDesc = fileResourceDesc;
	}

	/**
	 * Get the number of nodes in the model
	 * @return Number of nodes
	 */
	public long getNumNodes() {
		return this.numNodes;
	}

	/**
	 * Get the number of directed edges
	 * @return Number of directed edges
	 */
	public long getNumDirectedEdges() {
		return 0;
	}

	/**
	 * Get the number of undirected edges
	 * @return Number of undirected edges
	 */
	public long getNumUndirectedEdges() {
		long numEdges = 0;
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			//Read in each line and count
			while (true) {
				try {
					String inputLine = reader.readLine();
					if (inputLine == null) break;
					StringTokenizer tokenizer = new StringTokenizer(inputLine);
					
					if (tokenizer.countTokens() >= 2) {
						numEdges++;
					}
					else {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return numEdges;
	}

	/**
	 * Return an iterator over all the nodes
	 * @return Iterator over the nodes
	 */
	public Iterator getNodes() {
		return new NodeSectionIterator(this.fileResourceDesc, this.numNodes);
	}

	/**
	 * Get the node given an id
	 * 
	 * @param id The id of the node
	 * @return An instance of the node
	 */
	public Node getNode(Object id) {
		Node node = new Node();
		node.setPropertyValue(Node.ID, id);
		node.setPropertyValue(Node.LABEL,  id);
		return node;
	}

	/**
	 * Adds a node to the model
	 * @param id The id of the node to add
	 * @param node The node information
	 */
	public void addNode(Object id, Node node) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get an iterator over the directed edges
	 * @return an empty iterator over the edges
	 */
	public Iterator getDirectedEdges() {
		return (new ArrayList()).iterator();
	}

	/**
	 * Get a directed edge from the file
	 * @param origin The source node of the edge
	 * @param dest The target node of the edge
	 */
	public Edge getDirectedEdge(Node origin, Node dest) {
		return new Edge();
	}

	/**
	 * Add a directed edge to the model
	 * @param edge The edge to add
	 */
	public void addDirectedEdge(Edge edge) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get an iterator over the undirected edges
	 * @return Iterator over the undirected edges
	 */
	public Iterator getUndirectedEdges() {
		return new EdgeSectionIterator(fileResourceDesc);
	}

	/**
	 * Get an individual undirected edge
	 * 
	 * @param origin The Source of the undirected edge
	 * @param dest The target of the undirected edge
	 * @return The undirected edge
	 */
	public Edge getUndirectedEdge(Node origin, Node dest) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			//Searches for the undirected edge in the file
			while (true) {
				try {
					String inputLine = reader.readLine();
					if (inputLine == null) break;
					StringTokenizer tokenizer = new StringTokenizer(inputLine);
					
					if (tokenizer.hasMoreElements()) {
						String currentOrigin = tokenizer.nextToken().trim();
						String currentDest   = tokenizer.nextToken().trim();
						
						if (currentOrigin.equals(origin) &&
							currentDest.equals(dest)) { 
							Edge edge = new Edge();
							edge.put(Edge.ORIGIN, origin);
							edge.put(Edge.DEST, dest);
							
							return edge;
						}
					}
					else {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return new Edge();
	}

	/**
	 * Add an undirected edge to the model
	 * @param edge The edge to add
	 */
	public void addUndirectedEdge(Edge edge) {
		throw new UnsupportedOperationException();
/*
		try {
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(fileResourceDesc.getFile(), true)));
			out.println(edge);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
*/	}
	
	/**
	 * Remove a node from the model
	 * @param id The id of the node
	 */
	public void removeNode(Object id) {
		throw new UnsupportedOperationException();		
	}
	
	/**
	 * Remove a directed edge
	 * @param source Source node of the edge
	 * @param target Target node of the edge
	 */
	public void removeDirectedEdge(Node source, Node target) {
		throw new UnsupportedOperationException();		
	}

	/**
	 * Remove an undirected edge
	 * @param source Source node of the undirected edge
	 * @param target Target node of the undirected edge
	 */
	public void removeUndirectedEdge(Node source, Node target) {
		throw new UnsupportedOperationException();		
	}

	/**
	 * General class to iterate over the elements of the model
	 */
	private abstract class ElementIterator implements Iterator {
		private BufferedReader reader;
		private String         currentLine;
		
		/**
		 * Creates an element iterator
		 * @param frd The file to read from
		 */
		public ElementIterator (FileResourceDescriptor frd) {
			try {
				reader      = new BufferedReader(new FileReader(frd.getFile()));				
				currentLine = reader.readLine();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Checks whether there is another element in the list
		 * @return True if there are more elements
		 */
		public boolean hasNext() {
			if (currentLine != null) {
				return (currentLine.trim().length() > 0);
			}
			return false;
		}
		
		/**
		 * Retrieves the next element
		 * @param The element which is either a node or edge
		 */
		public Object next() {
			if (currentLine != null) {
				if (currentLine.trim().length() > 0) {
					Object node = getElement(currentLine);

					try {
						currentLine = reader.readLine();
						//System.out.println(currentLine);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return node;
				}
			}
			
			return null;
		}

		/**
		 * Remove the current object from the model
		 */
		public void remove() {
			throw new UnsupportedOperationException("Remove not supported with NodeIterator");
		}
				
		protected abstract Object getElement (String line);
	}
	
	/**
	 * Specifically handles the edges of the model
	 * @author Team NWB
	 */
	private class EdgeSectionIterator extends ElementIterator {
		/**
		 * Create the iterator
		 * @param frd The file descriptor of the model
		 */
		public EdgeSectionIterator (FileResourceDescriptor frd) {
			super (frd);
		}
		
		/**
		 * Get a line from the file and return the edge
		 * @param line The current line of the file
		 * @return An edge object
		 */
		protected Object getElement(String line) {
			StringTokenizer st = new StringTokenizer(line);

			Edge edge = new Edge();

			String origin = st.nextToken();
			String dest   = st.nextToken();
			edge.put(Edge.ORIGIN, origin);
			edge.put(Edge.DEST, dest);
			
			return edge;
		}
	}
	
	/**
	 * Iterator over the nodes
	 * @author Team NWB
	 */
	private class NodeSectionIterator extends ElementIterator {
		private long numNodes;
		private long currentNode;
		
		/**
		 * Create a node iterator
		 * @param frd The file source of the model (not used)
		 * @param numNodes Number of nodes in the model
		 */
		public NodeSectionIterator(FileResourceDescriptor frd, long numNodes) {
			super(frd);
			this.numNodes = numNodes;
			
			this.currentNode = 0;
		}
		
		/**
		 * Check whether or not there are more nodes
		 * @return True if there are more elements
		 */
		public boolean hasNext() {
			return this.currentNode < this.numNodes;
		}
		
		/**
		 * Get the next node
		 * @return Next node
		 */
		public Object next() {
			if (this.currentNode < this.numNodes) {
				Node node = new Node();

				long nodeId = currentNode + 1;
				node.put(Node.ID,    "" + nodeId);
				node.put(Node.LABEL, "" + nodeId);
				this.currentNode++;
				return node;
			}
			
			return null;
		}
		
		/**
		 * Unused for edge list iterator
		 */
		protected Object getElement(String line) {return null;}
	}
}

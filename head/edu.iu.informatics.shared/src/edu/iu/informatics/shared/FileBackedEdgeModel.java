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

public class FileBackedEdgeModel implements NWBModel {
	
	private FileResourceDescriptor fileResourceDesc;
	private long                   numNodes;
//	private NWBModel nwbModel; 
	
	public FileBackedEdgeModel(FileResourceDescriptor fileResourceDesc, long numNodes) {
		//System.out.println("filebackededgemodel: " + fileResourceDesc.getFilePath());
		//this.fileResourceDesc = fileResourceDesc;

		this.numNodes         = numNodes;
		this.fileResourceDesc = fileResourceDesc;
	}
/*
	public void setFileResourceDescriptor(FileResourceDescriptor fileResourceDesc) {
		this.fileResourceDesc = fileResourceDesc;
	}
*/
/*
	public void addEdge(Object origin, Object dest) {
		try {
			//Open the file to read from
			BufferedReader reader  = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			//Create and init the file to write to
			File tempFile = File.createTempFile("AddBasicEdge", ".tmp");
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));

			String line = reader.readLine();
			boolean edgePresent = false;
			
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line);

				String currentOrigin = st.nextToken();
				String currentDest   = st.nextToken();

				if (currentOrigin.equals(origin) &&
					currentDest.equals(dest)) {
					edgePresent = true;
				}
				out.write(line);

				line = reader.readLine();
			}
			
			if (!edgePresent) {
				out.println(origin + " " + dest);
			}

			reader.close();
			out.close();

			fileResourceDesc.getFile().delete();
			tempFile.renameTo(fileResourceDesc.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeEdge(Object origin, Object dest) {
		try {
			//Open the file to read from
			BufferedReader reader  = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			//Create and init the file to write to
			File tempFile = File.createTempFile("RemoveBasicEdge", ".tmp");
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));

			String line = reader.readLine();
			
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line);

				String currentOrigin = st.nextToken();
				String currentDest   = st.nextToken();

				if (!currentOrigin.equals(origin) &&
					!currentDest.equals(dest)) {
					out.write(line);
				}

				line = reader.readLine();
			}
			
			reader.close();
			out.close();

			fileResourceDesc.getFile().delete();
			tempFile.renameTo(fileResourceDesc.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/
/*	public Object getEdges(FileResourceDescriptor fileResourceDesc) {
		Collection edges = new HashSet();
//		System.out.println("Reading from file: " + fileResourceDesc.getFilePath());
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			while (true) {
				try {
					String inputLine = reader.readLine();
					//System.out.println("Reading line: " + inputLine);
					if (inputLine == null) break;
					StringTokenizer tokenizer = new StringTokenizer(inputLine);
					
					if (tokenizer.hasMoreElements()) {
						String origin = tokenizer.nextToken();
						String dest   = tokenizer.nextToken();
						
//						System.out.println(origin + ", " + dest);
						Edge bec = new Edge();
						bec.setPropertyValue(Edge.ORIGIN, origin);
						bec.setPropertyValue(Edge.DEST, dest);
						
						edges.add(bec);
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
		
		return edges;
	}
*//*
	public int getNumEdges() {
		int numEdges = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
			while (true) {
				try {
					String line = reader.readLine();
					Matcher matcher = pattern.matcher(line);
					if (matcher.matches()) {
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
*/


	public long getNumNodes() {
		return this.numNodes;
	}

	public long getNumDirectedEdges() {
		return 0;
	}

	public long getNumUndirectedEdges() {
		long numEdges = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			while (true) {
				try {
					String inputLine = reader.readLine();
					//System.out.println("Reading line: " + inputLine);
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

	public Iterator getNodes() {
		return new NodeSectionIterator(this.fileResourceDesc, this.numNodes);
		/*
		ArrayList nodeList = new ArrayList((int)this.numNodes);
		for (int i = 0; i < this.numNodes; ++i) {
			int nodeId = i+1;
			Node node = new Node();
			node.put(Node.ID, ""+nodeId);
			node.put(Node.LABEL, ""+nodeId);
			nodeList.add(node);
		}
		return nodeList.iterator();
		*/
	}

	public Node getNode(Object id) {
		Node node = new Node();
		node.setPropertyValue(Node.ID, id);
		node.setPropertyValue(Node.LABEL,  id);
		return node;
	}

	public void addNode(Object id, Node node) {
		throw new UnsupportedOperationException();
	}

	public Iterator getDirectedEdges() {
		return (new ArrayList()).iterator();
	}

	public Edge getDirectedEdge(Node origin, Node dest) {
/*		// TODO Auto-generated method stub
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			while (true) {
				try {
					String inputLine = reader.readLine();
					//System.out.println("Reading line: " + inputLine);
					if (inputLine == null) break;
					StringTokenizer tokenizer = new StringTokenizer(inputLine);
					
					if (tokenizer.hasMoreElements()) {
						String currentOrigin = tokenizer.nextToken().trim();
						String currentDest   = tokenizer.nextToken().trim();
						
//						System.out.println(origin + ", " + dest);
						if (currentOrigin.equals(origin) &&
							currentDest.equals(dest)) { 
							BasicEdgeComponent bec = new BasicEdgeComponent();
							bec.addAttribute(BasicEdgeComponent.ORIGIN, origin);
							bec.addAttribute(BasicEdgeComponent.DEST, dest);
							
							return bec;
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
		
		return null;*/
		return new Edge();
	}

	public void addDirectedEdge(Edge edge) {
		throw new UnsupportedOperationException();

/*		try {
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(fileResourceDesc.getFile(), true)));
			out.println(edge);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
*/	
	}

	public Iterator getUndirectedEdges() {
		return new EdgeSectionIterator(fileResourceDesc);
	}

	public Edge getUndirectedEdge(Node origin, Node dest) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			while (true) {
				try {
					String inputLine = reader.readLine();
					//System.out.println("Reading line: " + inputLine);
					if (inputLine == null) break;
					StringTokenizer tokenizer = new StringTokenizer(inputLine);
					
					if (tokenizer.hasMoreElements()) {
						String currentOrigin = tokenizer.nextToken().trim();
						String currentDest   = tokenizer.nextToken().trim();
						
//						System.out.println(origin + ", " + dest);
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

	public void addUndirectedEdge(Edge source) {
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
	
	public void removeNode(Object id) {
		throw new UnsupportedOperationException();		
	}
	
	public void removeDirectedEdge(Node source, Node target) {
		throw new UnsupportedOperationException();		
	}

	public void removeUndirectedEdge(Node source, Node target) {
		throw new UnsupportedOperationException();		
	}

	/**
	 * 
	 */
	private abstract class ElementIterator implements Iterator {
		private BufferedReader reader;
		private String         currentLine;
		
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
		
		public boolean hasNext() {
			if (currentLine != null) {
				return (currentLine.trim().length() > 0);
			}
			return false;
		}
		
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

		public void remove() {
			throw new UnsupportedOperationException("Remove not supported with NodeIterator");
		}
				
		protected abstract Object getElement (String line);
	}
	
	private class EdgeSectionIterator extends ElementIterator {
		public EdgeSectionIterator (FileResourceDescriptor frd) {
			super (frd);
		}
		
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
	
	private class NodeSectionIterator extends ElementIterator {
		private long numNodes;
		private long currentNode;
		
		public NodeSectionIterator(FileResourceDescriptor frd, long numNodes) {
			super(frd);
			this.numNodes = numNodes;
			
			this.currentNode = 0;
		}
		
		public boolean hasNext() {
			return this.currentNode < this.numNodes;
		}
		
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
		
		protected Object getElement(String line) {return null;}
	}

}

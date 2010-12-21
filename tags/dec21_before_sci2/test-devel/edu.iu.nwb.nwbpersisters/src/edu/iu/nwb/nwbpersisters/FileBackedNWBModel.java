package edu.iu.nwb.nwbpersisters;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import edu.iu.nwb.nwbpersisters.Edge;
import edu.iu.nwb.nwbpersisters.NWBModel;
import edu.iu.nwb.nwbpersisters.Node;

/**
 * Implementation of an NWB model that is based off of a file.  Hopefully this will
 * have the absolute minimum amount of memory necessary.
 * 
 * @author Team NWB
 */
public class FileBackedNWBModel implements NWBModel {
	private File fileResourceDesc;
	
	/**
	 * Accepts the NWB file that this is based off of
	 * @param frd The file resource descriptor for the NWB model
	 */
	public FileBackedNWBModel (File frd) {
		fileResourceDesc = frd;
	}
	
	/**
	 * Get the number of elements given a header
	 * 
	 * @param header The header to look for for counting
	 * @return Number of elements
	 */
	private int getNumElements(String header) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc));
			
			//find location of nodes
			while (true) {
				try {
					String line = reader.readLine();
				
					if (line.startsWith(header)) {break;}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			
			//Count the nodes -> look for number, then space, and finally a label
			int numElements = 0;
			try {
				String line = reader.readLine();
				while (line != null) {
					if (line.trim().length() > 0) {
						numElements++;
					} else {
						break;
					}
					line = reader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return numElements;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		return 0;
	}

	/**
	 * Retrieves the number of nodes in a NWB data file
	 */
	public long getNumNodes() {
		return getNumElements("*Nodes");
	}

	/**
	 * Retrieve the number of directed Edges
	 */
	public long getNumDirectedEdges() {
		return getNumElements("*DirectedEdges");
	}

	/**
	 * 
	 */
	public long getNumUndirectedEdges() {
		return getNumElements("*UndirectedEdges");
	}

	/**
	 * 
	 */
	public Iterator getNodes() {
		return new NodeSectionIterator(fileResourceDesc, "*Nodes");
/*		ArrayList nodeList = new ArrayList();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			//find location of nodes
			while (true) {
				try {
					String line = reader.readLine();
				
					if (line.startsWith("*Nodes")) {break;}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			
			//Count the nodes -> look for number, then space, and finally a label
			while (true) {
				try {
					String line = reader.readLine();
					StringTokenizer tokenCount = new StringTokenizer(line);
					if (tokenCount.countTokens() >= 2) {
						StringTokenizer st = new StringTokenizer(line);

						Integer nodeNum = new Integer(st.nextToken());
						String label = st.nextToken();

						Node bnc = new Node();
						bnc.addAttribute(Node.NUMBER, nodeNum);
						bnc.addAttribute(Node.LABEL, label);

						if (st.countTokens() == 4) {
							Double weight = new Double(st.nextToken());
							String type = st.nextToken();

							bnc.addAttribute(Node.WEIGHT, weight);
							bnc.addAttribute(Node.TYPE, type);
						}
//						System.out.println("node added: " + bnc);
						nodeList.add(bnc);
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
			return nodeList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return nodeList;*/
	}

	/**
	 * Gets a node from the backed file.
	 * 
	 * @param id Must be an Integer object of the node ID
	 * @return BasicNodeComponent found in file, may return null if node id not found
	 */
	public Node getNode(Object id) {
		try {
			BufferedReader reader  = new BufferedReader(new FileReader(fileResourceDesc));
			Node bnc = null;
			
			//find location of nodes
			while (true) {
				try {
					String line = reader.readLine();
				
					if (line.startsWith("*Nodes")) {break;}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			
			//Find the node with a particular id
			while (true) {
				try {
					String line = reader.readLine();
					
					if (line.trim().length() > 0) {
						StringTokenizer st = new StringTokenizer(line);

						Object nodeNum   = st.nextToken();

						if (id.toString().equals(nodeNum.toString().trim())) {
							Object label = st.nextToken();

							bnc = new Node();
							bnc.setPropertyValue(Node.ID, nodeNum);
							bnc.setPropertyValue(Node.LABEL,  label);

							if (st.countTokens() == 4) {
								Double weight = new Double(st.nextToken());
								String type = st.nextToken();

								bnc.setPropertyValue(Node.WEIGHT, weight);
								bnc.setPropertyValue(Node.TYPE, type);
							}
							break;
						}
					}
					else {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}				
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return bnc;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * Add a single node to the nwb file
	 */
	public void addNode(Object id, Node node) {
		throw new UnsupportedOperationException("AddNode currently not supported");
/*		try {
			//Open the file to read from
			BufferedReader reader  = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			//Create and init the file to write to
			File tempFile = File.createTempFile("AddNWBNode", ".nwb");
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));

			String line = reader.readLine();
			boolean inNodeSection = false;
			while (line != null) {
				if (inNodeSection) {
					
					if (line.trim().length() > 0) {
						StringTokenizer st = new StringTokenizer(line);

						Object addNodeId      = node.getAttribute(Node.NUMBER);
						Object currentNodeId  = st.nextToken();

						if (addNodeId.toString().equals(currentNodeId.toString())) {
							break;
						}

					}
					else {
						out.println(node.toString());
						out.println("");
						break;
					}
				}

				if (line.startsWith("*Nodes")) {
					inNodeSection = true;
				}

				line = reader.readLine();
			}

			reader.close();
			out.close();
			
			fileResourceDesc.getFile().delete();
			tempFile.renameTo(fileResourceDesc.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * 
	 */
	public Iterator getDirectedEdges() {
		return new EdgeSectionIterator(fileResourceDesc, "*DirectedEdges");
	}

	/**
	 * 
	 */
	public Edge getDirectedEdge(Node origin, Node dest) {
		return getEdge("*DirectedEdges", 
				        origin.getPropertyValue(Node.ID), 
				        dest.getPropertyValue(Node.ID));
	}

	/**
	 * 
	 */
	public void addDirectedEdge(Edge edge) {
		addEdge("*DirectedEdges", edge);
	}

	/**
	 * 
	 */
	public Iterator getUndirectedEdges() {
		return new EdgeSectionIterator(fileResourceDesc, "*UndirectedEdges");
	}

	/**
	 * 
	 */
	public Edge getUndirectedEdge(Node origin, Node dest) {
		return getEdge("*UndirectedEdges", 
				       origin.getPropertyValue(Node.ID), 
				       dest.getPropertyValue(Node.ID));
	}

	/**
	 * 
	 */
	public void addUndirectedEdge(Edge source) {
		addEdge("*UndirectedEdges", source);
	}

	/**
	 * 
	 * @param sectionPattern
	 * @return
	 */
/*	private Collection getEdges(String sectionPattern) {
		ArrayList nodeList = new ArrayList();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			//find location of nodes
			Pattern pattern = Pattern.compile(sectionPattern);
			while (true) {
				try {
					String line = reader.readLine();
				
					Matcher matcher = pattern.matcher(line);					
					if (matcher.matches()) {break;}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			
			//Count the nodes -> look for number for source, and another for target
			//pattern      = Pattern.compile("[a-zA-Z0-9]+");
			while (true) {
				try {
					String line = reader.readLine();
					if (line == null) {
						break;
					}
					
					StringTokenizer tokenCount = new StringTokenizer(line);
					//Matcher matcher = pattern.matcher(line);
					
					if (tokenCount.countTokens() >= 2) {
					//if (matcher.matches()) {
						StringTokenizer st = new StringTokenizer(line);

						Integer origin = new Integer(st.nextToken());
						Integer dest   = new Integer(st.nextToken());

						Edge bec = new Edge();
						bec.addAttribute(Edge.ORIGIN, origin);
						bec.addAttribute(Edge.DEST,   dest);

						if (st.countTokens() == 4) {
							Double weight = new Double(st.nextToken());
							String type = st.nextToken();

							bec.addAttribute(Edge.WEIGHT, weight);
							bec.addAttribute(Edge.TYPE, type);
						}
						nodeList.add(bec);
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
			return nodeList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return nodeList;
	}*/

	/**
	 * 
	 * @param sectionPattern
	 * @param origin
	 * @param dest
	 * @return
	 */
	private Edge getEdge (String sectionPattern, Object origin, Object dest) {
		try {
			BufferedReader reader  = new BufferedReader(new FileReader(fileResourceDesc));
			Edge bec = null;
			
			//find location of nodes
			while (true) {
				try {
					String line = reader.readLine();
				
					if (line.startsWith(sectionPattern)) {break;}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			
			//Find the node with a particular id
			while (true) {
				try {
					String line = reader.readLine();
					
					if (line.trim().length() > 0) {
						StringTokenizer st = new StringTokenizer(line);

						Object currentOrigin = st.nextToken();
						Object currentDest   = st.nextToken();

						if (origin.toString().equals(currentOrigin.toString()) &&
							dest.toString().equals(currentDest.toString())) {

							bec = new Edge();
							bec.setPropertyValue(Edge.ORIGIN, currentOrigin);
							bec.setPropertyValue(Edge.DEST,   currentDest);

							if (st.countTokens() > 2) {
								Object weight = st.nextToken();
								Object type   = st.nextToken();

								bec.setPropertyValue(Edge.WEIGHT, weight);
								bec.setPropertyValue(Edge.TYPE, type);
							}
							return bec;
						}
					}
					else {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}				
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param sectionPattern
	 * @param edge
	 */
	private void addEdge(String sectionPattern, Object edge) {
		throw new UnsupportedOperationException("AddEdge currently not supported");
/*		try {
			//Open the file to read from
			BufferedReader reader  = new BufferedReader(new FileReader(fileResourceDesc.getFile()));
			
			//Create and init the file to write to
			File tempFile = File.createTempFile("AddNWBEdge", ".nwb");
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));

			//Convert to basic edge
			Edge bec = (Edge)edge;
			
			//Copy up to this pattern
			Pattern nodeSectionPattern = Pattern.compile(sectionPattern);

			String line = reader.readLine();
			boolean inSection = false;
			while (line != null) {
				if (inSection) {
					Pattern nonemptyLinePattern = Pattern.compile("[a-zA-Z0-9]+");
					Matcher nonemptyLineMatcher = nonemptyLinePattern.matcher(line);
					
					if (nonemptyLineMatcher.matches()) {
						StringTokenizer st = new StringTokenizer(line);

						Integer currentOrigin = new Integer(st.nextToken());
						Integer currentDest   = new Integer(st.nextToken());

						if (currentOrigin.equals(bec.getAttribute(Edge.ORIGIN)) &&
							currentDest.equals(bec.getAttribute(Edge.DEST))) {
							
							line = edge.toString();
						}
					}
					else {
						out.println(edge.toString());
						inSection = false;
					}
				}
				out.println(line);

				Matcher matcher = nodeSectionPattern.matcher(line);
				if (matcher.matches()) {
					inSection = true;
				}

				line = reader.readLine();
			}

			reader.close();
			out.close();

			fileResourceDesc.getFile().delete();
			tempFile.renameTo(fileResourceDesc.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
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
		private String         header;
		private BufferedReader reader;
		private String         currentLine;
		
		public ElementIterator (File frd, String header) {
			try {
				reader      = new BufferedReader(new FileReader(frd));
				this.header = header;
				
				findSection();
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
			else return false;
		}
		
		public Object next() {
			if (currentLine != null) {
				if (currentLine.trim().length() > 0) {
					Object node = getElement(currentLine);

					try {
						currentLine = reader.readLine();
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
		
		private void findSection() {
			//find location of nodes
			while (true) {
				try {
					String line = reader.readLine();
				
					if (line.startsWith(header)) {break;}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}
		
		protected abstract Object getElement (String line);
	}
	
	private class NodeSectionIterator extends ElementIterator {
		public NodeSectionIterator (File frd, String header) {
			super (frd, header);
		}
		
		protected Object getElement(String line) {
			StringTokenizer st = new StringTokenizer(line);

			Node node = new Node();

			Object id = st.nextElement();
			Object label = st.nextElement();
			node.setPropertyValue(Node.ID, id);
			node.setPropertyValue(Node.LABEL, label);
			
			if (st.countTokens() > 2) {
				Object weight = st.nextElement();
				Object type = st.nextElement();
				node.setPropertyValue(Node.WEIGHT, weight);
				node.setPropertyValue(Node.TYPE, type);
			}

			return node;
		}

	}
	
	private class EdgeSectionIterator extends ElementIterator {
		public EdgeSectionIterator (File frd, String header) {
			super (frd, header);
		}
		
		protected Object getElement(String line) {
			StringTokenizer st = new StringTokenizer(line);

			Edge edge = new Edge();

			Object origin = st.nextElement();
			Object dest   = st.nextElement();
			edge.setPropertyValue(Edge.ORIGIN, origin);
			edge.setPropertyValue(Edge.DEST, dest);
			
			if (st.countTokens() > 2) {
				Object weight = st.nextElement();
				Object type = st.nextElement();
				
				edge.setPropertyValue(Edge.WEIGHT, weight);
				edge.setPropertyValue(Edge.TYPE, type);
			}

			return edge;
		}
	}

}
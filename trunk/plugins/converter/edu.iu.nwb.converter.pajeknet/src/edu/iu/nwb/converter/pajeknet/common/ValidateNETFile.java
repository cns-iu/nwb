package edu.iu.nwb.converter.pajeknet.common;

/*
 * 
 * This class is used to validate a Pajek .net file against a subset of
 * the specifications found in the Pajek manual, availble at: 
 * http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf
 * Right now we only support the basic id and label definitions
 * and the use of either Arcs or Nodes, but not both. .net files have
 * a slew of optional parameters that should be easy to write into a .nwb
 * file and then retreive them, but that will be for future releases.
 * 
 * Written by: Tim Kelley
 * Date: May 16, 2007
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ValidateNETFile {
	private boolean hasHeader_Vertices = false;
	private boolean hasHeader_Edges = false;
	private boolean hasHeader_Arcs = false;

	

	private boolean isFileGood = true;
	private boolean inVerticesSection = false;
	private boolean inEdgesSection = false;
	private boolean inArcsSection = false;
	private boolean hasTotalNumOfNodes = false;


	private int totalNumOfNodes, currentLine;
	private StringBuffer errorMessages = new StringBuffer();
	private ArrayList<NETVertex> vertices = new ArrayList<NETVertex>();
	private ArrayList<NETArcsnEdges>arcs = new ArrayList<NETArcsnEdges>(), edges = new ArrayList<NETArcsnEdges>(); 

	public void validateNETFormat(File fileHandler) throws FileNotFoundException, IOException {
		currentLine = 0;
		BufferedReader reader = 
			new BufferedReader(new FileReader(fileHandler));
		this.processFile(reader);

	}

	public boolean isDirectedGraph(){
		if (hasHeader_Arcs &&
				arcs.size()>0)
			return true;
		else
			return false;
	}

	public boolean isUndirectedGraph(){
		if(hasHeader_Edges &&
				edges.size()>0)
			return true;
		else
			return false;
	}

	public boolean getValidationResult(){
		return isFileGood;
	}
	public String getErrorMessages(){
		return errorMessages.toString();
	}
	public List<NETAttribute> getVertexAttrList(){ 
		return NETVertex.getVertexAttributes();
	}

	public List<NETAttribute> getEdgeAttrList(){
		return NETArcsnEdges.getArcsnEdgesAttributes();
	}

	public List<NETAttribute> getArcAttrList() {
		return NETArcsnEdges.getArcsnEdgesAttributes();
	}

	public int getTotalNumOfNodes(){
		return totalNumOfNodes;
	}

	public boolean getHasTotalNumOfNodes (){
		return hasTotalNumOfNodes;
	}
	
public ArrayList<NETVertex> getVertices(){
	return this.vertices;
}
public ArrayList<NETArcsnEdges> getArcs(){
	return this.arcs;
}
public ArrayList<NETArcsnEdges> getEdges(){
	return this.edges;
}


	/*
	 * 
	 *  validateVertexHeader takes in a string and checks to see if it starts with 
	 *  *vertices with an optional integer following the vertices declaration.
	 *  
	 *  Written by: Tim Kelley
	 *  Date: May 17th 2007
	 */


	public boolean validateVertexHeader(String s) throws IOException{
		//String s = r.readLine();

		s = s.toLowerCase();
		//this.errorMessages.append(s+"\n");
		if(s.startsWith(NETFileProperty.HEADER_VERTICES)){
			this.hasHeader_Vertices = true;
			this.inVerticesSection = true;
			this.inEdgesSection = false;
			this.inArcsSection = false;
			this.vertices = new ArrayList<NETVertex>();
			StringTokenizer st= new StringTokenizer(s);
			if (st.countTokens()>1){
				st.nextToken();
				//*****If it is not an integer...
				this.totalNumOfNodes = new Integer(st.nextToken()).intValue();
				this.hasTotalNumOfNodes = true;
			}
			else {
				this.hasTotalNumOfNodes = false;
			}
			return true;
		}

		return false;
	}

	/*
	 * validateArcHeader
	 * @inputs: String s
	 * this validates the transition into the Arc section of a .net file and intializes the attribute list
	 * for arcs.
	 * 
	 * written by: Tim Kelley
	 * 
	 */

	public boolean validateArcHeader(String s){
		s = s.toLowerCase();
		if(s.startsWith(NETFileProperty.HEADER_ARCS)) 
		{
			hasHeader_Arcs = true;
			inArcsSection = true;
			inVerticesSection = false;
			inEdgesSection = false;
			this.arcs = new ArrayList<NETArcsnEdges>();
			return true;
		}
		return false;
	}

	/*
	 * validateEdgeHeader
	 * @input String s
	 * validateEdgeHeader validates the transition into the Edges section of a .net file and
	 * initializes the attribute list for the edges.
	 * 
	 * written by: Tim Kelley
	 */

	public boolean validateEdgeHeader(String s){
		s = s.toLowerCase();
		if(s.startsWith(NETFileProperty.HEADER_EDGES)){
			hasHeader_Edges = true;
			inArcsSection = false;
			inVerticesSection = false;
			inEdgesSection = true;
			this.edges = new ArrayList<NETArcsnEdges>();
			return true;
		}

		return false;
	}

	/*
	 * 
	 * processVertices
	 * @input String s
	 * Written By: Tim Kelley
	 * Date: May 18, 2007
	 * 
	 * Here we process the vertices of a pajek .net file. The pajek file format does not include an attribute declaration
	 * as .nwb files do, so we do not check for attributes. Similarly, we cannot assume a string token length of 2
	 * as the .net file may have not only positional data, but display data as well. I treat these optional
	 * parameters as attributes to ease the conversion into .nwb format.
	 * 
	 */

	public NETVertex processVertices(String s){
		NETVertex nv = null;
			if (s.startsWith(NETFileProperty.PREFIX_COMMENTS)) {

			}
			else {
				try{
					nv = new NETVertex(s);
					//System.out.println(nv);
			
				}
				catch (NumberFormatException nfe){
					isFileGood = false;
					errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
					"Node id must be an integer and greater than 0.\n\n");
				}
				catch (Exception e){
					isFileGood = false;
					errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
							e.toString()+"\n\n");
				}
			}
			return nv;
		}
	
	public NETArcsnEdges processArcsnEdges(String s){
		NETArcsnEdges nae = null;
		if (s.startsWith(NETFileProperty.PREFIX_COMMENTS)) {

		}
		else {
			try{
				nae = new NETArcsnEdges(s);
				
				//System.out.println(nae);
		
			}
			catch (NumberFormatException nfe){
				isFileGood = false;
				errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
				"Node id must be an integer and greater than 0.\n\n");
			}
			catch (Exception e){
				isFileGood = false;
				errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
						e.toString()+"\n\n");
			}
		}

			return nae;
		
	}


	public void processFile(BufferedReader reader) throws IOException{
		String line = reader.readLine();

		while (line != null && isFileGood){
			currentLine++;
			//String line_lower = line.toLowerCase();
			if(this.validateVertexHeader(line)){
				line = reader.readLine();

				continue;
			}
			if(this.validateArcHeader(line)){
				line = reader.readLine();

				continue;
			}

			if(this.validateEdgeHeader(line)){
				line = reader.readLine();

				continue;
			}
			//this.errorMessages.append(this.hasHeader_Vertices+"\n");

			if(inVerticesSection && isFileGood){	

				this.vertices.add(processVertices(line));
				line = reader.readLine();

				continue;
			}

			if(inEdgesSection && isFileGood){
				this.edges.add(processArcsnEdges(line));
				line = reader.readLine();

				continue;
			}

			if(inArcsSection && isFileGood){
				this.arcs.add(processArcsnEdges(line));
				line = reader.readLine();

				continue;
			}

			if (isFileGood){
				this.checkFile();			
			}
			line = reader.readLine();
		}


	}

	public void checkFile(){
		if (!this.hasHeader_Vertices){
			this.isFileGood = false;
			this.errorMessages.append("*The file does not specify the Vertex header.\n\n");
		}/*else if (!hasTotalNumOfNodes && skipNodeList){
			this.isFileGood = false;
			this.errorMessages.append("*The file does not specify the total number of vertices and "+
			"does not list all vertices.\n\n");
		}*/else if (!this.hasHeader_Arcs && !this.hasHeader_Edges) {
			isFileGood = false;				
		} 
		/*else if (this.hasTotalNumOfNodes && this.skipNodeList){
			NETAttribute netAttr = new NETAttribute
			(NETFileProperty.ATTRIBUTE_ID, NETFileProperty.TYPE_INT);
			
			this.vertexAttrList.put(netAttr.getAttrName(),netAttr.getDataType());
			netAttr = new NETAttribute("label", NETFileProperty.TYPE_STRING);
			this.vertexAttrList.put(netAttr.getAttrName(), netAttr.getDataType());
			
		}	*/
	}

	


}




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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import org.cishell.utilities.UnicodeReader;

public class NETFileValidator {
	private boolean hasHeader_Vertices = false;
	private boolean hasHeader_Edges = false;
	private boolean hasHeader_Arcs = false;


	private boolean isFileGood = true;
	private boolean inVerticesSection = false;
	private boolean inEdgesSection = false;
	private boolean inArcsSection = false;


	private boolean hasTotalNumOfNodes = false;
	private boolean skipNodeList;

	private int totalNumOfNodes, currentLine;
	private StringBuffer errorMessages = new StringBuffer();
	private int numVertices, numArcs, numEdges;
	//private ArrayList vertices = new ArrayList();
	//private ArrayList arcs = new ArrayList(), edges = new ArrayList();

	public void validateNETFormat(File fileHandler) throws FileNotFoundException, IOException {
		currentLine = 0;
		totalNumOfNodes = 0;
		numVertices = 0;
		numArcs = 0;
		numEdges = 0;

		BufferedReader reader =
			new BufferedReader(new UnicodeReader(new FileInputStream(fileHandler)));
		this.processFile(reader);
	}

	public boolean isDirectedGraph(){
		if (hasHeader_Arcs &&
				numArcs>0)
			return true;
		else
			return false;
	}

	public boolean isUndirectedGraph(){
		if(hasHeader_Edges &&
				numEdges>0)
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
	public List getVertexAttrList(){
		return NETVertex.getVertexAttributes();
	}

	public List getEdgeAttrList(){
		return NETArcsnEdges.getArcsnEdgesAttributes();
	}

	public List getArcAttrList() {
		return NETArcsnEdges.getArcsnEdgesAttributes();
	}

	public int getTotalNumOfNodes(){
		return totalNumOfNodes;
	}

	public boolean getHasTotalNumOfNodes (){
		return hasTotalNumOfNodes;
	}

	public int getNumVertices(){
		return this.numVertices;
	}
	public int getNumArcs(){
		return this.numArcs;
	}
	public int getNumEdges(){
		return this.numEdges;
	}


	/*
	 *
	 *  validateVertexHeader takes in a string and checks to see if it starts with
	 *  *vertices with an optional integer following the vertices declaration.
	 *
	 *  Written by: Tim Kelley
	 *  Date: May 17th 2007
	 */


	public boolean validateVertexHeader(String s) {
		//String s = r.readLine();

		s = s.toLowerCase();
		//this.errorMessages.append(s+"\n");
		if(s.startsWith(NETFileProperty.HEADER_VERTICES)){
			this.hasHeader_Vertices = true;
			this.inVerticesSection = true;
			this.inEdgesSection = false;
			this.inArcsSection = false;

			//this.vertices = new ArrayList();
			numVertices = 0;
			StringTokenizer st= new StringTokenizer(s);

			if (st.countTokens()>1){
				st.nextToken();
				//*****If it is not an integer...
				this.totalNumOfNodes = new Integer(st.nextToken()).intValue();
				//System.out.println(this.totalNumOfNodes);
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

			numArcs = 0;
			if(numVertices == 0)
				this.skipNodeList = true;
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
			numEdges = 0;
			if(numVertices == 0)
				this.skipNodeList = true;
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
		try {
			nv = new NETVertex(s);
		} catch (NETFileFormatException e) {
			isFileGood = false;
			errorMessages.append(
					"*Wrong NET format at line "+currentLine+".\n"+
					e.getMessage()+"\n\n");
		}

		return nv;
	}


	public NETArcsnEdges processArcsnEdges(String s){
		NETArcsnEdges nae = null;

		try {
			nae = new NETArcsnEdges(s);
		} catch (NETFileFormatException e){
			isFileGood = false;
			errorMessages.append(
					"*Wrong NET format at line "+currentLine+".\n"+
					e.toString()+"\n\n");
		}

		return nae;
	}


	public void processFile(BufferedReader reader) throws IOException {
		NETVertex.clearAttributes();
		NETArcsnEdges.clearAttributes();
		String line = reader.readLine();
		while (line != null && isFileGood){
			currentLine++;
			line = line.trim();

			if(line.startsWith(NETFileProperty.PREFIX_COMMENTS) || (line.length() < 1)){
				line = reader.readLine();
				continue;
			}

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
				//System.out.println(line);
				continue;
			}


			if(inVerticesSection && isFileGood){

				processVertices(line);
				numVertices++;
				line = reader.readLine();

				continue;
			}

			if(inEdgesSection && isFileGood){
				processArcsnEdges(line);
				numEdges++;
				line = reader.readLine();

				continue;
			}

			if(inArcsSection && isFileGood){
				processArcsnEdges(line);
				numArcs++;
				line = reader.readLine();

				continue;
			}


			line = reader.readLine();
		}

		if (isFileGood) {
			this.checkFile();
		}

		if (this.hasTotalNumOfNodes && !this.skipNodeList && (this.totalNumOfNodes != this.numVertices)){
			this.errorMessages.append("The stated total number of vertices (" +
					this.totalNumOfNodes+") does not match the calculated number of vertices ("+
					this.numVertices+")");
			isFileGood = false;
		}

		if (!this.hasTotalNumOfNodes) {
			this.totalNumOfNodes = this.numVertices;
		}
	}

	public void checkFile(){
		if (!this.hasHeader_Vertices){
			this.isFileGood = false;
			this.errorMessages.append("*The file does not specify the Vertex header.\n\n");
		}else if (!hasTotalNumOfNodes && skipNodeList){
			this.isFileGood = false;
			this.errorMessages.append("*The file does not specify the total number of vertices and "+
			"does not list all vertices.\n\n");
		}else if (!(this.hasHeader_Arcs || this.hasHeader_Edges)) {
			isFileGood = false;
			this.errorMessages.append("This file does not have the correct header to specify Arcs or Edges");
		}
/*		else if (this.hasTotalNumOfNodes && this.skipNodeList){
			for(int i = 0; i < this.totalNumOfNodes; i++){
				String s = (i+1) + " \"" + (i+1) + "\"";
				try{
					NETVertex nv = new NETVertex(s);

					this.vertices.add(nv);
				}
				catch(Exception e){
					e.printStackTrace();
					isFileGood = false;
				}
			}
		}*/
	}
}




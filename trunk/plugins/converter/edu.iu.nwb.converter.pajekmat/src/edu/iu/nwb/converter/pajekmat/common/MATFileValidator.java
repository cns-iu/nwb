package edu.iu.nwb.converter.pajekmat.common;

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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.UnicodeReader;

import sun.util.logging.resources.logging;

public class MATFileValidator {
	private boolean hasHeader_Vertices = false;
	private boolean hasHeader_Matrix = false;

	private boolean isFileGood = true;
	private boolean inVertexSection = false;
	private boolean inMatrixSection = false;

	private boolean hasTotalNumOfNodes = false;
	private boolean skipNodeList = false;

	private int totalNumOfNodes, currentLine;
	private StringBuffer errorMessages = new StringBuffer();
	private ArrayList vertices = new ArrayList();
	private ArrayList arcs = new ArrayList(); 

	public void validateMATFormat(File fileHandler)
			throws FileNotFoundException, IOException, AlgorithmExecutionException {
		currentLine = 0;
		totalNumOfNodes = 0;
		
		BufferedReader reader =
			new BufferedReader(new UnicodeReader(new FileInputStream(fileHandler)));
		this.processFile(reader);
	}
	
	public boolean getValidationResult(){
		return isFileGood;
	}
	
	public String getErrorMessages(){
		return errorMessages.toString();
	}
	
	public List getVertexAttrList(){ 
		return MATVertex.getVertexAttributes();
	}

	public List getEdgeAttrList(){
		return MATArcs.getArcsnEdgesAttributes();
	}

	public List getArcAttrList() {
		return MATArcs.getArcsnEdgesAttributes();
	}

	public int getTotalNumOfNodes(){
		return totalNumOfNodes;
	}

	public boolean getHasTotalNumOfNodes (){
		return hasTotalNumOfNodes;
	}

	public ArrayList getVertices(){
		return this.vertices;
	}
	
	public ArrayList getArcs(){
		return this.arcs;
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
		if(s.startsWith(MATFileProperty.HEADER_VERTICES)){
			this.hasHeader_Vertices = true;
			inMatrixSection = false;
			inVertexSection = true;
			this.vertices = new ArrayList();
			StringTokenizer st= new StringTokenizer(s);
			//System.out.println(s);
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


	public boolean validateMatrixHeader(String s){
		s = s.toLowerCase();
		if(s.startsWith(MATFileProperty.HEADER_MATRIX)){
			hasHeader_Matrix = true;
			inMatrixSection = true;
			inVertexSection = false;
			this.arcs = new ArrayList();
			
			if(this.vertices.isEmpty() && this.hasTotalNumOfNodes)
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

	public MATVertex processVertices(String s){
		MATVertex nv = null;
		try{
			nv = new MATVertex(s);
			//	System.out.println(nv);

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

		return nv;
	}

	public void processMatrix(BufferedReader br, int lineNumber, String initial) throws AlgorithmExecutionException {

		String errorMessage;
		String ss = initial;
		int i = 0;
		try{
			while(ss  != null){
				//String ss = br.readLine();
				//System.out.println(ss);
				String[] connections = MATFileFunctions.processTokens(ss);
				if(connections.length != this.totalNumOfNodes){
					this.isFileGood = false;
					errorMessage = "The connection matrix does not match the number of vertices. There are " +
					this.totalNumOfNodes + " vertices, and the matrix specifies " + (connections.length-1) + " connections";
					errorMessages.append(errorMessage);
					break;
				}
				if(this.hasTotalNumOfNodes && this.skipNodeList){
				for(int j = 0; j < connections.length; j++){
					float f = MATFileFunctions.asAFloat(connections[j]);
					if(f > 0){
						String s = new Integer(i+1).toString() + " " + new Integer(j+1).toString() + " " +
						connections[j];
						this.arcs.add(new MATArcs(s));
					}
				}
				}
				else {
					int source, target;
					for(int j = 0; j < connections.length; j++){
						float f = MATFileFunctions.asAFloat(connections[j]);
						if(f > 0){
							source = ((MATVertex)this.vertices.get(i)).getID();
							target = ((MATVertex)this.vertices.get(j)).getID();
							String s = source + " " + target + " " + connections[j];
							//System.out.println(s);
							this.arcs.add(new MATArcs(s));
						}
					}
				}
				ss = br.readLine();
				lineNumber++;
				i++;
			}
		} catch(IOException ex) {
			isFileGood = false;
			errorMessage = "Error reading connection matrix at line: " + lineNumber +
			". Chances are there are not enough rows." +" Read " + i + 
			" lines, but expected to read " + this.totalNumOfNodes + " lines.";
			errorMessages.append(errorMessage);
		}
//		catch(Exception ex) {
//			isFileGood = false;
//			errorMessages.append(ex.getMessage());
//			ex.printStackTrace();
//		}
	}



	public void processFile(BufferedReader reader) throws IOException, AlgorithmExecutionException {
		MATVertex.clearAttributes();
		MATArcs.clearAttributes();
		String line = reader.readLine();
		while (line != null && isFileGood){
			currentLine++;
			//System.out.println(this.skipNodeList);
			line = line.trim();
			if (line.startsWith(MATFileProperty.PREFIX_COMMENTS)
					|| (line.length() < 1)){
				line = reader.readLine();
				continue;
			}

			if (this.validateVertexHeader(line)) {
				line = reader.readLine();

				continue;
			}

			if (this.validateMatrixHeader(line)) {
				line = reader.readLine();
				continue;
			}
			
			if (inVertexSection && isFileGood) {
				this.vertices.add(processVertices(line));
				line = reader.readLine();
				continue;
			}

			if (inMatrixSection && isFileGood) {
				processMatrix(reader, currentLine, line);
				line = reader.readLine();
				continue;
			}


			line = reader.readLine();
		}

		if (isFileGood){
			this.checkFile();			
		}
		this.totalNumOfNodes = this.vertices.size();

	}

	public void checkFile(){
		if(!this.hasHeader_Matrix){
			this.isFileGood = false;
			this.errorMessages.append("This file is a .mat file but does not have a *matrix line");
		}
		else if (!this.hasHeader_Vertices){
			this.isFileGood = false;
			this.errorMessages.append("*The file does not specify the Vertex header.\n\n");
		}else if (!hasTotalNumOfNodes && skipNodeList){
			this.isFileGood = false;
			this.errorMessages.append("*The file does not specify the total number of vertices and "+
			"does not list all vertices.\n\n");
		}
		else if (this.hasTotalNumOfNodes && this.skipNodeList){
			for(int i = 0; i < this.totalNumOfNodes; i++){
				String s = (i+1) + " \"" + (i+1) + "\"";
				try{
					//System.out.println("Creating a new MATVertex with" + s);
					MATVertex nv = new MATVertex(s);
					//	System.out.println(nv);
					this.vertices.add(nv);
				} catch(Exception e) {
					isFileGood = false;
				}
			}
		}

	}




}




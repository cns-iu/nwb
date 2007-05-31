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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ValidateNETFile {
	private boolean hasHeader_Vertices = false;
	private boolean hasHeader_Edges = false;
	private boolean hasHeader_Arcs = false;

	
//	private boolean hasHeader_NodeAttributes = false;
//	private boolean hasHeader_EdgeAttributes = false;
	private boolean isFileGood = true;
	private boolean inVerticesSection = false;
	private boolean inEdgesSection = false;
	private boolean inArcsSection = false;
	private boolean skipNodeList = true;
	private boolean hasTotalNumOfNodes = false;
	//if true, it indicates the previous line is a section header
	//If is useful to parse the attribute line
	private boolean passHeader = false;

	private int totalNumOfNodes, currentLine;
	private StringBuffer errorMessages = new StringBuffer();
	private Map<String, String>vertexAttrList;
	private Map<String, String>arcAttrList, edgeAttrList; 

	public void validateNETFormat(File fileHandler) throws FileNotFoundException, IOException {
		currentLine = 0;
		BufferedReader reader = 
			new BufferedReader(new FileReader(fileHandler));
		this.processFile(reader);

	}

	private boolean validateALine (String line, List<NETAttribute> attrList) throws Exception {
		//this.errorMessages.append(attrList.size()+"\n");
		if (line.length()<=0)
			return true;
		StringTokenizer st = new StringTokenizer(line);
		int totalTokens = st.countTokens();
		if (totalTokens<=0)
			return true;
		if (totalTokens<attrList.size())
			throw new Exception ("Did not specify all values for defined attributes!");
		String[] columns = processTokens(st);
		for (int i=0; i< attrList.size(); i++){
			NETAttribute netAttr = attrList.get(i);
			String dt = netAttr.getDataType();
			if (dt.equalsIgnoreCase(NETFileProperty.TYPE_STRING)){
				isAString(columns[i],netAttr.getAttrName());
			}
			else if (dt.equalsIgnoreCase(NETFileProperty.TYPE_INT)){
				isAnInteger(columns[i],netAttr.getAttrName());
			}
			else if (dt.equalsIgnoreCase(NETFileProperty.TYPE_FLOAT)){
				isAFloat(columns[i],netAttr.getAttrName());
			}			
		}
		return true;

	}  

	private boolean isAnInteger(String input, String attr) throws NumberFormatException, Exception{
		Integer value = new Integer (input);    
		if (attr.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_ID) ||
				attr.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_SOURCE) ||
				attr.equalsIgnoreCase(NETFileProperty.ATTRIBUTE_TARGET) ){
			if (value.intValue()<1)
				throw new Exception("The node id must be greater than 0.");
		}    		
		return true;    	
	}    

	private boolean isAString(String input, String attr) throws Exception {
		if (!input.startsWith("\"") || !input.endsWith("\"")) {
			throw new Exception("A string value must be surrounded by double quatation marks.");
		}    	
		return true;
	}

	private boolean isAFloat (String input, String attr) throws NumberFormatException, Exception {
		Float f = new Float (input);
		f.floatValue();
		return true;
	}

	private NETAttribute processAttrToken (String token) throws Exception {
		if (token.indexOf("*")!= -1){
			String attr_name = token.substring(0, token.indexOf("*"));
			if (attr_name.startsWith("//"))
				attr_name= attr_name.substring(2);
			String attr_type = token.substring(token.indexOf("*")+1);    		
			if (!(attr_type.equalsIgnoreCase(NETFileProperty.TYPE_FLOAT) ||
					attr_type.equalsIgnoreCase(NETFileProperty.TYPE_INT) ||
					attr_type.equalsIgnoreCase(NETFileProperty.TYPE_STRING))){
				throw new Exception ("The data type of the attribute "+attr_name+
				" is not acceptable. Only float, int and string are valid data types in the NWB format.");
			}
			else return new NETAttribute(attr_name, attr_type);    	
		}
		else
			throw new Exception ("Can not find * from attribut*datatype line.");
	}

	public String[] processTokens(String s){
		String[] tokens = s.split("\\s+");
		ArrayList<String> sl = new ArrayList<String>();
		StringBuffer bf = new StringBuffer();
		boolean append = false;
		for(String st: tokens){
			if(!append){
				if(!st.startsWith("\"")){
					sl.add(st);
				}
				else if (st.startsWith("\"") && st.endsWith("\"")){
					sl.add(st);
				}
				else {
					append = true;
					bf.append(st);
				}
			}
			else{
				if(st.endsWith("\\\"") ||
						!st.endsWith("\"")	){
					bf.append(" "+st);
				}
				else if (st.endsWith("\"")){    				
					bf.append(" "+st);
					sl.add(bf.toString());
					bf = new StringBuffer();
					append=false;
				}

			}
		}
		tokens = new String[sl.size()];
		return sl.toArray(tokens);
	}


	public String[] processTokens(StringTokenizer st){
		int total = st.countTokens();
		int tokenIndex =0;
		String [] tokens = new String [total];
		StringBuffer bf = new StringBuffer();
		boolean append = false;
		for (int index =0; index<total; index++){
			String element = st.nextToken();
			//System.out.print("\""+element+"\"\t");
			if (!append){
				if (!element.startsWith("\"")){
					tokens[tokenIndex]= element;
					tokenIndex++;

				}
				else if (element.startsWith("\"") &&
						element.endsWith("\"")){
					tokens[tokenIndex]= element;
					tokenIndex++;

				}
				else{
					append = true;
					bf.append(element);
				}    			
			}
			else {
				if(element.endsWith("\\\"") ||
						!element.endsWith("\"")	){
					bf.append(" "+element);
				}
				else if (element.endsWith("\"")){    				
					bf.append(" "+element);
					tokens[tokenIndex]= bf.toString();
					tokenIndex++;
					bf = new StringBuffer();
					append=false;
				}
			}
		}

		//	System.out.println();

		return tokens;


	}


	public boolean isDirectedGraph(){
		if (hasHeader_Arcs &&
				arcAttrList.size()>0)
			return true;
		else
			return false;
	}

	public boolean isUndirectedGraph(){
		if(hasHeader_Edges &&
				edgeAttrList.size()>0)
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
	public ArrayList<NETAttribute> getVertexAttrList(){ 
		ArrayList<NETAttribute> attributeList = new ArrayList<NETAttribute>();
		for(String s : this.vertexAttrList.keySet()){
			attributeList.add(new NETAttribute(s,this.vertexAttrList.get(s)));
		}
		return attributeList;
	}

	public ArrayList<NETAttribute> getEdgeAttrList(){
		ArrayList<NETAttribute> attributeList = new ArrayList<NETAttribute>();
		for(String s : this.edgeAttrList.keySet()){
			attributeList.add(new NETAttribute(s, this.edgeAttrList.get(s)));
		}
		return attributeList;
	}

	public ArrayList<NETAttribute> getArcAttrList() {
		ArrayList<NETAttribute> attributeList = new ArrayList<NETAttribute>();
		for(String s: this.arcAttrList.keySet()){
			attributeList.add(new NETAttribute(s, this.edgeAttrList.get(s)));
		}
		return attributeList;
	}

	public int getTotalNumOfNodes(){
		return totalNumOfNodes;
	}

	public boolean getHasTotalNumOfNodes (){
		return hasTotalNumOfNodes;
	}

	public boolean getSkipNodeList(){
		return skipNodeList; 
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
			this.passHeader = true;
			this.vertexAttrList = new LinkedHashMap<String, String>();
			//System.out.println("In the Vertices Section");
			//get the total number of nodes
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
			arcAttrList = new LinkedHashMap<String, String>();
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
			edgeAttrList = new LinkedHashMap<String, String>();
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

	public void processVertices(String s){
		s = s.toLowerCase();
		/*for(NETAttribute na : this.getVertexAttrList()){
			System.out.println(na.getAttrName()+ ":  :" + na.getDataType());
		}
		System.out.println("-----");*/
		
		/*
		 * 
		 * This will process the node list, there are no headers in .net so we don't check for that
		 * 
		 */
			if (s.startsWith(NETFileProperty.PREFIX_COMMENTS)) {

			}
			else {
				skipNodeList=false;
				String[] columns = processTokens(s);
				int realSize = columns.length;

				try{
					/* 
					 * This code will be moved into another function, too much playing around with indices.
					 */

					//this.errorMessages.append(s+"\n"+realSize+"\n");
					if(realSize < 2){
						isFileGood = false;
						errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
						"Each Vertex must have an ID and label\n\n");	
					}
					else if(realSize == 2){
						this.testVertexID(columns);
					}
					else {

						this.testVertices(columns);



					}

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

				passHeader = false;	
			}

		}
	


	public void processEdges(String s){
		s = s.toLowerCase();
		if(this.passHeader){
			if(this.parseAttributes(s, this.getEdgeAttrList(), NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.PREFIX_COMMENTS+
					NETFileProperty.ATTRIBUTE_SOURCE)){
				this.passHeader = false;
			}
			else if (s.startsWith(NETFileProperty.PREFIX_COMMENTS)){

			}
			else {
				StringTokenizer st = new StringTokenizer(s);
				String[] columns = processTokens(st);						

				if (columns.length == 2)	{
					try{
						isAnInteger(columns[0], NETFileProperty.ATTRIBUTE_SOURCE);
						isAnInteger (columns[1], NETFileProperty.ATTRIBUTE_TARGET);
						NETAttribute netAttr1 = new NETAttribute
						(NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.TYPE_INT);
						NETAttribute netAttr2 = new NETAttribute
						(NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.TYPE_INT);

						edgeAttrList.put(netAttr1.getAttrName(), netAttr1.getDataType());
						edgeAttrList.put(netAttr2.getAttrName(), netAttr2.getDataType());
						

					}catch (NumberFormatException nfe){
						isFileGood = false;
						errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
						"Vertex id must be an integer and bigger than 0.\n\n");
					}catch (Exception e){
						isFileGood = false;
						errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
								e.toString()+"\n\n");
					}
				}else {
					isFileGood = false;
					errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
							"The edge section that does not specify the attribute name and data type " +
					"can only have two columns: source*int target*int. \n\n");							
				}

				passHeader = false;
			}
		}
		else {// process node list
			//based on nodeAttrList to detect each node item in the node list
			//basically, make sure if there's a value, the value belongs to the declared
			//data type. If it is a string, it must be surrounded by double quotations
			try{

				validateALine (s, this.getEdgeAttrList());								
			}						
			catch (Exception e){
				isFileGood = false;
				errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
						e.toString()+"\n\n");								
			}				
		}	


	}

	public void processArcs(String s){
		s = s.toLowerCase();
		if(this.passHeader){
			if(this.parseAttributes(s, this.getArcAttrList(), NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.PREFIX_COMMENTS+
					NETFileProperty.ATTRIBUTE_SOURCE)){
				this.passHeader = false;
			}
			else if (s.startsWith(NETFileProperty.PREFIX_COMMENTS)){

			}
			else {
				StringTokenizer st = new StringTokenizer(s);
				String[] columns = processTokens(st);						

				if (columns.length == 2)	{
					try{
						isAnInteger(columns[0], NETFileProperty.ATTRIBUTE_SOURCE);
						isAnInteger (columns[1], NETFileProperty.ATTRIBUTE_TARGET);
						NETAttribute netAttr1 = new NETAttribute
						(NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.TYPE_INT);
						NETAttribute netAttr2 = new NETAttribute
						(NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.TYPE_INT);
						
						
						arcAttrList.put(netAttr1.getAttrName(), netAttr1.getDataType());
						arcAttrList.put(netAttr2.getAttrName(), netAttr2.getDataType());
						
					}catch (NumberFormatException nfe){
						isFileGood = false;
						errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
						"Vertex id must be an integer and bigger than 0.\n\n");
					}catch (Exception e){
						isFileGood = false;
						errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
								e.toString()+"\n\n");
					}
				}else {
					isFileGood = false;
					errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
							"The edge section that does not specify the attribute name and data type " +
					"can only have two columns: source*int target*int. \n\n");							
				}

				passHeader = false;
			}
		}
		else {// process node list
			//based on nodeAttrList to detect each node item in the node list
			//basically, make sure if there's a value, the value belongs to the declared
			//data type. If it is a string, it must be surrounded by double quotations
			try{

				validateALine (s, this.getArcAttrList());								
			}						
			catch (Exception e){
				isFileGood = false;
				errorMessages.append("*Wrong NET format at line "+currentLine+".\n"+
						e.toString()+"\n\n");								
			}				
		}	


	}

	public boolean parseAttributes(String parseString, List<NETAttribute> nal,String...strings){
		parseString = parseString.toLowerCase();
		for(String s : strings){
			if(parseString.startsWith(s)){
				StringTokenizer st = new StringTokenizer(s.trim());	//Get the string, trim off the trailing whitespace
				int totalTokens= st.countTokens();
				for (int i = 1; i<=totalTokens; i++){
					//process token
					try {
						NETAttribute attr = processAttrToken(st.nextToken());  	//if we have an attribute list in the file
						nal.add(attr);									//this adds the attributes to to our list.
					}catch (Exception e) {										
						isFileGood = false;
						errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
						"e.toString()\n\n");								
						break;
					}							

				}
				return true;
			}

		}
		return false;
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

				processVertices(line);
				line = reader.readLine();

				continue;
			}

			if(inEdgesSection && isFileGood){
				processEdges(line);
				line = reader.readLine();

				continue;
			}

			if(inArcsSection && isFileGood){
				processArcs(line);
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
		}else if (!hasTotalNumOfNodes && skipNodeList){
			this.isFileGood = false;
			this.errorMessages.append("*The file does not specify the total number of vertices and "+
			"does not list all vertices.\n\n");
		}else if (!this.hasHeader_Arcs && !this.hasHeader_Edges) {
			isFileGood = false;				
		} 
		else if (this.hasTotalNumOfNodes && this.skipNodeList){
			NETAttribute netAttr = new NETAttribute
			(NETFileProperty.ATTRIBUTE_ID, NETFileProperty.TYPE_INT);
			
			this.vertexAttrList.put(netAttr.getAttrName(),netAttr.getDataType());
			netAttr = new NETAttribute("label", NETFileProperty.TYPE_STRING);
			this.vertexAttrList.put(netAttr.getAttrName(), netAttr.getDataType());
			
		}	
	}

	public void testVertexID(String[] s) throws Exception{
		try
		{
			isAnInteger(s[0], NETFileProperty.ATTRIBUTE_ID);
			isAString (s[1], NETFileProperty.ATTRIBUTE_LABEL);
			NETAttribute netAttr = new NETAttribute
			(NETFileProperty.ATTRIBUTE_ID, NETFileProperty.TYPE_INT);	
			
			vertexAttrList.put(netAttr.getAttrName(), netAttr.getDataType());
			netAttr = new NETAttribute(NETFileProperty.ATTRIBUTE_LABEL, NETFileProperty.TYPE_STRING);
			vertexAttrList.put(netAttr.getAttrName(), netAttr.getDataType());
			
		}
		catch(Exception ex){
			throw ex;
		}
	}

	public boolean testVertexPosition(String...st) throws Exception{
		int length = st.length;
		boolean value = false;
		float f = 0;
		int i = 0;
		try{
			for(i = 0; i < length; i++){
				f = Float.parseFloat(st[i]);
				if((f > 0) && (f < 1)){  //we should actually make this better to handle round off errors.
				value = true;
				switch (i){
				case 0:
					this.vertexAttrList.put("Xpos", "float");
					break;
				case 1:
					this.vertexAttrList.put("Ypos", "float");
					break;
				case 2:
					this.vertexAttrList.put("Zpos", "float");
					break;
				}
				}
				else {
					value = false;
					throw new Exception("Positional data must be between 0.0 and 1.0");
				}
				
			}
			
		}
		catch(NumberFormatException ex){
			if((i > 0) && (i < 3)){
				value = false;
				throw new Exception("The file contains an invalid sequence in the positional data.");
			}
			else
				value = false;
		}

		
		return value;
	}

	public boolean testVertexShape(String st) throws Exception{
		boolean value = false;
		try {
			for(String s : NETFileShape.ATTRIBUTE_SHAPE_LIST){
				if(st.equalsIgnoreCase(s)){
					
					
					this.vertexAttrList.put("Shape", "string");
					return true;
				}
			}
		}
		catch(Exception ex){
			throw ex;
		}
		return value;
	}

	public boolean testVertexDefaults(Queue<String> qs) throws Exception{
		
		boolean value = true;
		while(!qs.isEmpty()){
			String s1 = qs.poll();
			
			if(qs.isEmpty()){
				value = false;
				throw new Exception("Expected a value for parameter: " + s1);
			}
			String s2 = qs.poll();
			

			boolean b1 = false ,b2 = false;
			if(isInList(s1,NETFileParameter.VERTEX_NUMBER_PARAMETER_LIST)){
				
				try{
					 b1 = this.isAFloat(s2, NETFileProperty.TYPE_FLOAT);
					 b2 = this.isAnInteger(s2, NETFileProperty.TYPE_INT);
				}
					 
				catch(NumberFormatException ex){
				}
					if(!(b1 || b2)){
						value = false;
						throw new Exception("Unexpected value: " + s2 + " for parameter " + s1);
					}
				
			}
			else if(isInList(s1,NETFileParameter.VERTEX_COLOR_PARAMETER_LIST)){
				
				
					try{
						b1 = this.isAFloat(s2, NETFileProperty.TYPE_FLOAT);
						b2 = this.isAnInteger(s2, NETFileProperty.TYPE_INT);
					
					}
					catch(NumberFormatException ex){

					}
					if((b1 || b2)){
						value = false;
						throw new Exception("Expected a String but received a number ("+ s2 + ") for parameter: " + s1);

					}
					
				} 
			else if(s1.startsWith(NETFileProperty.PREFIX_COMMENTS)){
				break;
			}
			
			else {
				value = false;
				throw new Exception("Unknown parameter: " + s1);
			}
		}

		return value;
	}

	public boolean testVertices(String...strings) throws Exception{
		/*for(NETAttribute na : this.vertexAttrList){
			System.out.println(na.getAttrName() + " " + na.getDataType());
		}*/
		String[] sa;
		boolean value = true;
		boolean checkPos = true;
		Queue<String> stringQueue = new ConcurrentLinkedQueue<String>();
		for(String s : strings){
			//System.out.println(s);
			stringQueue.add(s);
		}
		ArrayList<String> testConditions = new ArrayList<String>();
		testConditions.add(stringQueue.poll());
		testConditions.add(stringQueue.poll());

		try{
			sa = new String[testConditions.size()];
			this.testVertexID(testConditions.toArray(sa));

		}
		catch(Exception ex){
			value = false;
			throw ex;
		}
		testConditions.clear();
		try {
			String s = stringQueue.peek();
			try{
				this.isAFloat(s, null);
			} catch(NumberFormatException ex){
				checkPos = false;
			}
			if(checkPos){
				if(stringQueue.size() > 2){
					for(int i = 0; i < 3; i++){
						testConditions.add(stringQueue.poll());
					}
				}
				else {
					value = false;
					throw new Exception("If the file contains positional data, it must contain positional data for three dimensions.");
				}
				sa = new String[testConditions.size()];

				this.testVertexPosition((String[])testConditions.toArray(sa));
				if(!this.testVertexShape(stringQueue.peek())){

					testVertexDefaults(stringQueue);
				}
				else {
					stringQueue.poll();
					testVertexDefaults(stringQueue);
				}
			}
			else{
				String str = stringQueue.peek();
				if(!this.testVertexShape(str)){

					testVertexDefaults(stringQueue);
				}
				else {
					
					stringQueue.poll();
					testVertexDefaults(stringQueue);
				}
			}

		}
		catch(Exception ex){
			throw ex;
		}

		return value;
	}

	private static boolean isInList(String s, String...strings){
		boolean value = false;
		if(strings != null){
			for(String st : strings){
				if(s.equalsIgnoreCase(st))
					return true;
			}
		}
		return value;
	}


}




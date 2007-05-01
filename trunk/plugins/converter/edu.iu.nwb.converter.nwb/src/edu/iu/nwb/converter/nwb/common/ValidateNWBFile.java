package edu.iu.nwb.converter.nwb.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;

public class ValidateNWBFile {
	private boolean hasHeader_Nodes = false;
	private boolean hasHeader_UndirectedEdges = false;
	private boolean hasHeader_DirectedEdges = false;
//	private boolean hasHeader_NodeAttributes = false;
//	private boolean hasHeader_EdgeAttributes = false;
	private boolean isFileGood = true;
	private boolean inNodesSection = false;
	private boolean inUndirectededgesSection = false;
	private boolean inDirectededgesSection = false;
	private boolean skipNodeList = true;
	private boolean hasTotalNumOfNodes = false;
	//if true, it indicates the previous line is a section header
	//If is useful to parse the attribute line
	private boolean passHeader = false;
	
	private int totalNumOfNodes, currentLine;
	private StringBuffer errorMessages = new StringBuffer();
	private List nodeAttrList, directedEdgeAttrList, undirectedEdgeAttrList; 
	

	
	/*
	 * 
	 */
	public void validateNWBFormat(File fileHandler) 
    		throws FileNotFoundException, IOException    {
		currentLine = 0;
		BufferedReader reader = 
			new BufferedReader(new FileReader(fileHandler));
		String line = reader.readLine();
				
		while (line != null && isFileGood){
			currentLine++;
			String line_lower = line.toLowerCase();
			
			//process section header that looks like
			//  *nodes   or  *nodes 1000
			if(line_lower.startsWith(NWBFileProperty.HEADER_NODE)) 
			{
				hasHeader_Nodes = true;
				inNodesSection = true;
				inDirectededgesSection = false;
				inUndirectededgesSection = false;
				passHeader = true;
				nodeAttrList = new ArrayList();
				
				//get the total number of nodes
				StringTokenizer st= new StringTokenizer(line);
				if (st.countTokens()>1){
					st.nextToken();
					//*****If it is not an integer...
					totalNumOfNodes = new Integer(st.nextToken()).intValue();
					hasTotalNumOfNodes = true;
				}
				else {
					hasTotalNumOfNodes = false;
				}
				line = reader.readLine();
				continue;
			}
			
			if(line_lower.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES)) 
			{
				hasHeader_DirectedEdges = true;
				inDirectededgesSection = true;
				inNodesSection = false;
				inUndirectededgesSection = false;
				passHeader = true;
				directedEdgeAttrList = new ArrayList();
				line = reader.readLine();
				continue;
				
			}

			if(line_lower.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES)) 
			{
				hasHeader_UndirectedEdges = true;
				inUndirectededgesSection =true;
				inNodesSection = false;
				inDirectededgesSection = false;	
				passHeader = true;
				undirectedEdgeAttrList = new ArrayList();
				line = reader.readLine();
				continue;
			}
			
			if (inNodesSection && isFileGood){
				if (passHeader){//if previous line is a node header
					/* get attribute line
					 * handle four cases:
					 * 1.    id*int	label*string	attr3*dataType	....
					 * 2.  //id*int	label*string	attr3*dataType	....
					 * 3. without node list, but have a total number of nodes
					 * 4. without an attribute line, but have two column node id and label list
					 * won't accept an attribute list without data type declarations
					 * such as  id	label or //id	label
					 */					
					if (line_lower.startsWith(NWBFileProperty.ATTRIBUTE_ID)||
						line_lower.startsWith(NWBFileProperty.PREFIX_COMMENTS+
											NWBFileProperty.ATTRIBUTE_ID))
					{
						//process attribut line
						StringTokenizer st = new StringTokenizer(line);						
						for (int i = 1; i<=st.countTokens(); i++){
							//process token
							try {
								NWBAttribute attr = processAttrToken(st.nextToken());
								nodeAttrList.add(attr);
							}catch (Exception e) {
								isFileGood = false;
								errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
										"e.toString()\n\n");								
								break;
							}							
							
						}
						passHeader = false;
					}
					// ignore other comments
					else if (line_lower.startsWith(NWBFileProperty.PREFIX_COMMENTS)) {
						line = reader.readLine();
						continue;						
					}
					// assum the default attributes and data types
					// only accept two columns and assume the first column is id and second column is label
					// for backward compatibility
					else 
					{	
						StringTokenizer st = new StringTokenizer(line);	
						String[] columns = processTokens(st);
						int realSize = 0;
						for (int i=0; i<columns.length; i++){
							if (columns[i] != null)
								realSize++;
						}
						
						if (realSize == 2)	{
							try{
								isAnInteger(columns[0], NWBFileProperty.ATTRIBUTE_ID);
								isAString (columns[1], "label");
								NWBAttribute nwbAttr = new NWBAttribute
										(NWBFileProperty.ATTRIBUTE_ID, NWBFileProperty.TYPE_INT);								
								nodeAttrList.add(nwbAttr);
								nwbAttr = new NWBAttribute("label", NWBFileProperty.TYPE_STRING);
								nodeAttrList.add(nwbAttr);
							}catch (NumberFormatException nfe){
								isFileGood = false;
								errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
										"Node id must be an integer and bigger than 0.\n\n");
							}catch (Exception e){
								isFileGood = false;
								errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
										e.toString()+"\n\n");
							}
						}else {
							isFileGood = false;
							errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
								"The node section that does not specify the attribute name and data type " +
								"can only have two columns: id*int label*string. \n\n");							
						}
						
						passHeader = false;
					}
				}
				else {// process node list
					//based on nodeAttrList to detect each node item in the node list
					//basically, make sure if there's a value, the value belongs to the declared
					//data type. If it is a string, it must be surrounded by double quotations
					try{
						validateALine (line, nodeAttrList);
					}catch (Exception e){
						isFileGood = false;
						errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
							e.toString()+"\n\n");								
					}				
				}
			}
			if ((inDirectededgesSection || inUndirectededgesSection) && isFileGood){
				if (passHeader){//if previous line is an edge header
					/* get attribute line
					 * handle four cases:
					 * 1.    source*int	target*int	attr3*dataType ....
					 * 2.  //source*int	target*int	attr3*dataType	....
					 * 3. without an attribute line, but have two columns source id and target id list
					 * won't accept an attribute list without data type declarations
					 * such as  id	label or //id	label
					 * won't accept three columns: source, target, weight, because the program can not 
					 * assum the data type of weight, could be integer or float.
					 */					
					if (line_lower.startsWith(NWBFileProperty.ATTRIBUTE_SOURCE)||
						line_lower.startsWith(NWBFileProperty.PREFIX_COMMENTS+
											NWBFileProperty.ATTRIBUTE_SOURCE))
//					if (line_lower.startsWith("//source"))
					{
						//process attribut line
						StringTokenizer st = new StringTokenizer(line);
						int tokens = st.countTokens();
						for (int i = 1; i<=tokens; i++){
							String token = st.nextToken();
							//process token
							try {
								NWBAttribute attr = processAttrToken(token);
								if (inDirectededgesSection)
									directedEdgeAttrList.add(attr);
								else 
									undirectedEdgeAttrList.add(attr);
							}catch (Exception e) {
								isFileGood = false;
								errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
										e.toString()+"\n\n");								
								break;
							}							
							
						}
						passHeader = false;
					}
					// ignore other comments
					else if (line_lower.startsWith(NWBFileProperty.PREFIX_COMMENTS)) {
						line = reader.readLine();
						continue;						
					}
					// assum the default attributes and data types
					// only accept two columns and assume the first column is source and second column is target
					// for backward compatibility
					else 
					{	
						StringTokenizer st = new StringTokenizer(line);
						String[] columns = processTokens(st);						
						
						if (columns.length == 2)	{
							try{
								isAnInteger(columns[0], NWBFileProperty.ATTRIBUTE_SOURCE);
								isAnInteger (columns[1], NWBFileProperty.ATTRIBUTE_TARGET);
								NWBAttribute nwbAttr1 = new NWBAttribute
										(NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.TYPE_INT);
								NWBAttribute nwbAttr2 = new NWBAttribute
										(NWBFileProperty.ATTRIBUTE_TARGET, NWBFileProperty.TYPE_INT);
								if (inDirectededgesSection){
									directedEdgeAttrList.add(nwbAttr1);
									directedEdgeAttrList.add(nwbAttr2);
								}
								else {
									undirectedEdgeAttrList.add(nwbAttr1);
									undirectedEdgeAttrList.add(nwbAttr2);
								}								
							}catch (NumberFormatException nfe){
								isFileGood = false;
								errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
										"Node id must be an integer and bigger than 0.\n\n");
							}catch (Exception e){
								isFileGood = false;
								errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
										e.toString()+"\n\n");
							}
						}else {
							isFileGood = false;
							errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
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
						if (inDirectededgesSection){
							validateALine (line, directedEdgeAttrList);							
						}
						else {
							validateALine (line, undirectedEdgeAttrList);								
						}						
					}catch (Exception e){
						isFileGood = false;
						errorMessages.append("*Wrong NWB format at line "+currentLine+".\n"+
							e.toString()+"\n\n");								
					}				
				}				
			}			
			line = reader.readLine();		
		}
		
		if (isFileGood){
			if (!hasHeader_Nodes){
				isFileGood = false;
				errorMessages.append("*The file does not specify the node header.\n\n");
			}else if (!hasTotalNumOfNodes && skipNodeList){
				isFileGood = false;
				errorMessages.append("*The file does not specify the total number of nodes and "+
					"does not list all nodes.\n\n");
			}else if (!hasHeader_DirectedEdges && !hasHeader_UndirectedEdges) {
				isFileGood = false;				
			} 
			else if (hasTotalNumOfNodes && skipNodeList){
				NWBAttribute nwbAttr = new NWBAttribute
						(NWBFileProperty.ATTRIBUTE_ID, NWBFileProperty.TYPE_INT);								
				nodeAttrList.add(nwbAttr);
				nwbAttr = new NWBAttribute("label", NWBFileProperty.TYPE_STRING);
				nodeAttrList.add(nwbAttr);
			}				
		}
    }
	
	private boolean validateALine (String line, List attrList) throws Exception{
		if (line.length()<=0)
			return true;
		StringTokenizer st = new StringTokenizer(line);
		if (st.countTokens()<=0)
			return true;
		if (st.countTokens()<attrList.size())
			throw new Exception ("Did not specify all values for defined attributes!");
		String[] columns = processTokens(st);
		for (int i=0; i< attrList.size(); i++){
			NWBAttribute nwbAttr = (NWBAttribute) attrList.get(i);
			String dt = nwbAttr.getDataType();
			if (dt.equalsIgnoreCase(NWBFileProperty.TYPE_STRING)){
				isAString(columns[i],nwbAttr.getAttrName());
			}
			else if (dt.equalsIgnoreCase(NWBFileProperty.TYPE_INT)){
				isAnInteger(columns[i],nwbAttr.getAttrName());
			}
			else if (dt.equalsIgnoreCase(NWBFileProperty.TYPE_FLOAT)){
				isAFloat(columns[i],nwbAttr.getAttrName());
			}			
		}
		return true;

	}  
 
    private boolean isAnInteger(String input, String attr) throws NumberFormatException, Exception{
    	Integer value = new Integer (input);    	
    	if (attr.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID) ||
    		attr.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_SOURCE) ||
    		attr.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_TARGET) ){
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
    
    private NWBAttribute processAttrToken (String token) throws Exception {
    	if (token.indexOf("*")!= -1){
    		String attr_name = token.substring(0, token.indexOf("*"));
    		if (attr_name.startsWith("//"))
    			attr_name= attr_name.substring(2);
    		String attr_type = token.substring(token.indexOf("*")+1);    		
    		if (!(attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_FLOAT) ||
    			  attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_INT) ||
    			  attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_STRING))){
    			throw new Exception ("The data type of the attribute "+attr_name+
    			" is not acceptable. Only float, int and string are valid data types in the NWB format.");
    		}
    		else return new NWBAttribute(attr_name, attr_type);    	
    	}
    	else
    		throw new Exception ("Can not find * from attribut*datatype line.");
    }
    
    private String[] processTokens(StringTokenizer st){
    	int total = st.countTokens();
    	int tokenIndex =0;
    	String [] tokens = new String [total];
    	StringBuffer bf = new StringBuffer();
    	boolean append = false;
    	for (int index =0; index<total; index++){
    		String element = st.nextToken();
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

    	
    	return tokens;
    
    	
    }

    
	public boolean isDirectedGraph(){
		return hasHeader_DirectedEdges;
	}
	
	public boolean isUndirectedGraph(){
		return hasHeader_UndirectedEdges;
	}
    public boolean getValidationResult(){
    	return isFileGood;
    }
    public String getErrorMessages(){
    	return errorMessages.toString();
    }
    public List getNodeAttrList(){    	
    	return nodeAttrList;
    }
    
    public List getUndirectedEdgeAttrList(){
    	return undirectedEdgeAttrList;
    }
    
    public List getDirectedEdgeAttrList() {
    	return directedEdgeAttrList;
    }
    
    public int getTotalNumOfNodes(){
    	return totalNumOfNodes;
    }
   
    

}

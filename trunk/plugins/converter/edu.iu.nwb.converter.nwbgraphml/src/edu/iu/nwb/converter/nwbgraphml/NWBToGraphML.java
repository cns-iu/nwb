package edu.iu.nwb.converter.nwbgraphml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.converter.nwb.common.NWBFileProperty;
import edu.iu.nwb.converter.nwb.common.NWBAttribute;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBToGraphML implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;

    
    public NWBToGraphML(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.ciContext = context;
    }

    public Data[] execute() {
    	File inData, outData;
    	Data [] dm = null;
    	ValidateNWBFile validator;
    	

    	logger = (LogService)ciContext.getService(LogService.class.getName());

		Object inFile = data[0].getData();
		String format = data[0].getFormat();
		if (inFile instanceof File && 
				format.equalsIgnoreCase(NWBFileProperty.NWB_MIME_TYPE)){
			inData = (File)inFile;
			validator = new ValidateNWBFile();
			try {
				validator.validateNWBFormat(inData);
				if (validator.getValidationResult()) {
					outData = convertNWBtoGraphML(inData, validator);
					if (outData != null){
						dm = new Data[] {new BasicData(outData, "file:text/graphml+xml")};
						return dm;
					}
					else {
						logger.log(LogService.LOG_ERROR, "Failed to convert a file from NWB file format to GraphML file format.");
						return null;
					}
				}
				else {
					logger.log(LogService.LOG_ERROR, 
							"Unable to Make a File Conversion. " +
							"The input file has a bad NWB format. \n " +
							"Please review the latest NWB File Format Specification at \n" +
							"http://nwb.slis.indiana.edu/software.html, and update your file."
							);

					return null;
				}
			} catch (FileNotFoundException e) {
				logger.log(LogService.LOG_ERROR, "Unable to find the given file.", e);
				return null;
			} catch (IOException ioe) {
				logger.log(LogService.LOG_ERROR, "Got an IOException", ioe);
				return null;
			}
		}else {
			if (! (inFile instanceof File)){
				logger.log(LogService.LOG_ERROR, "Failed to convert a file from NWB format to GraphML format, because the input is not a file.");
			}
			else if (!(format.equalsIgnoreCase(NWBFileProperty.NWB_MIME_TYPE))){
				logger.log(LogService.LOG_ERROR,
						"Wrong input file type. Expecting "+ NWBFileProperty.NWB_MIME_TYPE+
						", but the input file type is "+format+".");
				
			}
			return null;	
			
		}
    }
    private File convertNWBtoGraphML(File nwbFile, ValidateNWBFile validator){
    	try{
    		File graphml  = getTempFile();
     		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(graphml)));
    		BufferedReader reader = new BufferedReader(new FileReader(nwbFile));

    		writeGraphMLHeader (out);
    		writeAttributes(out, validator);
    		printGraph (out, validator, reader);
    		out.println("</graph>");
    		out.println("</graphml>");
			reader.close();
			out.close();
    		return graphml;
    	}catch (FileNotFoundException e){
			logger.log(LogService.LOG_ERROR, 
					"Got an File Not Found Exception",e);
			return null;
		}catch (IOException ioe){
			logger.log(LogService.LOG_ERROR, "IOException",ioe);
			return null;
		} 	
    	
    }
    
    //  write a Header with XML Schema reference 
    private void writeGraphMLHeader (PrintWriter out){
    	out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"); 
    	out.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    	out.println("xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
     }
    
    // write GraphML-Attributes
    private void writeAttributes(PrintWriter out, ValidateNWBFile validator){
    	//    	first handle node attributes
    	List array = validator.getNodeAttrList();
    	for (int i=0; i<array.size(); i++){
    		NWBAttribute attr = (NWBAttribute) array.get(i);
    		String attrName = attr.getAttrName();
    		if (attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID)){
    			continue;
    		}
    		out.println("<key id=\""+attr.getAttrName()+
    				"\" for=\"node\" attr.name=\""+attr.getAttrName()+
    				"\" attr.type=\""+attr.getDataType()+"\" /> ");	
    	}
    	
    	if (validator.isDirectedGraph() && !validator.isUndirectedGraph()){
    		//this is a directed graph
    		array = validator.getDirectedEdgeAttrList();
        	for (int i=0; i<array.size(); i++){
        		NWBAttribute attr = (NWBAttribute) array.get(i);
        		String attrName = attr.getAttrName();
        		if (attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_SOURCE)||
        			attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_TARGET)	||
        			attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID)){
        			continue;
        		}
        		out.println("<key id=\""+attr.getAttrName()+
        				"\" for=\"edge\" attr.name=\""+attr.getAttrName()+
        				"\" attr.type=\""+attr.getDataType()+"\" />");
        	}
        	out.println("<graph edgedefault=\"directed\">");
    	}
    	else if (!validator.isDirectedGraph() && validator.isUndirectedGraph()){
    		//this is a undirected graph
    		array = validator.getUndirectedEdgeAttrList();
        	for (int i=0; i<array.size(); i++){
        		NWBAttribute attr = (NWBAttribute) array.get(i);
        		String attrName = attr.getAttrName();
        		if (attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_SOURCE)||
        			attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_TARGET)	||
        			attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID)){
        			continue;
        		}
        		out.println("<key id=\""+attr.getAttrName()+
        				"\" for=\"edge\" attr.name=\""+attr.getAttrName()+
        				"\" attr.type=\""+attr.getDataType()+"\" /> ");
        	}
        	out.println("<graph edgedefault=\"undirected\">");
    	}
    	else if (validator.isDirectedGraph() && validator.isUndirectedGraph()){
    		//hybrid graph, don't know how to handle it???
    	}    	
    }

    private void printGraph (PrintWriter out, ValidateNWBFile validator,
    		BufferedReader reader) throws IOException{
/*    	if (validator.getHasTotalNumOfNodes() && validator.getSkipNodeList()){
    		int totalNumOfNodes = validator.getTotalNumOfNodes();
    		for(int i = 1; i<=totalNumOfNodes; i++){
    			out.println("<node id=\""+i+"\">");
    			out.println("<data key=\"label\">"+i+"</data></node>");    			
    		}
    	}
*/    	
    	//read from nwb file and write to the graphml file
    	boolean inNodesSection = false;
		boolean inDirectededgesSection = false;
		boolean inUndirectededgesSection = false;
    	String line = reader.readLine();
        int edgeID = 0;
			
    	while (line != null){
    		if (line.length()==0){
    			line = reader.readLine();
				continue;
    		}
//    		String line_lower = line.toLowerCase();
    			
    		//find node section header that looks like
    		//  *nodes   or  *nodes 1000
    		if(line.startsWith(NWBFileProperty.HEADER_NODE)) 
    		{
    				inNodesSection = true;
    				inDirectededgesSection = false;
    				inUndirectededgesSection = false;
    				line = reader.readLine();
    				continue;
    		}
    		if(line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES)) 
    		{
    				inDirectededgesSection = true;
    				inNodesSection = false;
    				inUndirectededgesSection = false;
    				line = reader.readLine();
    				continue;    				
    		}

    		if(line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES)) 
    		{
    				inUndirectededgesSection =true;
    				inNodesSection = false;
    				inDirectededgesSection = false;	
    				line = reader.readLine();
    				continue;
    		}

    		if (inNodesSection)
    		{	//ignore attribute list line or comment line(s)
				if (line.startsWith(NWBFileProperty.ATTRIBUTE_ID)||
					line.startsWith(NWBFileProperty.PREFIX_COMMENTS+
											NWBFileProperty.ATTRIBUTE_ID)||
					line.startsWith(NWBFileProperty.PREFIX_COMMENTS))
				{
					line = reader.readLine();
	    			continue;
				}
 				else
 				{   
 					StringTokenizer st = new StringTokenizer(line);
 					String[] columns = validator.processTokens(st);
 				    List nodeAttrList = validator.getNodeAttrList();
  		   			out.println("<node id=\""+columns[0]+"\">");
 		    		for(int i = 1; i<nodeAttrList.size(); i++){
 		    			NWBAttribute attr = (NWBAttribute) nodeAttrList.get(i);
 		    			String value = columns[i];
 		    			if(attr.getDataType().equalsIgnoreCase(NWBFileProperty.TYPE_STRING)){
 		    				if (value.startsWith("\"")){
 		    					value=value.substring(1);
 		    				}
 		    				if (value.endsWith("\"")){
 		    					value = value.substring(0, value.length()-1);
 		    				}
 		    			}
 		    			if(! value.equalsIgnoreCase("*")) {
	 		    				out.println("<data key=\""+attr.getAttrName()+
		 		    					"\">"+value+"</data>");  	
	 		    			} else {
	 		    				/*
	 		    				 * Don't print anything.
	 		    				 * 
	 		    				 * If no data is specified for a 
	 		    				 * field, it should be okay to
	 		    				 * just not print the tag.
	 		    				 */
	 		    			}
 		    		}
 		    		out.println("</node>");

 				}
    		}//end if (inNodesSection)
    			
    		if (inDirectededgesSection || inUndirectededgesSection){
    			//ignore attribute list line or comment line(s)
				if (line.startsWith(NWBFileProperty.ATTRIBUTE_SOURCE)||
					line.startsWith(NWBFileProperty.PREFIX_COMMENTS+
											NWBFileProperty.ATTRIBUTE_SOURCE)||
					line.startsWith(NWBFileProperty.PREFIX_COMMENTS))	
				{
					line = reader.readLine();
	    			continue;
				}
 			    else{
 			    	List edgeAttrList = new ArrayList();
 					StringTokenizer st = new StringTokenizer(line);
 				    String[] columns = validator.processTokens(st);
 				    if (inDirectededgesSection)
 				    	edgeAttrList = validator.getDirectedEdgeAttrList();
 				    else if (inUndirectededgesSection)
 				    	edgeAttrList = validator.getUndirectedEdgeAttrList();
 				    edgeID++;
 				    //find if there is an id attribute
 				    int idColumnNumber = findAttr(NWBFileProperty.ATTRIBUTE_ID,edgeAttrList);
 				    int sourceColumnNumber = findAttr(NWBFileProperty.ATTRIBUTE_SOURCE, edgeAttrList);
 				    int targetColumnNumber = findAttr(NWBFileProperty.ATTRIBUTE_TARGET, edgeAttrList);
 				    if (idColumnNumber == -1)
 				    {
 				    	 if (edgeAttrList.size()>2){
 				    		 out.println("<edge id=\""+edgeID+"\" source=\""+columns[sourceColumnNumber]+
 				    			"\" target=\""+columns[targetColumnNumber]+"\">");
 				    		 for(int i = 0; i<edgeAttrList.size(); i++){
 	 				    		NWBAttribute attr = (NWBAttribute) edgeAttrList.get(i);
 	 				    		String  attrName = attr.getAttrName();
 	 				    		if (!(attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_SOURCE) ||
 	 				    			attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_TARGET)	||
 	 				    			attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID))){
 	 				    			String value = columns[i];
 	 		 		    			if(attr.getDataType().equalsIgnoreCase(NWBFileProperty.TYPE_STRING)){
 	 		 		    				if (value.startsWith("\"")){
 	 		 		    					value=value.substring(1);
 	 		 		    				}
 	 		 		    				if (value.endsWith("\"")){
 	 		 		    					value = value.substring(0, value.length()-1);
 	 		 		    				}
 	 		 		    			}
 	 		 		    			if(! value.equalsIgnoreCase("*")) {
 	 		 		    				out.println("<data key=\""+attr.getAttrName()+
 	 	 		 		    					"\">"+value+"</data>");  	
 	 		 		    			} else {
 	 		 		    				/*
 	 		 		    				 * Don't print anything.
 	 		 		    				 * 
 	 		 		    				 * If no data is specified for a 
 	 		 		    				 * field, it should be okay to
 	 		 		    				 * just not print the tag.
 	 		 		    				 */
 	 		 		    			}
 	 				    		}
 	 				    	}
 				    		out.println("</edge>"); 
 				    	 }else{
 				    		 out.println("<edge id=\""+edgeID+"\" source=\""+columns[sourceColumnNumber]+
 	 				    			"\" target=\""+columns[targetColumnNumber]+"\"/>");
 				    	 }
 				    }
 				    else {
 				    	if (edgeAttrList.size()>3){
 				    		out.println("<edge id=\""+columns[idColumnNumber]+
 				    				"\" source=\""+columns[sourceColumnNumber]+
 				    				"\" target=\""+columns[targetColumnNumber]+"\">");
 				    		for(int i = 0; i<edgeAttrList.size(); i++){
 	 				    		NWBAttribute attr = (NWBAttribute) edgeAttrList.get(i);
 	 				    		String  attrName = attr.getAttrName();
 	 				    		if (!(attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_SOURCE) ||
 	 				    			attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_TARGET)	||
 	 				    			attrName.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID))){
 	 				    			
 	 		 		    			if(! columns[i].equalsIgnoreCase("*")) {
 	 		 		    				out.println("<data key=\""+attr.getAttrName()+
 	 	 		 		    					"\">"+columns[i]+"</data>"); 	
 	 		 		    			} else {
 	 		 		    				/*
 	 		 		    				 * Don't print anything.
 	 		 		    				 * 
 	 		 		    				 * If no data is specified for a 
 	 		 		    				 * field, it should be okay to
 	 		 		    				 * just not print the tag.
 	 		 		    				 */
 	 		 		    			}
 	 				    		}
 	 				    	}
 				    		out.println("</edge>");  				    		
 				    	}
 				    	else{
 				    		out.println("<edge id=\""+columns[idColumnNumber]+
 				    				"\" source=\""+columns[sourceColumnNumber]+
 				    				"\" target=\""+columns[targetColumnNumber]+"\"/>");
 				    	}
 				    } 		    			
 			    }
    		}//end if (inDirectededgesSection || inUndirectededgesSection)
    			
    		line = reader.readLine();    		
    		
    	}//end while
    }
    	
    private int findAttr(String attrName, List attrList){
    	for(int i = 0; i<attrList.size(); i++){
    		NWBAttribute attr = (NWBAttribute) attrList.get(i); 
    		if (attr.getAttrName().equalsIgnoreCase(attrName)){
    			return i;
    		}
    	}
    	return -1;
    }
    		

    
	private File getTempFile(){
		File tempFile;
    
		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath+File.separator+"temp");
		if(!tempDir.exists())
			tempDir.mkdir();
		try{
			tempFile = File.createTempFile("NWB-Session-", ".xml", tempDir);
		
		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.toString());
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");

		}
		return tempFile;
	}			

	
}
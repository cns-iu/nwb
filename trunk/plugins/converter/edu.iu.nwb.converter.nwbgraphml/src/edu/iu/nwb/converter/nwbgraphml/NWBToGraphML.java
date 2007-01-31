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


import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.converter.nwb.common.NWBFileProperty;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBToGraphML implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;
    GUIBuilderService guiBuilder;
    
    public NWBToGraphML(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.ciContext = context;
    }

    public Data[] execute() {
    	File inData, outData;
    	Data [] dm = null;
    	ValidateNWBFile validator;
    	
    	guiBuilder = (GUIBuilderService) ciContext
				.getService(GUIBuilderService.class.getName());
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
					outData = convertNWBtoGraphML(inData);
					if (outData != null){
						dm = new Data[] {new BasicData(outData, "file:text/graphml+xml")};
						return dm;
					}
					else {
						guiBuilder.showError("Unable to Make a File Conversion",
								"Fail to convert a file from NWB file format to GraphML file format.",
								"");
						return null;
					}
				}
				else {
					guiBuilder.showError("Unable to Make a File Conversion",
							"The input file has a bad NWB format. \n"+
							"Please review the latest NWB File Formate Specification at \n"+
							"http://nwb.slis.indiana.edu/software.html, and update your file.", 
							"");

					return null;
				}
			} catch (FileNotFoundException e) {
				guiBuilder.showError("File Not Found Exception",
						"Got an File Not Found Exception", e);
				return null;
			} catch (IOException ioe) {
				guiBuilder.showError("IOException", "Got an IOException", ioe);
				return null;
			}
		}else {
			if (! (inFile instanceof File)){
				guiBuilder.showError("Unable to Make a File Conversion",
						"Fail to convert a file from NWB format to GraphML format, because the input is not a file.", 
						"");
			}
			else if (!(format.equalsIgnoreCase(NWBFileProperty.NWB_MIME_TYPE))){
				guiBuilder.showError("Unable to Make a File Conversion",
						"Wrong input file type. Expecting "+ NWBFileProperty.NWB_MIME_TYPE+
						", but the input file type is "+format+".", 
						"");
				
			}
			return null;	
			
		}
    }
    
    private File convertNWBtoGraphML(File nwbFile){
    	File graphml  = getTempFile();
    	boolean inProcessingNodes = false;
    	boolean inProcessingUndirectedEdges = false;
    	boolean wrongNWBFormat = false;
    	int totalNodes = 0;
    	int countNodes = 0;
    	
    	try{
    		BufferedReader reader = new BufferedReader(new FileReader(nwbFile));
			
    		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(graphml)));
			//write the header
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns/graphml\"");
			out.println("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			out.println("xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns/graphml\">");
			out.println("<graph edgedefault=\"undirected\">");
			
			//minor cheat because we know what the first attribute's index will be. Do this smarter later
			out.println("<key id='d0' for='node' attr.name='label' attr.type='string'/>");
	
			String line = reader.readLine();
			while(line != null){
				if(inProcessingNodes){
					//first detect if the node section is ended.			
					if(line.startsWith("*") || line.startsWith("//") ){
						inProcessingNodes = false;
						if (countNodes==0 && totalNodes>0){
							//nwb file ignore the nodes list
							for (int index=1; index<=totalNodes;index++){
								out.println(createNode(""+index, new String[]{""+index}));
								countNodes++;
							}
						}else if(countNodes == 0 && totalNodes ==0){
							wrongNWBFormat = true;
							break;
						}else if(totalNodes !=0 && countNodes!=totalNodes)	{
							wrongNWBFormat = true;
							break;
						}
						
					}
					else{					
					    //process and write a node
						StringTokenizer st = new StringTokenizer(line);
						int totalTokens = st.countTokens();
						if (totalTokens ==1 ){
							//only have node id
							String id = st.nextToken();
							out.println(createNode(id, new String[]{id}));
							countNodes++;
						}
						else if (totalTokens == 2){
							//have node id and label
							String id = st.nextToken();
							String label = st.nextToken();
							out.println(createNode(id, new String[]{label}));
							countNodes++;
						}
						else if (totalTokens > 2){
							//have node id and label
							String id = st.nextToken();
							String label = st.nextToken();
							//TODO NEED TO HANDLE ATTRIBUTES OF NODES
							out.println(createNode(id, new String[]{label}));
							countNodes++;							
						}					
						
					}				
				}//end if(inProcessNodes)
				
				if(inProcessingUndirectedEdges){
					//first detect if the undirectededges section is ended.			
					if(line.startsWith("*") || line.startsWith("//") ){
						inProcessingUndirectedEdges = false;
					}
					else {
						StringTokenizer st = new StringTokenizer(line);
						int totalTokens = st.countTokens();
						if (totalTokens == 2){
							//have node id and label
							String source = st.nextToken();
							String target = st.nextToken();
							out.println("<edge source=\""+source+"\" target=\""+target+"\"></edge>");
					    }else{
					    	wrongNWBFormat = true;
							break;
					    }
	
					}
				}//end if(inProcessingUndirectedEdges)
				
				//process nodes
				if(line.startsWith(NWBFileProperty.HEADER_NODE) || 
						   line.startsWith(NWBFileProperty.HEADER_NODE_LOWERCASE) ||
						   line.startsWith(NWBFileProperty.HEADER_NODE_UPPERCASE) ) {
					inProcessingNodes  = true;
					StringTokenizer st = new StringTokenizer(line);
					int totalTokens = st.countTokens();
					if (totalTokens == 2){
						st.nextToken();
						totalNodes = new Integer(st.nextToken()).intValue();						
					}
						   
				}
				
				//TODO Figure out the right format to present the directed edgs in graphml
	/*			if(line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES) || 
						   line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES_LOWERCASE) ||
						   line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES_LOWERFIRST) ||
						   line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES_UPPERCASE) ) {
					hasHeader_DirectedEdges = true;
				}
	*/
				if(line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES) || 
						   line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES_LOWERCASE) ||
						   line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES_LOWERFIRST) ||
						   line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES_UPPERCASE) ) {
					inProcessingUndirectedEdges = true;
				}
				line = reader.readLine();
			}//end while(line != null)
			out.println("</graph>");
			out.println("</graphml>");
			reader.close();
			out.close();
			if (wrongNWBFormat)
				return null;
			else
				return graphml;
	    	
		}catch (FileNotFoundException e){
			guiBuilder.showError("File Not Found Exception", 
					"Got an File Not Found Exception",e);	
			return null;
		}catch (IOException ioe){
			guiBuilder.showError("IOException", 
					"Got an IOException",ioe);
			return null;
		}

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
	
	private String createNode(String id, String[] attributes) {
		return createNode(id, attributes, "d");
	}

	private String createNode(String id, String[] attributes, String baseAttributeId) {
		
		StringBuffer node = new StringBuffer();
		node.append("<node id='");
		node.append(id);
		node.append("'>");
		
		for(int attributeIndex = 0; attributeIndex < attributes.length; attributeIndex++) {
			node.append("<data key='");
			node.append(baseAttributeId + attributeIndex);
			node.append("'>");
			node.append(attributes[attributeIndex]);
			node.append("</data>");
		}
		
		node.append("</node>");
		
		return node.toString();
	}

				

	
}
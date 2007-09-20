package edu.iu.nwb.converter.nwbpajeknet;

/*****
 * 
 * This is just temporary until we redo the way the NWB validator is handled.
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.NWBAttribute;
import edu.iu.nwb.converter.nwb.common.NWBFileProperty;
import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.converter.pajeknet.common.ARCEDGEParameter;
import edu.iu.nwb.converter.pajeknet.common.NETFileFunctions;
import edu.iu.nwb.converter.pajeknet.common.NETFileParameter;
import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;

public class NWBToPajeknet implements Algorithm {
	String[] noPrintParameters = { NETFileProperty.ATTRIBUTE_ID, NETFileProperty.ATTRIBUTE_LABEL, "xpos", "ypos", "zpos", "shape",NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.ATTRIBUTE_WEIGHT };
	Data[] data;
	Dictionary parameters;
	CIShellContext ciContext;
	LogService logger;

	Map vertexToIdMap;


	/**
	 * Intializes the algorithm
	 * @param data List of Data objects to convert
	 * @param parameters Parameters passed to the converter
	 * @param context Provides access to CIShell services
	 * @param transformer 
	 */
	public NWBToPajeknet(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.ciContext = context;
		this.logger = (LogService)ciContext.getService(LogService.class.getName());
	}

	/**
	 * Executes the conversion
	 * 
	 * @return A single java file object
	 */
	public Data[] execute() {
		File inData, outData;
		Data [] dm = null;
		ValidateNWBFile validator;

		Object inFile = data[0].getData();
		String format = data[0].getFormat();
		if (inFile instanceof File && 
				format.equalsIgnoreCase(NWBFileProperty.NWB_MIME_TYPE)){

			inData = (File)inFile;
			validator = new ValidateNWBFile();
			try {
				validator.validateNWBFormat(inData);
				if(validator.getValidationResult()){
					outData = convertNWBToNet(inData,validator);
					if(outData != null){
						dm = new Data[] {new BasicData(outData, NETFileProperty.NET_MIME_TYPE)};
						return dm;
					}else {
						logger.log(LogService.LOG_ERROR, "Problem executing transformation from .nwb to Pajek .net Output file was not created.");
						return null;
					}
				}else{
					logger.log(LogService.LOG_ERROR, "Problem executing transformation from .nwb to Pajek .net" + validator.getErrorMessages());
					return null;
				}
			}
			catch (FileNotFoundException fnf){
				logger.log(LogService.LOG_ERROR, "The specified .nwb file could not be found.", fnf);
			}
			catch (IOException ioe){
				logger.log(LogService.LOG_ERROR, "IO Errors while converting from .nwb to Pajek .net.", ioe);
			}
		}
		else
			logger.log(LogService.LOG_ERROR, "Unable to convert from .nwb to Pajek .net because input data is not a file");
		return null;
	}


	/**
	 * Creates a temporary file for the NWB file
	 * @return The temporary file
	 */
	private File getTempFile(){
		File tempFile;

		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath+File.separator+"temp");
		if(!tempDir.exists())
			tempDir.mkdir();
		try{
			tempFile = File.createTempFile("NWB-Session-", ".net", tempDir);

		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.toString());
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.net");

		}
		return tempFile;
	}

	private File convertNWBToNet(File nwbFile, ValidateNWBFile validator){
		try{
			File net = getTempFile();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(net)), true);
			BufferedReader reader = new BufferedReader(new FileReader(nwbFile));
			printGraph(out,validator,reader);

			return net;
		}catch (FileNotFoundException e){
			logger.log(LogService.LOG_ERROR, "Could not create the temp file for writing from .nwb to Pajek .net.", e);
			return null;
		}catch (IOException ioe){
			logger.log(LogService.LOG_ERROR, "IO Errors while writing from .nwb to Pajek .net.", ioe);
			return null;
		}
	}

	private void printGraph (PrintWriter out, ValidateNWBFile validator,
			BufferedReader reader) throws IOException{

		/*		if (validator.getHasTotalNumOfNodes() && validator.getSkipNodeList()){
			int totalNumOfNodes = validator.getTotalNumOfNodes();
			for(int i = 1; i<=totalNumOfNodes; i++){
				out.println(i+"\""+i+"\"");			
			}
		}
		 */
		//read from nwb file and write to the graphml file
		boolean inNodesSection = false;
		boolean inDirectededgesSection = false;
		boolean inUndirectededgesSection = false;
		String line = reader.readLine();

		while (line != null){
			
			if (line.length()==0){
				line = reader.readLine();
				continue;
			}

			//String line_lower = line.toLowerCase();

			//find node section header that looks like
			//  *nodes   or  *nodes 1000
			if(line.startsWith(NWBFileProperty.HEADER_NODE)) 
			{
				inNodesSection = true;
				inDirectededgesSection = false;
				inUndirectededgesSection = false;
				if(!validator.getHasTotalNumOfNodes())
				writeHeader(line.replace(NWBFileProperty.HEADER_NODE, "Vertices " + validator.getTotalNumOfNodes()), out);
				else
					writeHeader(line.replace(NWBFileProperty.HEADER_NODE, "Vertices "), out);
				line = reader.readLine();
				continue;
			}
			if(line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES)) 
			{
				inDirectededgesSection = true;
				inNodesSection = false;
				inUndirectededgesSection = false;
				writeHeader(line.replace(NWBFileProperty.HEADER_DIRECTED_EDGES, "Arcs " + validator.getTotalNumOfDirectedEdges()), out);
				line = reader.readLine();
				continue;    				
			}

			if(line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES)) 
			{
				inUndirectededgesSection =true;
				inNodesSection = false;
				inDirectededgesSection = false;
				writeHeader(line.replace(NWBFileProperty.HEADER_UNDIRECTED_EDGES, "Edges " + validator.getTotalNumOfUndirectedEdges()), out);
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
					writeNodes(line,out,validator, validator.getNodeAttrList());
				}
			}//end if (inNodesSection)

			if (inDirectededgesSection || inUndirectededgesSection){
				if (line.startsWith(NWBFileProperty.ATTRIBUTE_SOURCE)||
						line.startsWith(NWBFileProperty.PREFIX_COMMENTS+
								NWBFileProperty.ATTRIBUTE_SOURCE)||
								line.startsWith(NWBFileProperty.PREFIX_COMMENTS))	
				{
					line = reader.readLine();
					continue;
				}
				else{
					if(inDirectededgesSection){
						writeEdges(line, out, validator, validator.getDirectedEdgeAttrList());
					}
					if(inUndirectededgesSection){
						writeEdges(line, out, validator, validator.getUndirectedEdgeAttrList());
					}
				}

			}//end if (inDirectededgesSection || inUndirectededgesSection)

			line = reader.readLine();    		

		}//end while
	}




	private void writeHeader(String s, PrintWriter out){
		String st = NWBFileProperty.PRESERVED_STAR+s;
		//System.out.println(st);
		out.println(st);
	}

	private void writeNodes(String s, PrintWriter out, ValidateNWBFile validator, List nodeAttrList){

		String[] columns = NETFileFunctions.processTokens(s);
	//	System.out.println(s);
		int i = 0;
		for(Iterator ii = nodeAttrList.iterator(); ii.hasNext();){
			NWBAttribute na = (NWBAttribute) ii.next();
			String value = columns[i];
		//	System.out.print(value + "::");
			//value.replace("\"", "");
			if(value.equalsIgnoreCase("*")){
				
			}
			else if(NETFileFunctions.isInList(na.getAttrName(), noPrintParameters)){
				if(na.getDataType().equalsIgnoreCase(NWBFileProperty.TYPE_STRING)){
				String[] sa = value.split(" ");
				if(sa.length > 1)
					out.print(" \""+value+"\" ");
				else
					out.print(value + " ");
				}
				else
					out.print(value + " ");
			}
			else if((NETFileFunctions.isInList(na.getAttrName(), NETFileParameter.VERTEX_NUMBER_PARAMETER_LIST))){
				if(!value.equalsIgnoreCase("")){
					//	System.out.print(na.getAttrName() + " " + value + " ");

					out.print(na.getAttrName() + " " + value + " ");
				}
			}
			else if((NETFileFunctions.isInList(na.getAttrName(), NETFileParameter.VERTEX_STRING_PARAMETER_LIST))){
				//System.out.println(na.getAttrName() + " ");
				
				if(!value.equalsIgnoreCase("")){
					//		System.out.print(na.getAttrName() + " \"" + value + "\" ");

					out.print(na.getAttrName() + " \"" + value + "\" ");
				}
			}
			else if(na.getAttrName().startsWith("unknown")){
				String[] sa = value.split(" ");
				if(sa.length > 1)
					out.print(" \""+value+"\" ");
				else
				out.print(value+ " ");
			}
			else;

			i++;

		}
		//System.out.println();
		out.println();

	}

	private void writeEdges(String s, PrintWriter out, ValidateNWBFile validator, List edgeAttrList){
		//System.out.print(s + " ");
		int i = 0;
		String[] columns = NETFileFunctions.processTokens(s);
		/*if (inDirectededgesSection)
				edgeAttrList = validator.getDirectedEdgeAttrList();
			else if (inUndirectededgesSection)
				edgeAttrList = validator.getUndirectedEdgeAttrList();*/
		for(Iterator ii = edgeAttrList.iterator(); ii.hasNext();){
			NWBAttribute na = (NWBAttribute) ii.next();
			String value = columns[i];
			//System.out.print(na.getAttrName()+ " ");
			if(value.equalsIgnoreCase("*")){
				continue;
			}
			if(na.getAttrName().equals(NETFileProperty.ATTRIBUTE_LABEL) || na.getAttrName().equals(ARCEDGEParameter.PARAMETER_LABEL)){
				out.print(ARCEDGEParameter.PARAMETER_LABEL + " \"" + value + "\" ");
			}
			else if((NETFileFunctions.isInList(na.getAttrName(), noPrintParameters)) && !(na.getAttrName().equals(NETFileProperty.ATTRIBUTE_LABEL))){
				//System.out.print(value + " ");
				out.print(value + " ");
			}
			else{
			//	System.out.print(na.getAttrName() + " " + value + " ");
				out.print(na.getAttrName() + " " + value + " ");
			}
			i++;
		}
		//	System.out.println();
		out.println();
	}
}





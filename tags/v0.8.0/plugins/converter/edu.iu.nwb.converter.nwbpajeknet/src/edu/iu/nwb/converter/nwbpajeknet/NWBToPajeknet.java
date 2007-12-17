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
import java.util.HashMap;
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
		this.vertexToIdMap = new HashMap();
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
			out.flush();
			BufferedReader reader = new BufferedReader(new FileReader(nwbFile));
			printGraph(out,validator,reader);

			return net;
		}catch (FileNotFoundException e){
			logger.log(LogService.LOG_ERROR, "Could not create the temp file for writing from .nwb to Pajek .net.", e);
			return null;
		}catch (IOException ioe){
			logger.log(LogService.LOG_ERROR, "IO Errors while writing from .nwb to Pajek .net.", ioe);
			return null;
		}catch(Exception e){
			logger.log(LogService.LOG_ERROR, "Errors in translating between .nwb and .net." +
					"\n"+e.toString(), e);
			//e.printStackTrace();
			return null;
		}
	}

	private void printGraph (PrintWriter out, ValidateNWBFile validator,
			BufferedReader reader) throws Exception{

		int nodes = 1;
		boolean inNodesSection = false;
		boolean inDirectededgesSection = false;
		boolean inUndirectededgesSection = false;
		String line = reader.readLine();

		while (line != null){
			line = line.trim();
			
			if (line.length()==0 || line.startsWith(NWBFileProperty.PREFIX_COMMENTS)){
				line = reader.readLine();
				continue;
			}
			//System.out.println(line.startsWith(NWBFileProperty.HEADER_NODE));
			//String line_lower = line.toLowerCase();

			//find node section header that looks like
			//  *nodes   or  *nodes 1000
			if(line.startsWith(NWBFileProperty.HEADER_NODE)) 
			{
			//	System.out.println(line);
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
				writeHeader(line.replace(NWBFileProperty.HEADER_DIRECTED_EDGES, "Arcs "), out);
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
					writeNodes(line,out,validator, validator.getNodeAttrList(),nodes);
					nodes++;
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
		out.flush();
	}




	private void writeHeader(String s, PrintWriter out){
		out.flush();
		String st = NWBFileProperty.PRESERVED_STAR+s;
		
		out.print(st+"\r\n");

	}

	private void writeNodes(String s, PrintWriter out, ValidateNWBFile validator, List nodeAttrList, int mapper){
		out.flush();
		String[] columns = NETFileFunctions.processTokens(s);
		
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
				else{
					if(na.getAttrName().equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID)){
						this.vertexToIdMap.put(new Integer(value), new Integer(mapper));
						out.print(mapper + " ");
					}
					else
						out.print(value + " ");
				}

			}
			else if(na.getDataType().equalsIgnoreCase("float") || na.getDataType().equalsIgnoreCase("int")){
				if(!value.equalsIgnoreCase("")){
					//	System.out.print(na.getAttrName() + " " + value + " ");
					String ss = na.getAttrName();
					
					if(ss.matches("[bil]?c1")){
						
						ss = ss.replace("1", "");
						ss += " " + value + " ";
						for(int j = 1; j < 3; j++){
							ss += columns[j+i] + " ";
							ii.next();
						}
						i = i+2;
						out.print(ss);
					}else
						out.print(na.getAttrName() + " " + value + " ");
				}
			}
			else if(na.getDataType().equalsIgnoreCase("string")){
			

				if(!value.equalsIgnoreCase("")){
					if(na.getAttrName().startsWith("unknown")){
						String[] sa = value.split(" ");
						if(sa.length > 1)
							out.print(" \""+value+"\" ");
						else
							out.print(value+ " ");
					}
					else
						out.print(na.getAttrName() + " \"" + value + "\" ");
				}
			}

			else;

			i++;

		}
		
		out.print("\r\n");

	}

	private void writeEdges(String s, PrintWriter out, ValidateNWBFile validator, List edgeAttrList) throws Exception{
		//System.out.println(s);
		out.flush();
		
		int i = 0;
		String[] columns = NETFileFunctions.processTokens(s);
		/*if (inDirectededgesSection)
				edgeAttrList = validator.getDirectedEdgeAttrList();
			else if (inUndirectededgesSection)
				edgeAttrList = validator.getUndirectedEdgeAttrList();*/
		for(Iterator ii = edgeAttrList.iterator(); ii.hasNext();){
			NWBAttribute na = (NWBAttribute) ii.next();
			String value = columns[i];
			//System.out.println(value);
			//System.out.print(na.getAttrName()+ " ");
			if(value.equalsIgnoreCase("*")){

			}
			else if(na.getAttrName().equals(NETFileProperty.ATTRIBUTE_LABEL) || na.getAttrName().equals(ARCEDGEParameter.PARAMETER_LABEL)){
				out.print(ARCEDGEParameter.PARAMETER_LABEL + " \"" + value + "\" ");
			}
			else if((NETFileFunctions.isInList(na.getAttrName(), noPrintParameters)) && !(na.getAttrName().equals(NETFileProperty.ATTRIBUTE_LABEL))){
				if(na.getDataType().equalsIgnoreCase(NWBFileProperty.TYPE_STRING)){
					String[] sa = value.split(" ");
					if(sa.length > 1)
						out.print(" \""+value+"\" ");
					else
						out.print(value + " ");
				}
				else{
					if(na.getAttrName().equals(NWBFileProperty.ATTRIBUTE_SOURCE) || na.getAttrName().equals(NWBFileProperty.ATTRIBUTE_TARGET)){
						try{
							value = ((Integer)this.vertexToIdMap.get(new Integer(value))).toString();
						}catch(NullPointerException npe){
							throw new Exception("Edge references an undefined node " + value);
						}
					}
					out.print(value + " ");


				}
			}
			else if(na.getDataType().equalsIgnoreCase("float") || na.getDataType().equalsIgnoreCase("int")){
				if(!value.equalsIgnoreCase("")){
					//	System.out.print(na.getAttrName() + " " + value + " ");
					String ss = na.getAttrName();
					
					if(ss.matches("[bil]?c1")){
						
						ss = ss.replace("1", "");
						ss += " " + value + " ";
						for(int j = 1; j < 3; j++){
							ss += columns[j+i] + " ";
							ii.next();
						}
						i = i+2;
						out.print(ss);
					}else
						out.print(na.getAttrName() + " " + value + " ");
				}
			}
			else if(na.getDataType().equalsIgnoreCase("string")){
				

				if(!value.equalsIgnoreCase("")){
					if(na.getAttrName().startsWith("unknown")){
						String[] sa = value.split(" ");
						if(sa.length > 1)
							out.print(" \""+value+"\" ");
						else
							out.print(value+ " ");
					}
					else
						out.print(na.getAttrName() + " \"" + value + "\" ");
				}
			}

			else;
			i++;
		}
	
		out.print("\r\n");
	}
}





package edu.iu.nwb.converter.nwbpajeknet;

/*****
 * 
 * This is just temporary until we redo the way the NWB validator is handled.
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.UnicodeReader;

import edu.iu.nwb.converter.nwb.common.NWBAttribute;
import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.converter.pajeknet.common.ARCEDGEParameter;
import edu.iu.nwb.converter.pajeknet.common.NETFileFunctions;
import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class NWBToPajeknet implements Algorithm {
	public static final String[] noPrintParameters = { NETFileProperty.ATTRIBUTE_ID, NETFileProperty.ATTRIBUTE_LABEL, "xpos", "ypos", "zpos", "shape",NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.ATTRIBUTE_WEIGHT };
	
	private File inNWBFile;
	private Map vertexToIdMap;

	
	public NWBToPajeknet(Data[] data, Dictionary parameters, CIShellContext context) {
		this.inNWBFile = (File) data[0].getData();
		
		this.vertexToIdMap = new HashMap();
	}

	
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			ValidateNWBFile validator = new ValidateNWBFile();
			validator.validateNWBFormat(inNWBFile);
			if (validator.getValidationResult()) {
				File outNetFile = convertNWBToNet(inNWBFile, validator);
				
				return createOutData(outNetFile);
			} else {
				throw new AlgorithmExecutionException(
					"Error converting NWB to Pajek .net: "
						+ validator.getErrorMessages());
			}
		} catch (FileNotFoundException e) {
			String message = "Couldn't find NWB file: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		} catch (IOException e) {
			String message = "File access error: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
	}

	private Data[] createOutData(File outNetFile) {
		Data[] dm = new Data[]{ new BasicData(outNetFile, NETFileProperty.NET_MIME_TYPE) };
		return dm;
	}

	private File convertNWBToNet(File nwbFile, ValidateNWBFile validator)
			throws AlgorithmExecutionException{
		try {
			File outNetFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"NWBToPajekNet-", "net");
			PrintWriter out = new PrintWriter(outNetFile, "UTF-8");
			out.flush();
			BufferedReader reader =
				new BufferedReader(new UnicodeReader(new FileInputStream(nwbFile)));
			printGraph(out, validator, reader);

			return outNetFile;
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	private void printGraph(
			PrintWriter out, ValidateNWBFile validator, BufferedReader reader)
				throws IOException, AlgorithmExecutionException {
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

	private void writeNodes(String s,
							PrintWriter out,
							ValidateNWBFile validator,
							List nodeAttrList,
							int mapper) {
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

	private void writeEdges(String s, PrintWriter out, ValidateNWBFile validator, List edgeAttrList)
			throws AlgorithmExecutionException {
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
						try {
							value = ((Integer) this.vertexToIdMap.get(new Integer(value))).toString();
						} catch (NullPointerException e) {
							throw new AlgorithmExecutionException("Edge references an undefined node.", e);
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

			else; // TODO .......
			i++;
		}

		out.print("\r\n");
	}
}





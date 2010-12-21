package edu.iu.nwb.converter.pajekmatpajeknet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.UnicodeReader;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.pajekmat.common.MATFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETArcsnEdges;
import edu.iu.nwb.converter.pajeknet.common.NETAttribute;
import edu.iu.nwb.converter.pajeknet.common.NETFileFunctions;
import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETVertex;
import edu.iu.nwb.converter.pajeknet.common.NETFileValidator;

public class PajeknetToPajekmat implements Algorithm{
	public static final String[] noPrintParameters = { NETFileProperty.ATTRIBUTE_ID, NETFileProperty.ATTRIBUTE_LABEL, "xpos", "ypos", "zpos", "shape",NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.ATTRIBUTE_WEIGHT };
	
	private File inNETFile;

	
	public PajeknetToPajekmat(Data[] data, Dictionary parameters, CIShellContext cContext){
		inNETFile = (File) data[0].getData();
	}

	
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			NETFileValidator netValidator = new NETFileValidator();
			netValidator.validateNETFormat(inNETFile);
			if (netValidator.getValidationResult()){
				File outMATFile = convertNetToMat(netValidator, inNETFile);
				
				return new Data[]{ new BasicData(
							outMATFile, MATFileProperty.MAT_MIME_TYPE) };
			} else {
				throw new AlgorithmExecutionException(
						"Problem executing conversion from Pajek .net to .mat "
						+ netValidator.getErrorMessages());
			}
		} catch (FileNotFoundException e) {
			String message = "Couldn't find Pajek .net file: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		} catch (IOException e) {
			String message = "File access error: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
	}

	private File convertNetToMat(NETFileValidator vmf, File f) throws IOException {
		File outMATFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					"Pajek-", "mat");
		PrintWriter out = new PrintWriter(outMATFile, "UTF-8");
		BufferedReader br = new BufferedReader(new UnicodeReader(new FileInputStream(f)));
		processFile(vmf,br,out);
		out.close();
		return outMATFile;
	}
	
	private void processFile(NETFileValidator nv, BufferedReader reader, PrintWriter pw)
			throws IOException {
		boolean inVerticesSection = false;
		boolean inArcsSection = false;
		boolean inEdgesSection = false;
		
		String line = reader.readLine();
		//float[] edges = new float[nv.getTotalNumOfNodes()];
		while (line != null){
			//currentLine++;
		
			line = line.trim();
			if(line.startsWith(NETFileProperty.PREFIX_COMMENTS) || (line.length() < 1)){
				line = reader.readLine();
				continue;
			}

			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_VERTICES)){
				
				pw.print("*Vertices "+ nv.getTotalNumOfNodes()+"\r\n");
				inVerticesSection = true;
				inArcsSection = false;
				inEdgesSection = false;
				line = reader.readLine();
				continue;
			}
			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_ARCS)){
				pw.print("*Matrix "+"\r\n");
				inVerticesSection = false;
				inArcsSection = true;
				inEdgesSection = false;
				line = reader.readLine();

				continue;
			}

			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_EDGES)){
				pw.print("*Matrix "+"\r\n");
				inVerticesSection = false;
				inArcsSection = false;
				inEdgesSection = true;
				line = reader.readLine();

				continue;
			}


			if(inVerticesSection){	
				NETVertex netv = nv.processVertices(line);
				writeVertex(nv,pw,netv);
				line = reader.readLine();

				continue;
			}

			if(inEdgesSection){
				ArrayList connections = new ArrayList();
					while(line != null){
						if(!line.startsWith(NETFileProperty.PRESERVED_STAR)){
					
						if(!(processLine(line).length() < 1)){
							NETArcsnEdges ane = nv.processArcsnEdges(line);
							connections.add(ane);
						}
						}
						line = reader.readLine();
					}
							
				writeUndirectedMatrix(nv,pw,connections);
				
				continue;
			}

			if(inArcsSection){
				ArrayList connections = new ArrayList();
				while(line != null){
					if(!line.startsWith(NETFileProperty.PRESERVED_STAR)){
					if(!(processLine(line).length() < 1)){
						NETArcsnEdges ane = nv.processArcsnEdges(line);
						connections.add(ane);
					}
					}
					line = reader.readLine();
				}
				writeDirectedMatrix(nv,pw,connections);
				continue;
			}
			line = reader.readLine();
		}
	}
	
	private static String processLine(String line){
		if(line.startsWith(NETFileProperty.PREFIX_COMMENTS)) {
			return "";
		}
		else {
			return line;
		}
	}

	private void writeVertex(NETFileValidator vmf, PrintWriter pw, NETVertex nv) {
		    //pw.write(MATFileProperty.HEADER_VERTICES + " " + vmf.getVertices().size() + "\n");
			String s = "";
			for(int j = 0; j < NETVertex.getVertexAttributes().size(); j++){
				try{
					NETAttribute ma = (NETAttribute)NETVertex.getVertexAttributes().get(j);
					String attr = ma.getAttrName();
					String type = ma.getDataType();
					String value = nv.getAttribute(attr).toString();
					if(value == null){
						s += "";
					}
					else{
						if(NETFileFunctions.isInList(attr,noPrintParameters)){
							if(type.equalsIgnoreCase("string"))
								s += "\"" + nv.getAttribute(attr) + "\" ";
							else
								s += nv.getAttribute(attr)+" ";
						}
						else {
							if(type.equalsIgnoreCase("string")){
								if(attr.startsWith("unknown"))
									s += " \"" + nv.getAttribute(attr) + "\" ";
								else
									s += attr + " \"" + nv.getAttribute(attr) + "\" ";
							}
							else
								s += attr + " " + nv.getAttribute(attr) + " ";
						}
					}
				} catch(NullPointerException e) {
					s += "";
				}
			}
		 
	
		pw.print(s+"\r\n");
	
	}

	private void writeDirectedMatrix(NETFileValidator vmf, PrintWriter pw, ArrayList arcs) {
		NETArcsnEdges nae;
		int source, target;
		float weight = (float)0.0;
		//pw.println(MATFileProperty.HEADER_MATRIX);
		for(int ii = 0; ii < vmf.getTotalNumOfNodes(); ii++){
			for(int jj = 0; jj < vmf.getTotalNumOfNodes(); jj++){
				for(int kk = 0; kk < arcs.size(); kk++){
					nae = (NETArcsnEdges)arcs.get(kk);
					target = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_TARGET)).intValue();
					source = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_SOURCE)).intValue();					
					
					if(((ii+1) == source) && ((jj+1) == target)){
						try{
							weight = ((Float)nae.getAttribute(NETFileProperty.ATTRIBUTE_WEIGHT)).floatValue();
						} catch (NullPointerException e) {
							weight = 1;
						}
						break;
					}
					else {
						weight = (float)0.0;
					}
				}
				pw.print(weight + " ");
			}
			pw.print("\r\n");
		}	
	}
	
	private void writeUndirectedMatrix(NETFileValidator vmf, PrintWriter pw, ArrayList edges) {
		//float[][] matrix = new float[vmf.getVertices().size()][vmf.getVertices().size()];
		NETArcsnEdges nae;
		int source, target;
		float weight = (float)0.0;
		//pw.println(MATFileProperty.HEADER_MATRIX);
		for (int ii = 0; ii < vmf.getTotalNumOfNodes(); ii++){
			for (int jj = 0; jj < vmf.getTotalNumOfNodes(); jj++){
				for (int kk = 0; kk < edges.size(); kk++){
					nae = (NETArcsnEdges)edges.get(kk);
					target = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_TARGET)).intValue();
					source = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_SOURCE)).intValue();
					
					if((((ii+1) == source) && ((jj+1) == target))||
							(((ii+1) == target) && ((jj+1) == source))){
						try{
						weight = ((Float)nae.getAttribute(NETFileProperty.ATTRIBUTE_WEIGHT)).floatValue();
						}catch(NullPointerException npe){
							weight = 1;
						}
						break;
					}
					else {
						weight = (float)0.0;
					}
					
				}
				pw.print(weight + " ");
			}
			pw.print("\r\n");
		}	
	}		
}





package edu.iu.nwb.converter.pajekmatpajeknet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.pajekmat.common.MATFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETArcsnEdges;
import edu.iu.nwb.converter.pajeknet.common.NETAttribute;
import edu.iu.nwb.converter.pajeknet.common.NETFileFunctions;
import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETVertex;
import edu.iu.nwb.converter.pajeknet.common.ValidateNETFile;

public class PajeknetToPajekmat implements Algorithm{

	Data[] data;
	Dictionary parameters;
	CIShellContext ciContext;
	LogService logger;
	String[] noPrintParameters = { NETFileProperty.ATTRIBUTE_ID, NETFileProperty.ATTRIBUTE_LABEL, "xpos", "ypos", "zpos", "shape",NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.ATTRIBUTE_WEIGHT };


	public PajeknetToPajekmat(Data[] dm, Dictionary parameters, CIShellContext cContext){
		this.data = dm;
		this.parameters = parameters;
		this.ciContext = cContext;
		this.logger = (LogService)ciContext.getService(LogService.class.getName());
	}

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
			tempFile = new File (tempPath+File.separator+"netTemp"+File.separator+"temp.net");

		}
		return tempFile;
	}

	public Data[] execute() {
		File inData, outData;
		Data [] dm = null;
		ValidateNETFile validator;
		//System.out.println("Executing PajeknetToPajekMat");
		Object inFile = data[0].getData();

		if (inFile instanceof File){
			inData = (File)inFile;

			validator = new ValidateNETFile();
		//System.out.println("Before try");
			try {
		//System.out.println("Trying");
				validator.validateNETFormat(inData);
				if(validator.getValidationResult()){
					outData = convertNetToMat(validator);
					if(outData != null){
						dm = new Data[] {new BasicData(outData, MATFileProperty.MAT_MIME_TYPE)};
						return dm;
					}else {
						logger.log(LogService.LOG_ERROR, "Problem executing conversion from Pajek .net to .mat. Output file was not created");
						return null;
					}
				}else{
					logger.log(LogService.LOG_ERROR,"Problem executing conversion from Pajek .net to .mat " + validator.getErrorMessages());
					return null;
				}
			}
			catch (FileNotFoundException fnf){
				logger.log(LogService.LOG_ERROR, "Could not find the specified Pajek .net file.", fnf);
				return null;
			}
			catch (IOException ioe){
				logger.log(LogService.LOG_ERROR, "IO Error while converting from Pajek .net to .mat.", ioe);
				ioe.printStackTrace();
				return null;
			}
			catch( Exception ex){
				logger.log(LogService.LOG_ERROR, "Error while converting from Pajek .net to .mat." + validator.getErrorMessages(), ex);
				ex.printStackTrace();
				return null;
			}
		}
		else
			logger.log(LogService.LOG_ERROR, "Unable to convert the file. " +
			"Unable to convert from Pajek .net to .mat because input data is not a file");
		return null;
	}

	private File convertNetToMat(ValidateNETFile vmf){
		try{
			//System.out.println("Converting net to mat");
			File net = getTempFile();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(net)));	
			writeVertices(vmf, out);
			//System.out.println(vmf.isDirectedGraph() + " " + vmf.isUndirectedGraph());
			if(vmf.isDirectedGraph()){
				//System.out.println("Directed");
				writeDirectedMatrix(vmf, out);
			}
			else{
				//System.out.println("Undirected");
				writeUndirectedMatrix(vmf,out);
			}
			out.close();
			return net;
		}catch (FileNotFoundException e){
			logger.log(LogService.LOG_ERROR, "Unable to find the temporary .mat file.", e);
			return null;
		}catch (IOException ioe){
			logger.log(LogService.LOG_ERROR, "IO Errors while writing to the temporary .mat file.", ioe);
			return null;
		}
	}

	private void writeVertices(ValidateNETFile vmf, PrintWriter pw){
		pw.write(MATFileProperty.HEADER_VERTICES + " " + vmf.getVertices().size() + "\n");
		for(int i = 0; i < vmf.getVertices().size(); i++){
			NETVertex mv = (NETVertex)vmf.getVertices().get(i);
			String s = "";
			for(int j = 0; j < NETVertex.getVertexAttributes().size(); j++){
				try{
					NETAttribute ma = (NETAttribute)NETVertex.getVertexAttributes().get(j);
					String attr = ma.getAttrName();
					String type = ma.getDataType();
					String value = mv.getAttribute(attr).toString();
					if(value == null){
						s += "";
					}
					else{
						if(NETFileFunctions.isInList(attr,noPrintParameters)){
							if(type.equalsIgnoreCase("string"))
								s += "\"" + mv.getAttribute(attr) + "\" ";
							else
								s += mv.getAttribute(attr)+" ";
						}
						else {
							if(type.equalsIgnoreCase("string"))
								s += attr + " \"" + mv.getAttribute(attr) + "\" ";
							else
								s += attr + " " + mv.getAttribute(attr) + " ";
						}
					}
				}catch(NullPointerException ex){
					s += "";
				}
			}
			//System.out.println(s);
			pw.println(s);
		}
		//System.out.println("done here");
	}

	private void writeDirectedMatrix(ValidateNETFile vmf, PrintWriter pw){
		NETVertex nvi;
		NETVertex nvj;
		NETArcsnEdges nae;
		int source, target;
		float weight = (float)0.0;
		//System.out.println(vmf.getEdges().size());
		pw.println(MATFileProperty.HEADER_MATRIX);
		for(int i = 0; i < vmf.getVertices().size(); i++){
			nvi = (NETVertex)vmf.getVertices().get(i);
			for(int j = 0; j < vmf.getVertices().size(); j++){
				nvj = (NETVertex)vmf.getVertices().get(j);
				for(int k = 0; k < vmf.getArcs().size(); k++){
					nae = (NETArcsnEdges)vmf.getArcs().get(k);
					target = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_TARGET)).intValue();
					source = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_SOURCE)).intValue();
					//System.out.println(nvi.getID() + ":"+ source + "\t" + nvj.getID()+":"+target);
					
					if((nvi.getID() == source) && (nvj.getID() == target)){
						weight = ((Float)nae.getAttribute(NETFileProperty.ATTRIBUTE_WEIGHT)).floatValue();
					//System.out.println(weight);
						break;
					}
					else {
						weight = (float)0.0;
					}
				}
				pw.print(weight + " ");
			}
			pw.println();
		}
		
		}
	
	private void writeUndirectedMatrix(ValidateNETFile vmf, PrintWriter pw){
		NETVertex nvi;
		NETVertex nvj;
		//float[][] matrix = new float[vmf.getVertices().size()][vmf.getVertices().size()];
		NETArcsnEdges nae;
		int source, target;
		float weight = (float)0.0;
		//System.out.println(vmf.getEdges().size());
		pw.println(MATFileProperty.HEADER_MATRIX);
		for(int i = 0; i < vmf.getVertices().size(); i++){
			nvi = (NETVertex)vmf.getVertices().get(i);
			for(int j = 0; j < vmf.getVertices().size(); j++){
				nvj = (NETVertex)vmf.getVertices().get(j);
				for(int k = 0; k < vmf.getEdges().size(); k++){
					nae = (NETArcsnEdges)vmf.getEdges().get(k);
					target = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_TARGET)).intValue();
					source = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_SOURCE)).intValue();
					//System.out.println(nvi.getID() + ":"+ source + "\t" + nvj.getID()+":"+target);
					
					if(((nvi.getID() == source) && (nvj.getID() == target))||
							((nvi.getID() == target) && (nvj.getID() == source))){
						weight = ((Float)nae.getAttribute(NETFileProperty.ATTRIBUTE_WEIGHT)).floatValue();
						//System.out.println(weight);
						break;
					}
					else {
						weight = (float)0.0;
					}
					
				}
				pw.print(weight + " ");
			}
			pw.println();
		}
		
	}
			
		
		
	}





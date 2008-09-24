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
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.pajekmat.common.MATArcs;
import edu.iu.nwb.converter.pajekmat.common.MATAttribute;
import edu.iu.nwb.converter.pajekmat.common.MATFileFunctions;
import edu.iu.nwb.converter.pajekmat.common.MATFileProperty;
import edu.iu.nwb.converter.pajekmat.common.MATVertex;
import edu.iu.nwb.converter.pajekmat.common.ValidateMATFile;
import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;

public class PajekmatToPajeknet implements Algorithm{
	Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;
    String[] noPrintParameters = { NETFileProperty.ATTRIBUTE_ID, NETFileProperty.ATTRIBUTE_LABEL, "xpos", "ypos", "zpos", "shape",NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.ATTRIBUTE_WEIGHT };

    public PajekmatToPajeknet(Data[] data, Dictionary parameters, CIShellContext context){
    	 this.data = data;
         this.parameters = parameters;
         this.ciContext = context;
         this.logger = (LogService)ciContext.getService(LogService.class.getName());
    }
    
	private File getTempFile() throws IOException{
		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath+File.separator+"temp");
		if(!tempDir.exists())
			tempDir.mkdir();
		
		return File.createTempFile("NWB-Session-", ".net", tempDir);
	}

	public Data[] execute() throws AlgorithmExecutionException {
		File inData, outData;
    	Data [] dm = null;
    	ValidateMATFile validator;
    	
		Object inFile = data[0].getData();
    	
		if (inFile instanceof File){
			inData = (File)inFile;
			
			validator = new ValidateMATFile();
			
			try {
				validator.validateMATFormat(inData);
				if(validator.getValidationResult()){
					outData = convertMatToNet(validator);
					if(outData != null){
						dm = new Data[] {new BasicData(outData, NETFileProperty.NET_MIME_TYPE)};
						return dm;
					}else {
						throw new AlgorithmExecutionException("Problem executing conversion from Pajek .mat to .net. Output file was not created");
					}
				}else{
					throw new AlgorithmExecutionException("Problem executing conversion from Pajek .mat to .net" + validator.getErrorMessages());
				}
		}
			catch (FileNotFoundException fnf){
				throw new AlgorithmExecutionException("Could not find the specified Pajek .mat file.", fnf);
		}
			catch (IOException ioe){
				throw new AlgorithmExecutionException("IO Error while converting from Pajek .mat to .net.", ioe);
			}
		}
		else
			throw new AlgorithmExecutionException("Unable to convert the file. " +
					"Unable to convert from Pajek .mat to .net because input data is not a file");
	}
	
	private File convertMatToNet(ValidateMATFile vmf) throws AlgorithmExecutionException {
		try{
			File net = getTempFile();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(net)));	
			writeVertices(vmf, out);
			writeArcs(vmf, out);
			out.close();
			return net;
		}catch (FileNotFoundException e){
			throw new AlgorithmExecutionException("Unable to find the temporary .net file.", e);
		}catch (IOException ioe){
			throw new AlgorithmExecutionException("IO Errors while writing to the temporary .net file.", ioe);
		}
	}
	
	private void writeVertices(ValidateMATFile vmf, PrintWriter pw){
		pw.write(MATFileProperty.HEADER_VERTICES + " " + vmf.getTotalNumOfNodes() + "\n");
		for(int i = 0; i < vmf.getVertices().size(); i++){
			MATVertex mv = (MATVertex)vmf.getVertices().get(i);
			String s = "";
			for(int j = 0; j < MATVertex.getVertexAttributes().size(); j++){
				MATAttribute ma = (MATAttribute)MATVertex.getVertexAttributes().get(j);
				String attr = ma.getAttrName();
				String type = ma.getDataType();
				if(mv.getAttribute(attr) == null){
					s += "";
					continue;
				}
				if(MATFileFunctions.isInList(attr,noPrintParameters)){
					if(type.equalsIgnoreCase("string"))
						s += "\"" + mv.getAttribute(attr) + "\" ";
					else
						s += mv.getAttribute(attr)+" ";
				}
				else {
					if(type.equalsIgnoreCase("string")){
						if(attr.startsWith("unknown")){
						s += " \"" + mv.getAttribute(attr) + "\" ";
						}
						else{
							s += attr + " \"" + mv.getAttribute(attr) + "\" ";
						}
				}
						else
						s += attr + " " + mv.getAttribute(attr) + " ";
				}
			}
			pw.write(s + "\n");
		}
	}
	
	private void writeArcs(ValidateMATFile vmf, PrintWriter pw){
		pw.write(MATFileProperty.HEADER_ARCS + " " + (vmf.getArcs().size()) + "\n");
		for(int i = 0; i < vmf.getArcs().size(); i++){
			MATArcs mv = (MATArcs)vmf.getArcs().get(i);
			String s = "";
			for(int j = 0; j < MATArcs.getArcsnEdgesAttributes().size(); j++){
				MATAttribute ma = (MATAttribute)MATArcs.getArcsnEdgesAttributes().get(j);
				String attr = ma.getAttrName();
				String type = ma.getDataType();
				if(mv.getAttribute(attr) == null){
					s += "";
					continue;
				}
				else if(MATFileFunctions.isInList(attr,noPrintParameters)){
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
			pw.write(s + "\n");
		}
	}
}

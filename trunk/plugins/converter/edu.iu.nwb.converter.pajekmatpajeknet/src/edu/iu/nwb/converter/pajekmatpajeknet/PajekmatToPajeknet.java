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
						logger.log(LogService.LOG_ERROR, "Problem executing conversion from Pajek .mat to .net. Output file was not created");
						return null;
					}
				}else{
					logger.log(LogService.LOG_ERROR,"Problem executing conversion from Pajek .mat to .net" + validator.getErrorMessages());
					return null;
				}
		}
			catch (FileNotFoundException fnf){
			logger.log(LogService.LOG_ERROR, "Could not find the specified Pajek .mat file.", fnf);
		}
			catch (IOException ioe){
				logger.log(LogService.LOG_ERROR, "IO Error while converting from Pajek .mat to .net.", ioe);
			}
		}
		else
			logger.log(LogService.LOG_ERROR, "Unable to convert the file. " +
					"Unable to convert from Pajek .mat to .net because input data is not a file");
		return null;
	}
	
	private File convertMatToNet(ValidateMATFile vmf){
		try{
			File net = getTempFile();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(net)));	
			writeVertices(vmf, out);
			writeArcs(vmf, out);
			out.close();
			return net;
		}catch (FileNotFoundException e){
			logger.log(LogService.LOG_ERROR, "Unable to find the temporary .net file.", e);
			return null;
		}catch (IOException ioe){
			logger.log(LogService.LOG_ERROR, "IO Errors while writing to the temporary .net file.", ioe);
			return null;
		}
	}
	
	private void writeVertices(ValidateMATFile vmf, PrintWriter pw){
		pw.write(MATFileProperty.HEADER_VERTICES + " " + vmf.getTotalNumOfNodes() + "\n");
		for(int i = 0; i < vmf.getVertices().size(); i++){
			MATVertex mv = (MATVertex)vmf.getVertices().get(i);
			String s = "";
			for(int j = 0; j < mv.getVertexAttributes().size(); j++){
				MATAttribute ma = (MATAttribute)mv.getVertexAttributes().get(j);
				String attr = ma.getAttrName();
				String type = ma.getDataType();
				if(MATFileFunctions.isInList(attr,noPrintParameters)){
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
	
	private void writeArcs(ValidateMATFile vmf, PrintWriter pw){
		pw.write(MATFileProperty.HEADER_ARCS + " " + (vmf.getArcs().size()-1) + "\n");
		for(int i = 0; i < vmf.getArcs().size(); i++){
			MATArcs mv = (MATArcs)vmf.getArcs().get(i);
			String s = "";
			for(int j = 0; j < mv.getArcsnEdgesAttributes().size(); j++){
				MATAttribute ma = (MATAttribute)mv.getArcsnEdgesAttributes().get(j);
				String attr = ma.getAttrName();
				String type = ma.getDataType();
				if(MATFileFunctions.isInList(attr,noPrintParameters)){
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

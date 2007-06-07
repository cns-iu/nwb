package edu.iu.nwb.converter.nwbpajeknet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.NWBFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETArcsnEdges;
import edu.iu.nwb.converter.pajeknet.common.NETAttribute;
import edu.iu.nwb.converter.pajeknet.common.NETVertex;
import edu.iu.nwb.converter.pajeknet.common.ValidateNETFile;


public class PajeknetToNWB implements Algorithm {

	Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;
    GUIBuilderService guiBuilder;
    
    Map vertexToIdMap;
	
    
    /**
     * Intializes the algorithm
     * @param data List of Data objects to convert
     * @param parameters Parameters passed to the converter
     * @param context Provides access to CIShell services
     * @param transformer 
     */
    public PajeknetToNWB(Data[] data, Dictionary parameters, CIShellContext context) {
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
    	ValidateNETFile validator;
    	
		Object inFile = data[0].getData();
    	
		if (inFile instanceof File){
			inData = (File)inFile;
			
			validator = new ValidateNETFile();
			
			try {
				validator.validateNETFormat(inData);
				if(validator.getValidationResult()){
					outData = convertNetToNWB(validator);
					if(outData != null){
						dm = new Data[] {new BasicData(outData, "file:text/nwb")};
						return dm;
					}else {
						guiBuilder.showError("Problem executing transformation from Pajek .net to NWB", "Output file was not created","");
						return null;
					}
				}else{
					guiBuilder.showError("Problem executing transformation from Pajek .net to NWB", validator.getErrorMessages(), "");
					return null;
				}
		}
			catch (FileNotFoundException fnf){
			guiBuilder.showError("File Not Found Exception", "", fnf);
		}
			catch (IOException ioe){
				guiBuilder.showError("IOException", "", ioe);
			}
		}
		else
			guiBuilder.showError("Unable to convert the file", "Unable to convert from Pajek .net to nwb because input data is not a file", "");
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
			tempFile = File.createTempFile("NWB-Session-", ".nwb", tempDir);
		
		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.toString());
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");

		}
		return tempFile;
	}
	
	private File convertNetToNWB(ValidateNETFile validator){
		try{
			File nwb = getTempFile();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(nwb)));	
			writeNodes(validator, out);
			writeEdges(validator, out);
			writeArcs(validator, out);
			out.close();
			return nwb;
		}catch (FileNotFoundException e){
			guiBuilder.showError("File Not Found Exception", "Got a File Not Found Exception", e);
			return null;
		}catch (IOException ioe){
			guiBuilder.showError("IOException", "Got an IOException", ioe);
			return null;
		}
	}
	
	private void writeHeader(String s, PrintWriter out){
		String st = NWBFileProperty.PRESERVED_STAR+s;
		System.out.println(st);
		out.println(st);
	}
	
	private void writeNodeAttributeList(ValidateNETFile validator, PrintWriter out){
		String st = NWBFileProperty.PREFIX_COMMENTS;
		for(NETAttribute na : validator.getVertexAttrList()){
			st += na.getAttrName()+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
		}
		System.out.println(st);
		out.println(st);
	}
	private void writeUndirectedEdgeAttributeList(ValidateNETFile validator, PrintWriter out){
		String st = NWBFileProperty.PREFIX_COMMENTS;
		for(NETAttribute na : validator.getEdgeAttrList()){
			st += na.getAttrName()+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
			
		}
		System.out.println(st);
		out.println(st);
	}
	private void writeDirectedEdgeAttributeList(ValidateNETFile validator, PrintWriter out){
		String st = NWBFileProperty.PREFIX_COMMENTS;
		for(NETAttribute na : validator.getArcAttrList()){
			st += na.getAttrName()+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
		}
		System.out.println(st);
		out.println(st);
	}
	private void writeNodes(ValidateNETFile validator, PrintWriter out){
		if(!validator.getVertices().isEmpty()){
			writeHeader("Nodes", out);
			writeNodeAttributeList(validator, out);
		
		for(NETVertex nv : validator.getVertices()){
			String st = "";
			for(NETAttribute na : validator.getVertexAttrList()){
				try{
					Object o = nv.getAttribute(na.getAttrName());
					if(o.getClass().equals(String.class))
						st +=  "\"" + o.toString() + "\" ";
					else
					st += o.toString()+ " ";
					
				}catch(NullPointerException npe){
					st += "* ";
				}
			}
			System.out.println(st);
			out.println(st);
		}
		}
	}
	private void writeEdges(ValidateNETFile validator, PrintWriter out){
		if(!validator.getEdges().isEmpty()){
			writeHeader("UndirectedEdges", out);
			writeUndirectedEdgeAttributeList(validator, out);
		
		for(NETArcsnEdges nae : validator.getEdges()){
			String st = "";
			for(NETAttribute na : validator.getEdgeAttrList()){
				try{
					st += nae.getAttribute(na.getAttrName())+ " ";
				}catch(NullPointerException npe){
					st += "* ";
				}
				
				
			}
			System.out.println(st);
			out.println(st);
		}
		}
	}
	private void writeArcs(ValidateNETFile validator, PrintWriter out){
		if(!validator.getArcs().isEmpty()){
			writeHeader("DirectedEdges", out);
			writeDirectedEdgeAttributeList(validator, out);
		
		for(NETArcsnEdges nae : validator.getArcs()){
				String st = "";
				for(NETAttribute na : validator.getEdgeAttrList()){
					try{
						st += nae.getAttribute(na.getAttrName())+ " ";
					}catch(NullPointerException npe){
						st += "* ";
					}
					
					
				}
				System.out.println(st);
				out.println(st);
		}
	}
	}

}

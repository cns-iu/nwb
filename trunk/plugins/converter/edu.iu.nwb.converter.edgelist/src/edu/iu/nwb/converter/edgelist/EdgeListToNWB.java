package edu.iu.nwb.converter.edgelist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Map;


import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.edgelist.EdgeListValidatorFactory;
import edu.iu.nwb.converter.edgelist.EdgeListValidatorFactory.EdgeListValidator;
import edu.iu.nwb.converter.edgelist.EdgeListValidatorFactory.ValidateEdgeFile;

import java.util.HashMap;


/**
 * Converts from edgelist (raw data) to NWB file format
 * @author Felix Terkhorn
 */
public class EdgeListToNWB implements Algorithm {
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
    public EdgeListToNWB(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.ciContext = context;
        this.logger = (LogService)ciContext.getService(LogService.class.getName());
    }


    /*
     * Reads edgelist data in from reader and outputs it as NWB data to writer
     */
    public void transform(BufferedReader reader, BufferedWriter writer,  EdgeListValidatorFactory.ValidateEdgeFile validator)  
    	throws IOException {
    	int i=0,edgesCount;
		boolean badFormat = false;
		ArrayList validatorEdges = new ArrayList();
		
		try {
			if (badFormat) {
				GUIBuilderService guiBuilder =   (GUIBuilderService)ciContext.getService(GUIBuilderService.class.getName());
				guiBuilder.showError("Bad NWB Format",
						"Sorry, your file does not comply with edge-list format specifications.",
						"Sorry, your file does not comply with edge-list format specifications.\n"+
						"Please review the latest edge-list format specification at "+
						"https://nwb.slis.indiana.edu/community/?n=LoadData.Edgelist, and update your file. \n"
				);
				throw (new IOException("Improperly formatted edgelist file"));
			}
			// currentLine is null
			HashMap validatorMap = validator.getLabelIDMap(); 
			Iterator it = validatorMap.entrySet().iterator();
			
			validatorEdges = validator.getEdges();
			writer.write("*Nodes "+validatorMap.size()+"\n");
			writer.write("id*int  label*string\n");
			
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
			
				writer.write(((Integer)(pairs.getValue())).intValue() + " " +pairs.getKey() +"\n");
			}
			edgesCount = validator.getTotalNumOfEdges();
			if (validator.isUndirectedGraph()) { 
				writer.write("*UndirectedEdges "+edgesCount+"\n");
			} else {
				writer.write("*DirectedEdges "+edgesCount+"\n");
			}
			if (validator.getWeightCount() == 0) {
				writer.write("source*int  target*int\n");
			} else {// weightCount > 1
				if (validator.usesFloatWeight()) {
					writer.write("source*int  target*int  weight*float\n");
				} else {
					writer.write("source*int  target*int  weight*int\n");	
				}
				
			}
			for (i=0;i<validatorEdges.size();i++){
				if (((String[])(validatorEdges.get(i))).length > 2) { // this tuple has a weight value
					try {
					
						writer.write(((Integer)validatorMap.get(((String[])(validatorEdges.get(i)))[0])).intValue() + " " + ((Integer)validatorMap.get(((String[])(validatorEdges.get(i)))[1])).intValue() + " " + ((String[])(validatorEdges.get(i)))[2] +"\n");
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} else { // only source, target were found in this tuple
					try {
						writer.write(((Integer)validatorMap.get(((String[])(validatorEdges.get(i)))[0])).intValue() + " " + ((Integer)validatorMap.get(((String[])(validatorEdges.get(i)))[1])).intValue() +"\n");
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
					
				}

			}			
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		
    	// we need to take the edgelist and convert it into nwb format, then dump that to the writer..
		
    }
    
    /**
     * Executes the conversion
     * 
     * @return A single java file object
     */
    public Data[] execute() {
		File inFile = (File)data[0].getData();
		File outFile;
    	BufferedReader edgelistreader;
		BufferedWriter nwb;
		EdgeListValidatorFactory eLVFact;
		ValidateEdgeFile validator;
			
			
		// BufferedReader wrapping a FileReader -- should give you readline
		try {
			edgelistreader = new BufferedReader(new FileReader(inFile));


		} catch (FileNotFoundException e) {
			System.out.println(">>>> File not found! "+((File)inFile).getAbsolutePath()+"\n");
			return null;
		} catch (IOException e) {
			System.out.println(">>>> IO Exception\n");
			e.printStackTrace();
			return null;
		}
		File nwbFile = getTempFile();
		try {
			nwb = new BufferedWriter(new FileWriter(nwbFile));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			eLVFact = new EdgeListValidatorFactory();
			validator = eLVFact.new ValidateEdgeFile();
			validator.validateEdgeFormat(inFile);
			if (validator.getValidationResult()) {
				
				ArrayList edges = validator.getEdges();
				 
				transform(edgelistreader, nwb, validator);
			}
			return new Data[] {new BasicData(nwbFile, "file:text/nwb")};
		} catch (Exception e ) {
			e.printStackTrace();
			return null;
		}

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
}
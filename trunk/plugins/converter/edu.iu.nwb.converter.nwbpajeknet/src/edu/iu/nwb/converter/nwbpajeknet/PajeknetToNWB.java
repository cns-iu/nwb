package edu.iu.nwb.converter.nwbpajeknet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
						logger.log(LogService.LOG_ERROR, "Problem executing conversion from Pajek .net to .nwb. Output file was not created");
						return null;
					}
				}else{
					logger.log(LogService.LOG_ERROR,"Problem executing conversion from Pajek .net to .nwb" + validator.getErrorMessages());
					return null;
				}
		}
			catch (FileNotFoundException fnf){
			logger.log(LogService.LOG_ERROR, "Could not find the specified Pajek .net file.", fnf);
		}
			catch (IOException ioe){
				logger.log(LogService.LOG_ERROR, "IO Error while converting from Pajek .net to nwb.", ioe);
			}
		}
		else
			logger.log(LogService.LOG_ERROR, "Unable to convert the file. " +
					"Unable to convert from Pajek .net to nwb because input data is not a file");
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
			logger.log(LogService.LOG_ERROR, "Unable to find the temporary .nwb file.", e);
			return null;
		}catch (IOException ioe){
			logger.log(LogService.LOG_ERROR, "IO Errors while writing to the temporary .nwb file.", ioe);
			return null;
		}
	}
	
	private void writeHeader(String s, PrintWriter out){
		String st = NWBFileProperty.PRESERVED_STAR+s;
		//System.out.println(st);
		out.println(st);
	}
	
	private void writeNodeAttributeList(ValidateNETFile validator, PrintWriter out){
		String st = "";// = NWBFileProperty.PREFIX_COMMENTS;
		for(Iterator ii = validator.getVertexAttrList().iterator(); ii.hasNext();){
			NETAttribute na = (NETAttribute) ii.next();
			String s = na.getAttrName();
			if(s.charAt(s.length()-1) == 'c' && na.getDataType().equalsIgnoreCase("float")){
				for(int i = 0; i < 3; i++){
					st += s + (i+1) + NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
				}
			}
			else
				st += s+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
		}
		//System.out.println(st);
		out.println(st);
	}
	private void writeUndirectedEdgeAttributeList(ValidateNETFile validator, PrintWriter out){
		String st = "";//NWBFileProperty.PREFIX_COMMENTS;
		for(Iterator ii = validator.getEdgeAttrList().iterator(); ii.hasNext();){
			NETAttribute na = (NETAttribute) ii.next();
			String s = na.getAttrName();
			if(s.charAt(s.length()-1) == 'c' && na.getDataType().equalsIgnoreCase("float")){
				for(int i = 0; i < 3; i++){
					st += s + (i+1) + NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
				}
			}
			else
				st += s+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
			//st += na.getAttrName()+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
			
		}
		//System.out.println(st);
		out.println(st);
	}
	private void writeDirectedEdgeAttributeList(ValidateNETFile validator, PrintWriter out){
		String st = ""; //NWBFileProperty.PREFIX_COMMENTS;
		for(Iterator ii = validator.getArcAttrList().iterator(); ii.hasNext();){
			NETAttribute na = (NETAttribute) ii.next();
			String s = na.getAttrName();
			if(s.charAt(s.length()-1) == 'c' && na.getDataType().equalsIgnoreCase("float")){
				for(int i = 0; i < 3; i++){
					st += s + (i+1) + NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
				}
			}
			else
				st += s+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
			
		}
	//	System.out.println(st);
		out.println(st);
	}
	private void writeNodes(ValidateNETFile validator, PrintWriter out){
		if(!validator.getVertices().isEmpty()){
			out.flush();
			writeHeader("Nodes " + validator.getVertices().size(), out);
			writeNodeAttributeList(validator, out);
		
		for(Iterator ii = validator.getVertices().iterator(); ii.hasNext();){
			NETVertex nv = (NETVertex) ii.next();
			String st = "";
			for(Iterator jj = validator.getVertexAttrList().iterator(); jj.hasNext();){
				NETAttribute na = (NETAttribute) jj.next();
			//	System.out.println(na.getAttrName());
				try{
					Object o = nv.getAttribute(na.getAttrName());
					if(o.toString() == null){
						if(na.getDataType().equalsIgnoreCase("float") && na.getAttrName().matches("[bil]?c"))
							st += "* * * ";
						else
							st += "* ";
					}
					else
						if(na.getDataType().equalsIgnoreCase("string")){
							st +=  "\""+o + "\" ";
						}
						else {
							st +=  o + " ";
						}
							
					
				}catch(NullPointerException npe){
					st += "* ";
				}
			} 
		//	System.out.println(st);
			out.println(st);
		}
		}
	}
	private void writeEdges(ValidateNETFile validator, PrintWriter out){
		if(!validator.getEdges().isEmpty()){
			out.flush();
			writeHeader("UndirectedEdges " + validator.getEdges().size(), out);
			writeUndirectedEdgeAttributeList(validator, out);
		
		for(Iterator ii = validator.getEdges().iterator(); ii.hasNext();){
			NETArcsnEdges nae = (NETArcsnEdges) ii.next();
			String st = "";
			for(Iterator jj = validator.getEdgeAttrList().iterator(); jj.hasNext();){
				NETAttribute na = (NETAttribute) jj.next();
				try{
					Object o = nae.getAttribute(na.getAttrName());
					//System.out.println(o);
					if(o.toString() == null){
						if(na.getDataType().equalsIgnoreCase("float") && na.getAttrName().matches("[bil]?c")){
							//System.out.println("YES");
							st += "* * * ";
						}
						else{
							//System.out.println("NO");
							st += "* ";
						}
							
					}
					else 
						if(na.getDataType().equalsIgnoreCase("string")){
							st +=  "\""+o + "\" ";
						}
						else
							st +=  o + " ";
					
				}catch(NullPointerException npe){
					if(na.getDataType().equalsIgnoreCase("float") && na.getAttrName().matches("[bil]?c")){
						//System.out.println("YES");
						st += "* * * ";
					}
					else{
						//System.out.println("NO");
						st += "* ";
					}
						
				}
				
				
			}
		//	System.out.println(st);
			out.println(st);
		}
		}
	}
	private void writeArcs(ValidateNETFile validator, PrintWriter out){
		if(!validator.getArcs().isEmpty()){
			out.flush();
			writeHeader("DirectedEdges " + validator.getArcs().size(), out);
			writeDirectedEdgeAttributeList(validator, out);
		
		for(Iterator ii = validator.getArcs().iterator(); ii.hasNext();){
			NETArcsnEdges nae = (NETArcsnEdges) ii.next();
				String st = "";
				for(Iterator jj = validator.getEdgeAttrList().iterator(); jj.hasNext();){
					NETAttribute na = (NETAttribute) jj.next();
					try{
						Object o = nae.getAttribute(na.getAttrName());
						if(o.toString() == null){
							if(na.getDataType().equalsIgnoreCase("float") && na.getAttrName().matches("[bil]?c"))
								st += "* * * ";
							else
								st += "* ";
						}
						else 
							if(na.getDataType().equalsIgnoreCase("string")){
								st +=  "\""+o + "\" ";
							}
							else
								st +=  o + " ";
						
					}catch(NullPointerException npe){
						if(na.getDataType().equalsIgnoreCase("float") && na.getAttrName().matches("[bil]?c")){
							//System.out.println("YES");
							st += "* * * ";
						}
						else{
						//	System.out.println("NO");
							st += "* ";
						}
					}
					
					
				}
			//	System.out.println(st);
				out.println(st);
		}
	}
	}

}

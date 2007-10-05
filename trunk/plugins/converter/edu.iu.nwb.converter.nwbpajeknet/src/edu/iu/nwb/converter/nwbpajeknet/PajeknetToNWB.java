package edu.iu.nwb.converter.nwbpajeknet;

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
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.NWBFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETArcsnEdges;
import edu.iu.nwb.converter.pajeknet.common.NETAttribute;
import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
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
					outData = convertNetToNWB(validator,inData);
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
	
	private File convertNetToNWB(ValidateNETFile validator, File f){
		try{
			File nwb = getTempFile();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(nwb)),true);
		//	out.flush();
			BufferedReader br = new BufferedReader(new FileReader(f));
			processFile(validator,br,out);	
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
	
	private void processFile(ValidateNETFile nv, BufferedReader reader, PrintWriter pw) throws IOException{
		boolean inVerticesSection = false;
		boolean inArcsSection = false;
		boolean inEdgesSection = false;
		
		String line = reader.readLine();
		
		while (line != null){
			//currentLine++;
		

			if(line.startsWith(NETFileProperty.PREFIX_COMMENTS) || (line.length() < 1)){
				line = reader.readLine();
				continue;
			}

			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_VERTICES)){
				
				writeHeader("Nodes "+nv.getTotalNumOfNodes(),pw);
				writeNodeAttributeList(nv,pw);
				inVerticesSection = true;
				inArcsSection = false;
				inEdgesSection = false;
				/*if((nv.getNumVertices() == 0) && (nv.getTotalNumOfNodes() > 0)){
					for(int i = 0; i < nv.getTotalNumOfNodes(); i++){
						String s = (i+1) + " \"" + (i+1) + "\"";
						try{
							NETVertex netv = new NETVertex(s);
							writeNode(nv,pw,netv);
							
						}
						catch(Exception e){
							e.printStackTrace();
							
						}
					}
				}*/
				line = reader.readLine();

				continue;
			}
			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_ARCS)){
				writeHeader("DirectedEdges "+nv.getNumArcs(),pw);
				writeDirectedEdgeAttributeList(nv,pw);
				inVerticesSection = false;
				inArcsSection = true;
				inEdgesSection = false;
				line = reader.readLine();

				continue;
			}

			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_EDGES)){
				writeHeader("UndirectedEdges "+nv.getNumEdges(),pw);
				writeUndirectedEdgeAttributeList(nv,pw);
				inVerticesSection = false;
				inArcsSection = false;
				inEdgesSection = true;
				line = reader.readLine();

				continue;
			}


			if(inVerticesSection){	
				NETVertex netv = nv.processVertices(line);
				writeNode(nv,pw,netv);
				line = reader.readLine();

				continue;
			}

			if(inEdgesSection){
				NETArcsnEdges ane = nv.processArcsnEdges(line);
				writeEdge(nv,pw,ane);
				line = reader.readLine();

				continue;
			}

			if(inArcsSection){
				NETArcsnEdges ane = nv.processArcsnEdges(line);
				writeArc(nv,pw,ane);
				line = reader.readLine();

				continue;
			}


			line = reader.readLine();
		}	
		
	}
	
	private void writeHeader(String s, PrintWriter out){
		//out.flush();
		String st = NWBFileProperty.PRESERVED_STAR+s;
		//System.out.println(st);
		out.println(st);
	}
	
	private void writeNodeAttributeList(ValidateNETFile validator, PrintWriter out){
		//out.flush();
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
		//out.flush();
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
		//out.flush();
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
	private void writeNode(ValidateNETFile validator, PrintWriter out, NETVertex nv){
		
			String st = "";
			for(Iterator jj = validator.getVertexAttrList().iterator(); jj.hasNext();){
				NETAttribute na = (NETAttribute) jj.next();
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
					if(na.getDataType().equalsIgnoreCase("float") && na.getAttrName().matches("[bil]?c"))
						st += "* * * ";
					else
						st += "* ";
				}
			} 
		
			out.println(st);
		}
		
	
	private void writeEdge(ValidateNETFile validator, PrintWriter out, NETArcsnEdges nae){
	//	out.flush();
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
		
	
	private void writeArc(ValidateNETFile validator, PrintWriter out, NETArcsnEdges nae){
		//out.flush();
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

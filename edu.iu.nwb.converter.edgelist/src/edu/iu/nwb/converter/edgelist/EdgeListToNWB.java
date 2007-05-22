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
    public void transform(BufferedReader reader, BufferedWriter writer) 
    	throws IOException {
    	ArrayList edgelist = new ArrayList();
		String currentLine;
		String[] tokens;
		int i=0,edgesCount=0;
		HashMap map = new HashMap();
		int mapCount = 1;
		
		try {
			while ((currentLine = reader.readLine()) != null) {
				//		System.out.println(currentLine+"\n");
			
				tokens = currentLine.trim().split("\\s+");
				
				for(i = 0;i < tokens.length;i++){
					if (!map.containsKey(tokens[i])) {
						if (tokens[i].matches("\".*\"")) {
							map.put(tokens[i], new Integer(mapCount++));
						}
						else { 
							tokens[i] ="\""+tokens[i]+"\""; 
							map.put(tokens[i], new Integer(mapCount++));
						}
					}
			//		System.out.println(tokens[i]+"   ");
				}
				//System.out.println("\n");
				//System.out.println("Adding tokens "+tokens[0]+" "+tokens[1]+"\n");
				edgelist.add(tokens);
				edgesCount++;
			}
			// currentLine is null

			writer.write("*Nodes "+map.size()+"\n");
			writer.write("//id*int  label*string\n");
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				//System.out.println("Attempting to write: "+ ((Integer)(pairs.getValue())).intValue() + " " +pairs.getKey() +"\n");
				writer.write(((Integer)(pairs.getValue())).intValue() + " " +pairs.getKey() +"\n");
			}
			writer.write("*UndirectedEdges "+edgesCount+"\n");
			writer.write("//source*int  target*int\n");
			for (i=0;i<edgesCount;i++){
				//System.out.println("Attempting to write:"+((String[])(edgelist.get(i)))[0] + " " + ((String[])(edgelist.get(i)))[1] +"\n");
				writer.write(((Integer)map.get(((String[])(edgelist.get(i)))[0])).intValue() + " " + ((Integer)map.get(((String[])(edgelist.get(i)))[1])).intValue() +"\n");
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
    	BufferedReader edgelistreader;
		BufferedWriter nwb;
		
			
			
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
			transform(edgelistreader, nwb);
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
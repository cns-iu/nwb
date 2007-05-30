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

    /* handle this merging of tokens with tail recursion */
    public String[] tokenmanage (String[] tokens, int startFrom) {
    	//System.out.println("entering tokenmanage\n");
    	int i;
    	int j;
    	String newtokens[];
    	boolean breakflag = false;
    	for (i = startFrom;i < tokens.length;i++) {
    		if (breakflag) break;
    		// find "\"\w" tokens (position i) and match them with end tokens (position j)
    		// once matched and merged, set i = j, rinse and repeat
    		if (tokens[i].matches("\".*\"")) { // it's  a single word, quoted
    			// continue
    			//System.out.println("token "+i+" MATCHES: single word, quoted.\n");
    		}
    		else if (tokens[i].matches("\".*\".*")) { // end-quote happens in the middle of the token
    			// probably we should just continue...
    			//System.out.println("token "+i+" MATCHES: end-quote in middle of token\n");
    		}
    		else if (tokens[i].startsWith("\"")) {
    			// starts with ", but since previous ifs didn't match, we know it doesn't end
    			// with " or have a " in the middle
    			//System.out.println("token "+i+" MATCHES: multi-word string");
    			j = i+1;
    			if (j < tokens.length) {
    				for (;j < tokens.length;j++) {
    					if (tokens[j].matches(".*\"")) {
    						newtokens = tokenmerge(tokens,i,j);
    						return tokenmanage(newtokens, i+1);
    		/*				breakflag = true;
    						break;*/

    					}
    				}
    			}
    		}

    	}
    	/*
    	if (i < tokens.length - 1 && breakflag) { // we broke from the loop, and there is more work to do
    		return tokenmanage(newtokens, i+1);
    	}*/
    	return tokens;
    }
    
    /*
     * Takes a String[] of tokens and merges all tokens from index i to index j
     * Returns the number of 
     */
    public String[] tokenmerge(String[] tokens, int i, int j) {
    	//System.out.println("entering tokenmerge, i = "+i+", j = "+j+"\n");
    	int x,y;
    	String [] out = new String[tokens.length - (j - i)];
    	String merged = "";
    	for (y = i;y < j+1;y++){
    		//System.out.println("-  merging.  y="+y+"; j="+j+"\n");
    	    if (y == j) {
    	    	merged = merged.concat(tokens[y]);
    	    } else {
    	    	merged = merged.concat(tokens[y] + " ");	
    	    }
    		
    	}
    	for (x=0;x < tokens.length - (j - i);x++) {
    		//System.out.println("-  constructing result string.  x="+x+"\n-  length of tokens = "+tokens.length+"; (j-i) = "+(j-i)+"\n");
    		if (x < i) {
    			out[x] = tokens[x];
    		}
    		else if (x == i) {
    			out[x] = merged;
    		} else { // x > i
    			out[x] = tokens[x + (j - i)];
    		}
    	}
    	return out;
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
		int weightCount = 0;
		int l;
		String [] mergedTokens;
		int lineCounter = 1;
		boolean badFormat = false;
		
		try {
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.matches("\\s*")) {
					continue;
				}
				//System.out.println("...line "+lineCounter+"\n");
				tokens = currentLine.trim().split("\\s+");
				if (tokens.length < 2) {
					badFormat = true;
					break;
				}
				mergedTokens = tokenmanage(tokens,0);
				
				//System.out.println("Merged tokens: \n");
				//for (l = 0; l < mergedTokens.length;l++ ) {
					//System.out.println("*  "+mergedTokens[l]+"\n");
				//}
				
				// need to merge tokens that look like "\"\w"  with ending-quoted tokens that look like
				// "\w\"" and all those in between...
				// this should rewrite the array, tokens
				// so that the following code can work (relatively) unchanged

				for(i = 0;i < mergedTokens.length;i++){
					if (!map.containsKey(mergedTokens[i]) && mergedTokens[i].matches("\".*\"") && i < 2) 
					{ //	looks like "xyz" but we are not in third column
						map.put(mergedTokens[i], new Integer(mapCount++));
						//System.out.println(">  "+mergedTokens[i]+" looks QUOTED\n");
						//System.out.println(">  put "+mergedTokens[i]+" with "+(mapCount-1)+"\n");
						
					}
					else if (!map.containsKey("\""+mergedTokens[i]+"\"") && i < 2)
					{ // we are not in third column, unquoted data 
						//System.out.println(">  "+mergedTokens[i]+" looks unquoted\n");
						
						mergedTokens[i] ="\""+mergedTokens[i]+"\""; 
						map.put(mergedTokens[i], new Integer(mapCount++));
						//System.out.println(">  put "+mergedTokens[i]+" with "+(mapCount-1)+"\n");
					} else if (map.containsKey("\""+mergedTokens[i]+"\"") && i < 2) {
						mergedTokens[i] ="\""+mergedTokens[i]+"\"";
					}

					if (i > 1) {
						weightCount++;
					}
				}

				//System.out.println("adding current mergedTokens, length = "+mergedTokens.length+"\n");
				
				for (int j = 0;j< mergedTokens.length;j++) {
					//System.out.println("*  adding mergedToken["+j+"] = "+mergedTokens[j]+" to edgelist\n");
				}
				edgelist.add(mergedTokens);
				edgesCount++;
				lineCounter++;
			}
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
			//System.out.println("&  done with while\n");
			//for (l=0 ;l < map.size(); l++) {
				//System.out.println("-  map keys: "+map.keySet().toString()+"\n");
				//System.out.println("-  map entries: "+map.entrySet().toString()+"\n");
				/*for (l =0;l < edgelist.size();l++) {
					int j;
					System.out.println("edgelist "+l+"'s length: "+((String[])(edgelist.get(l))).length+"\n");
					for (j=0;j < ((String[])(edgelist.get(l))).length;j++){
						System.out.println("-  next in edgelist entry: "+ ((String[])(edgelist.get(l)))[j] +"\n");	
					}
					
				}*/
				
			//}
			writer.write("*Nodes "+map.size()+"\n");
			writer.write("id*int  label*string\n");
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
			
				writer.write(((Integer)(pairs.getValue())).intValue() + " " +pairs.getKey() +"\n");
			}
			writer.write("*UndirectedEdges "+edgesCount+"\n");
			if (weightCount == 0) {
				writer.write("source*int  target*int\n");
			} else {// weightCount > 1
				writer.write("source*int  target*int  weight*int\n");
			}
			for (i=0;i<edgelist.size();i++){
				//System.out.println("&  In writer for-loop.  i="+i+"\n");
				if (((String[])(edgelist.get(i))).length > 2) { // this tuple has a weight value
					//System.out.println("^  here\n");
					try {
					
						writer.write(((Integer)map.get(((String[])(edgelist.get(i)))[0])).intValue() + " " + ((Integer)map.get(((String[])(edgelist.get(i)))[1])).intValue() + " " + ((String[])(edgelist.get(i)))[2] +"\n");
					} catch (NullPointerException e) {
						//System.out.println("caught at 1\n");
						e.printStackTrace();
					}
				} else { // only source, target were found in this tuple
					//System.out.println("^  here2\n");
					try {
						writer.write(((Integer)map.get(((String[])(edgelist.get(i)))[0])).intValue() + " " + ((Integer)map.get(((String[])(edgelist.get(i)))[1])).intValue() +"\n");
					} catch (NullPointerException e) {
						//System.out.println("caught.  length of string[] in this edge = "+((String[])(edgelist.get(i))).length+"\n");
						e.printStackTrace();
					}
					
				}

			}			
			//System.out.println("^  flushing\n");
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
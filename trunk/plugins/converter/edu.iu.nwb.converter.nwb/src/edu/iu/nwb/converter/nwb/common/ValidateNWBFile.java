package edu.iu.nwb.converter.nwb.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ValidateNWBFile {
	private boolean hasHeader_Nodes = false;
	private boolean hasHeader_UndirectedEdges = false;
	private boolean hasHeader_DirectedEdges = false;
//	private boolean hasHeader_NodeAttributes = false;
//	private boolean hasHeader_EdgeAttributes = false;
	private boolean isFileGood = false;
	
	public void validateNWBFormat(File fileHandler) 
    		throws FileNotFoundException, IOException    {
		BufferedReader reader = 
			new BufferedReader(new FileReader(fileHandler));
		
		String line = reader.readLine();
		while(line != null){
			if(line.startsWith(NWBFileProperty.HEADER_NODE) || 
			   line.startsWith(NWBFileProperty.HEADER_NODE_LOWERCASE) ||
			   line.startsWith(NWBFileProperty.HEADER_NODE_UPPERCASE) ) {
				hasHeader_Nodes = true;
			}
			
			if(line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES) || 
			   line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES_LOWERCASE) ||
			   line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES_LOWERFIRST) ||
			   line.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES_UPPERCASE) ) {
				hasHeader_DirectedEdges = true;
			}

			if(line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES) || 
			   line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES_LOWERCASE) ||
			   line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES_LOWERFIRST) ||
			   line.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES_UPPERCASE) ) {
				hasHeader_UndirectedEdges = true;
			}				
			line = reader.readLine();	
		}
		
		if (!hasHeader_Nodes){
			isFileGood = false;
		}else if (!hasHeader_DirectedEdges && !hasHeader_UndirectedEdges) {
			isFileGood = false;	
		}else{
			isFileGood = true;
		}    	
    }
    
    public boolean getValidationResult(){
    	return isFileGood;
    }
    

}

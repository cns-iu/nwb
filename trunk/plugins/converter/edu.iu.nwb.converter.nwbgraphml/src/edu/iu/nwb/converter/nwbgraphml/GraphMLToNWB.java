package edu.iu.nwb.converter.nwbgraphml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.io.GraphMLFile;
import edu.uci.ics.jung.utils.Pair;
import edu.uci.ics.jung.utils.PredicateUtils;

/**
 * Converts from GraphML to NWB file format
 * @author Ben Markines 
 */
public class GraphMLToNWB implements Algorithm {
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
     */
    public GraphMLToNWB(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.ciContext = context;
    }

    /**
     * Executes the conversion
     * 
     * @return A single java file object
     */
    public Data[] execute() {
		Object inFile = data[0].getData();
    	
		if (inFile instanceof File){
			GraphMLFile graphMLFile = new GraphMLFile();
			try {
				Graph g = graphMLFile.load(new FileReader((File)inFile));
				File f = writeNWBFile(g);
				Data []dm = new Data[] {new BasicData(f, "file:text/nwb")};
				return dm;
			} catch (FileNotFoundException e) {
				logger.log(LogService.LOG_ERROR, e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		return null;
    }
    
    /**
     * Creates a file given a JUNG Graph object
     * @param g JUNG Graph object
     * @return NWB file
     */
    private File writeNWBFile(Graph g) {
    	File nwbFile = getTempFile();
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(nwbFile)));
			
			//print the vertices
			extractVertices(g);
			out.write("// GraphML to NWB conversion\n");
			out.write("*Nodes " + g.numVertices() + "\n");
			for (Iterator i = g.getVertices().iterator(); i.hasNext();) {
				Vertex vertex = (Vertex)i.next();
				String vertexStr = (String)vertexToIdMap.get(vertex);
				String label     = (String)vertex.getUserDatum("label");
				out.write(vertexStr + " " + label + "\n");				
			}
			
			//print the edges
			boolean isUndirectedGraph = PredicateUtils.enforcesUndirected(g);
			int numEdges = g.numEdges();
			out.write("\n");
			if (isUndirectedGraph) {
				out.write("*UndirectedEdges " + numEdges + "\n");
			}
			else {
				out.write("*DirectedEdges " + numEdges + "\n");				
			}
			Set s = g.getEdges();
			for (Iterator i = s.iterator(); i.hasNext();) {
				Edge e = (Edge)i.next();
				Pair nodePair = e.getEndpoints();
				Vertex firstNode  = (Vertex)nodePair.getFirst();
				Vertex secondNode = (Vertex)nodePair.getSecond();
				String firstNodeStr  = (String)vertexToIdMap.get(firstNode);
				String secondNodeStr = (String)vertexToIdMap.get(secondNode);
				out.write(firstNodeStr + " " + secondNodeStr + "\n");
			}
			
			out.close();
			return nwbFile;
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage());
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * Retrieves all the vertices from the JUNG Graph
     * @param g The JUNG Graph
     */
    private void extractVertices(Graph g) {
    	vertexToIdMap = new Hashtable();
    	Set set = g.getVertices();
    	int vertexId = 1;
    	for (Iterator i = set.iterator(); i.hasNext();) {
    		Vertex v = (Vertex)i.next();
    		vertexToIdMap.put(v, ""+vertexId);
    		++vertexId;
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
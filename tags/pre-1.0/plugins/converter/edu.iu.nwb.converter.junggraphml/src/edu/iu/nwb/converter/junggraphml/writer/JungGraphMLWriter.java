package edu.iu.nwb.converter.junggraphml.writer;

//Java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.StringTokenizer;

//CIShell
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;
//Jung
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLFile;
import edu.uci.ics.jung.graph.decorators.Indexer;


/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungGraphMLWriter implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService logger;

    
    public JungGraphMLWriter(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger = (LogService)context.getService(LogService.class.getName());


    }

    public Data[] execute() {
    	File tempFile = getTempFile();
    	File goodGraphML = getTempFile();
        
    	if (tempFile != null){
    		//Update the graph index. This is a fix for the bug I report at 
    		//https://nwb.slis.indiana.edu/wiki/?n=Nwbtool.BugAttackToleranceSave
    		Indexer.getAndUpdateIndexer((Graph)(data[0].getData()));
    		(new GraphMLFile()).save((Graph)(data[0].getData()), tempFile.getPath()) ;
    	}
    	
    	//TODO: This is a temporary fix. We found a bug in GraphMLFile.save. 
    	//It can generate a bad graphml file that won't be able to read in.
    	//Here I read in the file and clean the bad entry and save it back.
    	
    	try {
    		(new GraphMLFile()).load(new FileReader(tempFile));   
    		return new Data[]{new BasicData(tempFile, "file:text/graphml+xml")};
    		
    	}catch (FileNotFoundException exception){
    		logger.log(LogService.LOG_ERROR, "Unable to create the temp file for writing to GraphML.\n", exception);
    		return null;
    	}catch (Exception e){    	
	    	try{
	    		BufferedReader reader = new BufferedReader(new FileReader(tempFile));
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(goodGraphML)));
	
				String line = reader.readLine();
				while(line != null){
					if(line.startsWith("<graph edgedefault=")){
						StringBuffer goodline = new StringBuffer();
						StringTokenizer st = new StringTokenizer(line) ;
					     while (st.hasMoreTokens()) {
					    	 String token = st.nextToken();
					         if (token.equals("<graph")){
					         	goodline.append(token);	
					         }else if (token.startsWith("edgedefault=")){
					        	goodline.append(" "+token+">");
					        	break;
					         }			         
					     }
					     out.println(goodline.toString());
	
					}
					else if(line.startsWith ("<node" )){
						StringBuffer goodline = new StringBuffer();
						StringTokenizer st = new StringTokenizer(line) ;
					     while (st.hasMoreTokens()) {
					    	 String token = st.nextToken();
					         if (token.equals("<node")){
					         	goodline.append(token);	
					         }else if (token.startsWith("id=")){
					        	goodline.append(" "+token);
					        	token=token.replaceAll("id", "label");
					        	goodline.append(" "+token+"/>");
					        	break;
					         }			         
					     }
					     out.println(goodline.toString());
					}
					else
						out.println(line);
					line = reader.readLine();
				}
		    	reader.close();
				out.close();
	    	}catch (FileNotFoundException fnfe){
	    		logger.log(LogService.LOG_ERROR, "An Exception occured in the processing of the specified file." +
	    				"Unable to create the temp file for creating the default graphML file.\n", fnfe);
				return null;
			}catch (IOException ioe){
				logger.log(org.osgi.service.log.LogService.LOG_ERROR, "IO Errors in writing to GraphML.\n", ioe);
				return null;
			}    	
	    	
	        return new Data[]{new BasicData(goodGraphML, "file:text/graphml+xml")};
    	}
    	
    }
    
	private File getTempFile(){
		File tempFile;
    
		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath+File.separator+"temp");
		if(!tempDir.exists())
			tempDir.mkdir();
		try{
			tempFile = File.createTempFile("NWB-Session-", ".xml", tempDir);
		
		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.getMessage(), e);
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");
		}
		return tempFile;
	}
}
package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;

public class PropertyHandler {
	
	
	public static boolean validateProperties(final FileInputStream fis) throws IOException{
		final BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		boolean wellFormed = true;
		String line;
		Pattern p = Pattern.compile("^.*\\..*=.*\\..*");
		Matcher m;
		
		while((line = br.readLine()) != null){		
			if(line.startsWith("node.") || line.startsWith("edge.")){				
				m = p.matcher(line.subSequence(0, line.length())).reset();
				if(!m.find()){
					wellFormed = false;	
				}
			}	
		}
		br.close();	
		return wellFormed;		
	}

	public static Properties getProperties(String fileName, LogService log) throws AlgorithmExecutionException {
		final Properties aggregateDefs = new Properties();
		boolean wellFormed = true;
		
		try {
			File f = new File(fileName);
			FileInputStream in = new FileInputStream(f);
			
			wellFormed = validateProperties(in);
			
			if(wellFormed){
				in = new FileInputStream(f);
				aggregateDefs.load(in);
			}
			else{
				log.log(LogService.LOG_WARNING, "Your Aggregate Function File did not follow the specified format.\n" +
						"Continuing the extraction without additional analysis.");
				//need to add a documentation link about how to specify the aggregate function file.
			}
		} 
		catch (final FileNotFoundException fnfe) {
			throw new AlgorithmExecutionException(fnfe.getMessage(), fnfe);
		} catch (final IOException ie) {
			throw new AlgorithmExecutionException(ie.getMessage(), ie);
		}
		return aggregateDefs;
	}

}
